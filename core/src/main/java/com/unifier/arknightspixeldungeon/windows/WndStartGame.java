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

package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Challenges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.GamesInProgress;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.hero.HeroSubClass;
import com.unifier.arknightspixeldungeon.journal.Journal;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.InterlevelScene;
import com.unifier.arknightspixeldungeon.scenes.IntroScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.IconButton;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.unifier.arknightspixeldungeon.ui.Button;
import com.watabou.noosa.ui.Component;

public class WndStartGame extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 140;

	public WndStartGame(final int slot){
		
		Badges.loadGlobal();
		Journal.loadGlobal();
		
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12 );
		title.hardlight(Window.TITLE_COLOR);

		title.setPos(
                (WIDTH - title.width())/2f, 2 );

		//title.x = (WIDTH - title.width())/2f;
		//title.y = 2;
		add(title);
		
		float heroBtnSpacing = (WIDTH - 4*HeroBtn.WIDTH)/5f;
		
		float curX = heroBtnSpacing;
		for (HeroClass cl : HeroClass.values()){
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, title.height() + 4, HeroBtn.WIDTH, HeroBtn.HEIGHT);
			curX += HeroBtn.WIDTH + heroBtnSpacing;
			add(button);
		}
		
		ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
		separator.size(WIDTH, 1);
		separator.x = 0;
		separator.y = title.height() + 6 + HeroBtn.HEIGHT;
		add(separator);
		
		HeroPane ava = new HeroPane();
		ava.setRect(20, separator.y + 2, WIDTH-30, 80);
		add(ava);
		
		RedButton start = new RedButton(Messages.get(this, "start")){
			@Override
			protected void onClick() {
				if (GamesInProgress.selectedClass == null) return;
				
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				Dungeon.hero = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
				
				if (PDSettings.intro()) {
					PDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
			
			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}
		};
		start.visible = false;
		start.setRect(0, HEIGHT - 20, WIDTH, 20);
		add(start);
		
		//if (Badges.isUnlocked(Badges.Badge.VICTORY)){

        Image challengeIcon = Icons.get(Icons.CHALLENGE_OFF);

        int count = Challenges.getChallengeCount(PDSettings.challenges());

        if(count == 0)
        {
            challengeIcon = Icons.get(Icons.CHALLENGE_OFF);
        }
        else if(count < 3)
        {
            challengeIcon=Icons.get(Icons.CHALLENGE_ON);
        }
        else if(count < 7)
        {
            challengeIcon=Icons.get(Icons.CHALLENGE_MORE);
        }
        else if(count == 7)
            challengeIcon = Icons.get(Icons.CHALLENGE_FULL);

			IconButton challengeButton = new IconButton(
                    challengeIcon){
					//Icons.get( PDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
				@Override
				protected void onClick() {
					ArknightsPixelDungeon.scene().add(new WndChallenges(PDSettings.challenges(), true) {
						public void onBackPressed() {
							super.onBackPressed();

							int count = Challenges.getChallengeCount(PDSettings.challenges());

                            if(count == 0)
                            {
                                icon(Icons.get(Icons.CHALLENGE_OFF));
                            }
                            else if(count < 3)
                            {
                                icon(Icons.get(Icons.CHALLENGE_ON));
                            }
                            else if(count < 7)
                            {
                                icon(Icons.get(Icons.CHALLENGE_MORE));
                            }
                            else if(count == 7)
                                icon(Icons.get(Icons.CHALLENGE_FULL));
						}
					} );
				}
				
				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
					super.update();
				}
			};
			challengeButton.setRect(WIDTH - 20, HEIGHT - 20, 20, 20);
			challengeButton.visible = false;
			add(challengeButton);
			
		//}
		//else {
		//	Dungeon.challenges = 0;
		//	PDSettings.challenges(0);
		//}
		
		resize(WIDTH, HEIGHT);
		
	}
	
	private static class HeroBtn extends Button {
		
		private HeroClass cl;
		
		private Image hero;
		
		private static final int WIDTH = 24;
		private static final int HEIGHT = 16;
		
		HeroBtn ( HeroClass cl ){
			super();
			
			this.cl = cl;
			
			if (cl == HeroClass.WARRIOR){
				hero = new Image(Assets.WARRIOR, 0, 0, 13, 17);
			} else if (cl == HeroClass.MAGE){
				hero = new Image(Assets.MAGE, 0, 0, 13, 17);
			} else if (cl == HeroClass.ROGUE){
				hero = new Image(Assets.ROGUE, 0, 0, 13, 17);
			} else if (cl == HeroClass.HUNTRESS){
				hero = new Image(Assets.HUNTRESS, 0, 0, 13, 17);
			}
			add(hero);
			
		}
		
		@Override
		protected void layout() {
			super.layout();
			if (hero != null){
				hero.x = x + (width - hero.width()) / 2f;
				hero.y = y + (height - hero.height()) / 2f;
				PixelScene.align(hero);
			}
		}
		
		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				//if (cl == HeroClass.HUNTRESS && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_3)){
				//	hero.brightness( 0f );
				//} else {
					hero.brightness(0.6f);
				//}
			} else {
				hero.brightness(1f);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//if( cl == HeroClass.HUNTRESS && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_3)){
			//	ArknightsPixelDungeon.scene().add(
			//			new WndMessage(Messages.get(WndStartGame.class, "huntress_unlock")));
			//} else {
				GamesInProgress.selectedClass = cl;
			//}
		}
	}
	
	private class HeroPane extends Component {
		
		private HeroClass cl;
		
		private Image avatar;
		
		private IconButton heroItem;
		private IconButton heroLoadout;
		private IconButton heroMisc;
		private IconButton heroSubclass;
		
		private RenderedTextBlock name;
		
		private static final int BTN_SIZE = 20;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			avatar = new Image(Assets.AVATARS);
			avatar.scale.set(2f);
			add(avatar);
			
			heroItem = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ArknightsPixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_item")));
				}
			};
			heroItem.setSize(BTN_SIZE, BTN_SIZE);
			add(heroItem);
			
			heroLoadout = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ArknightsPixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_loadout")));
				}
			};
			heroLoadout.setSize(BTN_SIZE, BTN_SIZE);
			add(heroLoadout);
			
			heroMisc = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ArknightsPixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_misc")));
				}
			};
			heroMisc.setSize(BTN_SIZE, BTN_SIZE);
			add(heroMisc);
			
			heroSubclass = new IconButton(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					ArknightsPixelDungeon.scene().add(new WndMessage(msg));
				}
			};
			heroSubclass.setSize(BTN_SIZE, BTN_SIZE);
			add(heroSubclass);
			
			name = PixelScene.renderTextBlock(12);
			add(name);
			
			visible = false;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			avatar.x = x;
			avatar.y = y + (height - avatar.height() - name.height() - 2)/2f;
			PixelScene.align(avatar);

            name.setPos(
                    x + (avatar.width() - name.width())/2f,
                    avatar.y + avatar.height() + 3
            );
			PixelScene.align(name);
			
			heroItem.setPos(x + width - BTN_SIZE, y);
			heroLoadout.setPos(x + width - BTN_SIZE, heroItem.bottom());
			heroMisc.setPos(x + width - BTN_SIZE, heroLoadout.bottom());
			heroSubclass.setPos(x + width - BTN_SIZE, heroMisc.bottom());
		}
		
		@Override
		public synchronized void update() {
			super.update();
			if (GamesInProgress.selectedClass != cl){
				cl = GamesInProgress.selectedClass;
				if (cl != null) {
					avatar.frame(cl.ordinal() * 34, 0, 34, 38);
					
					name.text(Messages.capitalize(cl.title()));
					
					switch(cl){
						case WARRIOR:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SEAL, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.RATION, null));
							break;
						case MAGE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.HOLDER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null));
							break;
						case ROGUE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.DAGGER, null));
							heroMisc.icon(Icons.get(Icons.DEPTH));
							break;
						case HUNTRESS:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.BOOMERANG, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.KNUCKLEDUSTER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.DART, null));
							break;
					}
					
					layout();
					
					visible = true;
				} else {
					visible = false;
				}
			}
		}
	}
}
