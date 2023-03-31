package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.DublinnScoutSprite;
import com.unifier.arknightspixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class DublinnScout extends Mob {

    private boolean attached = false;

    {
        spriteClass = DublinnScoutSprite.class;

        HP = HT = 12;
        defenseSkill = 4;

        EXP = 2;
        maxLvl = 7;

        Buff.affect(this,DublinnScoutMagicResist.class);
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public void magicalDamage(int dmg, Object src){
        if(this.buff(DublinnScoutMagicResist.class)!=null)
        {
            dmg *= 0.3f;
            Buff.detach( this, DublinnScoutMagicResist.class );
        }
        super.magicalDamage(dmg,src);
    };

    public static class DublinnScoutMagicResist extends Buff{
        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) { icon.hardlight(0.5f, 1f, 2f);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public void detach() {
            super.detach();
            target.sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "blocked"));
        }

        @Override
        public String desc() { return Messages.get(this, "desc"); }
    }
}
