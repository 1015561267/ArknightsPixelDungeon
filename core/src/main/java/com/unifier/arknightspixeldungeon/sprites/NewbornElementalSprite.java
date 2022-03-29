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
import com.unifier.arknightspixeldungeon.actors.Char;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class NewbornElementalSprite extends MobSprite{

	public NewbornElementalSprite() {
		super();

		texture( Assets.ELEMENTAL );

		int ofs = 17;

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new MovieClip.Animation( 20, true );
		idle.frames( frames, ofs+0, ofs+1,ofs+2,ofs+3 );

		run = new MovieClip.Animation( 20, true );
		run.frames( frames, ofs+0, ofs+1,ofs+2,ofs+3 );

		attack = new MovieClip.Animation( 15, false );
		attack.frames( frames, ofs+4, ofs+5 );

		die = new MovieClip.Animation( 15, false );
		die.frames( frames, ofs+6,ofs+7, ofs+8);

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );
		add( CharSprite.State.BURNING );
	}

	@Override
	public void die() {
		super.die();
		remove( CharSprite.State.BURNING );
	}

	@Override
	public int blood() {
		return 0xFFFF7D13;
	}

}
