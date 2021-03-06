package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class SkipIndicator extends Tag {

    public static final int COLOR	= 0x00A2E8;

    private Image icon;

    public SkipIndicator() {
        super( 0xFF4C4C );

        setSize( 24, 16 );

        visible = true;

        hotArea.blockWhenInactive = true;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        icon = Icons.EXIT.get();
        add( icon );
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = right() - 10;
        icon.y = y + (height - icon.height) / 2;
    }

    @Override
    protected void onClick() {
        if(this.parent instanceof WndDialog)
        {
            ((WndDialog) this.parent).skipText();
        }
    }
}
