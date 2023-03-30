package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.DublinnShadowcasterSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DublinnShadowcaster extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = DublinnShadowcasterSprite.class;

        HP = HT = 12;
        defenseSkill = 3;

        EXP = 4;
        maxLvl = 9;

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
        return Random.NormalIntRange(0, 3);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return
                Dungeon.level.adjacent( pos, enemy.pos ) ||
                        (
                                Dungeon.level.distance(pos, enemy.pos) == 2 &&
                                new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos
                        );
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos ) || buff(Talent.ReprimandTracker.class) != null) {

            return super.doAttack( enemy );

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap( enemy.pos );
            } else {
                zap();
            }
            return !visible;
        }
    }

    private void zap() {
        spend( TIME_TO_ZAP );

        if(!doMagicAttack(enemy,magicType.DublinnShadowCaster)){
            magicHit(this,enemy,false);
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    public void magicHit(Char from,Char to,Boolean byReflect){
        if (hit( from, to, true )) {

            int bonus = 0;
            if(byReflect && from instanceof Hero &&  ((Hero) from).pointsInTalent(Talent.EYE_FOR_EYE) == 2){
                bonus += (int) (((Hero) enemy).rawdamageRoll(from,false) / (((Hero) enemy).belongings.weapon == null ? 1f : ((Hero) enemy).belongings.weapon.speedFactor(enemy)) * 0.33f);
            }

            int dmg = Random.Int( 1, 6 );
            to.damage( dmg + bonus, this );

            if (!to.isAlive()) {
                if(to == Dungeon.hero){
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "bolt_kill") );
                }
                else if(byReflect){
                    Talent.afterReflectKill();
                }
            }

            if(byReflect){
                Talent.doAfterReflect(dmg+bonus);
            }

        } else {
            to.sprite.showStatus( CharSprite.NEUTRAL,  to.defenseVerb() );

            if(to instanceof Hero){
                Talent.onDodge();
            }
        }
    }

    @Override
    public void call() {
        next();
    }



}
