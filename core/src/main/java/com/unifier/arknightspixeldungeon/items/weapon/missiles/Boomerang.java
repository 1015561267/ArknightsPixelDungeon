/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.unifier.arknightspixeldungeon.items.weapon.missiles;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Projecting;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Boomerang extends Weapon {

    public static final String AC_SHOOT	= "SHOOT";

	{
		image = ItemSpriteSheet.BOOMERANG;
		stackable = false;
		defaultAction = AC_SHOOT;
		usesTargeting = true;
		unique = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		//if (!isEquipped(hero)) actions.add(AC_EQUIP);
        actions.remove(AC_EQUIP);
        actions.add(AC_SHOOT);
		return actions;
	}

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SHOOT)) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell( shooter );
        }
    }

	@Override
	public int min(int lvl) {
		return  1 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  6 +     //half the base damage of a tier-1 weapon
				2 * lvl;//scales the same as a tier 1 weapon
	}

	@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return 9 - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}
	
	@Override
	public boolean isIdentified() {
		return levelKnown && cursedKnown;
	}
	
	@Override
	public Item upgrade( boolean enchant ) {
		super.upgrade( enchant );
		updateQuickslot();
		return this;
	}

    @Override
    public String info() {
        String info = desc();

        info += "\n\n" + Messages.get( Boomerang.class, "stats",
                Math.round(augment.damageFactor(min())),
                Math.round(augment.damageFactor(max())),
                STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.STR() > STRReq()){
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
        }

        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        return info;
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                knockArrow().cast(curUser, target);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(Boomerang.class, "prompt");
        }
    };

    public SpiritArrow knockArrow(){
        return new SpiritArrow();
    }

    public class SpiritArrow extends MissileWeapon {

        {
            image = ItemSpriteSheet.SPIRIT_ARROW;
        }

        @Override
        public boolean isBulletForEffect(){return true;}

        @Override
        public int min(int lvl) {
            return Boomerang.this.min(lvl);
        }

        @Override
        public int max(int lvl) {
            return Boomerang.this.max(lvl);
        }

        @Override
        public int damageRoll(Char owner ,Char enemy ,boolean isMagic) {
            return Boomerang.this.damageRoll(owner,enemy,isMagic);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            return Boomerang.this.proc(attacker, defender, damage);
        }

        @Override
        public float speedFactor(Char user) {
            return Boomerang.this.speedFactor(user);
        }

        @Override
        public float accuracyFactor(Char owner) {
                return super.accuracyFactor(owner);
        }

        @Override
        public int STRReq(int lvl) {
            return Boomerang.this.STRReq(lvl);
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, 0xCCFFC800, 1 );
            } else {
                if (!curUser.shoot( enemy, this )) {
                    Splash.at(cell, 0xCCFFC800, 1);
                }
            }
        }
        int flurryCount = -1;
        @Override
        public void cast(final Hero user, final int dst) {
                super.cast(user, dst);
        }

        @Override
        public int throwPos(Hero user, int dst) {
            if (Boomerang.this.hasEnchant(Projecting.class)
                    && !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4){
                return dst;
            } else {
                return super.throwPos(user, dst);
            }
        }
    }
}
