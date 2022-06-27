package com.unifier.arknightspixeldungeon.actors.buffs;

import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;

public class Hex extends FlavourBuff {

    public static final float DURATION	= 30f;

    {
        type = buffType.NEGATIVE;
    }

    @Override
    public int icon() {
        return BuffIndicator.HEX;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

}