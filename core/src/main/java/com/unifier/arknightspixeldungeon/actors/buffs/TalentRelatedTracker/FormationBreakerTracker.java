package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.watabou.utils.Bundle;

public class FormationBreakerTracker extends FlavourBuff {

    public int stack;
    protected static final String STACK	= "stack";

    public void set(int stack) {
        this.stack = stack;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STACK, stack );

    }
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        stack = bundle.getInt( STACK );
    }
}