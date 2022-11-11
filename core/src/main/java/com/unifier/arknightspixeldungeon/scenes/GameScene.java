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

package com.unifier.arknightspixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.Statistics;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.blobs.Blob;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.BannerSprites;
import com.unifier.arknightspixeldungeon.effects.BlobEmitter;
import com.unifier.arknightspixeldungeon.effects.EmoIcon;
import com.unifier.arknightspixeldungeon.effects.Flare;
import com.unifier.arknightspixeldungeon.effects.FloatingText;
import com.unifier.arknightspixeldungeon.effects.Ripple;
import com.unifier.arknightspixeldungeon.effects.SpellSprite;
import com.unifier.arknightspixeldungeon.effects.TalentSprite;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.items.Honeypot;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.potions.Potion;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.unifier.arknightspixeldungeon.journal.Journal;
import com.unifier.arknightspixeldungeon.levels.RegularLevel;
import com.unifier.arknightspixeldungeon.levels.traps.Trap;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.plants.Plant;
import com.unifier.arknightspixeldungeon.scripts.FormalPlot.SewerPlot;
import com.unifier.arknightspixeldungeon.scripts.Script;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.DiscardedItemSprite;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.tiles.CustomTiledVisual;
import com.unifier.arknightspixeldungeon.tiles.DungeonTerrainTilemap;
import com.unifier.arknightspixeldungeon.tiles.DungeonTileSheet;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.unifier.arknightspixeldungeon.tiles.DungeonWallsTilemap;
import com.unifier.arknightspixeldungeon.tiles.FogOfWar;
import com.unifier.arknightspixeldungeon.tiles.GridTileMap;
import com.unifier.arknightspixeldungeon.tiles.TerrainFeaturesTilemap;
import com.unifier.arknightspixeldungeon.tiles.WallBlockingTilemap;
import com.unifier.arknightspixeldungeon.ui.ActionIndicator;
import com.unifier.arknightspixeldungeon.ui.AttackIndicator;
import com.unifier.arknightspixeldungeon.ui.Banner;
import com.unifier.arknightspixeldungeon.ui.BossHealthBar;
import com.unifier.arknightspixeldungeon.ui.BusyIndicator;
import com.unifier.arknightspixeldungeon.ui.CharHealthIndicator;
import com.unifier.arknightspixeldungeon.ui.GameLog;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.InventoryPane;
import com.unifier.arknightspixeldungeon.ui.LootIndicator;
import com.unifier.arknightspixeldungeon.ui.MenuPane;
import com.unifier.arknightspixeldungeon.ui.QuickSlotButton;
import com.unifier.arknightspixeldungeon.ui.ResumeIndicator;
import com.unifier.arknightspixeldungeon.ui.RightClickMenu;
import com.unifier.arknightspixeldungeon.ui.StatusPane;
import com.unifier.arknightspixeldungeon.ui.TargetHealthIndicator;
import com.unifier.arknightspixeldungeon.ui.Toast;
import com.unifier.arknightspixeldungeon.ui.Toolbar;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndDialog;
import com.unifier.arknightspixeldungeon.windows.WndGame;
import com.unifier.arknightspixeldungeon.windows.WndHero;
import com.unifier.arknightspixeldungeon.windows.WndInfoCell;
import com.unifier.arknightspixeldungeon.windows.WndInfoItem;
import com.unifier.arknightspixeldungeon.windows.WndInfoMob;
import com.unifier.arknightspixeldungeon.windows.WndInfoPlant;
import com.unifier.arknightspixeldungeon.windows.WndInfoTrap;
import com.unifier.arknightspixeldungeon.windows.WndMessage;
import com.unifier.arknightspixeldungeon.windows.WndOptions;
import com.watabou.glwrap.Blending;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class GameScene extends PixelScene {

    public static boolean logActorThread;

	public static GameScene scene;

	private SkinnedBlock water;
	private DungeonTerrainTilemap tiles;
	private GridTileMap visualGrid;
	private TerrainFeaturesTilemap terrainFeatures;
	private DungeonWallsTilemap walls;
	private WallBlockingTilemap wallBlocking;
	private FogOfWar fog;
	private HeroSprite hero;

    private MenuPane menu;
    private StatusPane pane;

    private BossHealthBar boss;

	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group customTiles;
	private Group levelVisuals;
	private Group customWalls;
	private Group ripples;
	private Group plants;
	private Group traps;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group talents;
	private Group statuses;
	private Group emoicons;
	private Group healthIndicators;

    private InventoryPane inventory;
    private static boolean invVisible = true;

    private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ActionIndicator action;
	private ResumeIndicator resume;
	
	@Override
	public void create() {

        if (Dungeon.hero == null || Dungeon.level == null){
            ArknightsPixelDungeon.switchNoFade(TitleScene.class);
            return;
        }

		Music.INSTANCE.play( Assets.TUNE, true );

		PDSettings.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();

		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + PDSettings.zoom(), maxZoom));
        Camera.main.scrollable = true;

        switch (PDSettings.cameraFollow()) {
            case 4: default:    Camera.main.setFollowDeadzone(0);      break;
            case 3:             Camera.main.setFollowDeadzone(0.2f);   break;
            case 2:             Camera.main.setFollowDeadzone(0.5f);   break;
            case 1:             Camera.main.setFollowDeadzone(0.9f);   break;
        }

		scene = this;

		terrain = new Group();
		add( terrain );

		water = new SkinnedBlock(
			Dungeon.level.width() * DungeonTilemap.SIZE,
			Dungeon.level.height() * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() ){

			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
        water.autoAdjust = true;
        terrain.add( water );

		ripples = new Group();
		terrain.add( ripples );

		DungeonTileSheet.setupVariance(Dungeon.level.map.length, Dungeon.seedCurDepth());
		
		tiles = new DungeonTerrainTilemap();
		terrain.add( tiles );

		customTiles = new Group();
		terrain.add(customTiles);

		for( CustomTiledVisual visual : Dungeon.level.customTiles){
			addCustomTile(visual);
		}

		visualGrid = new GridTileMap();
		terrain.add( visualGrid );

		terrainFeatures = new TerrainFeaturesTilemap(Dungeon.level.plants, Dungeon.level.traps);
		terrain.add(terrainFeatures);
		
		levelVisuals = Dungeon.level.addVisuals();
		add(levelVisuals);
		
		heaps = new Group();
		add( heaps );

        for ( Heap heap : Dungeon.level.heaps.valueList() ) {
            addHeapSprite( heap );
        }
		
		emitters = new Group();
		effects = new Group();
		healthIndicators = new Group();
		emoicons = new Group();
		
		mobs = new Group();
		add( mobs );

        hero = new HeroSprite();
        hero.place( Dungeon.hero.pos );
        hero.updateArmor();
        mobs.add( hero );

		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}

		walls = new DungeonWallsTilemap();
		add(walls);

		customWalls = new Group();
		add(customWalls);

		for( CustomTiledVisual visual : Dungeon.level.customWalls){
			addCustomWall(visual);
		}

		wallBlocking = new WallBlockingTilemap();
		add (wallBlocking);

		add( emitters );
		add( effects );

		gases = new Group();
		add( gases );

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}


		fog = new FogOfWar( Dungeon.level.width(), Dungeon.level.height() );
		add( fog );

		spells = new Group();
		add( spells );

		talents = new Group();
		add( talents );

		statuses = new Group();
		add( statuses );
		
		add( healthIndicators );
		//always appears ontop of other health indicators
		add( new TargetHealthIndicator() );
		
		add( emoicons );
		
		add( cellSelector = new CellSelector( tiles ) );

        int uiSize = PDSettings.interfaceSize();

        menu = new MenuPane();
        menu.camera = uiCamera;
        menu.setPos( uiCamera.width-MenuPane.WIDTH, uiSize > 0 ? 0 : 1);
        add(menu);

		pane = new StatusPane();
		pane.camera = uiCamera;
		pane.setSize( uiCamera.width, 0 );
		add( pane );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		action = new ActionIndicator();
		action.camera = uiCamera;
		add( action );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		log = new GameLog();
		log.camera = uiCamera;
		log.newLine();
		add( log );

		layoutTags();

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = pane.bottom() + 1;
		add( busy );

		switch (InterlevelScene.mode) {
		case RESURRECT:
			ScrollOfTeleportation.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			ScrollOfTeleportation.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
                if(Script.checkChapter(Script.ID_SEWERS)) {
                    GameScene.scene.add(new WndDialog(new SewerPlot(),false));
                }
                break;

			case 6:
                if(Script.checkChapter(Script.ID_PRISON)) {
                    GameScene.scene.add(new WndDialog(new SewerPlot(),false));
                }
                break;

			case 11:
                if(Script.checkChapter(Script.ID_CAVES)) {
                    GameScene.scene.add(new WndDialog(new SewerPlot(),false));
                }
                break;

			case 16:
                if(Script.checkChapter(Script.ID_CITY)) {
                    GameScene.scene.add(new WndDialog(new SewerPlot(),false));
                }
                break;

			case 22:
                if(Script.checkChapter(Script.ID_HALLS)) {
                    GameScene.scene.add(new WndDialog(new SewerPlot(),false));
                }
                break;
			}

			if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
				Badges.validateNoKilling();
			}

			break;

            case CONTINUE:
                if(WndDialog.settedPlot!=null)
                {
                    GameScene.scene.add(new WndDialog(WndDialog.settedPlot,true));
                }
                break;

		default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

		Dungeon.hero.next();

        switch (InterlevelScene.mode){
            case FALL: case DESCEND: case CONTINUE:
                Camera.main.snapTo(hero.center().x, hero.center().y - DungeonTilemap.SIZE * (defaultZoom/Camera.main.zoom));
                break;
            case ASCEND:
                Camera.main.snapTo(hero.center().x, hero.center().y + DungeonTilemap.SIZE * (defaultZoom/Camera.main.zoom));
                break;
            default:
                Camera.main.snapTo(hero.center().x, hero.center().y);
        }

        Camera.main.panTo(hero.center(), 2.5f);

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
			if (Dungeon.depth == Statistics.deepestFloor
					&& (InterlevelScene.mode == InterlevelScene.Mode.DESCEND || InterlevelScene.mode == InterlevelScene.Mode.FALL)) {
				GLog.h(Messages.get(this, "descend"), Dungeon.depth);
				Sample.INSTANCE.play(Assets.SND_DESCEND);
			} else if (InterlevelScene.mode == InterlevelScene.Mode.RESET) {
				GLog.h(Messages.get(this, "warp"));
			} else {
				GLog.h(Messages.get(this, "return"), Dungeon.depth);
			}

			switch (Dungeon.level.feeling) {
				case CHASM:
					GLog.w(Messages.get(this, "chasm"));
					break;
				case WATER:
					GLog.w(Messages.get(this, "water"));
					break;
				case GRASS:
					GLog.w(Messages.get(this, "grass"));
					break;
				case DARK:
					GLog.w(Messages.get(this, "dark"));
					break;
				default:
			}
			if (Dungeon.level instanceof RegularLevel &&
					((RegularLevel) Dungeon.level).secretDoors > Random.IntRange(3, 4)) {
				GLog.w(Messages.get(this, "secrets"));
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			fadeIn();
		}

        GameScene.updateSkill(1,Dungeon.hero.skill_1);
        GameScene.updateSkill(2,Dungeon.hero.skill_2);
        GameScene.updateSkill(3,Dungeon.hero.skill_3);
	}

	public void destroy() {

		//tell the actor thread to finish, then wait for it to complete any actions it may be doing.
		if (actorThread.isAlive()){
			synchronized (GameScene.class){
				synchronized (actorThread) {
					actorThread.interrupt();
				}
				try {
					GameScene.class.wait(5000);
				} catch (InterruptedException e) {
					ArknightsPixelDungeon.reportException(e);
				}
				synchronized (actorThread) {
					if (Actor.processing()) {
						Throwable t = new Throwable();
						t.setStackTrace(actorThread.getStackTrace());
						throw new RuntimeException("timeout waiting for actor thread! ", t);
					}
				}
			}
		}

		freezeEmitters = false;
		
		scene = null;
		Badges.saveGlobal();
		Journal.saveGlobal();
		
		super.destroy();
	}

    public static void endActorThread(){
        if (actorThread != null && actorThread.isAlive()){
            Actor.keepActorThreadAlive = false;
            actorThread.interrupt();
        }
    }

    public boolean waitForActorThread(int msToWait, boolean interrupt){
        if (actorThread == null || !actorThread.isAlive()) {
            return true;
        }
        synchronized (actorThread) {
            if (interrupt) actorThread.interrupt();
            try {
                actorThread.wait(msToWait);
            } catch (InterruptedException e) {
                ArknightsPixelDungeon.reportException(e);
            }
            return !Actor.processing();
        }
    }


    @Override
	public synchronized void onPause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			ArknightsPixelDungeon.reportException(e);
		}
	}

	private static Thread actorThread;
            /*= new Thread() {
		@Override
		public void run() {
			Actor.process();
		}
	};*/

    //sometimes UI changes can be prompted by the actor thread.
    // We queue any removed element destruction, rather than destroying them in the actor thread.
    private ArrayList<Gizmo> toDestroy = new ArrayList<>();

    //the actor thread processes at a maximum of 60 times a second
    //this caps the speed of resting for higher refresh rate displays
    private float notifyDelay = 1/60f;

    public static boolean updateItemDisplays = false;

	@Override
	public synchronized void update() {
        lastOffset = null;

        if (updateItemDisplays){
            updateItemDisplays = false;
            QuickSlotButton.refresh();
            InventoryPane.refresh();
        }

		if (Dungeon.hero == null || scene == null) {
			return;
		}

		super.update();

        if (notifyDelay > 0) notifyDelay -= Game.elapsed;

        if (!Emitter.freezeEmitters) water.offset( 0, -5 * Game.elapsed );


        if(logActorThread){
            if (actorThread != null){
                logActorThread = false;
                String s = "";
                for (StackTraceElement t : actorThread.getStackTrace()){
                    s += "\n";
                    s += t.toString();
                }
                Class<? extends Actor> cl = Actor.getCurrentActorClass();
                String msg = "Actor thread dump was requested. " +
                        "Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " challenges:" +
                        " current actor:" + cl + "\ntrace:" + s;
                Gdx.app.getClipboard().setContents(msg);
                ArknightsPixelDungeon.reportException(
                        new RuntimeException(msg)
                );
                add(new WndMessage(Messages.get(this, "copied")));
            }
        }

        if (!Actor.processing() && Dungeon.hero.isAlive()) {
            if (actorThread == null || !actorThread.isAlive()) {

                actorThread = new Thread() {
                    @Override
                    public void run() {
                        Actor.process();
                    }
                };

                //if cpu cores are limited, game should prefer drawing the current frame
                if (Runtime.getRuntime().availableProcessors() == 1) {
                    actorThread.setPriority(Thread.NORM_PRIORITY - 1);
                }
                actorThread.setName("SHPD Actor Thread");
                Thread.currentThread().setName("SHPD Render Thread");
                Actor.keepActorThreadAlive = true;
                actorThread.start();
            } else if (notifyDelay <= 0f) {
                notifyDelay += 1/60f;
                synchronized (actorThread) {
                    actorThread.notify();
                }
            }
        }
		
		if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
			log.newLine();
		}

		if (tagAttack != attack.active ||
				tagLoot != loot.visible ||
				tagAction != action.visible ||
				tagResume != resume.visible) {

			//we only want to change the layout when new tags pop in, not when existing ones leave.
			boolean tagAppearing = (attack.active && !tagAttack) ||
									(loot.visible && !tagLoot) ||
									(action.visible && !tagAction) ||
									(resume.visible && !tagResume);

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagAction = action.visible;
			tagResume = resume.visible;

			if (tagAppearing) layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);

        for (Gizmo g : toDestroy){
            g.destroy();
        }
        toDestroy.clear();
	}

    private static Point lastOffset = null;

    private boolean tagAttack    = false;
	private boolean tagLoot      = false;
	private boolean tagAction    = false;
	private boolean tagResume    = false;

	public static void layoutTags() {

		if (scene == null) return;

		float tagLeft = PDSettings.flipTags() ? 0 : uiCamera.width - scene.attack.width();

		if (PDSettings.flipTags()) {
			scene.log.setRect(scene.attack.width(), scene.toolbar.top() - 2, uiCamera.width - scene.attack.width(), 0);
		} else {
			scene.log.setRect(0, scene.toolbar.top() - 2 , uiCamera.width - scene.attack.width(),  0 );
		}

		float pos = scene.toolbar.top();

		if (scene.tagAttack){
			scene.attack.setPos( tagLeft, pos - scene.attack.height());
			scene.attack.flip(tagLeft == 0);
			pos = scene.attack.top();
		}

		if (scene.tagLoot) {
			scene.loot.setPos( tagLeft, pos - scene.loot.height() );
			scene.loot.flip(tagLeft == 0);
			pos = scene.loot.top();
		}

		if (scene.tagAction) {
			scene.action.setPos( tagLeft, pos - scene.action.height() );
			scene.action.flip(tagLeft == 0);
			pos = scene.action.top();
		}

		if (scene.tagResume) {
			scene.resume.setPos( tagLeft, pos - scene.resume.height() );
			scene.resume.flip(tagLeft == 0);
		}
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}

	public void addCustomTile( CustomTiledVisual visual){
		customTiles.add( visual.create() );
	}

	public void addCustomWall( CustomTiledVisual visual){
		customWalls.add( visual.create() );
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {

	}

	private void addTrapSprite( Trap trap ) {

	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		if(sprite!=null){
            sprite.visible = Dungeon.level.heroFOV[mob.pos];
            mobs.add( sprite );
            sprite.link( mob );
        }
	}
	
	private synchronized void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt.destroy();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );

            //if (inventory != null && inventory.visible && prompt.right() > inventory.left() - 10){
            //    prompt.setPos(inventory.left() - prompt.width() - 10, prompt.top());
            //}

			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		addToFront( banner );
	}
	
	// -------------------------------------------------------

	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}

	public static void add( Trap trap ) {
		if (scene != null) {
			scene.addTrapSprite( trap );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		scene.addMobSprite( mob );
        Actor.add( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void add( CharHealthIndicator indicator ){
		if (scene != null) scene.healthIndicators.add(indicator);
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		if (scene != null) {
			Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
			ripple.reset(pos);
			return ripple;
		} else {
			return null;
		}
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}

    public static TalentSprite talentSprite() {
        return (TalentSprite)scene.talents.recycle( TalentSprite.class );
    }
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item, int pos ) {
		if (scene != null) scene.toolbar.pickup( item, pos );
	}

	public static void pickUpJournal( Item item, int pos ) {
		if (scene != null) scene.pane.pickup( item, pos );
	}
	
	public static void flashJournal(){
		if (scene != null) scene.pane.flash();
	}
	
	public static void updateKeyDisplay(){
		if (scene != null) scene.pane.updateKeys();
	}

	public static void resetMap() {
		if (scene != null) {
			scene.tiles.map(Dungeon.level.map, Dungeon.level.width() );
			scene.visualGrid.map(Dungeon.level.map, Dungeon.level.width() );
			scene.terrainFeatures.map(Dungeon.level.map, Dungeon.level.width() );
			scene.walls.map(Dungeon.level.map, Dungeon.level.width() );
		}
		updateFog();
	}

	//updates the whole map
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updateMap();
			scene.visualGrid.updateMap();
			scene.terrainFeatures.updateMap();
			scene.walls.updateMap();
			updateFog();
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updateMapCell( cell );
			scene.visualGrid.updateMapCell( cell );
			scene.terrainFeatures.updateMapCell( cell );
			scene.walls.updateMapCell( cell );
			//update adjacent cells too
			updateFog( cell, 1 );
		}
	}

	public static void plantSeed( int cell ) {
		if (scene != null) {
			scene.terrainFeatures.growPlant( cell );
		}
	}
	
	//todo this doesn't account for walls right now
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		if (scene != null) {
			cancelCellSelector();
			scene.addToFront(wnd);
		}
	}

    public static boolean showingWindow(){
        if (scene == null) return false;

        for (Gizmo g : scene.members){
            if (g instanceof Window) return true;
        }

        return false;
    }

    public static boolean interfaceBlockingHero(){
        if (scene == null) return false;

        if (showingWindow()) return true;

        if (scene.inventory != null && scene.inventory.isSelecting()){
            return true;
        }

        return false;
    }

    public static void toggleInvPane(){
        if (scene != null && scene.inventory != null){
            if (scene.inventory.visible){
                scene.inventory.visible = scene.inventory.active = invVisible = false;
                scene.toolbar.setPos(scene.toolbar.left(), uiCamera.height-scene.toolbar.height());
            } else {
                scene.inventory.visible = scene.inventory.active = invVisible = true;
                scene.toolbar.setPos(scene.toolbar.left(), scene.inventory.top()-scene.toolbar.height());
            }
            layoutTags();
        }
    }

    public static void centerNextWndOnInvPane(){
        if (scene != null && scene.inventory != null && scene.inventory.visible){
            lastOffset = new Point((int)scene.inventory.centerX() - uiCamera.width/2,
                    (int)scene.inventory.centerY() - uiCamera.height/2);
        }
    }

	public static void updateFog(){
		if (scene != null) {
			scene.fog.updateFog();
			scene.wallBlocking.updateMap();
		}
	}

	public static void updateFog(int x, int y, int w, int h){
		if (scene != null) {
			scene.fog.updateFogArea(x, y, w, h);
			scene.wallBlocking.updateArea(x, y, w, h);
		}
	}
	
	public static void updateFog( int cell, int radius ){
		if (scene != null) {
			scene.fog.updateFog( cell, radius );
			scene.wallBlocking.updateArea( cell, radius );
		}
	}
	
	public static void afterObserve() {
		if (scene != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null)
					mob.sprite.visible = Dungeon.level.heroFOV[mob.pos];
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}

    public static void handleCell( int cell ) {
        cellSelector.select( cell, PointerEvent.LEFT );
    }
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		if (scene != null)
			scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}

    public static WndBag selectItem( WndBag.ItemSelector listener ) {
        cancelCellSelector();

        if (scene != null) {
            //TODO can the inventory pane work in these cases? bad to fallback to mobile window
            if (scene.inventory != null && scene.inventory.visible && !showingWindow()){
                scene.inventory.setSelector(listener);
                return null;
            } else {
                WndBag wnd = WndBag.getBag( listener );
                show(wnd);
                return wnd;
            }
        }

        return null;
    }
	
	public static boolean cancel() {
		if (Dungeon.hero != null && (Dungeon.hero.curAction != null || Dungeon.hero.resting)) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
		if (scene != null && scene.toolbar != null) scene.toolbar.examining = false;
	}

	public static void examineCell( Integer cell ) {
		if (cell == null
				|| cell < 0
				|| cell > Dungeon.level.length()
				|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
			return;
		}

		ArrayList<String> names = new ArrayList<>();
		final ArrayList<Object> objects = new ArrayList<>();

		if (cell == Dungeon.hero.pos) {
			objects.add(Dungeon.hero);
			names.add(Dungeon.hero.className().toUpperCase(Locale.ENGLISH));
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Mob mob = (Mob) Actor.findChar(cell);
				if (mob != null) {
					objects.add(mob);
					names.add(Messages.titleCase( mob.name ));
				}
			}
		}

		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null && heap.seen) {
			objects.add(heap);
			names.add(Messages.titleCase( heap.toString() ));
		}

		Plant plant = Dungeon.level.plants.get( cell );
		if (plant != null) {
			objects.add(plant);
			names.add(Messages.titleCase( plant.name() ));
		}

		Trap trap = Dungeon.level.traps.get( cell );
		if (trap != null && trap.visible) {
			objects.add(trap);
			names.add(Messages.titleCase( trap.name() ));
		}

		if (objects.isEmpty()) {
			GameScene.show(new WndInfoCell(cell));
		} else if (objects.size() == 1){
			examineObject(objects.get(0));
		} else {
			GameScene.show(new WndOptions(Messages.get(GameScene.class, "choose_examine"),
					Messages.get(GameScene.class, "multiple_examine"), names.toArray(new String[names.size()])){
				@Override
				protected void onSelect(int index) {
					examineObject(objects.get(index));
				}
			});

		}
	}

    private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer cell ) {
            if (Dungeon.hero.handle( cell )) {
                Dungeon.hero.next();
            }
        }

        @Override
        public void onRightClick(Integer cell) {
            if (cell == null
                    || cell < 0
                    || cell > Dungeon.level.length()
                    || (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
                return;
            }

            ArrayList<Object> objects = getObjectsAtCell(cell);
            ArrayList<String> textLines = getObjectNames(objects);

            //determine title and image
            String title = null;
            Image image = null;
            if (objects.isEmpty()) {
                title = WndInfoCell.cellName(cell);
                image = WndInfoCell.cellImage(cell);
            } else if (objects.size() > 1){
                title = Messages.get(GameScene.class, "multiple");
                image = Icons.get(Icons.INFO);
            } else if (objects.get(0) instanceof Hero) {
                title = textLines.remove(0);
                image = HeroSprite.avatar(((Hero) objects.get(0)).heroClass, ((Hero) objects.get(0)).tier());
            } else if (objects.get(0) instanceof Mob) {
                title = textLines.remove(0);
                image = ((Mob) objects.get(0)).sprite();
            } else if (objects.get(0) instanceof Heap) {
                title = textLines.remove(0);
                image = new ItemSprite((Heap) objects.get(0));
            } else if (objects.get(0) instanceof Plant) {
                title = textLines.remove(0);
                image = TerrainFeaturesTilemap.tile(cell, Dungeon.level.map[cell]);
            } else if (objects.get(0) instanceof Trap) {
                title = textLines.remove(0);
                image = TerrainFeaturesTilemap.tile(cell, Dungeon.level.map[cell]);
            }

            //determine first text line
            if (objects.isEmpty()) {
                textLines.add(0, Messages.get(GameScene.class, "go_here"));
            } else if (objects.get(0) instanceof Hero) {
                textLines.add(0, Messages.get(GameScene.class, "go_here"));
            } else if (objects.get(0) instanceof Mob) {
                if (((Mob) objects.get(0)).alignment != Char.Alignment.ENEMY) {
                    textLines.add(0, Messages.get(GameScene.class, "interact"));
                } else {
                    textLines.add(0, Messages.get(GameScene.class, "attack"));
                }
            } else if (objects.get(0) instanceof Heap) {
                switch (((Heap) objects.get(0)).type) {
                    case HEAP:
                        textLines.add(0, Messages.get(GameScene.class, "pick_up"));
                        break;
                    case FOR_SALE:
                        textLines.add(0, Messages.get(GameScene.class, "purchase"));
                        break;
                    default:
                        textLines.add(0, Messages.get(GameScene.class, "interact"));
                        break;
                }
            } else if (objects.get(0) instanceof Plant) {
                textLines.add(0, Messages.get(GameScene.class, "trample"));
            } else if (objects.get(0) instanceof Trap) {
                textLines.add(0, Messages.get(GameScene.class, "interact"));
            }

            //final text formatting
            if (objects.size() > 1){
                textLines.add(0, "_" + textLines.remove(0) + ":_ " + textLines.get(0));
                for (int i = 1; i < textLines.size(); i++){
                    textLines.add(i, "_" + Messages.get(GameScene.class, "examine") + ":_ " + textLines.remove(i));
                }
            } else {
                textLines.add(0, "_" + textLines.remove(0) + "_");
                textLines.add(1, "_" + Messages.get(GameScene.class, "examine") + "_");
            }

            RightClickMenu menu = new RightClickMenu(image,
                    title,
                    textLines.toArray(new String[0])){
                @Override
                public void onSelect(int index) {
                    if (index == 0){
                        handleCell(cell);
                    } else {
                        if (objects.size() == 0){
                            GameScene.show(new WndInfoCell(cell));
                        } else {
                            examineObject(objects.get(index-1));
                        }
                    }
                }
            };
            scene.addToFront(menu);
            menu.camera = PixelScene.uiCamera;
            PointF mousePos = PointerEvent.currentHoverPos();
            mousePos = menu.camera.screenToCamera((int)mousePos.x, (int)mousePos.y);
            menu.setPos(mousePos.x-3, mousePos.y-3);

        }

        @Override
        public String prompt() {
            return null;
        }
    };

    private static ArrayList<Object> getObjectsAtCell( int cell ){
        ArrayList<Object> objects = new ArrayList<>();

        if (cell == Dungeon.hero.pos) {
            objects.add(Dungeon.hero);

        } else if (Dungeon.level.heroFOV[cell]) {
            Mob mob = (Mob) Actor.findChar(cell);
            if (mob != null) objects.add(mob);
        }

        Heap heap = Dungeon.level.heaps.get(cell);
        if (heap != null && heap.seen) objects.add(heap);

        Plant plant = Dungeon.level.plants.get( cell );
        if (plant != null) objects.add(plant);

        Trap trap = Dungeon.level.traps.get( cell );
        if (trap != null && trap.visible) objects.add(trap);

        return objects;
    }

    private static ArrayList<String> getObjectNames( ArrayList<Object> objects ){
        ArrayList<String> names = new ArrayList<>();
        for (Object obj : objects){
            if (obj instanceof Hero)        names.add(((Hero) obj).className().toUpperCase(Locale.ENGLISH));
            else if (obj instanceof Mob)    names.add(Messages.titleCase( ((Mob)obj).name() ));
            else if (obj instanceof Heap)   names.add(Messages.titleCase( ((Heap)obj).title() ));
            else if (obj instanceof Plant)  names.add(Messages.titleCase( ((Plant) obj).name() ));
            else if (obj instanceof Trap)   names.add(Messages.titleCase( ((Trap) obj).name() ));
        }
        return names;
    }

    public static void examineObject(Object o){
        if (o == Dungeon.hero){
            GameScene.show( new WndHero() );
        } else if ( o instanceof Mob ){
            GameScene.show(new WndInfoMob((Mob) o));
            //if (o instanceof Snake && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_SURPRISE_ATKS)){
            //    GLog.p(Messages.get(Guidebook.class, "hint"));
            //    GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_SURPRISE_ATKS);
            //}
        } else if ( o instanceof Heap ){
            GameScene.show(new WndInfoItem((Heap)o));
        } else if ( o instanceof Plant ){
            GameScene.show( new WndInfoPlant((Plant) o) );
        } else if ( o instanceof Trap ){
            GameScene.show( new WndInfoTrap((Trap) o));
        } else {
            GameScene.show( new WndMessage( Messages.get(GameScene.class, "dont_know") ) ) ;
        }
    }



    public static float ToolbarHeight(){return scene.toolbar.height();}

    public static float StatusHeight(){return scene.pane.height();}

    public static void updateSkill(int index,HeroSkill skill)
    {
        scene.pane.loadSkill(index,skill);
    }
}
