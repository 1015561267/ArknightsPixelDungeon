package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class RageTracker extends Buff {

    {
        type = buffType.POSITIVE;
    }

    public float rage = 0;
    public int rageLossBuffer = 0;

    @Override
    public boolean act() {

        if (rageLossBuffer > 0) {
            rageLossBuffer--;
        } else {
            rage -= GameMath.gate(0.1f,rage,1f) * 0.067f * Math.pow((target.HP / (float) target.HT),2);
//            GLog.i(Integer.toString((int)(rage * 100)));
            if (rage <= 0) {
                detach();
            }
        }

        spend(TICK);
        return true;
    }

    public int damageFactor(int dmg){
        float bonus = Math.min(1.5f, 1f + (rage / 2f));
        return Math.round(dmg * bonus);
    }

    public void damage(int damage){
//        rage += damage;
        rage += (damage/(float)target.HT)/3f;
        BuffIndicator.refreshHero(); //show new power immediately
        rageLossBuffer = 3; //2 turns until rage starts dropping
    }

    @Override
    public int icon() {
        return BuffIndicator.FURY;
    }

    @Override
    public String desc() {
        return Integer.toString((int)(rage * 100));
    }

    private static final String RAGE = "rage";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RAGE,rage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        rage = bundle.getFloat(RAGE);
    }
}