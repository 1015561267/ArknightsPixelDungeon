package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.utils.Bundle;

public class CollectComboTracker extends FlavourBuff {

    public float fadeTime = 3f;
    public int combo;

    @Override
    public boolean act() {

        if ((fadeTime -= TICK) <= 0){
            combo--;
            fadeTime = 3f;
        }

        if (combo <= 0) {
            detach();
        }

        return true;
    }

    public CollectComboTracker set(int combo) {
        this.combo = combo;
        return this;
    }

    private static final String FADETIME = "fadetime";
    private static final String COMBO = "combo";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FADETIME,fadeTime);
        bundle.put(COMBO,combo);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        fadeTime = bundle.getFloat(FADETIME);
        combo = bundle.getInt(COMBO);
    }
}
