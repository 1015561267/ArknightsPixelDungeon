package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;
import com.watabou.utils.RectF;

public class Tooltip extends Component {

    //tooltips require .5 seconds to appear, fade in over .1 second
    //they then persist until none are visible for .25 seconds or more
    private static float tooltipAlpha = -5f;
    private static float lastUsedTime = -1;

    public static void resetLastUsedTime() {
        lastUsedTime = -1;
        tooltipAlpha = -5;
    }

    private Component parent;
    private RectF parentDims;

    private NinePatch bg;
    private RenderedTextBlock text;

    public Tooltip(Component parent, String msg, int maxWidth) {
        super();
        text.text(msg, maxWidth);
        layout();

        this.parent = parent;
        parentDims = new RectF(parent.left(), parent.top(), parent.right(), parent.bottom());

        if (lastUsedTime == -1 || lastUsedTime > Game.timeTotal) {
            tooltipAlpha = -5f;

        } else {
            float elapsed = Game.timeTotal - lastUsedTime;
            if (elapsed >= 0.25f || tooltipAlpha < 1f) {
                tooltipAlpha = -5f;
            }
        }
        lastUsedTime = Game.timeTotal;
        bg.alpha(GameMath.gate(0, tooltipAlpha, 1));
        text.alpha(GameMath.gate(0, tooltipAlpha, 1));
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        bg = Chrome.get(Chrome.Type.TOAST_TR);
        add(bg);

        text = PixelScene.renderTextBlock(6);
        add(text);
    }

    @Override
    public synchronized void update() {
        //kill this tooltip if the parent is removed or moved in any way
        if (!parent.exists ||
                !parent.isActive() ||
                !parent.isVisible() ||
                parentDims.left != parent.left() ||
                parentDims.top != parent.top() ||
                parentDims.right != parent.right() ||
                parentDims.bottom != parent.bottom()) {
            killAndErase();
            return;
        }

        super.update();
        tooltipAlpha = Math.min(1f, tooltipAlpha + 10f * Game.elapsed);
        lastUsedTime = Game.timeTotal;

        bg.alpha(GameMath.gate(0, tooltipAlpha, 1));
        text.alpha(GameMath.gate(0, tooltipAlpha, 1));
    }

    @Override
    protected void layout() {

        text.setPos(x + bg.marginLeft(), y + bg.marginTop());
        bg.x = x;
        bg.y = y;
        bg.size(text.width() + bg.marginHor(), text.height() + bg.marginVer());

        width = bg.width;
        height = bg.height;

    }
}