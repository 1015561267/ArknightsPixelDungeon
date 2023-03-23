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
import com.unifier.arknightspixeldungeon.effects.CheckedCell;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.mechanics.ConeAOE;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.QuickSlotButton;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.ui.SkillLoader;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndExusiaiSkill;
import com.watabou.noosa.Image;
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
        int dmg = 3 + owner.lvl/3 + Dungeon.depth;
        return Math.max(0, dmg);
    }

    @Override
    public int shootDamageMax() {
        int dmg = 8 + owner.lvl + Dungeon.depth * 3;
        return Math.max(0, dmg);
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
        return 90f;
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
                if(owner.buff(SniperSight .class)==null){

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

    protected boolean doCheckCell(int cell, Hero owner) {

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.STOP_CHARS);
        int result = ballistica.collisionPos;

        if(result == owner.pos){
            GLog.i(Messages.get(ExusiaiSkill.class, "self_targeting"));
            return false;
        }

        return true;
    }

    protected void doShoot(Hero owner,Integer cell){
        int from = owner.pos;

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.STOP_CHARS);
        int result = ballistica.collisionPos;

        ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(from, result, ammoSprite() , new Callback() {
            @Override
            public void call() {
                doEnemyCheck(from,result);
            }
        });
    }

    protected void doDamageCalculation(int from, int to, Char enemy){

        int unPassableCounts = 0;
        Ballistica ballistica = new Ballistica(from, to, Ballistica.STOP_CHARS);
        for(int t : ballistica.subPath(1, ballistica.dist - 1)){
            if(!Dungeon.level.solid[t]) unPassableCounts++;
        }

        int damage = (int) (Random.Int(shootDamageMin(),shootDamageMax()) * (Math.max(0.5f,1 - 0.1f * unPassableCounts)));
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
        }else {
            QuickSlotButton.lastTarget = enemy;
            SkillLoader.lastTarget = enemy;
        }

        doCheckAfterShooting(1,false);
    }

    @Override
    protected void doCheckAfterShooting(int cost,boolean useHeat){

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

        doTimeSpend();
    }

    @Override
    protected void doTimeSpend(){
        owner.spendAndNext(2f);
    }

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

    public boolean useTargetting(){
        return owner.buff(SniperSight.class)!=null;
    }
}
