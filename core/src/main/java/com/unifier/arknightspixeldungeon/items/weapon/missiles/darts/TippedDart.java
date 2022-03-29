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

package com.unifier.arknightspixeldungeon.items.weapon.missiles.darts;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.PinCushion;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.Recipe;
import com.unifier.arknightspixeldungeon.plants.Blindweed;
import com.unifier.arknightspixeldungeon.plants.Dreamfoil;
import com.unifier.arknightspixeldungeon.plants.Earthroot;
import com.unifier.arknightspixeldungeon.plants.Fadeleaf;
import com.unifier.arknightspixeldungeon.plants.Firebloom;
import com.unifier.arknightspixeldungeon.plants.Icecap;
import com.unifier.arknightspixeldungeon.plants.Plant;
import com.unifier.arknightspixeldungeon.plants.Rotberry;
import com.unifier.arknightspixeldungeon.plants.Sorrowmoss;
import com.unifier.arknightspixeldungeon.plants.Starflower;
import com.unifier.arknightspixeldungeon.plants.Stormvine;
import com.unifier.arknightspixeldungeon.plants.Sungrass;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TippedDart extends Dart {
	
	{
		bones = true;
	}
	
	@Override
	public int STRReq(int lvl) {
		return 11;
	}
	
	@Override
	protected void rangedHit(Char enemy, int cell) {
		if (enemy.isAlive())
			Buff.affect(enemy, PinCushion.class).stick(new Dart());
		else
			Dungeon.level.drop( new Dart(), enemy.pos ).sprite.drop();
	}
	
	@Override
	public int price() {
		return 6 * quantity;
	}
	
	private static HashMap<Class<?extends Plant.Seed>, Class<?extends TippedDart>> types = new HashMap<>();
	static {
		types.put(Blindweed.Seed.class,     BlindingDart.class);
		types.put(Dreamfoil.Seed.class,     SleepDart.class);
		types.put(Earthroot.Seed.class,     ParalyticDart.class);
		types.put(Fadeleaf.Seed.class,      DisplacingDart.class);
		types.put(Firebloom.Seed.class,     IncendiaryDart.class);
		types.put(Icecap.Seed.class,        ChillingDart.class);
		types.put(Rotberry.Seed.class,      RotDart.class);
		types.put(Sorrowmoss.Seed.class,    PoisonDart.class);
		types.put(Starflower.Seed.class,    HolyDart.class);
		types.put(Stormvine.Seed.class,     ShockingDart.class);
		types.put(Sungrass.Seed.class,      HealingDart.class);
	}
	
	public static TippedDart randomTipped(){
		Plant.Seed s;
		do{
			s = (Plant.Seed) Generator.random(Generator.Category.SEED);
		} while (!types.containsKey(s.getClass()));
		
		try{
			return (TippedDart) types.get(s.getClass()).newInstance().quantity(2);
		} catch (Exception e) {
			ArknightsPixelDungeon.reportException(e);
			return null;
		}
		
	}
	
	public static class TipDart extends Recipe{
		
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;
			
			if (ingredients.get(0).getClass() == Dart.class){
				if (!(ingredients.get(1) instanceof Plant.Seed)){
					return false;
				}
			} else if (ingredients.get(0) instanceof Plant.Seed){
				if (ingredients.get(1).getClass() == Dart.class){
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}
			
			Plant.Seed seed = (Plant.Seed) ingredients.get(1);
			
			if (ingredients.get(0).quantity() >= 2
					&& seed.quantity() >= 1
					&& types.containsKey(seed.getClass())){
				return true;
			}
			
			return false;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 2;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			ingredients.get(0).quantity(ingredients.get(0).quantity() - 2);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);
			
			try{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				ArknightsPixelDungeon.reportException(e);
				return null;
			}
			
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			try{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				ArknightsPixelDungeon.reportException(e);
				return null;
			}
		}
	}
}
