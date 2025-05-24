package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.MagicMissile;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.mechanics.ConeAOE;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.ui.SkillLoader;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Shotgun extends ExusiaiSkill {
    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.SHOTGUN,0,0,128,64);
    }

    //each bullet
    @Override
    public int shootDamageMin(){
        return 1 + (int) (owner.lvl/5f + Dungeon.depth);
    }

    @Override
    public int shootDamageMax(){
        return 3 + (int)(owner.lvl/5f +  Dungeon.depth);
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        if(attachment.attachType() == Attachment.AttachType.FRONT_HANG ||
                attachment.attachType() == Attachment.AttachType.BELOW_HANG ||
                attachment.attachType() == Attachment.AttachType.BACK_HANG ||
                attachment.attachType() == Attachment.AttachType.BULLET )
            return true;
        return false;
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if(attachment == this.getFRONT_HANG()  || attachment == this.getBELOW_HANG() || attachment == this.getBACK_HANG() || attachment== this.getBULLET()){
            return true;
        }
        return false;
    }

    @Override
    public GunType getType() {
        return GunType.SHOTGUN;
    }

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.NONE);
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 60f;
    }

    @Override
    public int getMaxCharge() {
        return 5;
    }

    public void doAction() {
        if (!available()) {
            return;
        }

        if(doCheckBeforeAiming()){
            SkillLoader.targetting = true;
            SkillLoader.useTargeting();
            GameScene.selectCell(shotgunAimer);
        }
    }

    protected final CellSelector.Listener shotgunAimer = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            else {
                    if(doCheckCell(cell,owner)){
                        doShoot(owner,cell);
                    }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SniperRifle.class, "prompt");
        }
    };

    protected void doShoot(Hero owner,Integer cell){
        Ballistica ballistica = new Ballistica(owner.pos,cell,Ballistica.WONT_STOP);
        int maxDist = Math.min(ballistica.dist, initDistance());

        PointF fromP = new PointF(Dungeon.level.cellToPoint(owner.pos));
        fromP.x += 0.5f;
        fromP.y += 0.5f;

        PointF toP = new PointF(Dungeon.level.cellToPoint(ballistica.collisionPos));
        toP.x += 0.5f;
        toP.y += 0.5f;

        if (PointF.distance(fromP, toP) > maxDist){
            toP = PointF.inter(fromP, toP, maxDist/PointF.distance(fromP, toP) );
        }

        //now we can get the circle's radius. We bump it by 0.5 as we want the cone to reach
        // The edge of the target cell, not the center.
        float circleRadius = PointF.distance(fromP, toP);
        circleRadius += 0.5f;

        float initalAngle = PointF.angle(fromP, toP)/PointF.G2R;

        //here we save random bullet target cell in hashSet,only one missile sprite each cell.
        //as a enemy can be hit more than one time so HashMap is not optional to save their target and damage,at least not in a dual structure
        //FIXME may use more check to save times for each cell and use different bullet sprite,to improve visual;But it should be too fast to show too much difference to player

        HashSet<Integer> bulletResult = new HashSet<>();
        HashSet<Char> affectedEnemy = new HashSet<>();

        owner.busy();

        for(int i=0;i<bullets();i++){

            Integer destination = owner.pos;
            Integer count = 3;

            //try a few times to reduce bad performance around corner,as bullet would stop at user and have no affect
            //in fact it means bullet have more chance to hit ideal cell when at this condition
            while (destination == owner.pos && count-- > 0) {
                Ballistica randomBallistica = randomBallistica(owner.pos, fromP, circleRadius, initalAngle, degrees(), Ballistica.PROJECTILE, false);
                destination = randomBallistica.collisionPos;
            }

            bulletResult.add(destination);

            Char enemy = Actor.findChar(destination);

            if(enemy!=null && enemy!= owner){

                int damage = Random.Int(shootDamageMin(), shootDamageMax());

                int dr = enemy.drRoll();

                int effectiveDamage ;//= enemy.defenseProc(enemy, damage);

                effectiveDamage = Math.max(damage - dr, 0);

                if (enemy.buff(Vulnerable.class) != null) {
                    effectiveDamage *= 1.33f;
                }

                if (owner.buff(Weakness.class) != null) {
                    effectiveDamage *= 0.67f;
                }

                if(!affectedEnemy.contains(enemy)){
                    Buff.affect(enemy,Shotgun.DelayedDamage.class).initialize(enemy);
                }
                affectedEnemy.add(enemy);
                //Here we construct a "virtual" process by calculate the damage of each bullet but don't do it immediately,just remove the mob from Ballistica checking
                //so the close mob won't block later bullet if they should(and in fact will)be killed by former bullets.
                enemy.pretendDamage(effectiveDamage,this);
            }
        }

        //in fact enemy is already damaged,have to recall it
        //FIXME is it really necessary to pretend damage and recall,then do it in fact?
        for (Char enemy:affectedEnemy) {
            enemy.buff(Shotgun.DelayedDamage.class).recall();
        }

        Camera.main.shake(1.5f, 0.35f);

        ConeAOE effect = new ConeAOE( ballistica ,maxDist,degrees(),Ballistica.DISMISS_CHAR);

        for (Ballistica ray : effect.outerRays){
            MagicMissile visual = (MagicMissile)owner.sprite.parent.recycle( MagicMissile.class );
            visual.reset(  MagicMissile.FORCE_CONE,
                    owner.sprite,
                    ray.path.get(ray.dist),
                    null);
            visual.setSpeed(MagicMissile.SPEED * 2f);
        }

        for(Integer pos : bulletResult) {
            doEnemyCheck(owner.pos,pos);
            ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(owner.pos, pos, ammoSprite() , new Callback() {
                @Override
                public void call() {
                    //doEnemyCheck(owner.pos,pos);
                    //FIXME if damage logic is added at here there will be a bunch of Actor main process jam bugs,have no choice but to move it outside before I found a good way to do it.
                }
            });
        }

        doCheckAfterShooting(owner.pos,ballistica.collisionPos,1,false);
    }

    protected void doEnemyCheck(int from, int to){
        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];
        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            if(enemy.buff(Shotgun.DelayedDamage.class)!=null) {
                enemy.buff(Shotgun.DelayedDamage.class).triggerDamage();
            }
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
        }
    }


    private int initDistance() {
        return 3;
    }

    private int degrees() {
        return 45;
    }

    private int bullets() {
        return 7;
    }

    public Ballistica randomBallistica(Integer formPos,PointF fromP,float circleRadius,float coreAngle,float rangeVariety,int ballisticaParams,boolean isTriangularlyDistributed) {

        //FIXME quite a mess,it might be fixed after "random angel differ from selected" is not only used at here
        //very similar to coneAoe,but use a random angel instead

        PointF scan = new PointF();
        Point scanInt = new Point();

        float randomAngle = Random.Float( coreAngle - rangeVariety/2f , coreAngle + rangeVariety/2f);

        scan.polar(randomAngle * PointF.G2R, circleRadius);
        scan.offset(fromP);
        scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
        scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
        scanInt.set(
                (int) GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));

        Integer toPos = Dungeon.level.pointToCell(scanInt);

        Ballistica ray = new Ballistica(formPos, toPos, ballisticaParams);

        //here we check the dist,if it didn't hit any char and have radius no less than 4(although not common for shotgun),then consider outer ray
        //dismissing dying (or should dying) target is not realized at here,see TemporarilyRemove buff and how it is used in Ballistica.build()

        if(Actor.findChar(ray.path.get(ray.dist))!= null || circleRadius < 4){
            return ray;
        }
        else {
            scan.polar(randomAngle * PointF.G2R, circleRadius - 1);
            scan.offset(fromP);
            scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
            scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
            scanInt.set(
                    (int)GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                    (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
            toPos = Dungeon.level.pointToCell(scanInt);
            ray = new Ballistica(formPos, toPos, ballisticaParams);
            return ray;
            //it could and should be unable to hit any char in some time,just let it be
        }
    }

    public static class DelayedDamage extends Buff {

        public boolean dismissFlag = false;

        public int initHp;
        public int initShield;

        //mind that we save original damage value at here,let true damage process do their own job
        //what we concerned mainly is whether the enemy should die and be dismissed in Ballistica calculation

        public ArrayList<Integer> damageArray = new ArrayList<>();

        public void initialize(Char enemy) {
            initHp = enemy.HP;
            initShield = enemy.SHLD;
        }

        public void addDamage(int dmg) {
            damageArray.add(dmg);
        }

        public void triggerFlag() {
           dismissFlag = true;
        }

        public void triggerDamage() {
            //avoid void value
            //TODO now that bullet is already affected by random angle,it seems unfair to calculate hit/miss again.
            //The bullet number,random angle,even repeat check time and their damage,their value may need balance after test
            ArrayList<Boolean> burstArray = new ArrayList<>((Collections.nCopies(damageArray.size(),true)));
            target.multipleDamage(burstArray,damageArray,Shotgun.class,damageArray.size());
            detach();
        }

        public void recall() {
            target.HP = initHp;
            target.SHLD = initShield;
        }
        //TODO it should not need save/load as this is only for data transfer,will add if it really cause bug
    }

}
