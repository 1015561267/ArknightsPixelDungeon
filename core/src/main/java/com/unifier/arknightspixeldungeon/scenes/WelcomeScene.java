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

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.GamesInProgress;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.Rankings;
import com.unifier.arknightspixeldungeon.effects.BannerSprites;
import com.unifier.arknightspixeldungeon.effects.Fireball;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.Archs;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.StyledButton;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.FileUtils;

public class WelcomeScene extends PixelScene {

	private static int LATEST_UPDATE = ArknightsPixelDungeon.Demo;

    //used so that the game does not keep showing the window forever if cleaning fails
    private static boolean triedCleaningTemp = false;

	@Override
	public void create() {
		super.create();

		final int previousVersion = PDSettings.version();

		if (ArknightsPixelDungeon.versionCode == previousVersion) {
			ArknightsPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		title.brightness(0.6f);
		add( title );

		float topRegion = Math.max(95f, h*0.45f);

		title.x = (w - title.width()) / 2f;
		if (landscape())
			title.y = (topRegion - title.height()) / 2f;
		else
			title.y = 16 + (topRegion - title.height() - 16) / 2f;

		align(title);

		/*
		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );*/

        StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
            @Override
            protected void onClick() {
                super.onClick();
                if (previousVersion == 0 || PDSettings.intro()){

                    if (previousVersion > 0){
                        updateVersion(previousVersion);
                    }

                    PDSettings.version(ArknightsPixelDungeon.versionCode);
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = GamesInProgress.firstEmpty();
                    if (GamesInProgress.curSlot == -1){
                        PDSettings.intro(false);
                        ArknightsPixelDungeon.switchScene(TitleScene.class);
                    } else {
                        ArknightsPixelDungeon.switchScene(HeroSelectScene.class);
                    }
                } else {
                    updateVersion(previousVersion);
                    ArknightsPixelDungeon.switchScene(TitleScene.class);
                }

                //FIXME a temp trick due to have no intro and have to access challenge
                if(PDSettings.intro()){
                    PDSettings.intro(false);
                }

            }
        };

        float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		if (previousVersion != 0){
			DarkRedButton changes = new DarkRedButton(Messages.get(this, "changelist")){
				@Override
				protected void onClick() {
					super.onClick();
					updateVersion(previousVersion);
					ArknightsPixelDungeon.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(title.x, h-20, (title.width()/2)-2, 16);
			okay.textColor(0xBBBB33);
			add(okay);

			changes.setRect(okay.right()+2, h-20, (title.width()/2)-2, 16);
			changes.textColor(0xBBBB33);
			add(changes);
		} else {
			okay.setRect(title.x, h-20, title.width(), 16);
			okay.textColor(0xBBBB33);
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0) {
			message = Messages.get(this, "welcome_msg");
		} else if (previousVersion <= ArknightsPixelDungeon.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				//TODO: change the messages here in accordance with the type of patch.
				message = Messages.get(this, "patch_intro");
				message += "\n\n" + Messages.get(this, "patch_bugfixes");
				message += "\n" + Messages.get(this, "patch_translations");
				//message += "\n" + Messages.get(this, "patch_balance");

			}
		} else {
			message = Messages.get(this, "what_msg");
		}
		text.text(message, w-20);
		float textSpace = h - title.y - (title.height() - 10) - okay.height() - 2;
		text.setPos((w - text.width()) / 2f, title.y+(title.height() - 10) + ((textSpace - text.height()) / 2));
		add(text);

	}

	private void updateVersion(int previousVersion){
		
		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			try {
				Rankings.INSTANCE.load();
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
			}
		}
		
		//convert game saves from the old format
        /*
		if (previousVersion <= ArknightsPixelDungeon.v0_6_2e){
			//old save file names for warrior, mage, rogue, huntress
			String[] classes = new String[]{"warrior", "mage", "game", "ranger"};
			for (int i = 1; i <= classes.length; i++){
				String name = classes[i-1];
				if (FileUtils.fileExists(name + ".dat")){
					try {
						Bundle gamedata = FileUtils.bundleFromFile(name + ".dat");
						FileUtils.bundleToFile(GamesInProgress.gameFile(i), gamedata);
						FileUtils.deleteFile(name + ".dat");
						
						//rogue's safe files have a different name
						if (name.equals("game")) name = "depth";
						
						int depth = 1;
						while (FileUtils.fileExists(name + depth + ".dat")) {
							gamedata = FileUtils.bundleFromFile(name + depth + ".dat");
							FileUtils.bundleToFile(GamesInProgress.depthFile(i, depth), gamedata);
							FileUtils.deleteFile(name + depth + ".dat");
							depth++;
						}
					} catch (Exception e){
					}
				}
			}
		}*/
		
		//remove changed badges
        /*
		if (previousVersion <= ArknightsPixelDungeon.v0_6_0b){
			Badges.disown(Badges.Badge.ALL_WANDS_IDENTIFIED);
			Badges.disown(Badges.Badge.ALL_RINGS_IDENTIFIED);
			Badges.disown(Badges.Badge.ALL_SCROLLS_IDENTIFIED);
			Badges.disown(Badges.Badge.ALL_POTIONS_IDENTIFIED);
			Badges.disown(Badges.Badge.ALL_ITEMS_IDENTIFIED);
			Badges.saveGlobal();
		}*/
		
		PDSettings.version(ArknightsPixelDungeon.versionCode);
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private class DarkRedButton extends RedButton{
		{
			bg.brightness(0.4f);
		}

		DarkRedButton(String text){
			super(text);
		}

		@Override
		protected void onPointerDown() {
			bg.brightness(0.5f);
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}

		@Override
		protected void onPointerUp() {
			super.onPointerUp();
			bg.brightness(0.4f);
		}
	}
}
