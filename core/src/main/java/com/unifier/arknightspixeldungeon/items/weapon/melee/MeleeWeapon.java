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

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Blindness;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Cripple;
import com.unifier.arknightspixeldungeon.actors.buffs.Hex;
import com.unifier.arknightspixeldungeon.actors.buffs.Invisibility;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Vertigo;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MeleeWeapon extends Weapon {
	
	public int tier;

    //public static final String AC_REDDUCK = "REDDUCK";//绝影

    public ArrayList<type> weaponType()
    {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
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
	public int damageRoll(Char owner ,Char enemy ,boolean isMagic ) {
        int min = min();
        int max = max();

        int damage = 0;

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();

            if(((Hero) owner).hasTalent(Talent.WEAPON_ADAPT))
            {
                int buffedStr = Math.min(3,exStr);//first 3 str have double effect

                min += buffedStr;
                max += buffedStr * 2;
                exStr -= buffedStr;
            }

            if(((Hero) owner).hasTalent(Talent.LIGHT_WEAPON_MASTERY)){
                min += tier;
                max += level();

                if(((Hero) owner).pointsInTalent(Talent.LIGHT_WEAPON_MASTERY)==2){
                    Buff.affect(((Hero) owner).enemy(), Talent.LightWeaponMasteryTracker.class);//check Char.attack for more info(in 0.6.5 it consult before drroll and drroll happens before damageRoll,so it changed for convenience
                }
            }

            if (exStr > 0) {
                damage += Random.IntRange( 0, exStr );
            }

            int rawDamage = Random.NormalIntRange(min,max);

            int agumentDamage = augment.damageFactor(rawDamage);

            if(((Hero) owner).hasTalent(Talent.FULL_SUPPRESSION))
            {
                if( enemy.buff(Weakness.class) != null || enemy.buff(Vulnerable.class) != null || enemy.buff(Cripple.class) != null || enemy.buff(Blindness.class) != null || enemy.buff(Vertigo.class) != null || enemy.buff(Hex.class) != null || enemy.buff(Paralysis.class) != null)
                {
                    agumentDamage += agumentDamage * Random.Float(0.1f,0.3f);
                }
            }

            damage += agumentDamage;

            return damage;
        }

		return damage;
	}

	@Override
    public int rawdamageRoll( Char owner ,Char enemy ,boolean isMagic) {
        return augment.damageFactor(Random.NormalIntRange( min(), max() ));
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

    @Override
    protected void onThrow( int cell ) {
        Char enemy = Actor.findChar(cell);

        if ( curUser.hasTalent(Talent.WEAPON_THROW) && enemy != null && enemy instanceof Mob && enemy.alignment == Char.Alignment.ENEMY ) {
            curUser.attack( enemy );
            Invisibility.dispel();
        }

        super.onThrow(cell);
    }
}
