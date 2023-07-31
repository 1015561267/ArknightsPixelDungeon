package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.sprites.TouchOfTheSanguinarchSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TouchOfTheSanguinarch extends Mob {

    {
        spriteClass = TouchOfTheSanguinarchSprite.class;

        HP = HT = 4;
        defenseSkill = 6;

        EXP = 4;
        maxLvl = 9;
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public void damage( int dmg, Object src ) {

        if(dmg>0) dmg = 1;

        super.damage(dmg,src);
    }

    @Override
    //FIXME After finish this I realize this process actually contains message showing and hit/miss check,so after more multiple-damage ways implemented,it should be merged and used as a "multiple attack"function
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){

        ArrayList<Integer> tmp = new ArrayList<>();

        int i=0;

        for(Integer dmg : damageArray){
            if(burstArray.get(i) && dmg > 0){
                tmp.add(1);
            }else {
                tmp.add(0);
            }
            i++;
        }

        super.multipleDamage(burstArray,tmp,src,hittedTime);
    }

    @Override
    protected int pretendDefenseFactor(int dmg, Object src) {
        if(dmg>0) dmg = 1;
        return dmg;
    }

}
