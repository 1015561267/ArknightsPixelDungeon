package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.ExusiaiSkill;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public enum Attachment {
    //FIXME type judgement should be unnecessary as window should filte already,just in case
    RED_DOT_SIGHT(1,AttachType.GUN_SIGHT){
        @Override
        public Status condition(Hero hero, ExusiaiSkill skill){
            if(skill.getType() == ExusiaiSkill.GunType.REVOLVER || skill.getType() == ExusiaiSkill.GunType.VECTOR || skill.getType() == ExusiaiSkill.GunType.SNIPER_RIFLE){

            }
            return super.condition(hero,skill);
        }
    },
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

    public void doAttach(ExusiaiSkill exusiaiSkill) { }//almost same,involve in som

    public enum AttachType{
        GUN_SIGHT,
        FRONT_HANG,
        BELOW_HANG,
        BACK_HANG,
        AMMO_BOX,
        BULLET
    }

    public enum Status{
        using,
        available,
        locked_by_using,
        locked_by_talent,
        locked_by_generic,
        locked_by_pick,
        wrong_exist
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

    public Image icon(){
        Image image;
        int n = icon - 1;
        int row = n / 5;
        int col = n % 5;
        image = new Image(Assets.GUN_ATTACHMENTS, 32 * col , 32 * row, 32, 32);
        image.scale.set(0.5f,0.5f);
        return image;
    }

    public AttachType attachType(){return attachType;}

    public Boolean generic(){return generic;}

    public Status condition(Hero hero,ExusiaiSkill skill){

        if(skill == hero.skill_1){

        }
        else if(skill == hero.skill_2){


        }else if(skill == hero.skill_3){


        }

        return Status.available;}
        //return Status.wrong_exist;}

    public static ArrayList<Attachment> getAttachmentList(AttachType attachType){
        //in this function we return a list of all possible attachment for a selected skill(gun type)in a particular attachment slot.
        //it's necessary for changing attachment,as some gun may have generic attachment
        //this is a raw list,can be processed further,like judge which you use now / can use / locked by which condition
        ArrayList<Attachment> result = new ArrayList<>();

        switch (attachType){
            case GUN_SIGHT:
                result.add(RED_DOT_SIGHT);
                result.add(CLOSE_COMBAT_OPTICAL_SIGHT);
                result.add(THERMAL_IMAGING_SIGHT);
                result.add(MEDIUM_RANGE_SIGHT);
                result.add(LONG_RANGE_SNIPERSCOPE);
                result.add(LASER_RANGE_FINDER);
                result.add(HIGH_TECH_SIGHT);
                break;
            case FRONT_HANG:
                result.add(LENGTHENED_BARREL);
                result.add(SAW_OFF);
                result.add(TACTICAL_BAYONET);
                result.add(MUZZLE_COMPENSATOR);
                result.add(SILENCER);
                result.add(PHOENIX_MELTA);
                break;
            case BELOW_HANG:
                result.add(TACTICAL_LASER);
                result.add(TACTICAL_GRIP);
                result.add(TACTICAL_FLASHLIGHT);
                result.add(MOUNTED_GRENADE);
                result.add(ORIGINUMS_DUST_BOTTLE);
                result.add(ADDITIONAL_BATTERY_PACK);
                break;
            case BACK_HANG:
                result.add(BULLET_BELT);
                result.add(LIGHTWEIGHTED_GUNSTOCK);
                result.add(CLOSE_COMBAT_GUNSTOCK);
                result.add(CARTRIDGE_CASE_RETRIEVER);
                result.add(STIMPACK_MODULE);
                result.add(COOLING_OPTIMIZATION_MODULE);
                break;
            case AMMO_BOX:
                result.add(HIGH_CAPACITY_MAGAZINE);
                result.add(QUICK_PULL_MAGAZINE);
                result.add(SPRING_ACCELERATING_CLIP);
                result.add(ORIGINUMS_REFINING_CLIP);
                result.add(HIGH_POWERED_BATTERY);
                result.add(STABILIZING_CAPACITOR);
                break;
            case BULLET:
                result.add(ARMOR_PIERCING_BULLET);
                result.add(HOLLOW_POINT_BULLET);
                result.add(LIGHT_TRACER_BULLET);
                result.add(ELECTROMAGNETIC_INTERFERENCE_BULLET);
                result.add(BLACKJACK_SOLITAIRE);
                result.add(FARADIC_PLASMA);
                break;
            default:
        }

        return result;
    }

    public void storeInBundle( Bundle bundle ) {

    }
    public void restoreFromBundle( Bundle bundle ) {

    }

}

