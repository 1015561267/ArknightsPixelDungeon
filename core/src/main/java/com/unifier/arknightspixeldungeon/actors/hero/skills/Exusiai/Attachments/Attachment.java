package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments;

import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.ExusiaiSkill;
import com.unifier.arknightspixeldungeon.messages.Messages;

public enum Attachment {

    RED_DOT_SIGHT(1,AttachType.GUN_SIGHT),
    CLOSE_COMBAT_OPTICAL_SIGHT(2,AttachType.GUN_SIGHT),
    THERMAL_IMAGING_SIGHT(3,AttachType.GUN_SIGHT),
    MEDIUM_RANGE_SIGHT(4,AttachType.GUN_SIGHT),
    LONG_RANGE_SNIPERSCOPE(5,AttachType.GUN_SIGHT),
    LASER_RANGE_FINDER(6,AttachType.GUN_SIGHT),
    HIGH_TECH_SIGHT   (7,AttachType.GUN_SIGHT),


    LENGTHENED_BARREL(11, AttachType.FRONT_HANG),
    SAW_OFF(12, AttachType.FRONT_HANG),
    TACTICAL_BAYONET(13, AttachType.FRONT_HANG),
    MUZZLE_COMPENSATOR(14, AttachType.FRONT_HANG),
    SILENCER(15, AttachType.FRONT_HANG),
    PHOENIX_MELTA(16, AttachType.FRONT_HANG),

    TACTICAL_LASER(21, AttachType.BELOW_HANG),
    TACTICAL_GRIP(22, AttachType.BELOW_HANG),
    TACTICAL_FLASHLIGHT(23, AttachType.BELOW_HANG),
    MOUNTED_GRENADE(24, AttachType.BELOW_HANG),
    ORIGINUMS_DUST_BOTTLE(25, AttachType.BELOW_HANG),
    ADDITIONAL_BATTERY_PACK(27, AttachType.BELOW_HANG),

    BULLET_BELT(31, AttachType.BACK_HANG),
    LIGHTWEIGHTED_GUNSTOCK(32, AttachType.BACK_HANG),
    CLOSE_COMBAT_GUNSTOCK(33, AttachType.BACK_HANG),
    CARTRIDGE_CASE_RETRIEVER(34, AttachType.BACK_HANG),
    STIMPACK_MODULE(35, AttachType.BACK_HANG),
    COOLING_OPTIMIZATION_MODULE(36, AttachType.BACK_HANG),

    HIGH_CAPACITY_MAGAZINE(41, AttachType.AMMO_BOX),
    QUICK_PULL_MAGAZINE(42, AttachType.AMMO_BOX),
    SPRING_ACCELERATING_CLIP(43, AttachType.AMMO_BOX),
    ORIGINUMS_REFINING_CLIP(44, AttachType.AMMO_BOX),
    HIGH_POWERED_BATTERY(45, AttachType.AMMO_BOX),
    STABILIZING_CAPACITOR(46, AttachType.AMMO_BOX),

    ARMOR_PIERCING_BULLET(51, AttachType.BULLET),
    HOLLOW_POINT_BULLET(52, AttachType.BULLET),
    LIGHT_TRACER_BULLET(53, AttachType.BULLET),
    ELECTROMAGNETIC_INTERFERENCE_BULLET(54, AttachType.BULLET),
    BLACKJACK_SOLITAIRE(55, AttachType.BULLET),
    FARADIC_PLASMA(59, AttachType.BULLET);

    public void doDetach(ExusiaiSkill exusiaiSkill) { }//it involved in some necessary handle,or just do some penalty to avoid repeatedly switching

    public void doAttach(ExusiaiSkill exusiaiSkill) { }

    public enum AttachType{
        GUN_SIGHT,
        FRONT_HANG,
        BELOW_HANG,
        BACK_HANG,
        AMMO_BOX,
        BULLET
    }

    int icon;
    AttachType attachType;
    Boolean generic;

    Attachment(int icon,AttachType attachType){
        this(icon,attachType,false);
    }

    Attachment(int icon,AttachType attachType,boolean isGeneric){
        this.icon = icon;
        this.attachType = attachType;
        this.generic = isGeneric;
    }

    public String title(){
        //TODO translate this
        return Messages.get(this, name() + ".title");
    }

    public String desc(){
        return Messages.get(this, name() + ".desc");
    }

    public int icon(){return icon;}

    public AttachType attachType(){return attachType;}

    public Boolean generic(){return generic;}

}
