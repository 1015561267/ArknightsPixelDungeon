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

package com.unifier.arknightspixeldungeon.plants;

import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Bleeding;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Cripple;
import com.unifier.arknightspixeldungeon.actors.buffs.Drowsy;
import com.unifier.arknightspixeldungeon.actors.buffs.MagicalSleep;
import com.unifier.arknightspixeldungeon.actors.buffs.Poison;
import com.unifier.arknightspixeldungeon.actors.buffs.Slow;
import com.unifier.arknightspixeldungeon.actors.buffs.Vertigo;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfPurity;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.utils.GLog;

public class Dreamfoil extends Plant {

	{
		image = 10;
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		if (ch != null) {
			if (ch instanceof Mob)
				Buff.affect(ch, MagicalSleep.class);
			else if (ch instanceof Hero){
				GLog.i( Messages.get(this, "refreshed") );
				Buff.detach( ch, Poison.class );
				Buff.detach( ch, Cripple.class );
				Buff.detach( ch, Weakness.class );
				Buff.detach( ch, Bleeding.class );
				Buff.detach( ch, Drowsy.class );
				Buff.detach( ch, Slow.class );
				Buff.detach( ch, Vertigo.class);
			}
		}
	}

	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_DREAMFOIL;

			plantClass = Dreamfoil.class;
			alchemyClass = PotionOfPurity.class;
		}
	}
}