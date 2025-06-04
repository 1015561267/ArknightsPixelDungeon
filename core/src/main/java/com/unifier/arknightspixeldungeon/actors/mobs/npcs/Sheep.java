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

package com.unifier.arknightspixeldungeon.actors.mobs.npcs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.BestBuddySprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.BigGuySprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.HotSpringBuskerSprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.HotSpringFluffBagSprite;
import com.unifier.arknightspixeldungeon.sprites.out_of_use_sprite.SheepSprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.WanderLandFrisbeeSprite;
import com.unifier.arknightspixeldungeon.sprites.sheep_sprite.WanderLandStellarCasterSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Sheep extends NPC {

	private static final String[] LINE_KEYS = {"Baa!", "Baa?", "Baa.", "Baa..."};

	{
		spriteClass = SheepSprite.class;
	}

	public float lifespan;

	private boolean initialized = false;

	@Override
	protected boolean act() {
		if (initialized) {
			HP = 0;

			destroy();
			sprite.die();

		} else {
			initialized = true;
			spend( lifespan + Random.Float(2) );
		}
		return true;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

	@Override
	public void add( Buff buff ) {
	}

	//@Override
	//public boolean interact() {
	//	sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, Random.element( LINE_KEYS )) );
	//	Dungeon.hero.spendAndNext(1f);
	//	return false;
	//}

    @Override
    public boolean interact(Char c) {
        sprite.showStatus( CharSprite.NEUTRAL, Messages.get(Sheep.class, Random.element( LINE_KEYS )) );
        if (c == Dungeon.hero) {
            Dungeon.hero.spendAndNext(1f);
            //Sample.INSTANCE.play(Assets.SND_SHEEP, 1, Random.Float(0.91f, 1.1f));
            //sheep summoned by woolly bomb can be dispelled by interacting
            if (lifespan >= 20){
                spend(-cooldown());
            }
        }
        return true;
    }

	public static Sheep randomSheep(){
		switch (Random.Int(6)){
			default:
			case 0: return new BestBuddy();
			case 1: return new BIGGUY();
			case 2: return new HotSpringFluffBag();
			case 3: return new HotSpringBusker();
			case 4: return new WanderLandFrisbee();
			case 5: return new WanderLandStellarCaster();
		}
	}

	protected static class BestBuddy extends Sheep{
		{
			spriteClass = BestBuddySprite.class;
		}
	}

	protected static class BIGGUY extends Sheep{
		{
			spriteClass = BigGuySprite.class;
		}
	}

	protected static class HotSpringBusker extends Sheep{
		{
			spriteClass = HotSpringBuskerSprite.class;
		}
	}

	protected static class HotSpringFluffBag extends Sheep{
		{
			spriteClass = HotSpringFluffBagSprite.class;
		}
	}

	protected static class WanderLandFrisbee extends Sheep{
		{
			spriteClass = WanderLandFrisbeeSprite.class;
		}
	}

	protected static class WanderLandStellarCaster extends Sheep{
		{
			spriteClass = WanderLandStellarCasterSprite.class;
		}
	}

}