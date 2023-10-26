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

public class ItemSpriteSheet {

	private static final int WIDTH = 32;
    public static final int SIZE = 32;

	public static TextureFilm film = new TextureFilm( Assets.ITEMS, SIZE, SIZE );

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

    private static void assignItemRect( int item, int width, int height){
        int x = (item % WIDTH) * SIZE;
        int y = (item / WIDTH) * SIZE;

        film.add( item, x, y, x+width, y+height);
        //DeviceCompat.log("assignItemRect", item + " " + width + " " + height);

    }

	private static final int PLACEHOLDERS   =                               xy(1, 1);   //16 slots
	//null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN        = PLACEHOLDERS+0;
	public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
	public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
	public static final int WAND_HOLDER     = PLACEHOLDERS+3;
	public static final int RING_HOLDER     = PLACEHOLDERS+4;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS+5;
	public static final int POTION_HOLDER   = PLACEHOLDERS+6;
	public static final int SCROLL_HOLDER   = PLACEHOLDERS+7;
	public static final int SOMETHING       = PLACEHOLDERS+8;
	static{
		assignItemRect(NULLWARN,        32, 14);
		assignItemRect(WEAPON_HOLDER,   28, 28);
		assignItemRect(ARMOR_HOLDER,    28, 26);
		assignItemRect(WAND_HOLDER,     28, 28);
		assignItemRect(RING_HOLDER,     18,  14);
		assignItemRect(ARTIFACT_HOLDER, 28, 32);
		assignItemRect(POTION_HOLDER,   10, 26);
		assignItemRect(SCROLL_HOLDER,   22, 28);
		assignItemRect(SOMETHING,       16,  26);
	}

	private static final int UNCOLLECTIBLE  =                               xy(1, 2);   //16 slots
	public static final int GOLD            = UNCOLLECTIBLE+0;
	public static final int DEWDROP         = UNCOLLECTIBLE+1;
	public static final int PETAL           = UNCOLLECTIBLE+2;
	public static final int SANDBAG         = UNCOLLECTIBLE+3;
	public static final int DBL_BOMB        = UNCOLLECTIBLE+4;
	public static final int GUIDE_PAGE      = UNCOLLECTIBLE+5;
    public static final int SPIRIT_ARROW    = UNCOLLECTIBLE+6;
	static{
		assignItemRect(GOLD,        27, 26);
		assignItemRect(DEWDROP,     25, 24);
		assignItemRect(PETAL,       14,  16);
		assignItemRect(SANDBAG,     16, 16);
		assignItemRect(DBL_BOMB,    32, 30);
		assignItemRect(GUIDE_PAGE,  32, 26);

        assignItemRect(SPIRIT_ARROW,  16, 16);
	}





	private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
	public static final int BONES           = CONTAINERS+0;
	public static final int REMAINS         = CONTAINERS+1;
	public static final int TOMB            = CONTAINERS+2;
	public static final int GRAVE           = CONTAINERS+3;
	public static final int CHEST           = CONTAINERS+4;
	public static final int LOCKED_CHEST    = CONTAINERS+5;
	public static final int CRYSTAL_CHEST   = CONTAINERS+6;
	public static final int EBONY_CHEST     = CONTAINERS+7;
	static{
		assignItemRect(BONES,           29, 22);
		assignItemRect(REMAINS,         23, 19);
		assignItemRect(TOMB,            28, 27);
		assignItemRect(GRAVE,           28, 23);
		assignItemRect(CHEST,           28, 27);
		assignItemRect(LOCKED_CHEST,    28, 27);
		assignItemRect(CRYSTAL_CHEST,   28, 27);
		assignItemRect(EBONY_CHEST,     28, 27);
	}

	private static final int SINGLE_USE     =                               xy(1, 4);   //16 slots
	public static final int ANKH            = SINGLE_USE+0;
	public static final int STYLUS          = SINGLE_USE+1;
	
