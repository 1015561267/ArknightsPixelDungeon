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

package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.levels.Level;
import com.watabou.noosa.Image;

public enum Icons {

	SKULL,
	BUSY,
	COMPASS,
	INFO,
	WARNING,
	TARGET,
	MASTERY,
	WATA,
	SHPX,
	WARRIOR,
	MAGE,
	ROGUE,
	HUNTRESS,
	CLOSE,
    STAIRS,
	SLEEP,
	ALERT,
	LOST,
	BACKPACK,
	SEED_POUCH,
	SCROLL_HOLDER,
	POTION_BANDOLIER,
	WAND_HOLSTER,
	CHECKED,
	UNCHECKED,
	EXIT,
	NOTES,

	CHALLENGE_OFF,
	CHALLENGE_ON,
    CHALLENGE_MORE,
    CHALLENGE_FULL,

    SMALL_CHALLENGE_OFF,
    SMALL_CHALLENGE_ON,
    SMALL_CHALLENGE_MORE,
    SMALL_CHALLENGE_FULL,

    SMALL_GOLD,
	RESUME,

    CONTROLLER,
    INPUT,
    DISPLAY,
    NETWORK,
    AUDIO,
    SETTINGS,
    LANGUAGE,
    FLYING_SHEEP,

    SKIP,


    LIBGDX,
    TALENT,

    ENTER,
    RANKINGS,
    BADGES,
    CHANGES,
    ABOUT,

    BLACKWARRIOR,
    BLACKMAGE,
    BLACKROGUE,
    BLACKHUNTRESS,

