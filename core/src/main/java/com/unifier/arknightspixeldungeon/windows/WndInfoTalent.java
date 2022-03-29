package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.TalentIcon;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.utils.Callback;

public class WndInfoTalent extends Window {
    private static final float GAP = 2;

    private static final int WIDTH = 120;

    public WndInfoTalent(Talent talent, int points, Callback onUpgradeButton,Talent.checkResult result) {
            super();

        IconTitle titlebar = new IconTitle();

        titlebar.icon(new TalentIcon(talent));
        String title = Messages.titleCase(talent.title());
        if (points > 0) {
            title += " +" + points;
        }
        titlebar.label(title, Window.TITLE_COLOR);
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        RenderedTextBlock txtInfo = PixelScene.renderTextBlock(talent.desc(), 6);
        txtInfo.maxWidth(WIDTH);
        txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2 * GAP);
        add(txtInfo);

        resize(WIDTH, (int) (txtInfo.bottom() + GAP));
        

        if (result == Talent.checkResult.AVAILABLE || result == Talent.checkResult.ALREADY_UPGRADED_AVAILABLE) {
            RedButton upgrade = new RedButton(Messages.get(this, "upgrade")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    onUpgradeButton.call();
                }
            };
            upgrade.icon(Icons.get(Icons.TALENT));
            upgrade.setRect(0, txtInfo.bottom() + 2 * GAP, WIDTH, 18);
            add(upgrade);
            resize(WIDTH, (int) upgrade.bottom() + 1);
        }
        else {

            String msg="";

            switch (result)
            {
                case NOT_ENOUGH_POINTS_ALREADY_UPGRADED:
                case NOT_ENOUGH_POINTS_RUN_OUT: msg=Messages.get(this, "need_point");break;
                case NOT_ENOUGH_POINTS_LOW_LEVEL: msg=Messages.get(this, "low_level");break;
                case NEED_ITEM:msg=Messages.get(this, "need_item");break;
                case NEED_PRECONDITION:msg=Messages.get(this, "need_precondition");break;
                case MUTEX_TALENT:msg=Messages.get(this, "mutex");break;
                case ALREADY_FULL:msg=Messages.get(this, "full");break;
            }

            RedButton upgrade = new RedButton(msg);
            upgrade.icon(Icons.get(Icons.TALENT));
            upgrade.setRect(0, txtInfo.bottom() + 2 * GAP, WIDTH, 18);
            upgrade.active = false;
            upgrade.alpha( 0.3f );
            add(upgrade);
            resize(WIDTH, (int) upgrade.bottom() + 1);
        }
    }
}