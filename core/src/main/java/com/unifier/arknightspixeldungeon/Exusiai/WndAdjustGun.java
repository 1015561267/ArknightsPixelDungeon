package com.unifier.arknightspixeldungeon.Exusiai;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.ui.ItemSlot;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndAdjustGun extends Window {

    public ItemButton btnPressed;
    public ItemButton sight;

    public WndAdjustGun(int i) {

        RenderedTextBlock info = PixelScene.renderTextBlock("枪支调整界面",6);
        info.setPos(0,0);
        add(info);

        Image gun = new Image(Assets.Exusiai.REVOLVER);
        gun.x = 0;
        gun.y = info.height() + 2;
        gun.scale.set(2.4f,2.4f);
        add(gun);

//        Image sight = new Image(Assets.Exusiai.SIGHT);
//        sight.x = 0;
//        sight.y = 60;
//        sight.scale.set(0.4f,0.4f);
//        add(sight);

        sight = new ItemButton(){
            @Override
            protected void onClick() {
                if (sight.item != null) {
                    sight.item = null;
                    hide();
                    GameScene.show(new WndAdjustGun(i));
                } else {
                    btnPressed = sight;
                    GameScene.selectItem(itemSelector);
                }
            }
        };
//        sight.item(new AdjustTool());
        sight.setRect(0, gun.y + gun.height() + 2,20,20);
        add(sight);

        resize(60,(int)sight.bottom());
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(WndAdjustGun.class,"select");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return true;//FIXME The whole gun modifier need to be changed later
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                GameScene.show(new WndSelectAccessory(item,WndAdjustGun.this));
            }
        }
    };

    public static class ItemButton extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public Item item = null;

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = Chrome.get(Chrome.Type.WINDOW);
            add(bg);

            slot = new ItemSlot() {
                @Override
                protected void onPointerDown() {
                    bg.brightness(1.2f);
                    Sample.INSTANCE.play(Assets.SND_CLICK);
                }
                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }
                @Override
                protected void onClick() {
                    ItemButton.this.onClick();
                }
            };
            slot.enable(true);
            add(slot);
        }

        protected void onClick() {};

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            slot.setRect(x + 2,y + 2,width - 4,height - 4 );
        };

        public void item(Item item) {
            slot.item(this.item = item);
        }
    }
}
