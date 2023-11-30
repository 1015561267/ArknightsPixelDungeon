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
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.MirrorImage;
import com.watabou.noosa.TextureFilm;

public class MirrorSprite extends MobSprite {
	
	private static final int FRAME_WIDTH	= 13;
	private static final int FRAME_HEIGHT	= 17;

    private static final int IMPROVED_FRAME_WIDTH	= 32;
    private static final int IMPROVED_FRAME_HEIGHT	= 27;


    public MirrorSprite() {
		super();

        if(Dungeon.hero.heroClass== HeroClass.WARRIOR ) {
            texture(Assets.CHEN);
        }
        else {
            texture( Dungeon.hero.heroClass.spritesheet() );
        }

        updateArmor( 0 );
		idle();
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		updateArmor( ((MirrorImage)ch).tier );
	}
	
	public void updateArmor( int tier ) {

        if(Dungeon.hero.heroClass==HeroClass.WARRIOR ) {
            //SmartTexture texture = TextureCache.get(Assets.CHEN);
            //tiers = new TextureFilm(texture, texture.width, IMPROVED_FRAME_HEIGHT);
            TextureFilm film = new TextureFilm(HeroSprite.improvedTiers(), tier, IMPROVED_FRAME_WIDTH, IMPROVED_FRAME_HEIGHT);

            idle = new Animation(6, true);
            idle.frames(film, 0, 1, 2, 3);

            run = new Animation(16, true);
            run.frames(film, 4, 5, 6, 7, 8, 9, 10, 11, 12);

            die = new Animation(4, false);
            die.frames(film, 13, 14, 15, 16, 17, 18, 19, 20);

            attack = new Animation(15, false);
            attack.frames(film, 21, 22, 23, 24, 25, 26, 27, 22);
            return;
        }

		TextureFilm film = new TextureFilm( HeroSprite.tiers(), tier, FRAME_WIDTH, FRAME_HEIGHT );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( 20, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );
		
		die = new Animation( 20, false );
		die.frames( film, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( film, 13, 14, 15, 0 );
		
		idle();
	}
}
