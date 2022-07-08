package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.utils.Bundle;

public class WellPreparedTracker extends FlavourBuff {

    public int time = -1;

    public WellPreparedTracker setTime(int time) {
        this.time = time;
        return this;
    }

    protected static final String TIME = "time";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(TIME,time);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        time = bundle.getInt(TIME);
    }
}
