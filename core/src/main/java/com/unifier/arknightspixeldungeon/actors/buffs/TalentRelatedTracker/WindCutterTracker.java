package com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import static com.unifier.arknightspixeldungeon.Dungeon.hero;

public class WindCutterTracker extends FlavourBuff {

    protected int damage;
    protected float time;

    protected static final String DAMAGE	= "damage";
    protected static final String TIME = "time";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DAMAGE, damage );
        bundle.put( TIME , time);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damage = bundle.getInt( DAMAGE );
        time = bundle.getFloat(TIME);
    }

    public void set(int damage) {
        this.damage = damage;
    }

    @Override
    public void detach() {
        target.damage(damage, hero);
        if (!target.isAlive()) {
            GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", target.name)));
            //int exp = hero.lvl <= ((Mob) target).maxLvl ? ((Mob) target).EXP : 0;
            //if (exp > 0) {
            //    hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
            //    hero.earnExp(exp);
            //}
        }
        super.detach();
    }
}
