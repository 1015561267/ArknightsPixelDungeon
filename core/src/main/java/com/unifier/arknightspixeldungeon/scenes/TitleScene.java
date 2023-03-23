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
import com.unifier.arknightspixeldungeon.effects.BannerSprites;
import com.unifier.arknightspixeldungeon.effects.Fireball;
import com.unifier.arknightspixeldungeon.effects.particles.ArcParticle;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.Archs;
import com.unifier.arknightspixeldungeon.ui.Button;
import com.unifier.arknightspixeldungeon.ui.DoublesideAlignmentButton;
import com.unifier.arknightspixeldungeon.ui.ExitButton;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.PrefsButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {

    private static final int COLOR_POINT = 0xFFBBEEBB;

    private Emitter emitter;

    @Override
	public void create() {
		
		super.create();

		Music.INSTANCE.play( Assets.THEME, true );

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(95f, h*0.45f);

		title.x = (w - title.width()) / 2f;
		if (landscape())
			title.y = (topRegion - title.height()) / 2f;
		else
			title.y = 16 + (topRegion - title.height() - 16) / 2f;

		align(title);

        final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

        DoublesideAlignmentButton btnPlay = new DoublesideAlignmentButton(GREY_TR, Messages.get(this, "enter")){
            @Override
            protected void onClick() {
                if (GamesInProgress.checkAll().size() == 0){
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    ArknightsPixelDungeon.switchScene(HeroSelectScene.class);
                } else {
                    ArknightsPixelDungeon.switchNoFade( StartScene.class );
                }
            }

            @Override
            protected boolean onLongClick() {
                //making it easier to start runs quickly while debugging
                if (DeviceCompat.isDebug()) {
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    ArknightsPixelDungeon.switchScene(HeroSelectScene.class);
                    return true;
                }
                return super.onLongClick();
            }
        };
        btnPlay.icon(Icons.get(Icons.ENTER));
        add(btnPlay);


        DoublesideAlignmentButton btnRankings = new DoublesideAlignmentButton(GREY_TR,Messages.get(this, "rankings")){
            @Override
            protected void onClick() {
                ArknightsPixelDungeon.switchNoFade( RankingsScene.class );
            }
        };
        btnRankings.icon(Icons.get(Icons.RANKINGS));
        add(btnRankings);

        DoublesideAlignmentButton btnBadges = new DoublesideAlignmentButton(GREY_TR, Messages.get(this, "badges")){
            @Override
            protected void onClick() {
                ArknightsPixelDungeon.switchNoFade( BadgesScene.class );
            }
        };
        btnBadges.icon(Icons.get(Icons.BADGES));
        add(btnBadges);

        DoublesideAlignmentButton btnChanges = new DoublesideAlignmentButton(GREY_TR, Messages.get(this, "changes")){
            @Override
            protected void onClick() {
                ArknightsPixelDungeon.switchNoFade( ChangesScene.class );
            }
        };
        btnChanges.icon(Icons.get(Icons.CHANGES));
        add(btnChanges);

        DoublesideAlignmentButton btnAbout = new DoublesideAlignmentButton(GREY_TR, Messages.get(this, "about")){
            @Override
            protected void onClick() {
                ArknightsPixelDungeon.switchNoFade( AboutScene.class );
            }
        };
        btnAbout.icon(Icons.get(Icons.ABOUT));
        add(btnAbout);

        final int BTN_HEIGHT = 20;
        int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
        GAP /= landscape() ? 3 : 5;
        GAP = Math.max(GAP, 2);

        int LandScapeWidth = 192;
        int PortraitWidth = 132;

        ArcParticle.startingLine =  topRegion + 2 * GAP + BTN_HEIGHT * 3 / 2;

        final float startPos = (w - title.width()) / 2f;
        final float widthPos =  (title.width()/2)-1 ;
        final float plusPos  = startPos + widthPos + 2;

        float offset[] = {
                startPos + widthPos / 2 ,
                startPos + widthPos * 2 / 3 ,
                startPos + widthPos * 3 / 4 ,
                startPos + widthPos * 5 / 6 ,
                startPos + widthPos * 11 / 12 ,

                startPos + widthPos,

                plusPos + widthPos * 1 / 12 ,
                plusPos + widthPos * 1 / 6 ,
                plusPos + widthPos * 1 / 4 ,
                plusPos + widthPos * 1 / 3 ,
                plusPos + widthPos * 5 / 12 ,
        };

        float slope[] = {
                - 2f , - 1.5f , -1f , - 0.75f , - 0.5f ,
                0,
                0.5f , 0.75f, 1f , 1.5f , 2f
        };

        for(int i = 0;i <= 10;i++) {
            ArcParticle arc = new ArcParticle(offset[i], slope[i], 0.1f * i);
            arc.reset();
            add(arc);
        }

        if (landscape()) {
            btnPlay.setRect((w - title.width()) / 2f, topRegion+GAP, title.width(), BTN_HEIGHT);
            align(btnPlay);

            btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
            btnBadges.setRect(btnRankings.right()+2, btnPlay.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);

            btnChanges.setRect(btnPlay.left(), btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
            btnAbout.setRect(btnChanges.right()+2, btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
        } else {
            btnPlay.setRect((w - PortraitWidth) / 2f, topRegion+GAP, PortraitWidth, BTN_HEIGHT);
            align(btnPlay);

            btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
            btnBadges.setRect(btnRankings.right()+2, btnPlay.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);

            btnChanges.setRect(btnPlay.left(),btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
            btnAbout.setRect(btnChanges.right()+2, btnBadges.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
        }

		BitmapText version = new BitmapText(  Game.version + "", pixelFont);
		version.measure();
		version.hardlight( 0xCCCCCC );
		version.x = w - version.width();
		version.y = h - version.height();
		add( version );
		
		int pos = 0;
		
		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setRect( pos, 0, 16, 16 );
		add( btnPrefs );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );


		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
	
	private static class DashboardItem extends Button {
		
		public static final float SIZE	= 48;
		
		private static final int IMAGE_SIZE	= 32;
		
		private Image image;
		private RenderedTextBlock label;
		
		public DashboardItem( String text, int index ) {
			super();
			
			image.frame( image.texture.uvRect( index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE ) );
			this.label.text( text );
			
			setSize( SIZE, SIZE );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			image = new Image( Assets.DASHBOARD );
			add( image );
			
			label = renderTextBlock( 9 );
			add( label );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			image.x = x + (width - image.width()) / 2;
			image.y = y;
			align(image);

			label.setPos(
                    x + (width - label.width()) / 2,
                    image.y + image.height() +2
            );

			//label.x = x + (width - label.width()) / 2;
			//label.y = image.y + image.height() +2;
			align(label);
		}
		
		@Override
		protected void onPointerDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 0.8f );
		}
		
		@Override
		protected void onPointerUp() {
			image.resetColor();
		}
	}
}