	public static final int SEAL            = SINGLE_USE+3;
	public static final int TORCH           = SINGLE_USE+4;
	public static final int BEACON          = SINGLE_USE+5;
	public static final int BOMB            = SINGLE_USE+6;
	public static final int HONEYPOT        = SINGLE_USE+7;
	public static final int SHATTPOT        = SINGLE_USE+8;
	public static final int IRON_KEY        = SINGLE_USE+9;
	public static final int GOLDEN_KEY      = SINGLE_USE+10;
	public static final int CRYSTAL_KEY     = SINGLE_USE+11;
	public static final int SKELETON_KEY    = SINGLE_USE+12;
	public static final int MASTERY         = SINGLE_USE+13;
	public static final int KIT             = SINGLE_USE+14;
	public static final int AMULET          = SINGLE_USE+15;
	static{
		assignItemRect(ANKH,            32, 30);
		assignItemRect(STYLUS,          28, 28);
		
		assignItemRect(SEAL,            26,  26);
		assignItemRect(TORCH,           22, 28);
		assignItemRect(BEACON,          32, 24);
		assignItemRect(BOMB,            32, 22);
		assignItemRect(HONEYPOT,        32, 24);
		assignItemRect(SHATTPOT,        32, 24);
		assignItemRect(IRON_KEY,        16,  28);
		assignItemRect(GOLDEN_KEY,      16,  28);
		assignItemRect(CRYSTAL_KEY,     16,  28);
		assignItemRect(SKELETON_KEY,    16,  28);
		assignItemRect(MASTERY,         32, 26);
		assignItemRect(KIT,             22, 26);
		assignItemRect(AMULET,          32, 32);
	}

	                                                                                    //32 free slots

	private static final int WEP_TIER1      =                               xy(1, 7);   //8 slots
	public static final int WORN_SHORTSWORD = WEP_TIER1+0;
	public static final int CUDGEL          = WEP_TIER1+1;
	public static final int KNUCKLEDUSTER   = WEP_TIER1+2;
	public static final int RAPIER          = WEP_TIER1+3;
	public static final int DAGGER          = WEP_TIER1+4;
	public static final int MAGES_STAFF     = WEP_TIER1+5;
	static{
		assignItemRect(WORN_SHORTSWORD, 27, 24);
		assignItemRect(KNUCKLEDUSTER,   21, 29);
		assignItemRect(DAGGER,          26, 22);
		assignItemRect(MAGES_STAFF,     8, 27);
	}

	private static final int WEP_TIER2      =                               xy(9, 7);   //8 slots
	public static final int SHORTSWORD      = WEP_TIER2+0;
	public static final int HAND_AXE        = WEP_TIER2+1;
	public static final int SPEAR           = WEP_TIER2+2;
	public static final int QUARTERSTAFF    = WEP_TIER2+3;
	public static final int DIRK            = WEP_TIER2+4;
	static{
		assignItemRect(SHORTSWORD,      20, 27);
		assignItemRect(HAND_AXE,        24, 23);
		assignItemRect(SPEAR,           28, 28);
		assignItemRect(QUARTERSTAFF,    29, 27);
		assignItemRect(DIRK,            28, 28);
	}

	private static final int WEP_TIER3      =                               xy(1, 8);   //8 slots
	public static final int SWORD           = WEP_TIER3+0;
	public static final int MACE            = WEP_TIER3+1;
	public static final int SCIMITAR        = WEP_TIER3+2;
	public static final int ROUND_SHIELD    = WEP_TIER3+3;
	public static final int SAI             = WEP_TIER3+4;
	public static final int WHIP            = WEP_TIER3+5;
	static{
		assignItemRect(SWORD,           28, 27);
		assignItemRect(MACE,            29, 29);
		assignItemRect(SCIMITAR,        23, 22);
		assignItemRect(ROUND_SHIELD,    21, 19);
		assignItemRect(SAI,             27, 25);
		assignItemRect(WHIP,            25, 28);
	}

	private static final int WEP_TIER4      =                               xy(9, 8);   //8 slots
	public static final int LONGSWORD       = WEP_TIER4+0;
	public static final int BATTLE_AXE      = WEP_TIER4+1;
	public static final int FLAIL           = WEP_TIER4+2;
	public static final int RUNIC_BLADE     = WEP_TIER4+3;
	public static final int ASSASSINS_BLADE = WEP_TIER4+4;
	public static final int CROSSBOW        = WEP_TIER4+5;
	static{
		assignItemRect(LONGSWORD,       26, 26);
		assignItemRect(BATTLE_AXE,      21, 20);
		assignItemRect(FLAIL,           26, 24);
		assignItemRect(RUNIC_BLADE,     21, 22);
		assignItemRect(ASSASSINS_BLADE, 28, 26);
		assignItemRect(CROSSBOW,        18, 19);
	}

