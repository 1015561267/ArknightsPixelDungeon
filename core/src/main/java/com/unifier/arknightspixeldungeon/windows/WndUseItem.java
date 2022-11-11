package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.ui.InventoryPane;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.Window;

import java.util.ArrayList;

public class WndUseItem extends WndInfoItem {

    private static final float BUTTON_HEIGHT	= 16;

    private static final float GAP	= 2;

    public WndUseItem(final Window owner, final Item item ) {

        super(item);

        float y = height;

        //GLog.i("opened"+item.name());

        //if(Dungeon.hero.isAlive()){
        //    GLog.i("alive");
        //}

        //if(Dungeon.hero.belongings.contains(item)){
        //    GLog.i("contained");
       // }

        //for(String action : item.actions(Dungeon.hero)){
        //    GLog.i(action);
        //}

        if (Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(item)) {
            y += GAP;
            ArrayList<RedButton> buttons = new ArrayList<>();
            for (final String action : item.actions( Dungeon.hero )) {

                //GLog.i(action);

                RedButton btn = new RedButton( item.actionName(action, Dungeon.hero), 8 ) {
                    @Override
                    protected void onClick() {
                        hide();
                        if (owner != null && owner.parent != null) owner.hide();
                        if (Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(item)){
                            item.execute( Dungeon.hero, action );
                        }
                        Item.updateQuickslot();
                        if (action == item.defaultAction && item.usesTargeting && owner == null){
                            InventoryPane.useTargeting();
                        }
                    }
                };
                btn.setSize( btn.reqWidth(), BUTTON_HEIGHT );
                buttons.add(btn);
                add( btn );

                if (action.equals(item.defaultAction)) {
                    btn.textColor( TITLE_COLOR );
                }

            }
            y = layoutButtons(buttons, width, y);
        }

        resize( width, (int)(y) );
    }

    private static float layoutButtons(ArrayList<RedButton> buttons, float width, float y){
        ArrayList<RedButton> curRow = new ArrayList<>();
        float widthLeftThisRow = width;

        while( !buttons.isEmpty() ){
            RedButton btn = buttons.get(0);

            widthLeftThisRow -= btn.width();
            if (curRow.isEmpty()) {
                curRow.add(btn);
                buttons.remove(btn);
            } else {
                widthLeftThisRow -= 1;
                if (widthLeftThisRow >= 0) {
                    curRow.add(btn);
                    buttons.remove(btn);
                }
            }

            //layout current row. Currently forces a max of 3 buttons but can work with more
            if (buttons.isEmpty() || widthLeftThisRow <= 0 || curRow.size() >= 3){

                //re-use this variable for laying out the buttons
                widthLeftThisRow = width - (curRow.size()-1);
                for (RedButton b : curRow){
                    widthLeftThisRow -= b.width();
                }

                //while we still have space in this row, find the shortest button(s) and extend them
                while (widthLeftThisRow > 0){

                    ArrayList<RedButton> shortest = new ArrayList<>();
                    RedButton secondShortest = null;

                    for (RedButton b : curRow) {
                        if (shortest.isEmpty()) {
                            shortest.add(b);
                        } else {
                            if (b.width() < shortest.get(0).width()) {
                                secondShortest = shortest.get(0);
                                shortest.clear();
                                shortest.add(b);
                            } else if (b.width() == shortest.get(0).width()) {
                                shortest.add(b);
                            } else if (secondShortest == null || secondShortest.width() > b.width()){
                                secondShortest = b;
                            }
                        }
                    }

                    float widthToGrow;

                    if (secondShortest == null){
                        widthToGrow = widthLeftThisRow / shortest.size();
                        widthLeftThisRow = 0;
                    } else {
                        widthToGrow = secondShortest.width() - shortest.get(0).width();
                        if ((widthToGrow * shortest.size()) >= widthLeftThisRow){
                            widthToGrow = widthLeftThisRow / shortest.size();
                            widthLeftThisRow = 0;
                        } else {
                            widthLeftThisRow -= widthToGrow * shortest.size();
                        }
                    }

                    for (RedButton toGrow : shortest){
                        toGrow.setRect(0, 0, toGrow.width()+widthToGrow, toGrow.height());
                    }
                }

                //finally set positions
                float x = 0;
                for (RedButton b : curRow){
                    b.setRect(x, y, b.width(), b.height());
                    x += b.width() + 1;
                }

                //move to next line and reset variables
                y += BUTTON_HEIGHT+1;
                widthLeftThisRow = width;
                curRow.clear();

            }

        }

        return y - 1;
    }

}

