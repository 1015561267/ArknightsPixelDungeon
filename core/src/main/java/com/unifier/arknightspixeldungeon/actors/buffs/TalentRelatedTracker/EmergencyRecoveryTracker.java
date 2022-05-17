package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.watabou.utils.Bundle;

public class EmergencyRecoveryTracker extends Buff
{
    private float particalAmount;
    private static final String PARTICAL_AMOUNT	= "partical_amount";

    {
        actPriority = HERO_PRIO - 1;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PARTICAL_AMOUNT, particalAmount );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        particalAmount = bundle.getInt( PARTICAL_AMOUNT );
    }

    @Override
    public boolean act() {
        if (target.isAlive()&& target instanceof Hero) {
            if(((Hero) target).hasTalent(Talent.EMERGENCY_RECOVERY))
            {
                if(target.HP <= target.HT * 0.5f && ((Hero) target).pointsInTalent(Talent.EMERGENCY_RECOVERY)>=1 )
                {
                    particalAmount += 0.5f;
                }

                if(target.HP <= target.HT * 0.25f && ((Hero) target).pointsInTalent(Talent.EMERGENCY_RECOVERY)>= 2)
                {
                    particalAmount += 0.5f;
                }

                if(particalAmount >= 1)
                {
                    target.heal(this,(int)particalAmount);
                    particalAmount %= 1f;
                }
            }
            spend( TICK );
        }
        else detach();
        return true;
    }


}