	private static final int WEP_TIER5      =                               xy(1, 9);   //8 slots
	public static final int GREATSWORD      = WEP_TIER5+0;
	public static final int WAR_HAMMER      = WEP_TIER5+1;
	public static final int GLAIVE          = WEP_TIER5+2;
	public static final int GREATAXE        = WEP_TIER5+3;
	public static final int GREATSHIELD     = WEP_TIER5+4;
	public static final int GAUNTLETS       = WEP_TIER5+5;
	static{
		assignItemRect(GREATSWORD,  28, 28);
		assignItemRect(WAR_HAMMER,  26, 27);
		assignItemRect(GLAIVE,      27, 27);
		assignItemRect(GREATAXE,    28, 28);
		assignItemRect(GREATSHIELD, 22, 30);
		assignItemRect(GAUNTLETS,   28, 27);
	}

	                                                                                    //8 free slots

	private static final int MISSILE_WEP    =                               xy(1, 10);  //16 slots. 3 per tier + boomerang
	public static final int BOOMERANG       = MISSILE_WEP+0;
	
	public static final int DART            = MISSILE_WEP+1;
	public static final int THROWING_KNIFE  = MISSILE_WEP+2;
	public static final int THROWING_STONE  = MISSILE_WEP+3;
	
	public static final int FISHING_SPEAR   = MISSILE_WEP+4;
	public static final int SHURIKEN        = MISSILE_WEP+5;
	
	public static final int THROWING_SPEAR  = MISSILE_WEP+7;
	public static final int BOLAS           = MISSILE_WEP+8;
	
	public static final int JAVELIN         = MISSILE_WEP+10;
	public static final int TOMAHAWK        = MISSILE_WEP+11;
	
	public static final int TRIDENT         = MISSILE_WEP+13;
	public static final int THROWING_HAMMER = MISSILE_WEP+14;
	
	static{
		assignItemRect(BOOMERANG,       22, 24);
		
		assignItemRect(DART,            24, 24);

		assignItemRect(THROWING_KNIFE,  14, 22);
		assignItemRect(THROWING_STONE,  18, 16);
		
		assignItemRect(FISHING_SPEAR,   26, 12);
		assignItemRect(SHURIKEN,        24, 28);
		
		assignItemRect(THROWING_SPEAR,  30, 16);
		assignItemRect(BOLAS,           24, 20);
		
		assignItemRect(JAVELIN,         32, 22);
		assignItemRect(TOMAHAWK,        32, 30);
		
		assignItemRect(TRIDENT,         28, 30);
		assignItemRect(THROWING_HAMMER, 28, 26);
	}
	
	public static final int TIPPED_DARTS    =                               xy(1, 11);  //16 slots
	public static final int ROT_DART        = TIPPED_DARTS+0;
	public static final int INCENDIARY_DART = TIPPED_DARTS+1;
	public static final int HOLY_DART       = TIPPED_DARTS+2;
	public static final int BLINDING_DART   = TIPPED_DARTS+3;
	public static final int HEALING_DART    = TIPPED_DARTS+4;
	public static final int CHILLING_DART   = TIPPED_DARTS+5;
	public static final int SHOCKING_DART   = TIPPED_DARTS+6;
	public static final int POISON_DART     = TIPPED_DARTS+7;
	public static final int SLEEP_DART      = TIPPED_DARTS+8;
	public static final int PARALYTIC_DART  = TIPPED_DARTS+9;
	public static final int DISPLACING_DART = TIPPED_DARTS+10;

    public static final int ONE_BURST = TIPPED_DARTS+11;
    public static final int TWO_BURST = TIPPED_DARTS+12;
    public static final int THREE_BURST = TIPPED_DARTS+13;

    static {
		for (int i = TIPPED_DARTS; i < TIPPED_DARTS+16; i++)
			assignItemRect(i, 29, 26);
	}
	
