package com.unifier.arknightspixeldungeon.scenes;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.GamesInProgress;
import com.unifier.arknightspixeldungeon.effects.Flare;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.items.Amulet;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.Scene;
import com.watabou.utils.Random;

public class DemoScene extends PixelScene {

    public static boolean noText = false;

    private static final int WIDTH			= 120;
    private static final int BTN_HEIGHT		= 18;
    private static final float SMALL_GAP	= 2;
    private static final float LARGE_GAP	= 8;

    private Image amulet;

    {
        inGameScene = true;
    }

    @Override
    public void create() {
        super.create();

        RenderedTextBlock text = null;
        if (!noText) {
            text = renderTextBlock( Messages.get(this, "text"), 8 );
            text.maxWidth(WIDTH);
            add( text );
        }

        amulet = new Image( Assets.AMULET );
        add( amulet );

        RedButton btnStay = new RedButton( Messages.get(this, "stay") ) {
            @Override
            protected void onClick() {
                onBackPressed();
            }
        };
        btnStay.setSize( WIDTH, BTN_HEIGHT );
        add( btnStay );

        RedButton btnExit = new RedButton( Messages.get(this, "exit") ) {
            @Override
            protected void onClick() {
                Dungeon.win( Amulet.class );
                Dungeon.deleteGame( GamesInProgress.curSlot, true );
                Game.switchScene( RankingsScene.class );
            }
        };
        btnExit.setSize( WIDTH, BTN_HEIGHT );
        add( btnExit );

        float height;
        if (noText) {
            height = amulet.height + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();

            amulet.x = (Camera.main.width - amulet.width) / 2;
            amulet.y = (Camera.main.height - height) / 2;
            align(amulet);

            btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, amulet.y + amulet.height + LARGE_GAP );
            btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );

        } else {
            height = amulet.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();

            amulet.x = (Camera.main.width - amulet.width) / 2;
            amulet.y = (Camera.main.height - height) / 2;
            align(amulet);

            text.setPos((Camera.main.width - text.width()) / 2, amulet.y + amulet.height + LARGE_GAP);
            align(text);

            btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, text.top() + text.height() + LARGE_GAP );
            btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
        }

        new Flare( 8, 48 ).color( 0xFFDDBB, true ).show( amulet, 0 ).angularSpeed = +30;

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene( InterlevelScene.class );
    }

    private float timer = 0;

    @Override
    public void update() {
        super.update();

        if ((timer -= Game.elapsed) < 0) {
            timer = Random.Float( 0.5f, 5f );

            Speck star = (Speck)recycle( Speck.class );
            star.reset( 0, amulet.x + 10.5f, amulet.y + 5.5f, Speck.DISCOVER );
            add( star );
        }
    }
}
