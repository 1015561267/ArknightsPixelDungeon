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

package com.unifier.arknightspixeldungeon.actors;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.blobs.Blob;
import com.unifier.arknightspixeldungeon.actors.blobs.Electricity;
import com.unifier.arknightspixeldungeon.actors.blobs.ToxicGas;
import com.unifier.arknightspixeldungeon.actors.buffs.Bleeding;
import com.unifier.arknightspixeldungeon.actors.buffs.Bless;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Charm;
import com.unifier.arknightspixeldungeon.actors.buffs.Chill;
import com.unifier.arknightspixeldungeon.actors.buffs.Corrosion;
import com.unifier.arknightspixeldungeon.actors.buffs.Corruption;
import com.unifier.arknightspixeldungeon.actors.buffs.Cripple;
import com.unifier.arknightspixeldungeon.actors.buffs.Doom;
import com.unifier.arknightspixeldungeon.actors.buffs.EarthImbue;
import com.unifier.arknightspixeldungeon.actors.buffs.FireImbue;
import com.unifier.arknightspixeldungeon.actors.buffs.Frost;
import com.unifier.arknightspixeldungeon.actors.buffs.Hex;
import com.unifier.arknightspixeldungeon.actors.buffs.Hunger;
import com.unifier.arknightspixeldungeon.actors.buffs.MagicalSleep;
import com.unifier.arknightspixeldungeon.actors.buffs.Ooze;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Poison;
import com.unifier.arknightspixeldungeon.actors.buffs.Preparation;
import com.unifier.arknightspixeldungeon.actors.buffs.Slow;
import com.unifier.arknightspixeldungeon.actors.buffs.Speed;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.CounterStrikeTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.WellPreparedTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.Vertigo;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroSubClass;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.mobs.Shaman;
import com.unifier.arknightspixeldungeon.effects.Lightning;
import com.unifier.arknightspixeldungeon.effects.MagicMissile;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.armor.glyphs.Potential;
import com.unifier.arknightspixeldungeon.items.rings.RingOfElements;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.unifier.arknightspixeldungeon.items.wands.WandOfFireblast;
import com.unifier.arknightspixeldungeon.items.wands.WandOfLightning;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Blazing;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Grim;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Shocking;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.MissileWeapon;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Shuriken;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.Dart;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.unifier.arknightspixeldungeon.levels.Terrain;
import com.unifier.arknightspixeldungeon.levels.features.Chasm;
import com.unifier.arknightspixeldungeon.levels.features.Door;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Char extends Actor {
	
	public int pos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";
	
	public int HT;
	public int HP;
	public int SHLD;
	
	protected float baseSpeed	= 1;
	protected PathFinder.Path path;

	public int paralysed	    = 0;
	public boolean rooted		= false;
	public boolean flying		= false;
	public int invisible		= 0;

    public int talentTiers(){return 5;};

    //these are relative to the hero
	public enum Alignment{
		ENEMY,
		NEUTRAL,
		ALLY
	}
	public Alignment alignment;
	
	public int viewDistance	= 8;
	
	public boolean[] fieldOfView = null;
	
	private HashSet<Buff> buffs = new HashSet<>();

    public boolean charInView(Char target)
    {
        return fieldOfView[target.pos];
    }

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );
		return false;
	}
	
	protected static final String POS       = "pos";
	protected static final String TAG_HP    = "HP";
	protected static final String TAG_HT    = "HT";
	protected static final String TAG_SHLD  = "SHLD";
	protected static final String BUFFS	    = "buffs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( TAG_SHLD, SHLD );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		SHLD = bundle.getInt( TAG_SHLD );
		
		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}

	public void heal(Object source,int amount)//I have to make it more clear for some method need consult heal amount
    {
        HP = Math.min( HT , HP + amount );
    }

	public boolean attack( Char enemy ) {

		if (enemy == null || !enemy.isAlive()) return false;
		
		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

        if (enemy.isInvulnerable(getClass())) {

            if (visibleFight) {
                enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "invulnerable") );
                Sample.INSTANCE.play(Assets.SND_GOLD, 1f, Random.Float(0.96f, 1.05f));
            }
            return false;
        }

		else if (hit( this, enemy, false )) {

			int dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null){
				dmg = prep.damageRoll(this, enemy);
			} else {
				dmg = damageRoll(enemy,false);//There might be other hit check so raw damageroll function need to be improved
			}


			int dr = enemy.drRoll();
            if(enemy.buff(Talent.LightWeaponMasteryTracker.class)!=null)
            {
                dr /= 2 ;
                enemy.buff(Talent.LightWeaponMasteryTracker.class).detach();
            }
            if (this instanceof Hero){
                Hero h = (Hero)this;
                if (h.belongings.weapon instanceof MissileWeapon
                        && h.subClass == HeroSubClass.SNIPER){
                    dr = 0;
                }
            }

			if (enemy == Dungeon.hero && Dungeon.hero.buff(Talent.ParryTrackerUsing.class) != null) {
				dmg = 0;
			}

			if (enemy == Dungeon.hero && Dungeon.hero.buff(Talent.ParryTrackerPrepare.class) != null) {
				CounterStrikeTracker counterStrikeTracker = Dungeon.hero.buff(CounterStrikeTracker.class);
				if (counterStrikeTracker != null && counterStrikeTracker.absorbDamage == -1 && counterStrikeTracker.time == -1) {
					if (Dungeon.hero.pointsInTalent(Talent.COUNTER_STRIKE) == 1) {
						counterStrikeTracker.absorbDamage = dmg / 2;
						counterStrikeTracker.time = 1;
					} else if (Dungeon.hero.pointsInTalent(Talent.COUNTER_STRIKE) == 2) {
						counterStrikeTracker.absorbDamage = dmg / 4;
						counterStrikeTracker.time = 3;
					}
				}

				dmg = 0;
				Buff.affect(Dungeon.hero, Talent.ParryTrackerUsing.class,1f);
				Dungeon.hero.buff(Talent.ParryTrackerPrepare.class).detach();

				if (Dungeon.hero.pointsInTalent(Talent.PARRY) == 2) {
					Buff.affect(this,Paralysis.class,3f);
				}
			}

			int effectiveDamage = enemy.defenseProc( this, dmg );
			effectiveDamage = Math.max( effectiveDamage - dr, 0 );

            if ( enemy.buff( Vulnerable.class ) != null){
                effectiveDamage *= 1.33f;
            }

			effectiveDamage = attackProc( enemy, effectiveDamage );



			if (visibleFight) {
				Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			}

			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
			if (!enemy.isAlive()){
				return true;
			}

			//TODO: consider revisiting this and shaking in more cases.
			float shake = 0f;
			if (enemy == Dungeon.hero)
				shake = effectiveDamage / (enemy.HT / 4);

			if (shake > 1f)
				Camera.main.shake( GameMath.gate( 1, shake, 5), 0.3f );

			enemy.damage( effectiveDamage, this );

			if (buff(FireImbue.class) != null)
				buff(FireImbue.class).proc(enemy);
			if (buff(EarthImbue.class) != null)
				buff(EarthImbue.class).proc(enemy);

			enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
			enemy.sprite.flash();

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {

					Dungeon.fail( getClass() );
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name)) );

				} else if (this == Dungeon.hero) {
					GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)) );
				}
			}

			if(this instanceof Hero)
			    Talent.doAfterDamage((Hero) this,enemy,effectiveDamage);

			return true;
			
		} else {
			
			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
				
				Sample.INSTANCE.play(Assets.SND_MISS);
			}
			
			return false;
			
		}
	}

    public boolean attack( Char enemy , rangeType type) {//physical part of reflect talent,magical part can be found in mob.java

        if (enemy == null || !enemy.isAlive()) return false;

        boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

        if (enemy.isInvulnerable(getClass())) {

            if (visibleFight) {
                enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "invulnerable") );
                Sample.INSTANCE.play(Assets.SND_GOLD, 1f, Random.Float(0.96f, 1.05f));
            }
            return false;
        }

        if(enemy instanceof Hero && type != rangeType.Dismiss && ((Hero) enemy).hasTalent(Talent.REFLECT)){

            enemy.sprite.turnTo(enemy.pos,this.pos);

            Char reflected = this;
            enemy.sprite.centerEmitter().burst( Speck.factory( Speck.FORGE ), 3 );
            Sample.INSTANCE.play( Assets.SND_EVOKE, 0.2f, 0.2f, 0.8f  );

            Item sprite;
            switch (type){
                default:
                    case GnollTrickster:sprite = new ParalyticDart();break;
                    case Tengu:sprite = new Shuriken();break;
                    case Scorpio:sprite = new Dart();break;
            }

            //With ranged physical attack somehow same in anime,the only variable is the sprite
            ((MissileSprite)reflected.sprite.parent.recycle( MissileSprite.class )).
                    reset( enemy.pos,reflected.pos, sprite, new Callback() {
                        @Override
                        public void call() {

                            if(reflected==null || !reflected.isAlive())
                            {
                                return;
                            }

                            int dmg = reflected.damageRoll(reflected,false);
                            int dr = reflected.drRoll();

                            int effectiveDamage = reflected.defenseProc( reflected, dmg );
                            effectiveDamage = Math.max( effectiveDamage - dr, 0 );
                            effectiveDamage = attackProc( reflected, effectiveDamage );

                            if (visibleFight) {
                                Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
                            }

                            reflected.damage( effectiveDamage, this );
                            reflected.sprite.bloodBurstA( reflected.sprite.center(), effectiveDamage );
                            reflected.sprite.flash();
                            if (!reflected.isAlive() && visibleFight) {
                                GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", name)) );
                                //Actor.remove(reflected);
                            }
                        }
                    } );
            return false;
        }
        else return attack(enemy);
    }
	
	public static boolean hit( Char attacker, Char defender, boolean magic ) {
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );

		if (attacker.buff(Bless.class) != null) acuRoll *= 1.25f;
        if (attacker.buff(Hex.class) != null) acuRoll *= 0.8f;

		if (defender.buff(Bless.class) != null) defRoll *= 1.25f;
        if (defender.buff(Hex.class) != null) defRoll *= 0.8f;

        if (defender instanceof Hero && defender.buff(WellPreparedTracker.class) != null) {
        	int newTime = defender.buff(WellPreparedTracker.class).time - 1;

        	if (newTime == 0) {
				defender.buff(WellPreparedTracker.class).detach();
			} else {
				defender.buff(WellPreparedTracker.class).setTime(newTime);
			}

        	return false;
		}

        return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
	}
    protected boolean doMagicAttack( Char enemy ,magicType type) {

        boolean visible = Dungeon.level.heroFOV[pos];

        Char reflected = this;

        if (enemy instanceof Hero && type != magicType.Dismiss && ((Hero) enemy).hasTalent(Talent.REFLECT)){

            enemy.sprite.turnTo(enemy.pos,this.pos);

            enemy.sprite.centerEmitter().burst( Speck.factory( Speck.FORGE ), 3 );
            Sample.INSTANCE.play( Assets.SND_EVOKE, 0.2f, 0.2f, 0.8f  );


            switch (type){
                default:
                    case Dismiss: break;
                    case Shaman: enemy.sprite.parent.add( new Lightning( enemy.pos, reflected.pos, (Shaman)reflected ) );break;
                    case Warlock:	MagicMissile.boltFromChar( enemy.sprite.parent,
                            MagicMissile.SHADOW,
                            enemy.sprite,
                            reflected.pos,
                            new Callback() {
                        @Override
                        public void call() {
                            if(reflected!=null && reflected.isAlive())
                            {
                                magicHit(enemy,reflected);
                            }
                            next();
                        }
                    } );break;
                    case Eye:
                        break;//Eye is somehow different as gaze radial involve in sprite handle,see deathgaze() in Eye.java for more info
            }
            return true;
        }
        else return false;
    }

    public void magicHit(Char from,Char to){
        if (hit( from, to, true )) {

        } else {
            to.sprite.showStatus( CharSprite.NEUTRAL,to.defenseVerb() );
        }
    }



	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}
	
	public int drRoll() {
		return 0;
	}
	
	public int damageRoll(Char enemy, boolean isMagic) {
		return 1;
	}
	
	public int attackProc( Char enemy, int damage ) {
        if ( buff(Weakness.class) != null ){
            damage *= 0.67f;
        }
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage ) {
		return damage;
	}
	
	public float speed() {
		return buff( Cripple.class ) == null ? baseSpeed : baseSpeed * 0.5f;
	}
	
	public void damage( int dmg, Object src ) {
		
		if (!isAlive() || dmg < 0) {
			return;
		}

        if(isInvulnerable(src.getClass())){
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
            return;
        }

		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null){
			dmg *= 2;
		}
		
		Class<?> srcClass = src.getClass();
		if (isImmune( srcClass )) {
			dmg = 0;
		} else {
			dmg = Math.round( dmg * resist( srcClass ));
		}
		
		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		//FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
		if (src instanceof Hunger || SHLD == 0){
			HP -= dmg;
		} else if (SHLD >= dmg){
			SHLD -= dmg;
		} else if (SHLD > 0) {
			HP -= (dmg - SHLD);
			SHLD = 0;
		}

		if(this instanceof Hero) {
            Talent.onHealthLose((Hero) this, src, Math.min(dmg, HP));
        }

		sprite.showStatus( HP > HT / 2 ?
			CharSprite.WARNING :
			CharSprite.NEGATIVE,
			Integer.toString( dmg ) );

		if (HP < 0) HP = 0;

		if (!isAlive()) {
			die( src );
		}
	}
	
	public void destroy() {
		HP = 0;
		Actor.remove( this );
	}
	
	public void die( Object src ) {
		destroy();
		if (src != Chasm.class) sprite.die();
	}
	
	public boolean isAlive() {
		return HP > 0;
	}
	
	@Override
	protected void spend( float time ) {
		
		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}
		
		super.spend( time / timeScale );
	}
	
	public synchronized HashSet<Buff> buffs() {
		return new HashSet<>(buffs);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized void add( Buff buff ) {
		
		buffs.add( buff );
		Actor.add( buff );

		if (sprite != null)
			switch(buff.type){
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, buff.toString()); break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, buff.toString());break;
				case NEUTRAL:
					sprite.showStatus(CharSprite.NEUTRAL, buff.toString()); break;
				case SILENT: default:
					break; //show nothing
			}

	}
	
	public synchronized void remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );

	}
	
	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}
	
	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
			buff.detach();
		}
	}
	
	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}
	
	public int stealth() {
		return 0;
	}

    public boolean isInvulnerable(Class effect){return false;};

    public void move(int step ) {

		if (Dungeon.level.adjacent( step, pos ) && buff( Vertigo.class ) != null) {
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos]) || Actor.findChar( newPos ) != null)
				return;
			else {
				sprite.move(pos, newPos);
				step = newPos;
			}
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		pos = step;
		
		if (flying && Dungeon.level.map[pos] == Terrain.DOOR) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}
		
		if (!flying) {
			Dungeon.level.press( pos, this );
		}
	}
	
	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}

	public enum rangeType{
        Dismiss,//With some should not be considered able to reflect
        GnollTrickster,Tengu,Scorpio
    }

    public enum magicType{
        Dismiss,Shaman,Eye,Warlock
    }

	public void onAttackComplete(rangeType Type) {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	protected final HashSet<Class> resistances = new HashSet<>();
	
	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Class effect ){
		HashSet<Class> resists = new HashSet<>(resistances);
		for (Property p : properties()){
			resists.addAll(p.resistances());
		}
		for (Buff b : buffs()){
			resists.addAll(b.resistances());
		}
		
		float result = 1f;
		for (Class c : resists){
			if (c.isAssignableFrom(effect)){
				result *= 0.5f;
			}
		}
		return result * RingOfElements.resist(this, effect);
	}
	
	protected final HashSet<Class> immunities = new HashSet<>();
	
	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}
		
		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		return false;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		return new HashSet<>(properties);
	}

	public enum Property{
		BOSS ( new HashSet<Class>( Arrays.asList(Grim.class, ScrollOfPsionicBlast.class)),
				new HashSet<Class>( Arrays.asList(Corruption.class) )),
		MINIBOSS ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Corruption.class) )),
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),
		BLOB_IMMUNE ( new HashSet<Class>(),
				new HashSet<Class>( Arrays.asList(Blob.class) )),
		FIERY ( new HashSet<Class>( Arrays.asList(WandOfFireblast.class)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),
		ACIDIC ( new HashSet<Class>( Arrays.asList(ToxicGas.class, Corrosion.class)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),
		ELECTRIC ( new HashSet<Class>( Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class, Electricity.class, ShockingDart.class)),
				new HashSet<Class>()),
		IMMOVABLE;
		
		private HashSet<Class> resistances;
		private HashSet<Class> immunities;
		
		Property(){
			this(new HashSet<Class>(), new HashSet<Class>());
		}
		
		Property( HashSet<Class> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}
		
		public HashSet<Class> resistances(){
			return new HashSet<>(resistances);
		}
		
		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}
	}
}
