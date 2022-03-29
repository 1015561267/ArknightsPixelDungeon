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

public class BanditSprite extends MobSprite {
	
	public BanditSprite() {
		super();
		
		texture( Assets.THIEF );
		TextureFilm film = new TextureFilm( texture, 13, 15 );
		
		idle = new Animation( 4, true );
		idle.frames( film, 19, 19, 19, 20, 19, 19, 19, 19, 20 );
		
		run = new Animation( 15, true );
		run.frames( film, 21, 22, 23, 24, 25, 26 );
		
		die = new Animation( 10, false );
		die.frames( film, 27, 28, 29 );
		
		attack = new Animation( 12, false );
		attack.frames( film, 30, 31, 32 );
		
		idle();
	}
}