	private static final int ARMOR          =                               xy(1, 12);  //16 slots
	public static final int ARMOR_CLOTH     = ARMOR+0;
	public static final int ARMOR_LEATHER   = ARMOR+1;
	public static final int ARMOR_MAIL      = ARMOR+2;
	public static final int ARMOR_SCALE     = ARMOR+3;
	public static final int ARMOR_PLATE     = ARMOR+4;
	public static final int ARMOR_WARRIOR   = ARMOR+5;
	public static final int ARMOR_MAGE      = ARMOR+6;
	public static final int ARMOR_ROGUE     = ARMOR+7;
	public static final int ARMOR_HUNTRESS  = ARMOR+8;
	static{
		assignItemRect(ARMOR_CLOTH,     26, 27);
		assignItemRect(ARMOR_LEATHER,   30, 26);
		assignItemRect(ARMOR_MAIL,      28, 28);
		assignItemRect(ARMOR_SCALE,     30, 27);
		assignItemRect(ARMOR_PLATE,     30, 27);

		assignItemRect(ARMOR_WARRIOR,   15, 16);
		assignItemRect(ARMOR_MAGE,      15, 15);
		assignItemRect(ARMOR_ROGUE,     13, 15);
		assignItemRect(ARMOR_HUNTRESS,  15, 16);
	}

	                                                                                    //16 free slots

	private static final int WANDS              =                           xy(1, 14);  //16 slots
	public static final int WAND_MAGIC_MISSILE  = WANDS+0;
	public static final int WAND_FIREBOLT       = WANDS+1;
	public static final int WAND_FROST          = WANDS+2;
	public static final int WAND_LIGHTNING      = WANDS+3;
	public static final int WAND_DISINTEGRATION = WANDS+4;
	public static final int WAND_PRISMATIC_LIGHT= WANDS+5;
	public static final int WAND_CORROSION      = WANDS+6;
	public static final int WAND_LIVING_EARTH   = WANDS+7;
	public static final int WAND_BLAST_WAVE     = WANDS+8;
	public static final int WAND_CORRUPTION     = WANDS+9;
	public static final int WAND_WARDING        = WANDS+10;
	public static final int WAND_REGROWTH       = WANDS+11;
	public static final int WAND_TRANSFUSION    = WANDS+12;
	static {

        for (int i = WANDS; i < WANDS+16; i++)
            assignItemRect(i, 14, 14);

        assignItemRect(WAND_MAGIC_MISSILE,      23,  24);
        assignItemRect(WAND_FIREBOLT,      31,  29);
        assignItemRect(WAND_FROST,      21,  21);
        assignItemRect(WAND_LIGHTNING,      28,  28);
        assignItemRect(WAND_DISINTEGRATION,      26,  27);
        assignItemRect(WAND_PRISMATIC_LIGHT,      29,  28);
        assignItemRect(WAND_CORROSION,      28,  25);
        assignItemRect(WAND_LIVING_EARTH,      25,  26);
        assignItemRect(WAND_BLAST_WAVE,      26,  27);
        assignItemRect(WAND_CORRUPTION,      27,  28);
        assignItemRect(WAND_WARDING,      27,  27);
        assignItemRect(WAND_REGROWTH,      29,  29);
        assignItemRect(WAND_TRANSFUSION,      25,  25);

	}

	private static final int RINGS          =                               xy(1, 15);  //16 slots
	public static final int RING_GARNET     = RINGS+0;
	public static final int RING_RUBY       = RINGS+1;
	public static final int RING_TOPAZ      = RINGS+2;
	public static final int RING_EMERALD    = RINGS+3;
	public static final int RING_ONYX       = RINGS+4;
	public static final int RING_OPAL       = RINGS+5;
	public static final int RING_TOURMALINE = RINGS+6;
	public static final int RING_SAPPHIRE   = RINGS+7;
	public static final int RING_AMETHYST   = RINGS+8;
	public static final int RING_QUARTZ     = RINGS+9;
	public static final int RING_AGATE      = RINGS+10;
	public static final int RING_DIAMOND    = RINGS+11;
	static {
		for (int i = RINGS; i < RINGS+16; i++)
			assignItemRect(i, 10, 13);
	}

