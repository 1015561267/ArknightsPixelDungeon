package com.unifier.arknightspixeldungeon.scenes;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.journal.Journal;
import com.unifier.arknightspixeldungeon.ui.Archs;
import com.unifier.arknightspixeldungeon.ui.ExitButton;
import com.unifier.arknightspixeldungeon.ui.TalentsPane;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;

import java.io.IOException;

public class TalentScene extends PixelScene {

    private Archs archs;

    TalentsPane pane;

    float margin = 5;
    float top = 20;
    int scale = 6;

    @Override
    public void create() {
        scale = PDSettings.scale();//here I'd like a smaller scale to make it display better

        if (PDSettings.landscape() != null) {
            if (!PDSettings.landscape()) {
                PDSettings.scale((int)Math.ceil(2* Game.density));
                ArknightsPixelDungeon.seamlessResetScene();
            }
        }

        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        float width = w - 2 * margin;
        float height = h - 2 * top;

        pane = new TalentsPane(true,Dungeon.hero.talents,Dungeon.hero.talentTiers());
        add(pane);

        pane.setSize(width,height);

        if (PDSettings.landscape() != null) {
            if (PDSettings.landscape()) {
                pane.setPos(w/2 - pane.middleLine(), top);
            }
            else pane.setPos(margin,top);
        }
        else pane.setPos(margin,top);


        ExitButton btnExit = new ExitButton(){
            @Override
            protected void onClick() {
                Game.switchScene(GameScene.class);
            }
        };
        btnExit.setPos( w - btnExit.width(), 0 );
        add( btnExit );
    }

    protected void reset(){
        pane.reset();
    }

    @Override
    protected void onBackPressed() {
        Game.switchScene(GameScene.class);
    }

    @Override
    public void destroy() {
        try {
            Dungeon.saveAll();
            Badges.saveGlobal();
            Journal.saveGlobal();
            PDSettings.scale(scale);
        } catch (IOException e) {
            ArknightsPixelDungeon.reportException(e);
        }
        super.destroy();
    }

}
