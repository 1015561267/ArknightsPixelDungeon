package com.unifier.arknightspixeldungeon.Exusiai;

import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndInfoItem;

public class WndSelectAccessory extends WndInfoItem {

    public WndSelectAccessory(Item item,WndAdjustGun wndAdjustGun) {

        super(item);

        RedButton btnSelect = new RedButton("选择") {
            @Override
            protected void onClick() {
                wndAdjustGun.sight.item(item);
                hide();
            }
        };

        RedButton btnCancel = new RedButton("取消") {
            @Override
            protected void onClick() {
                GameScene.selectItem(wndAdjustGun.itemSelector, WndBag.Mode.ALL,Messages.get(this,"select"));
                hide();
            }
        };

        btnSelect.setRect(0,super.height + 2,super.width,16);
        btnCancel.setRect(0,super.height + 20,super.width,16);
        add(btnSelect);
        add(btnCancel);

        resize(super.width,(int)btnCancel.bottom());
    }
}
