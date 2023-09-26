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

package com.unifier.arknightspixeldungeon.effects;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class Beam extends Image {
	
	protected static final double A = 180 / Math.PI;

    protected float duration;

    protected float timeLeft;

	private Beam(PointF s, PointF e, Effects.Type asset, float duration) {
		super( Effects.get( asset ) );
		
		origin.set( 0, height / 2 );
		
		x = s.x - origin.x;
		y = s.y - origin.y;
		
		float dx = e.x - s.x;
		float dy = e.y - s.y;
		angle = (float)(Math.atan2( dy, dx ) * A);
		scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / width;

		Sample.INSTANCE.play( Assets.SND_RAY );
		
		timeLeft = this.duration = duration;
	}

	public static class DeathRay extends Beam{
		public DeathRay(PointF s, PointF e){
			super(s, e, Effects.Type.DEATH_RAY, 0.5f);
		}
	}

	public static class LightRay extends Beam{
		public LightRay(PointF s, PointF e){
			super(s, e, Effects.Type.LIGHT_RAY, 1f);
		}
	}

	public static class HealthRay extends Beam{
		public HealthRay(PointF s, PointF e){
			super(s, e, Effects.Type.HEALTH_RAY, 0.5f);
		}
	}

    public static class UnSheathRay extends Beam{

        private Callback callback;

		protected float unSheathScaleMax;

		public UnSheathRay(PointF s, PointF e, Callback callback){
            super(s, e, Effects.Type.CHEN_UNSHEATH, 0.25f);

			float dx = e.x - s.x;
			float dy = e.y - s.y;
			angle = (float)(Math.atan2( dy, dx ) * A);
			//used in update()
			unSheathScaleMax = (float)Math.sqrt( dx * dx + dy * dy ) / width;
			scale.x = 0;

			this.callback = callback;
        }

        @Override
        public void update() {
            if ((timeLeft - Game.elapsed) <= 0) { if (callback != null) { callback.call(); } }//Note here is - to predict and call

			updateMotion();

			float p = timeLeft / duration;
			//alpha( p );
			scale.set( unSheathScaleMax * (1-p) , p );//in this way we can let the beam become a little more "shooting-like" with a cheap code cost

			if ((timeLeft -= Game.elapsed) <= 0) {
				killAndErase();
			}
        }
    }

	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / duration;
		alpha( p );
		scale.set( scale.x, p );
		
		if ((timeLeft -= Game.elapsed) <= 0) {
			killAndErase();
		}
	}
	
	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}
}
