package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Bless;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Hex;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.QuickSlotButton;
import com.unifier.arknightspixeldungeon.ui.SkillLoader;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndExusiaiSkill;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class ExusiaiSkill extends HeroSkill {

    Attachment GUN_SIGHT = Attachment.NULL_ATTACHMENT;
    Attachment FRONT_HANG= Attachment.NULL_ATTACHMENT;
    Attachment BELOW_HANG= Attachment.NULL_ATTACHMENT;
    Attachment BACK_HANG = Attachment.NULL_ATTACHMENT;
    Attachment AMMO_BOX  = Attachment.NULL_ATTACHMENT;
    Attachment BULLET    = Attachment.NULL_ATTACHMENT;

    public void setGUN_SIGHT(Attachment GUN_SIGHT) {
        this.GUN_SIGHT = GUN_SIGHT;
    }

    public void setFRONT_HANG(Attachment FRONT_HANG) {
        this.FRONT_HANG = FRONT_HANG;
    }

    public void setBELOW_HANG(Attachment BELOW_HANG) {
        this.BELOW_HANG = BELOW_HANG;
    }

    public void setBACK_HANG(Attachment BACK_HANG) {
        this.BACK_HANG = BACK_HANG;
    }

    public void setAMMO_BOX(Attachment AMMO_BOX) {
        this.AMMO_BOX = AMMO_BOX;
    }

    public void setBULLET(Attachment BULLET) {
        this.BULLET = BULLET;
    }

    public Attachment getGUN_SIGHT(){ return GUN_SIGHT; }
    public Attachment getFRONT_HANG(){ return FRONT_HANG; }
    public Attachment getBELOW_HANG(){ return BELOW_HANG; }
    public Attachment getBACK_HANG(){ return BACK_HANG; }
    public Attachment getAMMO_BOX(){ return AMMO_BOX; }
    public Attachment getBULLET(){ return BULLET; }

    public static Char lastTarget = null;
    //just like quickSlotButton,make it

    public float heat;
    public boolean overHeat;
    public boolean extraAmmo;

    {
        heat = 0;
        extraAmmo = false;
    }

    public boolean useTargetting(){return true;}

    public void handleLongClick() {
        GameScene.show(new WndExusiaiSkill(this));
    }

    public boolean useHeat(){
        if(getAMMO_BOX()!= null){
            return getAMMO_BOX() == Attachment.HIGH_POWERED_BATTERY;
        }
        return false;
    }

    @Override
    public boolean act() {
        spend(TICK);

        if(useHeat()){
            decreaseHeat(TICK);
        }

        else {
            if(charge == 0){
                getCoolDown(TICK);
            }
        }

        return true;
    }

    protected void increaseHeat(float amount){
        heat += amount;
    };

    protected void decreaseHeat(float amount){
        heat -= amount;
    };

    public void getCoolDown(float amount){

        if(charge == 0){
            cooldown -= amount;
            if(cooldown <= 0){
                charge = getMaxCharge();
                cooldown = 0;//before run out of ammo,skill would never cool down,unless use a HIGH_POWERED_BATTERY,see startReload() to get more info;
            }
        }

    }

    @Override
    public boolean activated() {
        if(useHeat()) return !overHeat;
        else return charge > 0;
    }

    public boolean available(){
        if(useHeat()){
            if(overHeat){
                GLog.h(Messages.get(this, "overheat"));
                return false;
            }
        }
        else {
            if(!extraAmmo && charge == 0){
                GLog.h(Messages.get(this, "noammo"));
                return false;
            }
        }
        return true;
    }

    public void doAction() {
        if (!available()) {
            return;
        }
        if(doCheckBeforeAiming()){
            SkillLoader.targetting = true;
            SkillLoader.useTargeting();
            GameScene.selectCell(aimer);
        }
    }

    protected boolean doCheckBeforeAiming(){
        return true;
    };


    public void doAfterAction() {


    }

    protected final CellSelector.Listener aimer = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;

            if(doCheckCell(cell,owner)){
                doShoot(owner,cell);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(CellSelector.class, "prompt");
        }
    };

    protected boolean doCheckCell(int cell, Hero owner) {

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.PROJECTILE);
        int result = ballistica.collisionPos;

        if(result == owner.pos){
            GLog.i(Messages.get(ExusiaiSkill.class, "self_targeting"));
            return false;
        }

        return true;
    }

    protected void doShoot(Hero owner,Integer cell){
        int from = owner.pos;
        int to = cell;

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.PROJECTILE);
        int result = ballistica.collisionPos;

        ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(from, result, ammoSprite() , new Callback() {
            @Override
            public void call() {
                doEnemyCheck(from,result);
            }
        });
    }


    protected Item ammoSprite(){
        return new Item(){
            {
                image = ItemSpriteSheet.SPIRIT_ARROW;
            }

            @Override
            public boolean isBulletForEffect(){return true;}
        };
    }

    protected void doEnemyCheck(int from, int to){
        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];
        GLog.i("1");
        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            if(doHitCheck(from,to,enemy)){
                GLog.i("2-1");
                doDamageCalculation(from,to,enemy);
                GLog.i("2-2");
            }else {
                if (visibleFight) {
                    GLog.i("3-1");
                    Splash.at( to, 0xCCFFC800, 1 );
                    String defense = enemy.defenseVerb();
                    enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
                    Sample.INSTANCE.play(Assets.SND_MISS);
                }
                GLog.i("4-1");
                doCheckAfterShooting(1,false);
                GLog.i("4-2");
            }
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
            GLog.i("5-1");
            doCheckAfterShooting(1,false);
            GLog.i("5-2");
        }
    }

    protected float gunAccuracyModifier(int from, int to, Char enemy){return 0f;}//basiclly there are no accuracy change,details are written in extended class

    protected float attachmentAccuracyModifier(int from, int to, Char enemy){
        float result = 0f;
        return result;}//FIXME most accuracy modifier attachment will take affect at here,but the most important thing is get the whole process done first.

    protected boolean doHitCheck(int from, int to,Char enemy){

        int basicAccuracy = owner.getAttackSkill();

        float gunAccuracyModifier = this.gunAccuracyModifier(from,to,enemy);

        float attachmentAccuracyModifier = this.attachmentAccuracyModifier(from,to,enemy);

        basicAccuracy = (int) (basicAccuracy + (gunAccuracyModifier * basicAccuracy) + (attachmentAccuracyModifier * basicAccuracy));

        float acuRoll = Random.Float( basicAccuracy );
        float defRoll = Random.Float( enemy.defenseSkill( owner ) );

        if (owner.buff(Bless.class) != null) acuRoll *= 1.25f;
        if (owner.buff(Hex.class) != null) acuRoll *= 0.8f;

        if (enemy.buff(Bless.class) != null) defRoll *= 1.25f;
        if (enemy.buff(Hex.class) != null) defRoll *= 0.8f;

        GLog.i(owner.getAttackSkill()+" "+basicAccuracy+" "+acuRoll+" "+defRoll);

       return acuRoll >= defRoll;
    }

    protected void doDamageCalculation(int from, int to, Char enemy){

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
        }else {
            QuickSlotButton.lastTarget = enemy;
            SkillLoader.lastTarget = enemy;
        }

        doCheckAfterShooting(1,false);
    }

    protected void doCheckAfterShooting(int cost,boolean useHeat){
        if(useHeat){
            heat += cost;
            if(heat >= heatThreshold()){
                heat = heatThreshold();
                overHeat = true;
                Buff.affect( owner, Burning.class ).reignite( owner );
                GLog.w(Messages.get(this, "over_heat"));
            }
        }
        else {
            if (extraAmmo){
                extraAmmo = false;
            }else {
                charge -= cost;
                if(charge == 0) startReload();
            }
        }

        doTimeSpend();
    }

    protected void startReload(){

        if(useHeat()) return;//should never use heat to get this function run

        charge = 0;
        cooldown = getReloadTime();

        if(cooldown == 0) {
            charge = getMaxCharge();
        }
    }

    protected float getReloadTime(){
        return rawCD();
    }

    protected void doTimeSpend(){
        owner.spendAndNext(1f);
    }

    public float heatThreshold(){
        return 100;
    }


    private static final String HEAT = "heat";
    private static final String OVERHEAT = "overHeat";
    private static final String EXTRAAMMO = "extraAmmo";

    private static final String ATTACHMENT_GUN_SIGHT ="attachment_gun_sight";
    private static final String ATTACHMENT_FRONT_HANG ="attachment_front_hang";
    private static final String ATTACHMENT_BELOW_HANG ="attachment_below_hang";
    private static final String ATTACHMENT_BACK_HANG ="attachment_back_hang";
    private static final String ATTACHMENT_AMMO_BOX ="attachment_ammo_box";
    private static final String ATTACHMENT_BULLET ="attachment_bullet";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( HEAT, heat );
        bundle.put( OVERHEAT, overHeat );
        bundle.put( EXTRAAMMO, extraAmmo );

        bundle.put(ATTACHMENT_GUN_SIGHT, GUN_SIGHT);
        bundle.put(ATTACHMENT_FRONT_HANG, FRONT_HANG);
        bundle.put(ATTACHMENT_BELOW_HANG,BELOW_HANG);
        bundle.put(ATTACHMENT_BACK_HANG,BACK_HANG);
        bundle.put(ATTACHMENT_AMMO_BOX,AMMO_BOX);
        bundle.put(ATTACHMENT_BULLET, BULLET);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);

        heat = bundle.getInt( HEAT );
        overHeat = bundle.getBoolean(OVERHEAT);
        extraAmmo = bundle.getBoolean(EXTRAAMMO);

        GUN_SIGHT =  bundle.getEnum(ATTACHMENT_GUN_SIGHT,Attachment.class);
        FRONT_HANG = bundle.getEnum(ATTACHMENT_FRONT_HANG,Attachment.class);
        BELOW_HANG = bundle.getEnum(ATTACHMENT_BELOW_HANG,Attachment.class);
        BACK_HANG = bundle.getEnum(ATTACHMENT_BACK_HANG,Attachment.class);
        AMMO_BOX = bundle.getEnum(ATTACHMENT_AMMO_BOX,Attachment.class);
        BULLET = bundle.getEnum(ATTACHMENT_BULLET,Attachment.class);
}

    public abstract Image bluePrintPicture();

    //if not marked,damage here represent single bullet's damage,many gun may used things based on this
    public abstract int shootDamageMin();

    public abstract int shootDamageMax();

    public void switchAttachment(Attachment attachment, Attachment.Status condition){

        if(!checkAttachmentType(attachment)){
            return;
        }

        else if(condition == Attachment.Status.using)
        {
            doDetach(attachment);
        }

        else {
            if(condition == Attachment.Status.locked_by_using){
                allDetach(attachment);
            }

            Attachment replaced = Attachment.NULL_ATTACHMENT;
            switch (attachment.attachType()){
                case GUN_SIGHT:replaced = getGUN_SIGHT();break;
                case FRONT_HANG:replaced = getFRONT_HANG();break;
                case BELOW_HANG:replaced = getBELOW_HANG();break;
                case BACK_HANG:replaced = getBACK_HANG();break;
                case AMMO_BOX:replaced = getAMMO_BOX();break;
                case BULLET:replaced = getBULLET();break;
            }

            if(replaced != Attachment.NULL_ATTACHMENT){
                doDetach(replaced);
            }

            doAttach(attachment);
        }
    }

    public void doDetach(Attachment attachment) {
        switch (attachment.attachType()){
            case GUN_SIGHT: GUN_SIGHT = Attachment.NULL_ATTACHMENT;break;
            case FRONT_HANG:FRONT_HANG = Attachment.NULL_ATTACHMENT;break;
            case BELOW_HANG:BELOW_HANG = Attachment.NULL_ATTACHMENT;break;
            case BACK_HANG:BACK_HANG = Attachment.NULL_ATTACHMENT;break;
            case AMMO_BOX:AMMO_BOX = Attachment.NULL_ATTACHMENT;break;
            case BULLET:BULLET = Attachment.NULL_ATTACHMENT;break;
        }
    }

    public void doAttach(Attachment attachment) {
        switch (attachment.attachType()){
            case GUN_SIGHT: GUN_SIGHT = attachment;break;
            case FRONT_HANG:FRONT_HANG = attachment;break;
            case BELOW_HANG:BELOW_HANG = attachment;break;
            case BACK_HANG:BACK_HANG = attachment;break;
            case AMMO_BOX:AMMO_BOX = attachment;break;
            case BULLET:BULLET = attachment;break;
        }
    }

    private static void allDetach(Attachment replaced) {
        for(ExusiaiSkill temp:getSkillList()){
            if (temp.equippingAttachment(replaced)){
                temp.doDetach(replaced);
            }
        }
    }

    protected abstract boolean checkAttachmentType(Attachment attachment);

    public ExusiaiSkill getOccupied(Attachment attachment) {

        for(ExusiaiSkill temp:getSkillList()){
            //GLog.i(temp.name());
            if(this != temp && temp.equippingAttachment(attachment)){
                return temp;
            }
        }

        return null;
    }

    public abstract boolean equippingAttachment(Attachment attachment);

    public boolean canUse(Attachment attachment) {
        return true;
    }

    public enum GunType{
        ASSAULT_RIFLE,GRENADE_LAUNCHER,HEAVY_MACHINE_GUN,PLASMA_CANNON,REVOLVER,SHOTGUN,SNIPER_RIFLE,VECTOR
    }//looks stupid,but it could be more stupid if I use if-instanceof-elseif in attachment judgement

    public abstract GunType getType();

    public static ArrayList<ExusiaiSkill> getSkillList(){

        ArrayList<ExusiaiSkill> arrayList = new ArrayList<>();

        if(Dungeon.hero.heroClass == HeroClass.HUNTRESS){
            if(Dungeon.hero.skill_1!=null){
                arrayList.add((ExusiaiSkill) Dungeon.hero.skill_1);
            }
            if(Dungeon.hero.skill_2!=null){
                arrayList.add((ExusiaiSkill) Dungeon.hero.skill_2);
            }
            if(Dungeon.hero.skill_3!=null){
                arrayList.add((ExusiaiSkill) Dungeon.hero.skill_3);
            }
        }

        return arrayList;
    }

}
