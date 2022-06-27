package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.effects.TalentSprite;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class RallyForceTracker extends Buff {

    public float recoverAmount = 0;
    public float rechargeAmount = 0;
    public boolean doubledSpeed = false;
    public State state=State.WAITING;

    private enum State{
        WAITING, RECOVERING ,RECHARGING;
    }

    private static final String STATE = "state";
    private static final String RECOVERAMOUNT = "recoveramount";
    private static final String RECHARGEAMOUNT = "rechargeamount";
    private static final String DOUBLEDSPEED = "doubledspeed";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE,state);
        bundle.put(RECOVERAMOUNT, recoverAmount);
        bundle.put(RECHARGEAMOUNT, rechargeAmount);
        bundle.put(DOUBLEDSPEED,doubledSpeed);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state = bundle.getEnum(STATE, State.class);
        recoverAmount = bundle.getInt(RECOVERAMOUNT);
        rechargeAmount = bundle.getInt(RECHARGEAMOUNT);
        doubledSpeed = bundle.getBoolean(DOUBLEDSPEED);
    }

    @Override
    public boolean act() {

        switch (state){
            default:
            case WAITING:
                if (target.HP <= target.HT /4 ){
                    state = State.RECOVERING;
                    recoverAmount = target.HT / 2;
                    TalentSprite.show(target, Talent.RALLY_FORCE,TalentSprite.Phase.FADE_IN);
                }
                break;
            case RECOVERING:
                float recover = target.HT * 0.05f * (doubledSpeed ? 2 : 1);

                //GLog.w(recover + " " + recoverAmount + " "+Math.min(recover,recoverAmount) + " " + (target.HT - target.HP) );

                int heal = (int)Math.min(Math.min(recover,recoverAmount),target.HT - target.HP);
                recoverAmount -= heal;
                if(recoverAmount<=1) {recoverAmount = 0;heal++;}


                target.heal(this, heal);
                //target.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, (int) Math.ceil( heal / 50 ));

                if(recoverAmount <= 0)
                {
                    state = State.RECHARGING;
                    rechargeAmount = getNeededRecharge();
                    TalentSprite.show(target, Talent.RALLY_FORCE,TalentSprite.Phase.STATIC);
                }
                break;
            case RECHARGING:{
                if (rechargeAmount<=0){
                    rechargeAmount = 0;
                    state = State.WAITING;
                    act();
                }
                break;
            }
        }
        spend(TICK);
        return true;
    }

    private float getNeededRecharge() {
        return doubledSpeed ? target.HT  : target.HT * 1.5f;
    }

    @Override
    public int icon() {
        return super.icon();
    }

    @Override
    public void tintIcon(Image icon) {
    }

    public void getCharge(float amount){
        rechargeAmount -= amount;
    }
}
