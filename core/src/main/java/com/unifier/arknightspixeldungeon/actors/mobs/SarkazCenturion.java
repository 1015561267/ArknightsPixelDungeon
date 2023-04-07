package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.LockedFloor;
import com.unifier.arknightspixeldungeon.actors.buffs.MagicalSleep;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.items.keys.SkeletonKey;
import com.unifier.arknightspixeldungeon.items.wands.WandOfBlastWave;
import com.unifier.arknightspixeldungeon.levels.SewerBossLevel;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.SarkazCenturionSprite;
import com.unifier.arknightspixeldungeon.ui.BossHealthBar;
import com.unifier.arknightspixeldungeon.utils.GLog;
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
    }

    @Override
    public boolean act() {

        if (abilityCd == 0 && paralysed <= 0){
            //if can use ability then always use it,unless controlled
            ((SarkazCenturionSprite)sprite).performAbility();
            spend(attackDelay());
            next();
            return true;
        } else return super.act();
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
    public boolean attack( Char enemy ) {

        boolean result = super.attack(enemy);

        if (abilityCd > 0){//ability aoe attack won't charge itself
            abilityCd --;
            if (abilityCd == 0){
                ((SarkazCenturionSprite)sprite).chargingAbility();
            }
        }
        return result;
    }

    public void doAbility() {
        int count = 0;
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
            Camera.main.shake( GameMath.gate( 1, count, 9), 0.2f );
        }

        abilityCd = resetAbilityCd();
        //next();
        //spend( attackDelay() )
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
            Buff.affect(this,CenturionBerserk.class).on(6f);
        }
        else if(phase == 2 && HP<=15){
            HP = 15;
            phase++;
            Buff.affect(this,CenturionBerserk.class).on(6f);
        }
    }

    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){
        int beforeHitHP = HP;

        ArrayList<Integer> tempArray = new ArrayList<>();

            for(Integer record : damageArray){
                if(phase==3){
                    record /= 2;
                }
                if(buff(CenturionBerserk.class)!=null){
                    record = (int) Math.ceil(record/10);
                }
                tempArray.add(record);
                GLog.i(String.valueOf(record));
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
            Buff.affect(this,CenturionBerserk.class).on(6f);
        }
        else if(phase == 2 && HP<=15){
            HP = 15;
            phase++;
            Buff.affect(this,CenturionBerserk.class).on(6f);
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        GameScene.bossSlain();

        Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();

        Badges.validateBossSlain();

        yell( "被击败台词，待替换" );
    }

    public float speed() {
        return super.speed() * (buff( CenturionBerserk.class ) != null ? 1.33f : 1f);
    }

    public boolean haveToBleed() {
        return buff( CenturionBerserk.class ) != null;
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
        phase = bundle.getInt(PHASE);
        abilityCd = bundle.getInt(ABILITY_CD);

        //if (state instanceof Hunting) {
        //    this.state = new SarkazCenturionHunting();
        ////}

        BossHealthBar.assignBoss(this);
        if (buffs(CenturionBerserk.class) != null) {
            BossHealthBar.bleed(true);
        }
    }
    public static class CenturionBerserk extends Buff {

        private static final String LEFT	= "left";
        private float left;

        @Override
        public boolean act() {
            spend( TICK );
            left -= TICK;

            if (left <= 0) {
                ((SewerBossLevel)Dungeon.level).onBerserkEnd();
                target.sprite.showStatus(CharSprite.NEUTRAL,"zzz");
                Buff.affect(target, MagicalSleep.class);
                ((SarkazCenturion)target).abilityCd = ((SarkazCenturion)target).resetAbilityCd();
                BossHealthBar.bleed(false);
                ((SarkazCenturionSprite)target.sprite).spray(false);
                detach();
            }

            return true;
        }

        public void on(float time) {
            left = time;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEFT, left );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            left = bundle.getFloat( LEFT );
        }

        @Override
        public boolean attachTo( Char target ) {
            ((SewerBossLevel)Dungeon.level).onBerserkBegin();
            target.sprite.showStatus(CharSprite.WARNING,"狂暴化！");
            BossHealthBar.bleed(true);
            ((SarkazCenturionSprite)target.sprite).spray(true);

            for (int i : PathFinder.NEIGHBOURS8){
                Char ch = Actor.findChar(target.pos + i);
                if(ch!=null && ch instanceof Hero){
                    int oppositeHero = ch.pos + (ch.pos - target.pos);
                    Ballistica trajectory = new Ballistica(ch.pos, oppositeHero, Ballistica.MAGIC_BOLT);
                    WandOfBlastWave.throwChar(ch, trajectory, 2);
                    break;
                }
            }

            return super.attachTo(target);
        }
    }

    public static class DerivativeRat extends Rat {
        public boolean isDerivative(){return true;}
    }

    public static class DerivativeDublinnScout extends DublinnScout {
        public boolean isDerivative(){return true;}
    }


    public boolean isDerivative(){return false;}


    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (abilityCd == 0){
            ((SarkazCenturionSprite)sprite).chargingAbility();
        }
    }
}