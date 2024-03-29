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
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.effects.Lightning;
import com.unifier.arknightspixeldungeon.effects.particles.SparkParticle;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.ShamanSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Shaman extends Mob implements Callback {

	private static final float TIME_TO_ZAP	= 1f;
	
	{
		spriteClass = ShamanSprite.class;
		
		HP = HT = 18;
		defenseSkill = 8;
		
		EXP = 6;
		maxLvl = 14;
		
		loot = Generator.Category.SCROLL;
		lootChance = 0.33f;
		
		properties.add(Property.ELECTRIC);
	}
	
	@Override
	public int damageRoll(Char enemy, boolean isMagic) {
		return Random.NormalIntRange( 2, 8 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.distance( pos, enemy.pos ) <= 1 || buff(Talent.ReprimandTracker.class) != null) {
			
			return super.doAttack( enemy );
			
		} else {
			
			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap( enemy.pos );
			}else sprite.parent.add( new Lightning( this.pos, enemy.pos, (Shaman)this) );

			spend( TIME_TO_ZAP );

			if(doMagicAttack(enemy,magicType.Shaman))
            {
                magicHit(enemy,this,true);
            }

			else magicHit(this,enemy,false);

			/*if (hit( this, enemy, true )) {
				int dmg = Random.NormalIntRange(3, 10);
				if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage( dmg, this );
				
				enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
				enemy.sprite.flash();
				
				if (enemy == Dungeon.hero) {
					
					Camera.main.shake( 2, 0.3f );
					
					if (!enemy.isAlive()) {
						Dungeon.fail( getClass() );
						GLog.n( Messages.get(this, "zap_kill") );
					}
				}
			} else {
				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}*/
			
			return !visible;
		}
	}
	
	@Override
	public void call() {
		next();
	}

    public void magicHit(Char from,Char to,Boolean byReflect){
        if (hit( from, to, true )) {
            int dmg = Random.NormalIntRange(3, 10);
            if (Dungeon.level.water[to.pos] && !to.flying) {
                dmg *= 1.5f;
            }

            int bonus = 0;
            if(byReflect && from instanceof Hero &&  ((Hero) from).pointsInTalent(Talent.EYE_FOR_EYE) == 2){
                bonus += (int) (((Hero) enemy).rawdamageRoll(from,false) / (((Hero) enemy).belongings.weapon == null ? 1f : ((Hero) enemy).belongings.weapon.speedFactor(enemy)) * 0.33f);
            }

            int damage = dmg + bonus;

            to.damage( damage, from );

            to.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
            to.sprite.flash();

            if (to == Dungeon.hero) {
                Camera.main.shake( 2, 0.3f );
                if (!to.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "zap_kill") );
                }
            }

            if (!to.isAlive() && byReflect) {
                Talent.afterReflectKill();
            }


            if(byReflect) {
                Talent.doAfterReflect(damage);
            }

        } else {
            to.sprite.showStatus( CharSprite.NEUTRAL,  to.defenseVerb() );

            if(to instanceof Hero){
                Talent.onDodge();
            }
        }
    }
}
