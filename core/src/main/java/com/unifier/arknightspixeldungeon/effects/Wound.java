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

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.utils.Callback;

public class Wound extends Image {

	private static final float TIME_TO_FADE = 0.8f;
	
	protected float time;

    protected Callback callback;

    public Wound() {
		super( Effects.get( Effects.Type.WOUND ));
		hardlight(1f, 0f, 0f);
		origin.set( width / 2, height / 2 );
	}

	public Wound(Callback callback)
    {
        super( Effects.get( Effects.Type.WOUND ) );
        hardlight(1f, 0f, 0f);
        origin.set( width / 2, height / 2 );

        this.callback=callback;
    }

    public Wound(Image image)
    {
        super( image );
        hardlight(1f, 0f, 0f);
        origin.set( width / 2, height / 2 );
        this.callback=callback;
    }

    public Wound(Image image,Callback callback)
    {
        super( image );
        hardlight(1f, 0f, 0f);
        origin.set( width / 2, height / 2 );
        this.callback=callback;
    }

    public void reset( int p ) {
		revive();

		x = (p % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;
		
		time = TIME_TO_FADE;
	}

	public void reset(Visual v) {
		revive();

		point(v.center(this));

		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
            if(callback != null)
                callback.call();
        } else {

			float p = time / fadeTime();
			alpha( p );
			scale.x = 1 + p;
		}
	}
	
	public static void hit( Char ch ) {
		hit( ch, 0 );
	}
	
	public static void hit( Char ch, float angle ) {
		if (ch.sprite.parent != null) {
			Wound w = (Wound) ch.sprite.parent.recycle(Wound.class);
			ch.sprite.parent.bringToFront(w);
			w.reset(ch.sprite);
			w.angle = angle;
		}
	}
	
	public static void hit( int pos ) {
		hit( pos, 0 );
	}
	
	public static void hit( int pos, float angle ) {
		Group parent = Dungeon.hero.sprite.parent;
		Wound w = (Wound)parent.recycle( Wound.class );
		parent.bringToFront( w );
		w.reset( pos );
		w.angle = angle;
    }

    public static void hit(int pos,float angle ,Callback callback) {
        Group parent = Dungeon.hero.sprite.parent;
        Wound w = (Wound)parent.recycle( Wound.class);
        parent.bringToFront( w );
        w.reset( pos );
        w.time = fadeTime();//0.8f is too slow,and even more if you have to wait it end then start the next
        w.angle = angle;
        w.callback=callback;
	}

    protected static float fadeTime(){ return 0.8f; }
}
