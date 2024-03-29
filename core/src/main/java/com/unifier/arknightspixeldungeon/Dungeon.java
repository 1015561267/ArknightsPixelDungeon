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

package com.unifier.arknightspixeldungeon;

import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Amok;
import com.unifier.arknightspixeldungeon.actors.buffs.Awareness;
import com.unifier.arknightspixeldungeon.actors.buffs.Blindness;
import com.unifier.arknightspixeldungeon.actors.buffs.Light;
import com.unifier.arknightspixeldungeon.actors.buffs.MagicalSight;
import com.unifier.arknightspixeldungeon.actors.buffs.MindVision;
import com.unifier.arknightspixeldungeon.actors.buffs.SniperSight;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.Blacksmith;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.Imp;
import com.unifier.arknightspixeldungeon.actors.mobs.npcs.Wandmaker;
import com.unifier.arknightspixeldungeon.items.Ankh;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.artifacts.DriedRose;
import com.unifier.arknightspixeldungeon.items.potions.Potion;
import com.unifier.arknightspixeldungeon.items.rings.Ring;
import com.unifier.arknightspixeldungeon.items.scrolls.Scroll;
import com.unifier.arknightspixeldungeon.journal.Notes;
import com.unifier.arknightspixeldungeon.levels.CavesBossLevel;
import com.unifier.arknightspixeldungeon.levels.CavesLevel;
import com.unifier.arknightspixeldungeon.levels.CityBossLevel;
import com.unifier.arknightspixeldungeon.levels.CityLevel;
import com.unifier.arknightspixeldungeon.levels.DeadEndLevel;
import com.unifier.arknightspixeldungeon.levels.HallsBossLevel;
import com.unifier.arknightspixeldungeon.levels.HallsLevel;
import com.unifier.arknightspixeldungeon.levels.LastLevel;
import com.unifier.arknightspixeldungeon.levels.LastShopLevel;
import com.unifier.arknightspixeldungeon.levels.Level;
import com.unifier.arknightspixeldungeon.levels.PrisonBossLevel;
import com.unifier.arknightspixeldungeon.levels.PrisonLevel;
import com.unifier.arknightspixeldungeon.levels.SewerBossLevel;
import com.unifier.arknightspixeldungeon.levels.SewerLevel;
import com.unifier.arknightspixeldungeon.levels.rooms.secret.SecretRoom;
import com.unifier.arknightspixeldungeon.levels.rooms.special.SpecialRoom;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.scripts.Plot;
import com.unifier.arknightspixeldungeon.ui.QuickSlotButton;
import com.unifier.arknightspixeldungeon.utils.BArray;
import com.unifier.arknightspixeldungeon.utils.DungeonSeed;
import com.unifier.arknightspixeldungeon.windows.WndAlchemy;
import com.unifier.arknightspixeldungeon.windows.WndDialog;
import com.unifier.arknightspixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Dungeon {

    //enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public static enum LimitedDrops {
		//limited world drops
		STRENGTH_POTIONS,
		UPGRADE_SCROLLS,
		ARCANE_STYLI,

		//Health potion sources
		//enemies
		SWARM_HP,
		GUARD_HP,
		BAT_HP,
		WARLOCK_HP,
		SCORPIO_HP,
		//alchemy
		COOKING_HP,
		BLANDFRUIT_SEED,

        //Other limited enemy drops
        DUBLINNPHALANXINFANTRY_WEP,

		//doesn't use Generator, so we have to enforce one armband drop here
		THIEVES_ARMBAND,

		//containers
		DEW_VIAL,
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped(){
			return count != 0;
		}
		public void drop(){
			count = 1;
		}

		public static void reset(){
			for (LimitedDrops lim : values()){
				lim.count = 0;
			}
		}

		public static void store( Bundle bundle ){
			for (LimitedDrops lim : values()){
				bundle.put(lim.name(), lim.count);
			}
		}

		public static void restore( Bundle bundle ){
			for (LimitedDrops lim : values()){
				if (bundle.contains(lim.name())){
					lim.count = bundle.getInt(lim.name());
				} else {
					lim.count = 0;
				}
				
			}
		}
	}

	public static int challenges;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth;
	public static int gold;
    public static int energy;//FIXME not used yet,only for bugfix like ui usage
	
	public static HashSet<Integer> chapters;

	public static WndDialog savedWindow;

	public static SparseArray<ArrayList<Item>> droppedItems;

	public static int version;

	public static Plot plot;

    public static String customSeedText = "";
    public static long seed;

	public static void init() {

		version = Game.versionCode;
		challenges = PDSettings.challenges();

		seed = DungeonSeed.randomSeed();

		Actor.clear();
		Actor.resetNextID();
		
		Random.seed( seed );

			Scroll.initLabels();
			Potion.initColors();
			Ring.initGems();

			SpecialRoom.initForRun();
			SecretRoom.initForRun();

		Random.seed();
		
		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		
		depth = 0;
		gold = 0;

		droppedItems = new SparseArray<ArrayList<Item>>();

		for (LimitedDrops a : LimitedDrops.values())
			a.count = 0;
		
		chapters = new HashSet<Integer>();
		
		FrostNovaQuestPlot.Quest.reset();

		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Generator.reset();
		Generator.initArtifacts();
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		GamesInProgress.selectedClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}
	
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		depth++;
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}
		
		Level level;
		switch (depth) {
		case 1:
		case 2:
		case 3:
		case 4:
			level = new SewerLevel();
			break;
		case 5:
			level = new SewerBossLevel();
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			level = new PrisonLevel();
			break;
		case 10:
			level = new PrisonBossLevel();
			break;
		case 11:
		case 12:
		case 13:
		case 14:
			level = new CavesLevel();
			break;
		case 15:
			level = new CavesBossLevel();
			break;
		case 16:
		case 17:
		case 18:
		case 19:
			level = new CityLevel();
			break;
		case 20:
			level = new CityBossLevel();
			break;
		case 21:
			level = new LastShopLevel();
			break;
		case 22:
		case 23:
		case 24:
			level = new HallsLevel();
			break;
		case 25:
			level = new HallsBossLevel();
			break;
		case 26:
			level = new LastLevel();
			break;
		default:
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		
		level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		level.reset();
		switchLevel( level, level.entrance );
	}

	public static long seedCurDepth(){
		return seedForDepth(depth);
	}

	public static long seedForDepth(int depth){
		Random.seed( seed );
		for (int i = 0; i < depth; i ++)
			Random.Long(); //we don't care about these values, just need to go through them
		long result = Random.Long();
		Random.seed();
		return result;
	}
	
	public static boolean shopOnLevel() {
		return depth == 6 || depth == 11 || depth == 16;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
	}
	
	///@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		
		if (pos < 0 || pos >= level.length()){
			pos = level.exit;
		}
		
		PathFinder.setMapSize(level.width(), level.height());
		
		Dungeon.level = level;
		DriedRose.restoreGhostHero( level, pos );
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.addDelayed( respawner, level.respawnTime() );
		}

		hero.pos = pos;
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		hero.curAction = hero.lastAction = null;
		
		observe();
		try {
			saveAll();
		} catch (IOException e) {
			ArknightsPixelDungeon.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() );
		}
		dropped.add( item );
	}

	public static boolean posNeeded() {
		//2 POS each floor set
		int posLeftThisSet = 2 - (LimitedDrops.STRENGTH_POTIONS.count - (depth / 5) * 2);
		if (posLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);

		//pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
		int targetPOSLeft = 2 - floorThisSet/2;
		if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft --;

		if (targetPOSLeft < posLeftThisSet) return true;
		else return false;

	}
	
	public static boolean souNeeded() {
		int souLeftThisSet;
		//3 SOU each floor set, 1.5 (rounded) on forbidden runes challenge
		if (isChallenged(Challenges.NO_SCROLLS)){
			souLeftThisSet = Math.round(1.5f - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 1.5f));
		} else {
			souLeftThisSet = 3 - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 3);
		}
		if (souLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < souLeftThisSet;
	}
	
	public static boolean asNeeded() {
		//1 AS each floor set
		int asLeftThisSet = 1 - (LimitedDrops.ARCANE_STYLI.count - (depth / 5));
		if (asLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}
	
	private static final String VERSION		= "version";
	private static final String SEED		= "seed";
	private static final String CHALLENGES	= "challenges";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String DROPPED     = "dropped%d";
	private static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limited_drops";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	
	public static void saveGame( int save ) throws IOException {
		try {
			Bundle bundle = new Bundle();

			version = Game.versionCode;
			bundle.put( VERSION, version );
			bundle.put( SEED, seed );
			bundle.put( CHALLENGES, challenges );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );

			for (int d : droppedItems.keyArray()) {
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
			}

			quickslot.storePlaceholders( bundle );

			Bundle limDrops = new Bundle();
			LimitedDrops.store( limDrops );
			bundle.put ( LIMDROPS, limDrops );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();

			FrostNovaQuestPlot.Quest.storeInBundle( quests );

			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			SpecialRoom.storeRoomsInBundle( bundle );
			SecretRoom.storeRoomsInBundle( bundle );

			WndAlchemy.storeInBundle( bundle );

			WndDialog.storeInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Notes.storeInBundle( bundle );
			Generator.storeInBundle( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Ring.save( bundle );

			Actor.storeNextID( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );



			FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( save );
			ArknightsPixelDungeon.reportException(e);
		}
	}
	
	public static void saveLevel( int save ) throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		FileUtils.bundleToFile(GamesInProgress.depthFile( save, depth), bundle);
	}
	
	public static void saveAll() throws IOException {
		if (hero != null && hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( GamesInProgress.curSlot );
			saveLevel( GamesInProgress.curSlot );

			GamesInProgress.set( GamesInProgress.curSlot, depth, challenges, hero );

		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );
			
		}
	}
	
	public static void loadGame( int save ) throws IOException {
		loadGame( save, true );
	}
	
	public static void loadGame( int save, boolean fullLoad ) throws IOException {
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

		version = bundle.getInt( VERSION );

		seed = bundle.contains( SEED ) ? bundle.getLong( SEED ) : DungeonSeed.randomSeed();

		Actor.restoreNextID( bundle );

		quickslot.reset();
		QuickSlotButton.reset();

		Dungeon.challenges = bundle.getInt( CHALLENGES );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Ring.restore( bundle );

		quickslot.restorePlaceholders( bundle );
		
		if (fullLoad) {

			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				FrostNovaQuestPlot.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				FrostNovaQuestPlot.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);
		}
		
		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		Notes.restoreFromBundle( bundle );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );

		WndAlchemy.restoreFromBundle( bundle, hero );

        WndDialog.restoreFromBundle(bundle);

		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );
		
		Statistics.restoreFromBundle( bundle );
		Generator.restoreFromBundle( bundle );

		droppedItems = new SparseArray<>();
		for (int i=2; i <= Statistics.deepestFloor + 1; i++) {
			ArrayList<Item> dropped = new ArrayList<Item>();
			if (bundle.contains(Messages.format( DROPPED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( DROPPED, i ) ) ) {
					dropped.add( (Item)b );
				}
			if (!dropped.isEmpty()) {
				droppedItems.put( i, dropped );
			}
		}
	}
	
	public static Level loadLevel( int save ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.depthFile( save, depth)) ;
		
		Level level = (Level)bundle.get( LEVEL );
		
		if (level == null){
			throw new IOException();
		} else {
			return level;
		}
	}
	
	public static void deleteGame( int save, boolean deleteLevels ) {
		
		FileUtils.deleteFile(GamesInProgress.gameFile(save));
		
		if (deleteLevels) {
			FileUtils.deleteDir(GamesInProgress.gameFolder(save));
		}
		
		GamesInProgress.delete( save );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.version = bundle.getInt( VERSION );
		info.challenges = bundle.getInt( CHALLENGES );
		Hero.preview( info, bundle.getBundle( HERO ) );
		Statistics.preview( info, bundle );
	}
	
	public static void fail( Class cause ) {
		if (hero.belongings.getItem( Ankh.class ) == null) {
			Rankings.INSTANCE.submit( false, cause );
		}
	}
	
	public static void win( Class cause ) {

		hero.belongings.identify();

		int chCount = 0;
		for (int ch : Challenges.MASKS){
			if ((challenges & ch) != 0) chCount++;
		}
		
		if (chCount != 0) {
			Badges.validateChampion(chCount);
		}

		Rankings.INSTANCE.submit( true, cause );
	}

    public static void observe(){
        int dist = Math.max(Dungeon.hero.viewDistance, 8);
        //dist *= 1f + 0.25f*Dungeon.hero.pointsInTalent(Talent.FARSIGHT);

        if (Dungeon.hero.buff(MagicalSight.class) != null){
            dist = Math.max( dist, MagicalSight.DISTANCE );
        }

        observe( dist+1 );
    }

    public static void observe( int dist ) {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero, level.heroFOV);

        int x = hero.pos % level.width();
        int y = hero.pos / level.width();

        //left, right, top, bottom
        int l = Math.max( 0, x - dist );
        int r = Math.min( x + dist, level.width() - 1 );
        int t = Math.max( 0, y - dist );
        int b = Math.min( y + dist, level.height() - 1 );

        int width = r - l + 1;
        int height = b - t + 1;

        int pos = l + t * level.width();

        for (int i = t; i <= b; i++) {
            BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
            pos+=level.width();
        }

        GameScene.updateFog(l, t, width, height);

        if (hero.buff(MindVision.class) != null){
            for (Mob m : level.mobs.toArray(new Mob[0])){
                BArray.or( level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited );
                BArray.or( level.visited, level.heroFOV, m.pos - 1, 3, level.visited );
                BArray.or( level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited );
                //updates adjacent cells too
                GameScene.updateFog(m.pos, 2);
            }
        }

        if (hero.buff(Awareness.class) != null){
            for (Heap h : level.heaps.valueList()){
                BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
                BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
                BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
                GameScene.updateFog(h.pos, 2);
            }
        }

        //we have to update gunSight at here,before fog of war as this is not a completion like MindVision or so,it can be narrowed and changed,and performs better as it contains a considerable amount of cells
        if(hero.buff(SniperSight.class)!= null && hero.buff(Blindness.class)== null)
        {
            GameScene.updateFog();
        }

        /*for (TalismanOfForesight.CharAwareness c : hero.buffs(TalismanOfForesight.CharAwareness.class)){
            Char ch = (Char) Actor.findById(c.charID);
            if (ch == null || !ch.isAlive()) continue;
            BArray.or( level.visited, level.heroFOV, ch.pos - 1 - level.width(), 3, level.visited );
            BArray.or( level.visited, level.heroFOV, ch.pos - 1, 3, level.visited );
            BArray.or( level.visited, level.heroFOV, ch.pos - 1 + level.width(), 3, level.visited );
            GameScene.updateFog(ch.pos, 2);
        }*/

        /*for (TalismanOfForesight.HeapAwareness h : hero.buffs(TalismanOfForesight.HeapAwareness.class)){
            if (Dungeon.depth != h.depth) continue;
            BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
            BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
            BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
            GameScene.updateFog(h.pos, 2);
        }*/

        /*for (RevealedArea a : hero.buffs(RevealedArea.class)){
            if (Dungeon.depth != a.depth) continue;
            BArray.or( level.visited, level.heroFOV, a.pos - 1 - level.width(), 3, level.visited );
            BArray.or( level.visited, level.heroFOV, a.pos - 1, 3, level.visited );
            BArray.or( level.visited, level.heroFOV, a.pos - 1 + level.width(), 3, level.visited );
            GameScene.updateFog(a.pos, 2);
        }*/

        /*for (Char ch : Actor.chars()){
            if (ch instanceof WandOfWarding.Ward
                    || ch instanceof WandOfRegrowth.Lotus
                    || ch instanceof SpiritHawk.HawkAlly){
                x = ch.pos % level.width();
                y = ch.pos / level.width();

                //left, right, top, bottom
                dist = ch.viewDistance+1;
                l = Math.max( 0, x - dist );
                r = Math.min( x + dist, level.width() - 1 );
                t = Math.max( 0, y - dist );
                b = Math.min( y + dist, level.height() - 1 );

                width = r - l + 1;
                height = b - t + 1;

                pos = l + t * level.width();

                for (int i = t; i <= b; i++) {
                    BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
                    pos+=level.width();
                }
                GameScene.updateFog(ch.pos, dist);
            }
        }*/

        GameScene.afterObserve();
    }

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable(){
		if (passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static PathFinder.Path findPath(Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}

		return PathFinder.find( from, to, passable );

	}
	
	public static int findStep(Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		if (Dungeon.level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Dungeon.level.avoid[to]) ? to : -1;
		}

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}
		
		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}
		
		return PathFinder.getStep( from, to, passable );

	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {

		setupPassable();
		if (ch.flying) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}
		
		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}
		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
