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

package com.unifier.arknightspixeldungeon.sprites.out_of_use_sprite;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class SwarmSprite extends MobSprite {
	
	public SwarmSprite() {
		super();
		
		texture( Assets.SWARM );
		
		TextureFilm frames = new TextureFilm( texture, 17, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 20, true );
		run.frames( frames, 2,3,4,5 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 6,7 );
		
		die = new Animation( 15, false );
		die.frames( frames, 8,9,10);
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFF8BA077;
	}
}
