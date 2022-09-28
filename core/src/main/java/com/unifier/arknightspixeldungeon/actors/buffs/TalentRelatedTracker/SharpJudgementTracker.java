package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.watabou.utils.Bundle;

public class SharpJudgementTracker extends Buff {

    protected int stack = 0;

    protected static final String STACK	= "stack";

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

    public void setStack(int t) {
        stack = t;
    }

    public int getStack(){return stack;}

    @Override
    public boolean act() {//It's removed at
        if (target.isAlive()) {
            spend(TICK);
        } else {
            detach();
        }
        return true;
    }
}
