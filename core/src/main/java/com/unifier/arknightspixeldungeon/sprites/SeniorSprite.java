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
import com.watabou.utils.Random;

public class SeniorSprite extends MobSprite {
	
	private Animation kick;
	
	public SeniorSprite() {
		super();
		
		texture( Assets.MONK );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 6, true );
		idle.frames( frames, 16,17,16 );
		
		run = new Animation( 15, true );
		run.frames( frames, 18,19,20,21,22,23 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 27,28,29 );
		
		kick = new Animation( 10, false );
		kick.frames( frames,27,28,29 );
		
		die = new Animation( 15, false );
		die.frames( frames, 24,25,26 );
		
		play( idle );
	}
	
	@Override
	public void attack( int cell ) {
		super.attack( cell );
		if (Random.Float() < 0.3f) {
			play( kick );
		}
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim == kick ? attack : anim );
	}
}
