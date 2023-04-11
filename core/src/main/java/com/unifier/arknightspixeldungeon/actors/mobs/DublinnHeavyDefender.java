package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.food.MysteryMeat;
import com.unifier.arknightspixeldungeon.items.wands.Wand;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.DublinnHeavyDefenderSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;

import java.util.ArrayList;

public class DublinnHeavyDefender extends DublinnSnifferHound {
    //raw GreatCrab,well that extend sounds stupid

    {
        spriteClass = DublinnHeavyDefenderSprite.class;

        HP = HT = 25;
        defenseSkill = 0; //see damage()
        baseSpeed = 1f;

        EXP = 6;

        state = WANDERING;

        properties.add(Property.MINIBOSS);
    }

    private int moving = 0;

    @Override
    protected boolean getCloser( int target ) {
        //this is used so that the crab remains slower, but still detects the player at the expected rate.
        moving++;
        if (moving < 3) {
            return super.getCloser( target );
        } else {
            moving = 0;
            return true;
        }

    }

    @Override
    public void damage( int dmg, Object src ){
        //crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
        //All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
        if ((enemySeen && state != SLEEPING && paralysed == 0)
                && (src instanceof Wand || src instanceof Char)){
            GLog.n( Messages.get(this, "noticed") );
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked") );
        } else {
            super.damage( dmg, src );
        }
    }


    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime) {
        if ((enemySeen && state != SLEEPING && paralysed == 0)
                && (src instanceof Wand || src instanceof Char)){
            GLog.n( Messages.get(this, "noticed") );
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked") );
        } else {
            super.multipleDamage(burstArray,damageArray,src,hittedTime);
        }
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        FrostNovaQuestPlot.Quest.process();

        Dungeon.level.drop( new MysteryMeat(), pos );
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
    }
}
