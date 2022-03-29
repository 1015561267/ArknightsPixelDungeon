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

package com.unifier.arknightspixeldungeon.items.weapon.melee;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.Wound;
import com.unifier.arknightspixeldungeon.items.KindOfWeapon;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MeleeWeapon extends Weapon {
	
	public int tier;

    public static final String AC_REDDUCK = "REDDUCK";//绝影

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        if (hero.heroClass == HeroClass.WARRIOR && isEquipped(hero))
        {
            actions.add(AC_REDDUCK);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_REDDUCK)) {
            //In addition to equipping itself, item reassigns itself to the quickslot
            //This is a special case as the item is being removed from inventory, but is staying with the hero.

           int intialPos=hero.pos;

            ArrayList<Mob> targets =  new ArrayList<Mob>();

            KindOfWeapon curWep = Dungeon.hero.belongings.weapon;
            if (curWep == null) {
                GLog.i("You need a melee weapon to use this skill");
                return;
            }

            for (Mob mob : Dungeon.level.mobs) {
                if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY) {
                    targets.add( mob );
                }
            }

            hero.busy();

            if (Dungeon.level.heroFOV[hero.pos]) CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            hero.sprite.visible = false;
            Sample.INSTANCE.play( Assets.SND_PUFF );

            if(!targets.isEmpty())
            {
                doSlash(targets,0);
            }
            else {
                GLog.i("No enemy in sight");
                hero.sprite.visible = true;
                Sample.INSTANCE.play( Assets.SND_PUFF );
                hero.spendAndNext(1f);
            }


        }
    }

    private void doSlash(ArrayList<Mob> targets, int t){
        Hero hero = Dungeon.hero;

        Mob mob=targets.get(Random.Int(targets.size()));

        Wound.hit(mob.pos, Random.Int(360),new Callback() {
            @Override
            public void call() {
                mob.damage(hero.damageRoll(), hero);

                if (!mob.isAlive()){
                    GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", mob.name)) );
                    int exp = Dungeon.hero.lvl <= mob.maxLvl ? mob.EXP : 0;
                    if (exp > 0) {
                        Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
                        Dungeon.hero.earnExp(exp);
                    }
                    targets.remove(mob);
                }

                if(t>=9 || targets.isEmpty()){
                    hero.spendAndNext(1f);
                    if (Dungeon.level.heroFOV[hero.pos]) CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
                    hero.sprite.visible = true;
                    Sample.INSTANCE.play( Assets.SND_PUFF );
                }
                else {
                    doSlash(targets,t+1);
                }
            }
        });
    }

	@Override
	public int min(int lvl) {
		return  tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl) {
		return  5*(tier+1) +    //base
				lvl*(tier+1);   //level scaling
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		
		return damage;
	}
	
	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (Dungeon.hero.STR() > STRReq()){
				info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

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
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

}
