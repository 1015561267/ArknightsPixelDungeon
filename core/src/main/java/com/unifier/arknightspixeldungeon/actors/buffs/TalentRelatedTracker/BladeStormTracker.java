package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.watabou.utils.Bundle;

public class BladeStormTracker extends Buff {

    protected int stack;
    protected float time;
    protected boolean token;


    private static final int Max_Stack = 5;


    protected static final String STACK	= "stack";
    protected static final String TIME = "time";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STACK, stack );
        bundle.put( TIME , time);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        stack = bundle.getInt( STACK );
        time = bundle.getFloat(TIME);
    }

    @Override
    public boolean attachTo(Char target) {
        stack = 1 ;
        token = false;
        return true;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            spend(TICK);
            if (token){token = false;}
            else detach();
        } else {
            detach();
        }
        return true;
    }

    public int getStack(){return stack;}

    public void refresh(){
        stack = Math.max(stack++,Max_Stack);
        token = true;
    }
}
