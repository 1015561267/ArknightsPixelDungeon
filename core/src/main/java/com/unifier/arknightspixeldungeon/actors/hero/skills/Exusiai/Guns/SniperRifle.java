package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.SniperSight;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.CheckedCell;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.effects.particles.SnipeParticle;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.mechanics.ConeAOE;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.ui.QuickSlotButton;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.ui.SkillLoader;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndExusiaiSkill;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SniperRifle extends ExusiaiSkill {
    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.SNIPER_RIFLE,0,0,128,64);
    }

    @Override
    public int shootDamageMin() {
        return 3 + owner.lvl/3 + Dungeon.depth;
    }

    @Override
    public int shootDamageMax() {
        return 8 + owner.lvl + Dungeon.depth * 3;
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        if(attachment.attachType() == Attachment.AttachType.GUN_SIGHT
                || attachment.attachType() == Attachment.AttachType.FRONT_HANG
                || attachment.attachType() == Attachment.AttachType.AMMO_BOX
                || attachment.attachType() == Attachment.AttachType.BACK_HANG ){
            return true;
        }
        return false;
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if(attachment == this.getGUN_SIGHT()  || attachment == this.getFRONT_HANG() || attachment == this.getAMMO_BOX() || attachment== this.getBACK_HANG()){
            return true;
        }
        return false;
    }

    @Override
    public GunType getType() {
        return GunType.SNIPER_RIFLE;
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
        return 80f;
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
            GameScene.selectCell(sniperAimer);
        }
    }

    protected final CellSelector.Listener sniperAimer = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            else {
                if(owner.buff(SniperSight.class)==null){
                    if(cell != owner.pos){

                        PointF fromP = new PointF(Dungeon.level.cellToPoint(owner.pos));
                        fromP.x += 0.5f;
                        fromP.y += 0.5f;
                        PointF toP = new PointF(Dungeon.level.cellToPoint(cell));
                        toP.x += 0.5f;
                        toP.y += 0.5f;

                        float initalAngle = PointF.angle(fromP, toP)/PointF.G2R;

                        Buff.affect(owner,SniperSight.class).set(initalAngle,initDistance(),degrees());

                        ConeAOE coneAOE = new ConeAOE(owner.pos ,initDistance(),initalAngle,degrees(),Ballistica.WONT_STOP);
                        for(int i : coneAOE.cells)
                        {
                            GameScene.effectOverFog(new CheckedCell( i , owner.pos));
                        }
                        owner.busy();
                        owner.sprite.operate(cell);
                        owner.spendAndNext(1f);
                    }else {
                        GLog.i(Messages.get(ExusiaiSkill.class, "self_targeting"));
                    }
                }
                else {
                    if(doCheckCell(cell,owner)){
                        doShoot(owner,cell);
                    }
                }
            }
        }

        @Override
        public String prompt() {
            if(owner.buff(SniperSight.class)!=null)
            {
                return Messages.get(CellSelector.class, "prompt");
            }else return Messages.get(SniperRifle.class, "prompt");
        }
    };

    private int initDistance() {
        return owner.viewDistance;
    }

    private int degrees() {
        return 60;
    }

    @Override
    protected boolean doCheckCell(int cell, Hero owner) {
        boolean visibleFight = Dungeon.level.heroFOV[cell];
        if(!visibleFight)
        {
            GLog.i(Messages.get(SniperRifle.class, "out_of_sight"));
            return false;
        }

        if(cell == owner.pos){
            GLog.i(Messages.get(ExusiaiSkill.class, "self_targeting"));
            return false;
        }

        Char enemy = Char.findChar(cell);
        if(enemy==null||!(enemy.alignment == Char.Alignment.ENEMY)){
            GLog.i(Messages.get(SniperRifle.class, "no_targeting"));
            return false;
        }
        return true;
    }

    @Override
    protected void doShoot(Hero owner,Integer cell){

        owner.busy();

        int from = owner.pos;
        //Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.STOP_CHARS);
        //int result = ballistica.collisionPos;

        //((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(from, cell, ammoSprite() , new Callback() {
        //    @Override
        //    public void call() {
        //        doEnemyCheck(from,cell);
        //    }
        //});

        //Inspired by new blue archive pd from cocoa
        SniperRifle trigger = this;
        Dungeon.hero.sprite.operate(Dungeon.hero.pos, new Callback() {//FIXME need animation later
            @Override
            public void call() {
                Dungeon.hero.sprite.idle();
                CellEmitter.center(cell).burst(SnipeParticle.factory(trigger,cell,new Callback() {
                    @Override
                    public void call() {
                        doCheckAfterShooting(owner.pos,cell,1,false);  //턴을 소모하지 않음
                    }
                }), 1);
            }
        });
    }

    public void doShot(int cell){
        this.doEnemyCheck(Dungeon.hero.pos, cell);//gota fix it
    }

    @Override
    protected void doEnemyCheck(int from, int to){
        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];
        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            if(doHitCheck(from,to,enemy)){
                doDamageCalculation(from,to,enemy);
            }else {
                if (visibleFight) {
                    Splash.at( to, 0xCCFFC800, 1 );
                    String defense = enemy.defenseVerb();
                    enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
                    Sample.INSTANCE.play(Assets.SND_MISS);
                }
                //doCheckAfterShooting(1,false);
            }
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
            //doCheckAfterShooting(1,false);
        }
    }

    @Override
    protected void doDamageCalculation(int from, int to, Char enemy){

        //int unPassableCounts = 0;
        //Ballistica ballistica = new Ballistica(from, to, Ballistica.STOP_CHARS);
        //for(int t : ballistica.subPath(1, ballistica.dist - 1)){
        //    if(!Dungeon.level.solid[t]) unPassableCounts++;
        //}

        int damage = Random.Int(shootDamageMin(),shootDamageMax());
        int dr = enemy.drRoll();

        int effectiveDamage = enemy.defenseProc( enemy, damage );
        effectiveDamage = Math.max( effectiveDamage - dr, 0 );

        if ( enemy.buff( Vulnerable.class ) != null){
            effectiveDamage *= 1.33f;
        }

        if ( owner.buff(Weakness.class) != null ){
            effectiveDamage *= 0.67f;
        }

        if (!enemy.isAlive()){
            return;
        }

        enemy.damage( effectiveDamage, this );

        enemy.sprite.bloodBurstA( enemy.sprite.center(), effectiveDamage );
        enemy.sprite.flash();

        if (!enemy.isAlive()) {
            GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())) );
        }else{
            QuickSlotButton.lastTarget = enemy;
            SkillLoader.lastTarget = enemy;
        }

        doCheckAfterShooting(from, to,1,false);
    }

    @Override
    protected void doCheckAfterShooting(int from, int to,int cost,boolean useHeat){

        boolean forceCancel = false;

        if(useHeat){
            heat += cost;
            if(heat >= heatThreshold()){
                heat = heatThreshold();
                overHeat = true;
                Buff.affect( owner, Burning.class ).reignite( owner );
                GLog.w(Messages.get(this, "over_heat"));
                forceCancel = true;
            }
        }
        else {
            if (extraAmmo){
                extraAmmo = false;
                if(charge == 0) forceCancel = true;
            }else {
                charge -= cost;
                if(charge == 0) {
                    startReload();
                    forceCancel = true;
                }
            }
        }

        if(forceCancel && owner.buff(SniperSight.class)!= null){
            owner.buff(SniperSight.class).detach();
            GameScene.flash(0x88000000, false);
        }

        doTimeSpend(from, to, cost, useHeat);
    }

    @Override
    protected float shootTime(){
       return 2f;
    }

    @Override
    public void handleLongClick() {
        if(owner.buff(SniperSight.class)!= null){
            owner.buff(SniperSight.class).detach();
            owner.busy();
            owner.sprite.operate(owner.pos);
            GameScene.flash(0x88000000, false);
            owner.spendAndNext(1f);
        }
        else GameScene.show(new WndExusiaiSkill(this));
    }

    @Override
    public boolean useTargetting(){
        return owner.buff(SniperSight.class)!=null;
    }
}
