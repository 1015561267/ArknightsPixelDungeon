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
import com.watabou.noosa.Image;

public class BannerSprites {

	public enum  Type {
		PIXEL_DUNGEON,
		BOSS_SLAIN,
		GAME_OVER,
	};

	public static Image get( Type type ) {
		Image icon = new Image( Assets.BANNERS );
		switch (type) {
			case PIXEL_DUNGEON:
				icon.frame( icon.texture.uvRect( 0, 0, 141, 70 ) );
				break;
			case BOSS_SLAIN:
				icon.frame( icon.texture.uvRect( 0, 70, 145, 121 ) );
				break;
			case GAME_OVER:
				icon.frame( icon.texture.uvRect( 0, 125, 98, 168 ) );
				break;
		}
		return icon;
	}
}
