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
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.GamesInProgress;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroSubClass;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.InterlevelScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.scenes.StartScene;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import java.util.Locale;

public class WndGameInProgress extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 120;
	
	private int GAP	  = 5;
	
	private float pos;
	
	public WndGameInProgress(final int slot){
		
		final GamesInProgress.Info info = GamesInProgress.check(slot);
		
		String className = info.heroClass.title();;

		IconTitle title = new IconTitle();
		title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );

        if (info.subClass != HeroSubClass.NONE){
            title.label((Messages.get(this, "titlewithmastery", info.level, className ,info.subClass.title())).toUpperCase(Locale.ENGLISH));
            //className = info.subClass.title();
        } else {
            title.label((Messages.get(this, "title", info.level, className)).toUpperCase(Locale.ENGLISH));
        }

		title.color(Window.SHPX_COLOR);
		title.setRect( 0, 0, WIDTH, 0 );
		add(title);
		
		if (info.challenges > 0) GAP -= 2;
		
		pos = title.bottom() + GAP;
		
		if (info.challenges > 0) {
			RedButton btnChallenges = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					Game.scene().add( new WndChallenges( info.challenges, false ) );
				}
			};
			float btnW = btnChallenges.reqWidth() + 2;
			btnChallenges.setRect( (WIDTH - btnW)/2, pos, btnW , btnChallenges.reqHeight() + 2 );
			add( btnChallenges );
			
			pos = btnChallenges.bottom() + GAP;
		}
		
		pos += GAP;
		
		statSlot( Messages.get(this, "str"), info.str );
		if (info.shld > 0) statSlot( Messages.get(this, "health"), info.hp + "+" + info.shld + "/" + info.ht );
		else statSlot( Messages.get(this, "health"), (info.hp) + "/" + info.ht );
		statSlot( Messages.get(this, "exp"), info.exp + "/" + Hero.maxExp(info.level) );
		
		pos += GAP;
		statSlot( Messages.get(this, "gold"), info.goldCollected );
		statSlot( Messages.get(this, "depth"), info.maxDepth );
		
		pos += GAP;
		
		RedButton cont = new RedButton(Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				
				Dungeon.hero = null;
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				ArknightsPixelDungeon.switchScene(InterlevelScene.class);
			}
		};
		
		RedButton erase = new RedButton( Messages.get(this, "erase")){
			@Override
			protected void onClick() {
				super.onClick();
				
				ArknightsPixelDungeon.scene().add(new WndOptions(
						Messages.get(WndGameInProgress.class, "erase_warn_title"),
						Messages.get(WndGameInProgress.class, "erase_warn_body"),
						Messages.get(WndGameInProgress.class, "erase_warn_yes"),
						Messages.get(WndGameInProgress.class, "erase_warn_no") ) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0) {
							FileUtils.deleteDir(GamesInProgress.gameFolder(slot));
							GamesInProgress.setUnknown(slot);
							ArknightsPixelDungeon.switchNoFade(StartScene.class);
						}
					}
				} );
			}
		};
		
		cont.setRect(0, HEIGHT - 20, WIDTH/2 -1, 20);
		add(cont);
		
		erase.setRect(WIDTH/2 + 1, HEIGHT-20, WIDTH/2 - 1, 20);
		add(erase);
		
		resize(WIDTH, HEIGHT);
	}
	
	private void statSlot( String label, String value ) {
		
		RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
		//txt.y = pos;
		txt.setPos(0,pos);
		add( txt );

        txt = PixelScene.renderTextBlock( value, 8 );
        txt.setPos(WIDTH * 0.6f, pos);
        PixelScene.align(txt);
        add( txt );
		
		pos += GAP + txt.height();
	}
	
	private void statSlot( String label, int value ) {
		statSlot( label, Integer.toString( value ) );
	}
}
