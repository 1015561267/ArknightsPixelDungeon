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

public class ShieldedSprite extends MobSprite {
	
	public ShieldedSprite() {
		super();
		
		texture( Assets.BRUTE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 16, 16, 16, 17, 16, 16, 17, 17 );
		
		run = new Animation( 12, true );
		run.frames( frames, 18, 19, 20, 21,22,23 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 27, 28 );
		
		die = new Animation( 12, false );
		die.frames( frames, 24, 25, 26 );
		
		play( idle );
	}
}
