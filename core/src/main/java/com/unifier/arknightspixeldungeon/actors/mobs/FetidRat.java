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

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.blobs.Blob;
import com.unifier.arknightspixeldungeon.actors.blobs.StenchGas;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Ooze;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FetidRat extends Rat {

	{
		spriteClass = FetidRatSprite.class;

		HP = HT = 20;
		defenseSkill = 5;

		EXP = 4;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
		properties.add(Property.DEMONIC);
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		GameScene.add(Blob.seed(pos, 20, StenchGas.class));

		return super.defenseProc(enemy, damage);
	}

    @Override
    public ArrayList<Integer> multipleDefenseProc(Char enemy, ArrayList<Integer> damage, ArrayList<Boolean> burstArray, int hittedTime) {

	    if(damage.size()>0) {
            GameScene.add(Blob.seed(pos, 20 * Random.Int(1, hittedTime), StenchGas.class));
        }//release random gas amount for now
        return super.multipleDefenseProc(enemy, damage, burstArray, hittedTime);

    }

	@Override
	public void die( Object cause ) {
		super.die( cause );

		FrostNovaQuestPlot.Quest.process();
	}
	
	{
		immunities.add( StenchGas.class );
	}
}