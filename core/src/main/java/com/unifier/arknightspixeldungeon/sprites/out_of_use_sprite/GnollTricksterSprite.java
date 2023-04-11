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
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.sprites.MobSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollTricksterSprite extends MobSprite {

	private Animation cast;

	public GnollTricksterSprite() {
		super();

		texture( Assets.GNOLL );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new MovieClip.Animation( 2, true );
		idle.frames( frames, 16, 16, 16, 17, 16, 16, 17, 17 );

		run = new MovieClip.Animation( 12, true );
		run.frames( frames, 20, 21, 22, 23 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 18, 19, 16 );

		cast = attack.clone();

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 24, 25, 26 );

		play( idle );
	}

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent(cell, ch.pos)) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( ch.pos, cell, new ParalyticDart(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete(Char.rangeType.GnollTrickster);
						}
					} );

			play( cast );
			turnTo( ch.pos , cell );

		} else {

			super.attack( cell );

		}
	}
}
