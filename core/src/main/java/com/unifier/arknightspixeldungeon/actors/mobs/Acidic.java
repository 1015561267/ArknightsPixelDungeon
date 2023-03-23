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

package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Ooze;
import com.unifier.arknightspixeldungeon.sprites.AcidicSprite;

import java.util.ArrayList;

public class Acidic extends Scorpio {

	{
		spriteClass = AcidicSprite.class;
		
		properties.add(Property.ACIDIC);
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

        if (Dungeon.level.adjacent(pos, enemy.pos)){
            Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
        }

		/*int dmg = Random.IntRange( 0, damage );
		if (dmg > 0) {
			enemy.damage( dmg, this );
			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail(getClass());
				GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name())));
			}
		}*/
		return super.defenseProc( enemy, damage );
	}

    @Override
    public ArrayList<Integer> multipleDefenseProc(Char enemy, ArrayList<Integer> damage, ArrayList<Boolean> burstArray, int hittedTime) {
	    //Update to newest SPD,Teller,2023-2-1
        if (Dungeon.level.adjacent(pos, enemy.pos)){
            Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
        }

        return super.multipleDefenseProc(enemy, damage, burstArray, hittedTime);
    }
}
