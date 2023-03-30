package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.effects.particles.EnergyParticle;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.DublinnSniperSprite;
import com.watabou.utils.Random;

public class DublinnSniper extends Mob {

    boolean reloaded = false;

    private final float TIME_TO_RELOAD = 1f; // 1 turn to reload and 1 turn to shoot,make it attack every 2 turns

    {
        spriteClass = DublinnSniperSprite.class;

        HP = HT = 10;
        defenseSkill = 5;
        baseSpeed = 0.5f;

        EXP = 3;
        maxLvl = 9;

        reloaded = false;
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 3, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 14;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos && buff(Talent.ReprimandTracker.class) == null;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if(reloaded){//doAttack already called by canAttack
            reloaded = false;
            return super.doAttack(enemy);
        }
        else {
            sprite.centerEmitter().burst( EnergyParticle.FACTORY, 10 );
            sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, "reloading"));
            reloaded = true;
            spend( attackDelay() );
            return true;
        }
    }

}
