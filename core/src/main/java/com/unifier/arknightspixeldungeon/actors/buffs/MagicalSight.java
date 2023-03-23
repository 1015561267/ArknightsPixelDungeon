package com.unifier.arknightspixeldungeon.actors.buffs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class MagicalSight extends FlavourBuff {

    public static final float DURATION = 50f;

    public static final int DISTANCE = 12;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.MIND_VISION;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 1.67f, 1f);
    }

    //@Override
    //public float iconFadePercent() {
    //    return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    //}

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)){
            Buff.detach(target, Blindness.class);
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
        GameScene.updateFog();
    }

    {
        immunities.add(Blindness.class);
    }
}
