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

package com.unifier.arknightspixeldungeon.actors.mobs.out_of_use_mob;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.food.MysteryMeat;
import com.unifier.arknightspixeldungeon.items.wands.Wand;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.out_of_use_sprite.GreatCrabSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;

import java.util.ArrayList;

public class GreatCrab extends Crab {

	{
		spriteClass = GreatCrabSprite.class;

		HP = HT = 25;
		defenseSkill = 0; //see damage()
		baseSpeed = 1f;

		EXP = 6;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
	}

	private int moving = 0;

	@Override
	protected boolean getCloser( int target ) {
		//this is used so that the crab remains slower, but still detects the player at the expected rate.
		moving++;
		if (moving < 3) {
			return super.getCloser( target );
		} else {
			moving = 0;
			return true;
		}

	}

	@Override
	public void damage( int dmg, Object src ){
		//crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
		//All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
		if ((enemySeen && state != SLEEPING && paralysed == 0)
				&& (src instanceof Wand || src instanceof Char)){
			GLog.n( Messages.get(this, "noticed") );
			sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked") );
		} else {
			super.damage( dmg, src );
		}
	}


    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime) {
        if ((enemySeen && state != SLEEPING && paralysed == 0)
                && (src instanceof Wand || src instanceof Char)){
            GLog.n( Messages.get(this, "noticed") );
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked") );
        } else {
            super.multipleDamage(burstArray,damageArray,src,hittedTime);
        }
    }

	@Override
	public void die( Object cause ) {
		super.die( cause );

		FrostNovaQuestPlot.Quest.process();

		Dungeon.level.drop( new MysteryMeat(), pos );
		Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
	}
}
