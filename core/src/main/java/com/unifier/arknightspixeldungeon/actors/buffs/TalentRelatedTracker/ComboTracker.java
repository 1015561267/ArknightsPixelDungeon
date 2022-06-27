package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.watabou.utils.Bundle;

public class ComboTracker extends Buff {

    private static final String STACK = "stack";
    private static final String FADETIME = "fadetime";
    private static final String MISS = "miss";
    private static final String COMBO = "combo";

    private static final float DURATION = 5f;
    private static final int MAX_COMBO = 10;

    protected int stack = 0;
    protected float fadeTime = DURATION;
    protected boolean missTracker = false;
    protected int comboTracker = 0;


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STACK, stack);
        bundle.put(FADETIME,fadeTime);
        bundle.put(MISS, missTracker);
        bundle.put(COMBO, comboTracker);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        stack = bundle.getInt(STACK);
        fadeTime = bundle.getFloat(FADETIME);
        missTracker = bundle.getBoolean(MISS);
        comboTracker = bundle.getInt(COMBO);
    }

    @Override
    public boolean act() {

        if((fadeTime -= TICK) <= 0){
            detach();
        }

        spend(TICK);
        return true;
    }

    public void hit() {

        stack();
        fadeTime = DURATION;
        missTracker = false;

        if(target instanceof Hero && ((Hero) target).hasTalent(Talent.DEADLY_COMBO)){
            comboTracker++;
        }
    }

    public void miss(){
        if(!missTracker){
            missTracker = true;
        }
        else detach();
    }

    public boolean combo(){
        if (comboTracker>=2) {comboTracker = 0 ; return true;}
        else return false;
    }

    public void stack(){ stack = Math.max(stack++,MAX_COMBO); }

    public int getStack(){return stack;}
}