    DEPTH,      //depth icons have two variants, for regular and seeded runs
    DEPTH_CHASM,
    DEPTH_WATER,
    DEPTH_GRASS,
    DEPTH_DARK,
    DEPTH_LARGE,
    DEPTH_TRAPS,
    DEPTH_SECRETS,;



	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {
		Image icon = new Image( Assets.ICONS );
		switch (type) {
		    case SKULL:
			    icon.frame( icon.texture.uvRect( 0, 0, 8, 8 ) );
			    break;
            case BUSY:
                icon.frame( icon.texture.uvRect( 16, 0, 24, 8 ) );
                break;
			case COMPASS:
                icon.frame( icon.texture.uvRect( 32, 0, 45, 6 ) );
                break;
            case TARGET:
                icon.frame( icon.texture.uvRect( 48, 0, 65, 16 ) );
                break;

    		case INFO:
	    		icon.frame( icon.texture.uvRect( 80, 0, 94, 14 ) );
		    	break;
            case WARNING:
                icon.frame( icon.texture.uvRect( 96, 0, 110, 14 ) );
                break;
            case WATA:
                icon.frame( icon.texture.uvRect( 112, 0, 127, 10 ) );
                break;


            case BACKPACK:
                icon.frame( icon.texture.uvRect( 0, 16, 10, 26 ) );
                break;
            case SCROLL_HOLDER:
                icon.frame( icon.texture.uvRect( 16, 17, 25, 26 ) );
                break;
            case SEED_POUCH:
                icon.frame( icon.texture.uvRect( 32, 17, 43, 26 ) );
                break;
            case WAND_HOLSTER:
                icon.frame( icon.texture.uvRect( 48, 17, 58, 26 ) );
                break;
            case POTION_BANDOLIER:
                icon.frame( icon.texture.uvRect( 64, 16, 73, 24 ) );
                break;
            case EXIT:
                icon.frame( icon.texture.uvRect( 80, 16, 95, 27 ) );
                break;
            case MASTERY:
                icon.frame( icon.texture.uvRect( 96, 16, 110, 30 ) );
                break;
            case SHPX:
                icon.frame( icon.texture.uvRect( 64, 0, 78, 14 ) );
                break;


            case WARRIOR:
                icon.frame( icon.texture.uvRect( 32, 32, 48, 48 ) );
                break;
            case MAGE:
                icon.frame( icon.texture.uvRect( 48, 32, 64, 48 ) );
                break;
            case ROGUE:
                icon.frame( icon.texture.uvRect( 64, 32, 80, 48 ) );
                break;
            case HUNTRESS:
                icon.frame( icon.texture.uvRect( 80, 32, 96, 48 ) );
                break;
            case NOTES:
                icon.frame( icon.texture.uvRect( 96, 32, 106, 43 ) );
                break;



            case SLEEP:
                icon.frame( icon.texture.uvRect( 0, 48, 9, 56 ) );
                break;
            case ALERT:
                icon.frame( icon.texture.uvRect( 16, 48, 24, 56 ) );
                break;
            case LOST:
                icon.frame( icon.texture.uvRect( 32, 48, 40, 56 ) );
                break;
            case STAIRS:
                icon.frame( icon.texture.uvRect( 48, 48, 61, 64 ) );
                break;
            case CLOSE:
                icon.frame( icon.texture.uvRect( 64, 48, 75, 59 ) );
                break;
            //case CHALLENGE_OFF:
            //    icon.frame( icon.texture.uvRect( 80, 48, 94, 60 ) );
            //    break;
            //case CHALLENGE_ON:
            //    icon.frame( icon.texture.uvRect( 96, 48, 110, 60 ) );
            //    break;


            case CONTROLLER:
                icon.frame( icon.texture.uvRect( 128, 0, 144, 12 ) );
                break;
            case INPUT:
                icon.frame( icon.texture.uvRect( 144, 0, 159, 12 ) );
                break;
            case DISPLAY:
                icon.frame( icon.texture.uvRect( 160, 0, 173, 16 ) );
                break;

            case NETWORK:
                icon.frame( icon.texture.uvRect( 176, 0, 191, 15 ) );
                break;

            case AUDIO:
                icon.frame( icon.texture.uvRect( 192, 0, 206, 14 ) );
                break;

            case SETTINGS:
                icon.frame( icon.texture.uvRect( 208, 0, 222, 14 ) );
                break;

            case LANGUAGE:
                icon.frame( icon.texture.uvRect( 224, 0, 238, 11 ) );
                break;

            case FLYING_SHEEP:
                icon.frame( icon.texture.uvRect( 239, 0, 256, 16 ) );
                break;

            case SKIP:
                icon.frame( icon.texture.uvRect( 0, 64, 22, 76 ) );
                break;
            case LIBGDX:
                icon.frame( icon.texture.uvRect( 48, 64, 64, 77 ) );
                break;
            case TALENT:
                icon.frame( icon.texture.uvRect( 80, 64, 96, 77 ) );
                break;

            case ENTER:
                icon.frame( icon.texture.uvRect( 80, 64, 96, 80 ) );
                break;
            case BADGES:
                icon.frame( icon.texture.uvRect( 96, 64, 112, 80 ) );
                break;
            case ABOUT:
                icon.frame( icon.texture.uvRect( 96, 80, 110, 94 ) );
                break;

            case CHANGES:
                icon.frame( icon.texture.uvRect( 80, 80, 93, 94 ) );
                break;

            case RANKINGS:
                icon.frame( icon.texture.uvRect( 0, 80, 17, 96 ) );
                break;

            case SMALL_GOLD:
                icon.frame( icon.texture.uvRect( 48, 84, 61, 92 ) );
                break;
            case RESUME:
                icon.frame( icon.texture.uvRect( 64, 80, 75, 91 ) );
                break;


            case BLACKWARRIOR:
                icon.frame( icon.texture.uvRect( 0, 96, 16, 112 ) );
                break;
            case BLACKMAGE:
                icon.frame( icon.texture.uvRect( 16, 96, 32, 112 ) );
                break;
            case BLACKROGUE:
                icon.frame( icon.texture.uvRect( 32, 96, 48, 112 ) );
                break;
            case BLACKHUNTRESS:
                icon.frame( icon.texture.uvRect( 48, 96, 64, 112 ) );
                break;


            case CHECKED:
                icon.frame( icon.texture.uvRect( 0, 112, 20, 119 ) );
                break;
            case UNCHECKED:
                icon.frame( icon.texture.uvRect( 20, 112, 40, 119 ) );
                break;
            case CHALLENGE_OFF:
                icon.frame( icon.texture.uvRect( 40, 112, 58, 128 ) );
                break;
            case CHALLENGE_ON:
                icon.frame( icon.texture.uvRect( 58, 112, 76, 128 ) );
                break;
            case CHALLENGE_MORE:
                icon.frame( icon.texture.uvRect( 76, 112, 94, 128 ) );
                break;
            case CHALLENGE_FULL:
                icon.frame( icon.texture.uvRect( 94, 112, 112, 128 ) );
                break;

            case SMALL_CHALLENGE_OFF:
                icon.frame( icon.texture.uvRect( 64, 96, 73, 105 ) );
                break;
            case SMALL_CHALLENGE_ON:
                icon.frame( icon.texture.uvRect( 80, 96, 89, 105 ) );
                break;
            case SMALL_CHALLENGE_MORE:
                icon.frame( icon.texture.uvRect( 96, 96, 105, 105 ) );
                break;
            case SMALL_CHALLENGE_FULL:
                icon.frame( icon.texture.uvRect( 112, 96, 121, 105 ) );
                break;

            case DEPTH:
                icon.frame( icon.texture.uvRectBySize( 128, 98 , 6, 7 ) );
                //icon.frame( icon.texture.uvRect( 128, 98 , 134, 104 ) );
                break;
            case DEPTH_CHASM:
                icon.frame( icon.texture.uvRectBySize( 136, 97 , 7, 7 ) );
                break;
            case DEPTH_WATER:
                icon.frame( icon.texture.uvRectBySize( 144, 97 , 7, 7 ) );
                break;
            case DEPTH_GRASS:
                icon.frame( icon.texture.uvRectBySize( 152, 97 , 7, 7 ) );
                break;
            case DEPTH_DARK:
                icon.frame( icon.texture.uvRectBySize( 160, 97 , 7, 7 ) );
                break;
            case DEPTH_LARGE:
                icon.frame( icon.texture.uvRectBySize( 168, 97 , 7, 7 ) );
                break;
            case DEPTH_TRAPS:
                icon.frame( icon.texture.uvRectBySize( 176, 97 , 7, 7 ) );
                break;
            case DEPTH_SECRETS:
                icon.frame( icon.texture.uvRectBySize( 184, 97 , 7, 7 ) );
                break;

        }
		return icon;
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return get( WARRIOR );
		case MAGE:
			return get( MAGE );
		case ROGUE:
			return get( ROGUE );
		case HUNTRESS:
			return get( HUNTRESS );
		default:
			return null;
		}
	}

	public static Image getDark(HeroClass cl)//This is for temporary use
    {
        switch (cl) {
            case WARRIOR:
                return get( BLACKWARRIOR );
            case MAGE:
                return get( BLACKMAGE );
            case ROGUE:
                return get( BLACKROGUE );
            case HUNTRESS:
                return get( BLACKHUNTRESS );
            default:
                return null;
        }
    }

    public static Image get(Level.Feeling feeling){
        switch (feeling){
            case NONE: default:
                return get(DEPTH);
            case CHASM:
                return get(DEPTH_CHASM);
            case WATER:
                return get(DEPTH_WATER);
            case GRASS:
                return get(DEPTH_GRASS);
            case DARK:
                return get(DEPTH_DARK);
            //case LARGE:
            //    return get(DEPTH_LARGE);
            //case TRAPS:
            //    return get(DEPTH_TRAPS);
            //case SECRETS:
            ///    return get(DEPTH_SECRETS);
        }
    }
}
