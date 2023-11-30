package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.watabou.noosa.Image;

public class AssaultRifle extends ExusiaiSkill{
    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.ASSAULT_RIFLE, 0, 0, 128, 64);
    }

    @Override
    public int shootDamageMin(){
        return (int) (owner.lvl/5f + Dungeon.depth/2f);
    }

    @Override
    public int shootDamageMax(){
        return (int)(owner.lvl/2.5f + Dungeon.depth);
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        return true;//this gun can equip attachment of each type
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if(attachment == this.getGUN_SIGHT()
                || attachment == this.getFRONT_HANG()
                || attachment == this.getBELOW_HANG()
                || attachment == this.getBACK_HANG()
                || attachment == this.getAMMO_BOX()
                || attachment == this.getBULLET()){
            return true;
        }
        return false;
    }

    @Override
    public GunType getType() {
        return GunType.ASSAULT_RIFLE;
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
        return 75f;
    }

    @Override
    public int getMaxCharge() {
        return 20;
    }
}
