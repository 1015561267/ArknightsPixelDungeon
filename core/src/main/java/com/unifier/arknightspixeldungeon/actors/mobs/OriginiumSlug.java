package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.sprites.OriginiumSlugSprite;
import com.watabou.utils.Random;

public class OriginiumSlug extends Mob {

    //raw rat

    {
        spriteClass = OriginiumSlugSprite.class;

        HP = HT = 8;
        defenseSkill = 2;

        maxLvl = 5;
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

}
