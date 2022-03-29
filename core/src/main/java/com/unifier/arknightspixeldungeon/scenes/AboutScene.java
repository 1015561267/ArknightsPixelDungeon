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
import com.unifier.arknightspixeldungeon.effects.Flare;
import com.unifier.arknightspixeldungeon.ui.Archs;
import com.unifier.arknightspixeldungeon.ui.ExitButton;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class AboutScene extends PixelScene {

    private static final String TTL_APD = "Ark Pixel Dungeon";

    private static final String TXT_Rawberry = "Anchor:Rawberry";

    private static final String TXT_Maidens = "Char and interface:Maidens";
    private static final String TXT_Passerby = "Itmes and map:Passerby";
    private static final String TXT_Teller = "Code:Teller";
    private static final String TXT_Honeypot = "Sprite:HoneyPot";

    private static final String TTL_SHPX = "Shattered Pixel Dungeon";
	private static final String TTL_WATA = "Pixel Dungeon";

    private static final String TTL_IP = "Ip by : Hypergryph";

    //Reworked about scene with our developer group,code done by Teller,2021.7.21

	
	@Override
	public void create() {
		super.create();

		final float colWidth = Camera.main.width / (landscape() ? 2 : 1);
		final float colTop = (Camera.main.height / 2) - (landscape() ? 70 : 90);
		final float wataOffset = landscape() ? colWidth : 0;

        float topY = colTop;

        RenderedTextBlock apd = renderTextBlock(TTL_APD,16);
        apd.hardlight(0xA0A0A0);
        add(apd);

        apd.setPos(
                (colWidth - apd.width())/2 ,
                colTop
        );
        align(apd);

        Image iconR = new Image(Assets.RAEBERRY);
        int number = Random.Int(4);
        iconR.frame(iconR.texture.uvRect(16*number, 0, 16*(number+1), 16));

        iconR.x = (colWidth - iconR.width()) / 2;
        iconR.y = apd.height() + colTop + 5;
        align(iconR);
        add(iconR);
        new Flare(7, 64).color(0x515151, true).show(iconR, 0).angularSpeed = +10;
        //new Flare(7, 64).color(0xBD1A11, true).show(iconR, 1).angularSpeed = +10;

        RenderedTextBlock Rawberrytitle = renderTextBlock(TXT_Rawberry , 8);
        Rawberrytitle.hardlight(0xBD1A11);
        add(Rawberrytitle);
        //Rawberrytitle.x = (colWidth - Rawberrytitle.width())/2;
        //Rawberrytitle.y = iconR.y + iconR.height()+5;
        Rawberrytitle.setPos(
                (colWidth - Rawberrytitle.width())/2,
                iconR.y + iconR.height() +5
        );

        topY = iconR.y + iconR.height() + Rawberrytitle.height() + 10;

        align(Rawberrytitle);

        Image iconM = new Image(Assets.MAIDENS);
        iconM.frame(iconM.texture.uvRect(0, 0, 16, 16));

        iconM.x = (colWidth / 2 - iconM.width()) / 2;
        iconM.y = topY;

        align(iconM);
        add(iconM);
        new Flare(7, 32).color(0x2E7033, true).show(iconM, 0).angularSpeed = +10;


        RenderedTextBlock Madienstitle = renderTextBlock(TXT_Maidens,5);
        Madienstitle.hardlight(0xA7B5A8);
        add(Madienstitle);

        Madienstitle.setPos(
                (colWidth / 2 - Madienstitle.width())/2 ,
                iconM.y + iconM.height() + 2
        );

        //Madienstitle.x = (colWidth / 2 - Madienstitle.width())/2;
        //Madienstitle.y = iconM.y + iconM.height()+5;
        align(Madienstitle);

        Image iconP = new Image(Assets.PASSERBY);
        iconP.frame(iconP.texture.uvRect(0, 0, 16, 16));
        iconP.x = (colWidth / 2 - iconP.width()) / 2  + colWidth / 2;
        iconP.y = topY;
        align(iconP);
        add(iconP);
        new Flare(7, 32).color(0xE7F5E8, true).show(iconP, 0).angularSpeed = +10;

        RenderedTextBlock Passerbytitle = renderTextBlock(TXT_Passerby,5);
        Passerbytitle.hardlight(0x6C34A8);
        add(Passerbytitle);

        Passerbytitle.setPos(
                (colWidth / 2 - Passerbytitle.width())/2  + colWidth / 2 ,
                iconP.y + iconP.height() + 2
                );
        //Passerbytitle.x = (colWidth / 2 - Passerbytitle.width())/2  + colWidth / 2;
        //Passerbytitle.y = iconP.y + iconP.height()+5;
        align(Passerbytitle);

        topY = iconP.y + iconP.height() + 10 + Passerbytitle.height() + 2;

        Image iconT = new Image(Assets.TELLER);
        iconT.frame(iconT.texture.uvRect(0, 0, 16, 16));
        iconT.x = (colWidth / 2 - iconT.width()) / 2;
        iconT.y = topY;
        align(iconT);
        add(iconT);
        new Flare(7, 32).color(0x706220, true).show(iconT, 0).angularSpeed = +10;

        RenderedTextBlock Tellertitle = renderTextBlock(TXT_Teller,5);
        Tellertitle.hardlight(0x706220);
        add(Tellertitle);

        Tellertitle.setPos(
                (colWidth / 2 - Tellertitle.width())/2 ,
                iconT.y + iconT.height()
        );

        //Tellertitle.x = (colWidth / 2 - Tellertitle.width())/2;
        //Tellertitle.y = iconT.y + iconT.height()+5;
        align(Tellertitle);

        Image iconH = new Image(Assets.HONEYPOT);
        int random = Random.Int(2);
        iconH.frame(iconH.texture.uvRect( random * 16 ,0, random * 16 + 14, 16));
        iconH.x = (colWidth / 2 - iconH.width()) / 2  + colWidth / 2;
        iconH.y = topY;
        align(iconH);
        add(iconH);
        new Flare(7, 32).color(0x615139, true).show(iconH, 0).angularSpeed = +10;

        RenderedTextBlock Honeypottitle = renderTextBlock(TXT_Honeypot,5);
        Honeypottitle.hardlight(0xE8DDC7);
        add(Honeypottitle);

        Honeypottitle.setPos(
                (colWidth / 2 - Honeypottitle.width())/2  + colWidth / 2,
                iconH.y + iconH.height()
        );
        //Honeypottitle.x = (colWidth / 2 - Honeypottitle.width())/2  + colWidth / 2;
        //Honeypottitle.y = iconH.y + iconH.height()+5;
        align(Honeypottitle);

        topY = iconH.y + iconH.height() + 5 + Honeypottitle.height() + 2;

		Image shpx = Icons.SHPX.get();

		shpx.x = wataOffset + (colWidth - shpx.width()) / 2;
		shpx.y = landscape() ?
                colTop + 20 : topY ;
		align(shpx);
		add( shpx );
		new Flare( 7, 32 ).color( 0x225511, true ).show( shpx, 0 ).angularSpeed = +10;

		RenderedTextBlock shpxtitle = renderTextBlock( TTL_SHPX, 4 );
		shpxtitle.hardlight( Window.SHPX_COLOR );
		add( shpxtitle );

        shpxtitle.setPos(
                wataOffset + (colWidth - shpxtitle.width()) / 2,
                shpx.y + shpx.height() + 2
        );
		//shpxtitle.x = wataOffset + (colWidth - shpxtitle.width()) / 2;
		//shpxtitle.y = shpx.y + shpx.height() + 2;
		align(shpxtitle);


		topY = shpx.y + shpx.height() + 20 + shpxtitle.height();

		Image wata = Icons.WATA.get();
		wata.x = wataOffset + (colWidth - wata.width()) / 2;
		wata.y = topY;
		align(wata);
		add( wata );

		new Flare( 7, 32 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +10;

		RenderedTextBlock wataTitle = renderTextBlock( TTL_WATA, 4 );
		wataTitle.hardlight(Window.TITLE_COLOR);
		add( wataTitle );

        wataTitle.setPos(
                wataOffset + (colWidth - wataTitle.width()) / 2,
                wata.y + wata.height() + 2
        );

		//wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
		//wataTitle.y = wata.y + wata.height + 2;

		align(wataTitle);

		topY = wata.y + wata.height() + 2 + wataTitle.height();

        Image hypergryph = new Image(Assets.HYPERGRYPH);
        hypergryph.frame(hypergryph.texture.uvRect(0, 0, 28, 32));

        hypergryph.scale.x *= 0.5f;
        hypergryph.scale.y *= 0.5f;


        hypergryph.x = wataOffset + (colWidth - hypergryph.width()) / 2;
        hypergryph.y = topY;


        align(hypergryph);
        add( hypergryph );

        Image replacement = hypergryph;
        replacement.height /= 2 ;
        replacement.width  /= 2 ;
        //FIXME
        // Caution:because the actual icon is bigger and lack support like spd 0.8+ so I have to use things like this,before you add anything,you'd better make sure you know how this works
        //Teller 2021-7-28

        new Flare( 7, 32 ).color( 0x666666, true ).show( replacement, 0 ).angularSpeed = +10;

        RenderedTextBlock hypergryphTitle = renderTextBlock( TTL_IP, 4 );
        hypergryphTitle.hardlight(0x666666);
        add( hypergryphTitle );

        hypergryphTitle.setPos(
                wataOffset + (colWidth - hypergryphTitle.width()) / 2,
                replacement.y + replacement.height + 2
        );

        //hypergryphTitle.x = wataOffset + (colWidth - hypergryphTitle.width()) / 2;
        //hypergryphTitle.y = replacement.y + replacement.height + 2;
        align(hypergryphTitle);

        Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		ArknightsPixelDungeon.switchNoFade(TitleScene.class);
	}
}
