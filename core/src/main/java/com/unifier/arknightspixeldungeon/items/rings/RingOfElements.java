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

package com.unifier.arknightspixeldungeon.items.rings;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.blobs.Electricity;
import com.unifier.arknightspixeldungeon.actors.blobs.ToxicGas;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Charm;
import com.unifier.arknightspixeldungeon.actors.buffs.Chill;
import com.unifier.arknightspixeldungeon.actors.buffs.Corrosion;
import com.unifier.arknightspixeldungeon.actors.buffs.Frost;
import com.unifier.arknightspixeldungeon.actors.buffs.Ooze;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Poison;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.mobs.Eye;
import com.unifier.arknightspixeldungeon.actors.mobs.Shaman;
import com.unifier.arknightspixeldungeon.actors.mobs.Warlock;
import com.unifier.arknightspixeldungeon.actors.mobs.Yog;
import com.unifier.arknightspixeldungeon.levels.traps.DisintegrationTrap;
import com.unifier.arknightspixeldungeon.levels.traps.GrimTrap;

import java.util.HashSet;

public class RingOfElements extends Ring {
	
	@Override
	protected RingBuff buff( ) {
		return new Resistance();
	}
	
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Burning.class );
		RESISTS.add( Charm.class );
		RESISTS.add( Chill.class );
		RESISTS.add( Frost.class );
		RESISTS.add( Ooze.class );
		RESISTS.add( Paralysis.class );
		RESISTS.add( Poison.class );
		RESISTS.add( Corrosion.class );
		RESISTS.add( Weakness.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );
		
		RESISTS.add( ToxicGas.class );
		RESISTS.add( Electricity.class );
		
		RESISTS.add( Shaman.class );
		RESISTS.add( Warlock.class );
		RESISTS.add( Eye.class );
		RESISTS.add( Yog.BurningFist.class );
	}
	
	public static float resist( Char target, Class effect ){
		if (getBonus(target, Resistance.class) == 0) return 1f;
		
		for (Class c : RESISTS){
			if (c.isAssignableFrom(effect)){
				return (float)Math.pow(0.875, getBonus(target, Resistance.class));
			}
		}
		
		return 1f;
	}
	
	public class Resistance extends RingBuff {
	
	}
}
