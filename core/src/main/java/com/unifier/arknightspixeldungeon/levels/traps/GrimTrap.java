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

package com.unifier.arknightspixeldungeon.levels.traps;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.MagicMissile;
import com.unifier.arknightspixeldungeon.effects.particles.ShadowParticle;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class GrimTrap extends Trap {

	{
		color = GREY;
		shape = LARGE_DOT;
	}

	@Override
	public Trap hide() {
		//cannot hide this trap
		return reveal();
	}

	@Override
	public void activate() {
		Char target = Actor.findChar(pos);

		//find the closest char that can be aimed at
		if (target == null){
			for (Char ch : Actor.chars()){
				Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
				if (bolt.collisionPos == ch.pos &&
						(target == null || Dungeon.level.trueDistance(pos, ch.pos) < Dungeon.level.trueDistance(pos, target.pos))){
					target = ch;
				}
			}
		}

		if (target != null){
			final Char finalTarget = target;
			final GrimTrap trap = this;
			int damage;
			
			//almost kill the player
			if (finalTarget == Dungeon.hero && ((float)finalTarget.HP/finalTarget.HT) >= 0.9f){
				damage = finalTarget.HP-1;
			//kill 'em
			} else {
				damage = finalTarget.HP;
			}
			
			final int finalDmg = damage;
			
			Actor.add(new Actor() {
				
				{
					//it's a visual effect, gets priority no matter what
					actPriority = VFX_PRIO;
				}
				
				@Override
				protected boolean act() {
					final Actor toRemove = this;
					((MagicMissile)finalTarget.sprite.parent.recycle(MagicMissile.class)).reset(
							MagicMissile.SHADOW,
							DungeonTilemap.tileCenterToWorld(pos),
							finalTarget.sprite.center(),
							new Callback() {
								@Override
								public void call() {
									finalTarget.damage(finalDmg, trap);
									if (finalTarget == Dungeon.hero) {
										Sample.INSTANCE.play(Assets.SND_CURSED);
										if (!finalTarget.isAlive()) {
											Dungeon.fail( GrimTrap.class );
											GLog.n( Messages.get(GrimTrap.class, "ondeath") );
										}
									} else {
										Sample.INSTANCE.play(Assets.SND_BURNING);
									}
									finalTarget.sprite.emitter().burst(ShadowParticle.UP, 10);
									Actor.remove(toRemove);
									next();
								}
							});
					return false;
				}
			});
		} else {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 10);
			Sample.INSTANCE.play(Assets.SND_BURNING);
		}
	}
}
