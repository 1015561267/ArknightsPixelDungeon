package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class CurrencyIndicator extends Component {

    private static final float TIME	= 2f;

    private int lastGold = 0;
    private int lastEnergy = 0;

    private BitmapText gold;
    private BitmapText energy;

    private float goldTime;
    private float energyTime;

    @Override
    protected void createChildren() {
        gold = new BitmapText( PixelScene.pixelFont);
        add( gold );

        energy = new BitmapText( PixelScene.pixelFont);
        add( energy );

        gold.visible = energy.visible = false;
    }

    @Override
    protected void layout() {
        energy.x = x + (width - energy.width()) / 2;
        energy.y = bottom() - energy.height();

        gold.x = x + (width - gold.width()) / 2;
        if (energy.visible) {
            gold.y = bottom() - gold.height()- gold.height() + 2;
        } else {
            gold.y = bottom() - gold.height();
        }
    }

    @Override
    public void update() {
        super.update();

        if (gold.visible) {

            goldTime -= Game.elapsed;
            if (goldTime > 0) {
                gold.alpha( goldTime > TIME / 2 ? 1f : goldTime * 2 / TIME );
            } else {
                gold.visible = false;
            }

        }

        if (energy.visible) {

            energyTime -= Game.elapsed;
            if (energyTime > 0) {
                energy.alpha( energyTime > TIME / 2 ? 1f : energyTime * 2 / TIME );
            } else {
                energy.visible = false;
            }

        }

        if (Dungeon.gold != lastGold) {

            lastGold = Dungeon.gold;

            gold.text( Integer.toString(lastGold) );
            gold.measure();
            gold.hardlight( 0xFFFF00 );

            gold.visible = true;
            goldTime = TIME;

            layout();
        }

        if (Dungeon.energy != lastEnergy) {
            lastEnergy = Dungeon.energy;

            energy.text( Integer.toString(lastEnergy) );
            energy.measure();
            energy.hardlight( 0x44CCFF );

            energy.visible = true;
            energyTime = TIME;

            layout();
        }
    }
}
