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

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Light;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ReflectTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.Terror;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.effects.Beam;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.particles.PurpleParticle;
import com.unifier.arknightspixeldungeon.items.Dewdrop;
import com.unifier.arknightspixeldungeon.items.wands.WandOfDisintegration;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Grim;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.EyeSprite;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Eye extends Mob {
	
	{
		spriteClass = EyeSprite.class;
		
		HP = HT = 100;
		defenseSkill = 20;
		viewDistance = Light.DISTANCE;
		
		EXP = 13;
		maxLvl = 25;
		
		flying = true;

		HUNTING = new Hunting();
		
		loot = new Dewdrop();
		lootChance = 0.5f;

		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll(Char enemy, boolean isMagic) {
		return Random.NormalIntRange(20, 30);
	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}
	
	private Ballistica beam;
	private int beamTarget = -1;
	private int beamCooldown;
	public boolean beamCharged;

	@Override
	protected boolean canAttack( Char enemy ) {

		if (beamCooldown == 0) {
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);

			if (enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)){
				beam = aim;
				beamTarget = aim.collisionPos;
				return true;
			} else
				//if the beam is charged, it has to attack, will aim at previous location of target.
				return beamCharged;
		} else
			return super.canAttack(enemy);
	}

	@Override
	protected boolean act() {
		if (beamCharged && state != HUNTING){
			beamCharged = false;
		}
		if (beam == null && beamTarget != -1) {
			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
			sprite.turnTo(pos, beamTarget);
		}
		if (beamCooldown > 0)
			beamCooldown--;
		return super.act();
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (beamCooldown > 0 || buff(Talent.ReprimandTracker.class) != null) {
			return super.doAttack(enemy);
		} else if (!beamCharged){
			((EyeSprite)sprite).charge( enemy.pos );
			spend( attackDelay()*2f );
			beamCharged = true;
			return true;
		} else {

			spend( attackDelay() );
			
			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);

			int reflectedPos = beam.collisionPos;

            for (int pos : beam.subPath(1, beam.dist)) {
                Char ch = Actor.findChar( pos );
                if (ch instanceof Hero && enemy.buff(ReflectTracker.class)!=null && enemy.buff(ReflectTracker.class).spendStack()) {
                    reflectedPos = ch.pos;
                    break;
                }
            }

            beam = new Ballistica(pos, reflectedPos, Ballistica.STOP_TERRAIN);

			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos] ) {
				sprite.zap( beam.collisionPos );
				return false;
			} else {
				deathGaze();
				return true;
			}
		}

	}

	@Override
	public void damage(int dmg, Object src) {
		if (beamCharged) dmg /= 4;
		super.damage(dmg, src);
	}

    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){
        ArrayList<Integer> tempArray = new ArrayList<>();

        if (beamCharged) {
            for(Integer dmg : damageArray){
                dmg /= 4;
                tempArray.add(dmg);
            }
            damageArray = tempArray;
        }
        super.multipleDamage(burstArray,damageArray,src,hittedTime);
    }

    protected int pretendDefenseFactor(int dmg, Object src) {
        if (beamCharged) dmg /= 4;
        return dmg;
    }


    public void deathGaze(){

	    boolean tracker = false;

	    Char hero = null;

		if (!beamCharged || beamCooldown > 0 || beam == null)
			return;

		beamCharged = false;
		beamCooldown = Random.IntRange(3, 6);

		boolean terrainAffected = false;

		for (int pos : beam.subPath(1, beam.dist)) {

			if (Dungeon.level.flamable[pos]) {

				Dungeon.level.destroy( pos );
				GameScene.updateMap( pos );
				terrainAffected = true;

			}

			Char ch = Actor.findChar( pos );
			if (ch == null) {
				continue;
			}


            if (ch instanceof Hero && enemy.buff(ReflectTracker.class)!=null && enemy.buff(ReflectTracker.class).canSpendStack()){
                //FIXME Death gaze involved in sprite handle,I haven't come up with a good way although merge it into other range attck handling is better
                tracker = true;
                hero = ch;
                break;
            }
            else magicHit(this,ch,false);
    }

		if (terrainAffected) {
			Dungeon.observe();
		}

		if(tracker)
        {
            enemy.sprite.turnTo(enemy.pos,this.pos);
            Char reflected = this;

            enemy.sprite.centerEmitter().burst( Speck.factory( Speck.FORGE ), 3 );
            Sample.INSTANCE.play( Assets.SND_EVOKE, 0.2f, 0.2f, 0.8f  );

            enemy.sprite.parent.add(new Beam.DeathRay(enemy.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(reflected.pos)));
            beam = new Ballistica(enemy.pos, reflected.pos, Ballistica.STOP_TERRAIN);

            for (int pos : beam.subPath(1, beam.dist)) {

                Char ch = Actor.findChar( pos );
                if (ch == null) {
                    continue;
                }
                if(ch.isAlive()) {
                    magicHit(reflected, ch,true);
                }
            }

        }
        beam = null;
        beamTarget = -1;
	}

    public void magicHit(Char from,Char to,Boolean byReflect){

        if (hit( from, to, true )) {

            int bonus = 0;
            if(byReflect && from instanceof Hero && ((Hero) from).pointsInTalent(Talent.EYE_FOR_EYE) == 2){
                bonus += (int) (((Hero) enemy).rawdamageRoll(from,false) / (((Hero) enemy).belongings.weapon == null ? 1f : ((Hero) enemy).belongings.weapon.speedFactor(enemy)) * 0.33f);
            }

            int damage = Random.NormalIntRange( 30, 50 ) + bonus;

            to.damage( damage , this );

            if (Dungeon.level.heroFOV[pos]) {
                to.sprite.flash();
                CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            }

            if (!to.isAlive()) {
                if(to == Dungeon.hero){
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "deathgaze_kill") );
                }
                else if(byReflect){
                    Talent.afterReflectKill();
                }
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

	private static final String BEAM_TARGET     = "beamTarget";
	private static final String BEAM_COOLDOWN   = "beamCooldown";
	private static final String BEAM_CHARGED    = "beamCharged";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( BEAM_TARGET, beamTarget);
		bundle.put( BEAM_COOLDOWN, beamCooldown );
		bundle.put( BEAM_CHARGED, beamCharged );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(BEAM_TARGET))
			beamTarget = bundle.getInt(BEAM_TARGET);
		beamCooldown = bundle.getInt(BEAM_COOLDOWN);
		beamCharged = bundle.getBoolean(BEAM_CHARGED);
	}

	{
		resistances.add( WandOfDisintegration.class );
		resistances.add( Grim.class );
	}
	
	{
		immunities.add( Terror.class );
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			//even if enemy isn't seen, attack them if the beam is charged
			if (beamCharged && enemy != null && canAttack(enemy)) {
				enemySeen = enemyInFOV;
				return doAttack(enemy);
			}
			return super.act(enemyInFOV, justAlerted);
		}
	}



}
