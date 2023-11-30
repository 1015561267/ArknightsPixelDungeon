package com.unifier.arknightspixeldungeon.actors.buffs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TimeBubble extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    private float left;
    ArrayList<Integer> presses = new ArrayList<>();

    @Override
    public int icon() {
        return BuffIndicator.SLOW;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 1f, 0);
    }

    public TimeBubble reset(float left){
        this.left = left;
        return this;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    public void processTime(float time){
        left -= time;

        //use 1/1,000 to account for rounding errors
        if (left < -0.001f){

            //if (Dungeon.hero.buff(Talent.SeizeOpportunityTracker.class) != null) {
            //    Dungeon.hero.buff(Talent.SeizeOpportunityTracker.class).detach();
            //}

            //if (Dungeon.hero.hasTalent(Talent.SEIZE_OPPORTUNITY)) {
            //    Dungeon.hero.skill_2.getCoolDown(Dungeon.hero.skill_2.rawCD() * 0.25f);
            //    Dungeon.hero.skill_3.getCoolDown(Dungeon.hero.skill_3.rawCD() * 0.25f);
            //}

            detach();
        }

    }

    public void setDelayedPress(int cell){
        if (!presses.contains(cell)) {
            presses.add(cell);
        }
    }

    public void triggerPresses() {
        for (int cell : presses) {
            Dungeon.level.press(cell,null,true);
        }

        presses = new ArrayList<>();
    }

    @Override
    public boolean attachTo(Char target) {
        if (Dungeon.level != null)
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                mob.sprite.add(CharSprite.State.PARALYSED);
        GameScene.freezeEmitters = true;
        return super.attachTo(target);
    }

    @Override
    public void detach(){
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
            mob.sprite.remove(CharSprite.State.PARALYSED);
        GameScene.freezeEmitters = false;

        super.detach();
        triggerPresses();
    }

    private static final String PRESSES = "presses";
    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        int[] values = new int[presses.size()];
        for (int i = 0; i < values.length; i ++)
            values[i] = presses.get(i);
        bundle.put( PRESSES , values );

        bundle.put( LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        int[] values = bundle.getIntArray( PRESSES );
        for (int value : values)
            presses.add(value);

        left = bundle.getFloat(LEFT);
    }

}