	private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
	public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
	public static final int ARTIFACT_CAPE       = ARTIFACTS+2;
	public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
	public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
	public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
	public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
	public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
	public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
	public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
	public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
	public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
	public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
	public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
	public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
	public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
	public static final int ARTIFACT_SANDALS    = ARTIFACTS+16;
	public static final int ARTIFACT_SHOES      = ARTIFACTS+17;
	public static final int ARTIFACT_BOOTS      = ARTIFACTS+18;
	public static final int ARTIFACT_GREAVES    = ARTIFACTS+19;
	public static final int ARTIFACT_ROSE1      = ARTIFACTS+20;
	public static final int ARTIFACT_ROSE2      = ARTIFACTS+21;
	public static final int ARTIFACT_ROSE3      = ARTIFACTS+22;
	static{
		assignItemRect(ARTIFACT_CLOAK,      27,  21);
		assignItemRect(ARTIFACT_ARMBAND,    21, 25);
		assignItemRect(ARTIFACT_CAPE,       30, 29);
		assignItemRect(ARTIFACT_TALISMAN,   16, 28);
		assignItemRect(ARTIFACT_HOURGLASS,  29, 30);
		assignItemRect(ARTIFACT_TOOLKIT,    27, 28);
		assignItemRect(ARTIFACT_SPELLBOOK,  28, 26);
		assignItemRect(ARTIFACT_BEACON,     11, 26);
		assignItemRect(ARTIFACT_CHAINS,     17, 28);
		assignItemRect(ARTIFACT_HORN1,      18, 30);
		assignItemRect(ARTIFACT_HORN2,      18, 30);
		assignItemRect(ARTIFACT_HORN3,      18, 30);
		assignItemRect(ARTIFACT_HORN4,      25, 30);
		assignItemRect(ARTIFACT_CHALICE1,   21, 21);
		assignItemRect(ARTIFACT_CHALICE2,   21, 21);
		assignItemRect(ARTIFACT_CHALICE3,   21, 21);
		assignItemRect(ARTIFACT_SANDALS,    20, 23 );
		assignItemRect(ARTIFACT_SHOES,      22, 25 );
		assignItemRect(ARTIFACT_BOOTS,      23, 26 );
		assignItemRect(ARTIFACT_GREAVES,    28, 27);
		assignItemRect(ARTIFACT_ROSE1,      16, 20);
		assignItemRect(ARTIFACT_ROSE2,      16, 20);
		assignItemRect(ARTIFACT_ROSE3,      24, 24);
	}

	                                                                                    //32 free slots

	private static final int SCROLLS        =                               xy(1, 20);  //16 slots
	public static final int SCROLL_KAUNAN   = SCROLLS+0;
	public static final int SCROLL_SOWILO   = SCROLLS+1;
	public static final int SCROLL_LAGUZ    = SCROLLS+2;
	public static final int SCROLL_YNGVI    = SCROLLS+3;
	public static final int SCROLL_GYFU     = SCROLLS+4;
	public static final int SCROLL_RAIDO    = SCROLLS+5;
	public static final int SCROLL_ISAZ     = SCROLLS+6;
	public static final int SCROLL_MANNAZ   = SCROLLS+7;
	public static final int SCROLL_NAUDIZ   = SCROLLS+8;
	public static final int SCROLL_BERKANAN = SCROLLS+9;
	public static final int SCROLL_ODAL     = SCROLLS+10;
	public static final int SCROLL_TIWAZ    = SCROLLS+11;
	static {
		for (int i = SCROLLS; i < SCROLLS+16; i++)
			assignItemRect(i, 26, 27);
	}
	
	private static final int STONES        =                                xy(1, 21);  //16 slots
	public static final int STONE_KAUNAN   = STONES+0;
	public static final int STONE_SOWILO   = STONES+1;
	public static final int STONE_LAGUZ    = STONES+2;
	public static final int STONE_YNGVI    = STONES+3;
	public static final int STONE_GYFU     = STONES+4;
	public static final int STONE_RAIDO    = STONES+5;
	public static final int STONE_ISAZ     = STONES+6;
	public static final int STONE_MANNAZ   = STONES+7;
	public static final int STONE_NAUDIZ   = STONES+8;
	public static final int STONE_BERKANAN = STONES+9;
	public static final int STONE_ODAL     = STONES+10;
	public static final int STONE_TIWAZ    = STONES+11;
	static {
		for (int i = STONES; i < STONES+16; i++)
			assignItemRect(i, 14, 22);
	}

