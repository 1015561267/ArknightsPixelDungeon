package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.mobs.Eye;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.actors.mobs.Shaman;
import com.unifier.arknightspixeldungeon.actors.mobs.Warlock;
import com.unifier.arknightspixeldungeon.effects.TalentSprite;
import com.watabou.utils.Bundle;

public class DragonScaleTracker extends Buff
{
    private float particalAmount;
    private int stack;
    private boolean upgraded;


    private static final String PARTICAL_AMOUNT	= "partical_amount";
    private static final String STACK	= "stack";
    private static final String UPGRADED	= "upgraded";

    {
        actPriority = HERO_PRIO - 1;
        particalAmount = 0f;
        stack = 0;
        upgraded = false;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PARTICAL_AMOUNT, particalAmount );
        bundle.put(STACK,stack);
        bundle.put(UPGRADED,upgraded);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        particalAmount = bundle.getFloat( PARTICAL_AMOUNT );
        stack = bundle.getInt(STACK);
        upgraded = bundle.getBoolean(UPGRADED);
    }

    @Override
    public boolean act() {
        if (target.isAlive()&& target instanceof Hero) {
            spend( TICK );
        }
        else detach();
        return true;
    }

    public void stack(int damage, Object source){
        if(source instanceof Mob){
            if(stack < stackThreshold(upgraded)){
                particalAmount += damage / target.HT;
                if(particalAmount >= chargeThreshold(upgraded)){
                    particalAmount = 0;
                    stack ++;

                    TalentSprite.show(target,Talent.DRAGON_SCALE,TalentSprite.Phase.FADE_IN);
                }
            }
        }
    }

    public int affect(int damage,Object source){
        if(stack>0 && source instanceof Mob){
            if(source instanceof Eye || source instanceof Shaman || source instanceof Warlock){
                if(damage>target.HT * 0.3f){
                    damage = (int) Math.floor(target.HT * 0.3f);
                    stack--;
                    TalentSprite.show(target,Talent.DRAGON_SCALE,TalentSprite.Phase.STATIC);
                }
            }

            else if(damage > target.HT * 0.15f){
                damage = (int) Math.floor(target.HT * 0.15f);
                stack--;
                TalentSprite.show(target,Talent.DRAGON_SCALE,TalentSprite.Phase.STATIC);
            }
        }
        return damage;
    }

    public void upgrade(){
        upgraded = true;
    }

    public static int stackThreshold(boolean upgraded){
        return upgraded ? 3 : 2;
    }

    public static float chargeThreshold(boolean upgraded){
        return upgraded ? 0.45f : 0.6f;
    }
}