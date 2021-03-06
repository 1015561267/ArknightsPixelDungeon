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

package com.unifier.arknightspixeldungeon.items.armor.glyphs;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;

public class Stone extends Armor.Glyph {

	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x222222 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		
		testing = true;
		float evasion = defender.defenseSkill(attacker);
		float accuracy = attacker.attackSkill(defender);
		testing = false;
		
		float hitChance;
		if (evasion >= accuracy){
			hitChance = 1f - (1f - (accuracy/evasion))/2f;
		} else {
			hitChance = 1f - (evasion/accuracy)/2f;
		}
		
		//60% of dodge chance is applied as damage reduction
		hitChance = (2f + 3f*hitChance)/5f;
		
		damage = (int)Math.ceil(damage * hitChance);
		
		return damage;
	}
	
	private boolean testing = false;
	
	public boolean testingEvasion(){
		return testing;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return GREY;
	}

}
