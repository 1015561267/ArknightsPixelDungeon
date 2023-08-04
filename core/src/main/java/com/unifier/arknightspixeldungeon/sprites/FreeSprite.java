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
import com.watabou.noosa.TextureFilm;

public class FreeSprite extends MobSprite {
	
	public FreeSprite() {
		super();
		
		texture( Assets.FREE );

        TextureFilm frames = new TextureFilm( texture, 32, 28 );

        idle = new Animation( 6, true );
        idle.frames( frames, 0, 1, 2, 3, 4 );

        run = new Animation( 8, true );
        run.frames( frames, 0, 1, 2, 3, 4 );

        attack = new Animation( 8, false );
        attack.frames( frames, 5, 6, 7, 8 );

        die = new Animation( 7, false );
        die.frames( frames, 9, 10, 11, 12, 13, 14, 15, 16  );

        play( idle );
	}
	
	@Override
	public int blood() {
		return 0x93b2c8;
	}
}
