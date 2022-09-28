package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SonicCuttingTracker extends Buff {

    public int stack = 0;//just as token so don't need to last after skill is done

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

    @Override
    public boolean attachTo(Char target) {
        stack = 0 ;
        return true;
    }

    public void stack(){ stack += Random.IntRange(1,3); }

    @Override
    public boolean act() {
        detach();
        return true;
    }
}
