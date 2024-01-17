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
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.particles.ShaftParticle;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;

public class GhostSprite extends MobSprite {

    public Animation leave;

	public GhostSprite() {
		super();
		
		texture( Assets.GHOST );

        TextureFilm frames = new TextureFilm( texture, 29, 31 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0, 1, 2, 3, 0, 1, 4, 3);

        run = new Animation( 8, true );
        run.frames( frames, 5, 6, 7, 8, 9, 10, 11);

        attack = new Animation( 15, false );
        attack.frames( frames, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);

        die = new Animation( 7, false );
        die.frames( frames, 22, 23, 24, 25, 26 );

        leave = new Animation( 8, false );
        leave.frames( frames, 0, 1, 2, 3, 0, 1, 4, 3);

        play( idle );
	}

    @Override
    public void play(Animation anim) {
        //Shouldn't interrupt the dieing animation
        if (curAnim == null || curAnim != die || curAnim != leave) {
            super.play(anim);
        }
    }
	
	@Override
	public void draw() {
		Blending.setLightMode();
        Blending.setNormalMode();
        super.draw();
	}

    @Override
    public void die() {
        super.die();
        emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
        emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
    }

	public void leave() {
        sleeping = false;
        play(leave);
        if (emo != null) {
            emo.killAndErase();
        }
        if (health != null){
            health.killAndErase();
        }
		emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
		emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
	}

    private static final float FADE_TIME	= 3f;

    @Override
    public void onComplete( Animation anim ) {

        super.onComplete( anim );

        if (anim == die || anim == leave) {
            parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
                @Override
                protected void onComplete() {
                    GhostSprite.this.killAndErase();
                    parent.erase( this );
                };
            } );
        }
    }
	
	@Override
	public int blood() {
		return 0xFFFFFF;
	}
}
