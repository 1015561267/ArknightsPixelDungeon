package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.sprites.DublinnSnifferHoundSprite;
import com.watabou.utils.Random;

public class DublinnSnifferHound extends Mob {

    {
        spriteClass = DublinnSnifferHoundSprite.class;

        HP = HT = 15;
        defenseSkill = 5;
        baseSpeed = 2f;

        EXP = 3;
        maxLvl = 8;
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }
}