	private static final int POTIONS        =                               xy(1, 22);  //16 slots
	public static final int POTION_CRIMSON  = POTIONS+0;
	public static final int POTION_AMBER    = POTIONS+1;
	public static final int POTION_GOLDEN   = POTIONS+2;
	public static final int POTION_JADE     = POTIONS+3;
	public static final int POTION_TURQUOISE= POTIONS+4;
	public static final int POTION_AZURE    = POTIONS+5;
	public static final int POTION_INDIGO   = POTIONS+6;
	public static final int POTION_MAGENTA  = POTIONS+7;
	public static final int POTION_BISTRE   = POTIONS+8;
	public static final int POTION_CHARCOAL = POTIONS+9;
	public static final int POTION_SILVER   = POTIONS+10;
	public static final int POTION_IVORY    = POTIONS+11;
	static {
		for (int i = POTIONS; i < POTIONS+16; i++)
			assignItemRect(i, 8, 27);
	}

	private static final int SEEDS          =                               xy(1, 23);  //16 slots
	public static final int SEED_ROTBERRY   = SEEDS+0;
	public static final int SEED_FIREBLOOM  = SEEDS+1;
	public static final int SEED_STARFLOWER = SEEDS+2;
	public static final int SEED_BLINDWEED  = SEEDS+3;
	public static final int SEED_SUNGRASS   = SEEDS+4;
	public static final int SEED_ICECAP     = SEEDS+5;
	public static final int SEED_STORMVINE  = SEEDS+6;
	public static final int SEED_SORROWMOSS = SEEDS+7;
	public static final int SEED_DREAMFOIL  = SEEDS+8;
	public static final int SEED_EARTHROOT  = SEEDS+9;
	public static final int SEED_FADELEAF   = SEEDS+10;
	public static final int SEED_BLANDFRUIT = SEEDS+11;
	static{
		for (int i = SEEDS; i < SEEDS+16; i++)
			assignItemRect(i, 13, 27);
	}

	                                                                                    //16 free slots

	private static final int FOOD       =                                   xy(1, 25);  //16 slots
	public static final int MEAT        = FOOD+0;
	public static final int STEAK       = FOOD+1;
	public static final int OVERPRICED  = FOOD+2;
	public static final int CARPACCIO   = FOOD+3;
	public static final int BLANDFRUIT  = FOOD+4;
	public static final int RATION      = FOOD+5;
	public static final int PASTY       = FOOD+6;
	public static final int PUMPKIN_PIE = FOOD+7;
	public static final int CANDY_CANE  = FOOD+8;
	static{
		assignItemRect(MEAT,        24, 26);
		assignItemRect(STEAK,       24, 26);
		assignItemRect(OVERPRICED,  14, 30);
		assignItemRect(CARPACCIO,   24, 26);
		assignItemRect(BLANDFRUIT,  18,  24);
		assignItemRect(RATION,      14, 30);
		assignItemRect(PASTY,       28, 30);
		assignItemRect(PUMPKIN_PIE, 32, 26);
		assignItemRect(CANDY_CANE,  32, 24);
	}

	private static final int QUEST  =                                       xy(1, 26);  //32 slots
	public static final int SKULL   = QUEST+0;
	public static final int DUST    = QUEST+1;
	public static final int CANDLE  = QUEST+2;
	public static final int EMBER   = QUEST+3;
	public static final int PICKAXE = QUEST+4;
	public static final int ORE     = QUEST+5;
	public static final int TOKEN   = QUEST+6;
	static{
		assignItemRect(SKULL,   32, 22);
		assignItemRect(DUST,    26, 26);
		assignItemRect(CANDLE,  22, 26);
		assignItemRect(EMBER,   26, 26);
		assignItemRect(PICKAXE, 28, 28);
		assignItemRect(ORE,     28, 24);
		assignItemRect(TOKEN,   24, 28);

	}

	private static final int BAGS       =                                   xy(1, 28);  //16 slots
	public static final int VIAL        = BAGS+0;
	public static final int POUCH       = BAGS+1;
	public static final int HOLDER      = BAGS+2;
	public static final int BANDOLIER   = BAGS+3;
	public static final int HOLSTER     = BAGS+4;
	static{
		assignItemRect(VIAL,        20, 24);
		assignItemRect(POUCH,       19, 18);
		assignItemRect(HOLDER,      21, 27);
		assignItemRect(BANDOLIER,   29, 18);
		assignItemRect(HOLSTER,     28, 14);
	}

	                                                                                    //64 free slots
}
