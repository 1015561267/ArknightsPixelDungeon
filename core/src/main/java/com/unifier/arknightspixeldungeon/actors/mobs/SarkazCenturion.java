package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.unifier.arknightspixeldungeon.actors.buffs.LockedFloor;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.levels.SewerBossLevel;
import com.unifier.arknightspixeldungeon.sprites.SarkazCenturionSprite;
import com.unifier.arknightspixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SarkazCenturion extends Mob {

    {
        HP = HT = 150;
        EXP = 10;
        defenseSkill = 6;
        spriteClass = SarkazCenturionSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);

        HUNTING = new SarkazCenturionHunting();
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {

        float bouns = 1f;
        if(abilityCd==0){

            switch (phase){
                case 1:bouns = 1.2f;break;
                case 2:bouns = 1.5f;break;
                case 3:bouns = 2f;break;
            }
        }

        if(buffs(CenturionBerserk.class)!=null)
        {
            bouns += 0.6f;
        }

        return (int) (bouns * Random.NormalIntRange( 1, 8 ));
    }

    @Override
    public int attackSkill( Char target ) {
        int attack = 10;
        if(abilityCd==0){
            attack+=20;
        }
        return attack;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    private int phase = 1;
    private int abilityCd = resetAbilityCd();

    private int resetAbilityCd() {
        return 4-phase;//every 3/2/1 attacks trigger once
    }

    @Override
    protected boolean act() {
        return super.act();
    }

    @Override
    public boolean attack( Char enemy ) {

        boolean result = super.attack(enemy);

        if (result && abilityCd > 0){//ability aoe attack won't charge itself
            abilityCd --;
            if (abilityCd == 0){
                ((SarkazCenturionSprite)sprite).showWarn(1);
            }
        }
        return result;
    }

    public void doAbility() {

        int count = 1;
        for (int i : PathFinder.NEIGHBOURS8){
            Char ch = Actor.findChar(pos + i);
            if(ch!=null){
                attack(ch);
                count ++;
                if(ch instanceof Hero){
                    count += 3;
                }
            }
        }

        if(count>0){
            Camera.main.shake( GameMath.gate( 3, count, 9), 0.2f );
        }

        abilityCd = resetAbilityCd();
        spend( attackDelay() );
        return;
    }

    @Override
    public void damage(int dmg, Object src) {

        if(phase == 3){
            dmg /= 2;
        }

        if(buff(CenturionBerserk.class)!=null){
            dmg = (int) Math.ceil(dmg/10);
        }

        super.damage(dmg, src);

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);

        if(phase == 1 && HP<=75){
            HP = 75;
            phase++;
            Buff.affect(this,CenturionBerserk.class,6f);
        }
        else if(phase == 2 && HP<=15){
            HP = 15;
            phase++;
            Buff.affect(this,CenturionBerserk.class,6f);
        }
    }

    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){
        int beforeHitHP = HP;

        ArrayList<Integer> tempArray = new ArrayList<>();

        if(phase == 3 || buff(CenturionBerserk.class)!=null){
            for(Integer record : damageArray){
                if(phase==3){
                    record /= 2;
                }
                if(buff(CenturionBerserk.class)!=null){
                    record = (int) Math.ceil(record/10);
                }
                tempArray.add(record);
            }
        }

        super.multipleDamage(burstArray,tempArray,src,hittedTime);
        int totaldmg = beforeHitHP - HP;

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) {
            lock.addTime(totaldmg*2f);
        }

        if(phase == 1 && HP<=75){
            HP = 75;
            phase++;
            Buff.affect(this,CenturionBerserk.class,6f);
        }
        else if(phase == 2 && HP<=15){
            HP = 15;
            phase++;
            Buff.affect(this,CenturionBerserk.class,6f);
        }
    }

    public float speed() {
        return super.speed() * (buff( CenturionBerserk.class ) != null ? 1.33f : 1f);
    }

    private static final String PHASE = "phase";
    private static final String ABILITY_CD = "ability_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( ABILITY_CD, abilityCd );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt( PHASE );
        abilityCd = bundle.getInt( ABILITY_CD );

        BossHealthBar.assignBoss(this);
        if (phase == 3) BossHealthBar.bleed(true);
    }

    public static class CenturionBerserk extends FlavourBuff {
        @Override
        public boolean attachTo( Char target ) {
            ((SewerBossLevel)Dungeon.level).onBerserkBegin();
            return super.attachTo(target);
        }

        @Override
        public void detach() {
            if(((SarkazCenturion)target).phase != 3){
                ((SewerBossLevel)Dungeon.level).onBerserkEnd();
            }
            super.detach();
        }
    }

    private class SarkazCenturionHunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (abilityCd == 0){//if can use ability then always use it,unless controlled
                ((SarkazCenturionSprite)sprite).performAbility();
                return true;
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }
}