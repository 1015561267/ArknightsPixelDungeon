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

package com.unifier.arknightspixeldungeon;;

import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;

public class ArknightsPixelDungeon extends Game {
	
	//variable constants for specific older versions of shattered, used for data conversion
	//versions older than v0.6.0b are no longer supported, and data from them is ignored

	public static final int Demo = 1; //remove version code and other support,make it a new start for us.
    //By Teller 2021/8/9
	
	public ArknightsPixelDungeon(PlatformSupport platform) {
        super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );
		
		//v0.6.2
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.rooms.secret.RatKingRoom.class,
				"com.unifier.arknightspixeldungeon.levels.rooms.special.RatKingRoom" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.rooms.standard.PlantsRoom.class,
				"com.unifier.arknightspixeldungeon.levels.rooms.standard.GardenRoom" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.rooms.special.GardenRoom.class,
				"com.unifier.arknightspixeldungeon.levels.rooms.special.FoliageRoom" );
		
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.WornDartTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.WornTrap" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.PoisonDartTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.PoisonTrap" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.ShockingTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.ParalyticTrap" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.ShockingTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.LightningTrap" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.GrippingTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.SpearTrap" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.BurningTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.FireTrap" );
		
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.actors.buffs.BlobImmunity.class,
				"com.unifier.arknightspixeldungeon.actors.buffs.GasesImmunity" );
		
		//v0.6.3
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.weapon.missiles.Tomahawk.class,
				"com.unifier.arknightspixeldungeon.items.weapon.missiles.Tamahawk" );
		
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.Dart.class,
				"com.unifier.arknightspixeldungeon.items.weapon.missiles.Dart" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.IncendiaryDart.class,
				"com.unifier.arknightspixeldungeon.items.weapon.missiles.IncendiaryDart" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.ParalyticDart.class,
				"com.unifier.arknightspixeldungeon.items.weapon.missiles.CurareDart" );
		
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.wands.WandOfCorrosion.class,
				"com.unifier.arknightspixeldungeon.items.wands.WandOfVenom" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.actors.blobs.CorrosiveGas.class,
				"com.unifier.arknightspixeldungeon.actors.blobs.VenomGas" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.actors.buffs.Corrosion.class,
				"com.unifier.arknightspixeldungeon.actors.buffs.Venom" );
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.levels.traps.CorrosionTrap.class,
				"com.unifier.arknightspixeldungeon.levels.traps.VenomTrap" );
		
		//v0.6.4
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.bags.VelvetPouch.class,
				"com.unifier.arknightspixeldungeon.items.bags.SeedPouch" );
		
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.bags.MagicalHolster.class,
				"com.unifier.arknightspixeldungeon.items.bags.WandHolster" );
		
		//v0.6.5
		com.watabou.utils.Bundle.addAlias(
				com.unifier.arknightspixeldungeon.items.stones.StoneOfAugmentation.class,
				"com.unifier.arknightspixeldungeon.items.Weightstone" );
		
	}

    @Override
    public void create() {
        super.create();

        updateSystemUI();
        PDAction.loadBindings();

        Music.INSTANCE.enable( PDSettings.music() );
        Music.INSTANCE.volume( PDSettings.musicVol()* PDSettings.musicVol()/100f );
        Sample.INSTANCE.enable( PDSettings.soundFx() );
        Sample.INSTANCE.volume( PDSettings.SFXVol()* PDSettings.SFXVol()/100f );

		Sample.INSTANCE.load(
				Assets.SND_CLICK,
				Assets.SND_BADGE,
				Assets.SND_GOLD,

				Assets.SND_STEP,
				Assets.SND_WATER,
				Assets.SND_OPEN,
				Assets.SND_UNLOCK,
				Assets.SND_ITEM,
				Assets.SND_DEWDROP,
				Assets.SND_HIT,
				Assets.SND_MISS,

				Assets.SND_DESCEND,
				Assets.SND_EAT,
				Assets.SND_READ,
				Assets.SND_LULLABY,
				Assets.SND_DRINK,
				Assets.SND_SHATTER,
				Assets.SND_ZAP,
				Assets.SND_LIGHTNING,
				Assets.SND_LEVELUP,
				Assets.SND_DEATH,
				Assets.SND_CHALLENGE,
				Assets.SND_CURSED,
				Assets.SND_EVOKE,
				Assets.SND_TRAP,
				Assets.SND_TOMB,
				Assets.SND_ALERT,
				Assets.SND_MELD,
				Assets.SND_BOSS,
				Assets.SND_BLAST,
				Assets.SND_PLANT,
				Assets.SND_RAY,
				Assets.SND_BEACON,
				Assets.SND_TELEPORT,
				Assets.SND_CHARMS,
				Assets.SND_MASTERY,
				Assets.SND_PUFF,
				Assets.SND_ROCKS,
				Assets.SND_BURNING,
				Assets.SND_FALLING,
				Assets.SND_GHOST,
				Assets.SND_SECRET,
				Assets.SND_BONES,
				Assets.SND_BEE,
				Assets.SND_DEGRADE,
				Assets.SND_MIMIC );
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}

    public static void seamlessResetScene(SceneChangeCallback callback) {
        if (scene() instanceof PixelScene){
            ((PixelScene) scene()).saveWindows();
            switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
        } else {
            resetScene();
        }
    }

    public static void seamlessResetScene(){
        seamlessResetScene(null);
    }

    @Override
    protected void switchScene() {
        super.switchScene();
        if (scene instanceof PixelScene){
            ((PixelScene) scene).restoreWindows();
        }
    }

    @Override
    public void resize( int width, int height ) {
        if (width == 0 || height == 0){
            return;
        }

        if (scene instanceof PixelScene &&
                (height != Game.height || width != Game.width)) {
            PixelScene.noFade = true;
            ((PixelScene) scene).saveWindows();
        }

        super.resize( width, height );

        updateDisplaySize();

    }

    @Override
    public void destroy(){
        super.destroy();
        GameScene.endActorThread();
    }

    public void updateDisplaySize(){
        platform.updateDisplaySize();
    }

    public static void updateSystemUI() {
        platform.updateSystemUI();
    }
}