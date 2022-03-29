package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;

public class DoublesideAlignmentButton extends StyledButton {

    //Made to make this button have icon align right while align right,it may have problem if text is too long
    //By Teller 2021/8/13

    public DoublesideAlignmentButton(Chrome.Type type, String label) {
        super(type, label);
    }

    @Override
    protected void layout() {
        super.layout();

        bg.x = x;
        bg.y = y;
        bg.size( width, height );

        float componentWidth = 0;

        if (icon != null) componentWidth += icon.width() + 2;

        float length = this.width;


        if (text != null && !text.text().equals("")){
            if (multiline) text.maxWidth( (int)(width - componentWidth - bg.marginHor() - 2));
            componentWidth += text.width() + 2;

            text.setPos(
                    //x + (width() + componentWidth)/2f - text.width() - 1,
                    x + (width() - componentWidth)/2f + 1,
                    y + (height() - text.height()) / 2f
            );
            PixelScene.align(text);

            if (icon != null) {
                icon.x = x + (width() + componentWidth)/2f - icon.width() - 1;
                icon.y = y + (height() - icon.height()) / 2f;
                PixelScene.align(icon);
            }

        }
    }
}
