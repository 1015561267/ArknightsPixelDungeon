package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.watabou.utils.Bundle;

public class ReflectTracker extends Buff {

    protected int stack;
    protected float time;

    protected static final String STACK	= "stack";
    protected static final String TIME = "time";
    private static final float rawCoolDown = 50f;

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
    public boolean act() {
        if (target.isAlive()) {
            spend(TICK);

            if(stack < maxStack()){
                setCoolDown(TICK);
            }

        } else {
            detach();
        }
        return true;
    }

    public void setCoolDown(float tick) {
        time -= tick;
        if(time <= 0f){
            if (gainStack()){
                time = rawCoolDown + time;
            }
            else time = rawCoolDown;
        }
    }

    public boolean spendStack(){
        if(stack > 0){
            stack --;
            return true;
        }
        return false;
    }

    public boolean gainStack(){

        if(stack<maxStack()){
            stack++;
            return true;
        }

        return false;
    }

    public int maxStack(){
        return ((Hero)target).pointsInTalent(Talent.REFLECT) + 1;
    }
}
