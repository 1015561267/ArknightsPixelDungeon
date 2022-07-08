package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.utils.Bundle;

public class CounterStrikeTracker extends FlavourBuff {

    public int absorbDamage = -1;
    public int time = -1;

    protected static final String TIME = "time";
    protected static final String ABSORB = "absorb";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(TIME,time);
        bundle.put(ABSORB,absorbDamage);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        time = bundle.getInt(TIME);
        absorbDamage = bundle.getInt(ABSORB);
    }
}
