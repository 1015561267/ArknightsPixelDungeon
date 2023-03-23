package com.unifier.arknightspixeldungeon.actors.buffs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class SniperSight extends Buff{

    {
        type = Buff.buffType.POSITIVE;
    }

    public float angle = 0;
    public int distance = 0;
    public int degrees = 60;

    public int distanceMax(){
        return 16;
    }

    public void set( float angle ,int initDistance,int degrees) {
        this.angle = angle;
        this.distance = Math.min(initDistance,distanceMax());
        this.degrees = degrees;
    }

    @Override
    public int icon() {
        return BuffIndicator.MARK;
    }
    //temporarily,for it should be merged into skill ui

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 1.67f, 1f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc(){
        return Messages.get(this, "desc", distance ,distanceMax());
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
                if(distance<distanceMax()){
                    distance++;
                    Dungeon.observe();
                    GameScene.updateFog();
                }
                spend( TICK );
        } else {
            detach();
        }
        return true;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)){
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
        GameScene.updateFog();
    }

    private static final String ANGLE	= "angle";
    private static final String DISTANCE	= "distance";


    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ANGLE, angle );
        bundle.put( DISTANCE, distance );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        angle = bundle.getInt( ANGLE );
        distance = bundle.getInt( DISTANCE );
    }
}
