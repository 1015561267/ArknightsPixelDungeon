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
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.Archs;
import com.unifier.arknightspixeldungeon.ui.ExitButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.ScrollPane;
import com.unifier.arknightspixeldungeon.ui.StyledButton;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.unifier.arknightspixeldungeon.ui.changelist.Ark_Demo_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.ChangeInfo;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_1_X_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_2_X_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_3_X_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_4_X_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_5_X_Changes;
import com.unifier.arknightspixeldungeon.ui.changelist.v0_6_X_Changes;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

    public static int changesSelected = 0;

    @Override
    public void create() {
        super.create();

        Music.INSTANCE.play( Assets.THEME, true );

        int w = Camera.main.width;
        int h = Camera.main.height;

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        NinePatch panel = Chrome.get(Chrome.Type.TOAST);

        int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
        int ph = h - 36;

        panel.size( pw, ph );
        panel.x = (w - pw) / 2f;
        panel.y = title.bottom() + 5;
        align( panel );
        add( panel );

        final ArrayList<ChangeInfo> changeInfos = new ArrayList<>();

        switch (changesSelected){
            case 0: default:
                Ark_Demo_Changes.addAllChanges(changeInfos);
                break;
            case 1:
                v0_6_X_Changes.addAllChanges(changeInfos);
                break;
            case 2:
                v0_5_X_Changes.addAllChanges(changeInfos);
                v0_4_X_Changes.addAllChanges(changeInfos);
                v0_3_X_Changes.addAllChanges(changeInfos);
                v0_2_X_Changes.addAllChanges(changeInfos);
                v0_1_X_Changes.addAllChanges(changeInfos);
                break;
        }

        ScrollPane list = new ScrollPane( new Component() ){

            @Override
            public void onClick(float x, float y) {
                for (ChangeInfo info : changeInfos){
                    if (info.onClick( x, y )){
                        return;
                    }
                }
            }

        };
        add( list );

        Component content = list.content();
        content.clear();

        float posY = 0;
        float nextPosY = 0;
        boolean second = false;
        for (ChangeInfo info : changeInfos){
            if (info.major) {
                posY = nextPosY;
                second = false;
                info.setRect(0, posY, panel.innerWidth(), 0);
                content.add(info);
                posY = nextPosY = info.bottom();
            } else {
                if (!second){
                    second = true;
                    info.setRect(0, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = info.bottom();
                } else {
                    second = false;
                    info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = Math.max(info.bottom(), nextPosY);
                    posY = nextPosY;
                }
            }
        }

        content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop() - 1,
                panel.innerWidth() + 2,
                panel.innerHeight() + 2);
        list.scrollTo(0, 0);

        StyledButton btnA_D = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "ArkD"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 0) {
                    changesSelected = 0;
                    ArknightsPixelDungeon.seamlessResetScene();
                }
            }
        };
        if (changesSelected != 0) btnA_D.textColor( 0xBBBBBB );
        btnA_D.setRect(list.left()-4f, list.bottom(), 26, changesSelected == 0 ? 19 : 15);
        addToBack(btnA_D);

        StyledButton btn0_6 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "v0.6"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 1) {
                    changesSelected = 1;
                    ArknightsPixelDungeon.seamlessResetScene();
                }
            }
        };
        if (changesSelected != 1) btn0_6.textColor( 0xBBBBBB );
        btn0_6.setRect(btnA_D.right() + 1, list.bottom(), 26, changesSelected == 1 ? 19 : 15);
        addToBack(btn0_6);

        StyledButton btnOld = new StyledButton(Chrome.Type.GREY_BUTTON_TR,"old"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 2) {
                    changesSelected = 2;
                    ArknightsPixelDungeon.seamlessResetScene();
                }
            }
        };
        if (changesSelected != 2) btnOld.textColor( 0xBBBBBB );
        btnOld.setRect(btn0_6.right() + 1, list.bottom(), 33, changesSelected == 2 ? 19 : 15);
        addToBack(btnOld);

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ArknightsPixelDungeon.switchNoFade(TitleScene.class);
    }

}
