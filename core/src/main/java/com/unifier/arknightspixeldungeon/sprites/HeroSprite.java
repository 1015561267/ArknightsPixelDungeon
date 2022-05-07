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

package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

public class HeroSprite extends CharSprite {
	
	private static final int FRAME_WIDTH	= 13;
	private static final int FRAME_HEIGHT	= 17;

    private static final int SKILL_WIDTH	= 17;
    private static final int SKILL_HEIGHT	= 17;

	private static final int RUN_FRAMERATE	= 20;
	
	private static TextureFilm tiers;
	
	private Animation fly;
	private Animation read;

    public enum skillAnimationType{
        unsheath_start,unsheath_over,shadowless_start,shadowless_over
    }


	public HeroSprite() {
		super();

		link( Dungeon.hero );

        texture( Dungeon.hero.heroClass.spritesheet() );
		updateArmor();

		if (ch.isAlive())
			idle();
		else
			die();
	}
	
	public void updateArmor() {

		TextureFilm film = new TextureFilm( tiers(), ((Hero)ch).tier(), FRAME_WIDTH, FRAME_HEIGHT );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );
		
		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10 );
		
		attack = new Animation( 15, false );
		attack.frames( film, 11, 12, 13, 0 );
		
		zap = attack.clone();
		
		operate = new Animation( 8, false );
		operate.frames( film, 14, 15, 14, 15 );
		
		fly = new Animation( 1, true );
		fly.frames( film, 16 );

		read = new Animation( 20, false );
		read.frames( film, 17, 18, 18, 18, 18, 18, 18, 18, 18, 17 );

		if (ch.isAlive())
			idle();
		else
			die();
	}

    @Override
	public void place( int p ) {
		super.place( p );
		//Camera.main.target = this;
	}

	@Override
	public void move( int from, int to ) {
		super.move( from, to );
		if (ch.flying) {
			play( fly );
		}

		//Camera.main.target = this;
        Camera.main.panFollow(this, 20f);
	}

	@Override
	public void jump( int from, int to, Callback callback ) {
		super.jump( from, to, callback );
		play( fly );
	}

	public void read() {
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				ch.onOperateComplete();
			}
		};
		play( read );
	}

	@Override
	public void bloodBurstA(PointF from, int damage) {
		//Does nothing.

		/*
		 * This is both for visual clarity, and also for content ratings regarding violence
		 * towards human characters. The heroes are the only human or human-like characters which
		 * participate in combat, so removing all blood associated with them is a simple way to
		 * reduce the violence rating of the game.
		 */
	}

	@Override
	public void update() {
		sleeping = ch.isAlive() && ((Hero)ch).resting;
		
		super.update();
	}
	
	public void sprint( float speed ) {
		run.delay = 1f / speed / RUN_FRAMERATE;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.ROGUE );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		return tiers;
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
		
		RectF patch = tiers().get( armorTier );
		Image avatar = new Image( cl.spritesheet() );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.shift( patch.left, patch.top );
		avatar.frame( frame );
		
		return avatar;
	}

    public static Image Portrait(HeroClass cl, int armorTier) {
        int row = -1;
        switch (cl) {
            case WARRIOR:
                row = 0;
                break;
            case MAGE:
                row = 1;
                break;
            case ROGUE:
                row = 2;
                break;
            case HUNTRESS:
                row = 3;
                break;
        }

        return new Image(Assets.PORTRAITS, 0, 35* row, 36, 35);
    }

    public void setSkillCallbackAnimation(Callback callback,skillAnimationType skillAnimationType)
    {
        switch (skillAnimationType)
        {
            case unsheath_start:
            case unsheath_over:
                texture(Assets.UNSHEATH);
                break;
            case shadowless_start:
            case shadowless_over:
               texture(Assets.SHADOWLESS);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + skillAnimationType);
        }


        TextureFilm frames;
        Animation animation;

        switch (skillAnimationType)
        {
            case unsheath_start:
                frames = new TextureFilm( texture, 16, SKILL_HEIGHT );
                animation = new Animation(10,false);
                animation.frames(frames,0,1,2,3,4);
                break;
            case unsheath_over:
                frames = new TextureFilm( texture, 16, SKILL_HEIGHT );
                animation = new Animation(8,false);
                animation.frames(frames,5,6,7,8);
                break;
            case shadowless_start:
                frames = new TextureFilm( texture, SKILL_WIDTH, SKILL_HEIGHT );
                animation = new Animation(30,false);
                animation.frames(frames,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33);
                break;
            case shadowless_over:
                frames = new TextureFilm( texture, SKILL_WIDTH, SKILL_HEIGHT );
                animation = new Animation(20,false);
                animation.frames(frames,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + skillAnimationType);
        }

        animCallback = callback;
        play(animation);
    }

    public void setAfterSkillAnimation()
    {
        texture( Dungeon.hero.heroClass.spritesheet() );
        updateArmor();
        if (ch.isAlive())
            idle();
        else
            die();
    }
}
