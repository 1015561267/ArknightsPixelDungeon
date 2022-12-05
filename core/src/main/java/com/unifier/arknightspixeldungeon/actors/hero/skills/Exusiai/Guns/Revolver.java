package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.watabou.noosa.Image;

public class Revolver extends ExusiaiSkill {

    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.REVOLVER,0,0,128,64);
    }

    @Override
    public boolean activated() {
        return false;
    }

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.NONE);
    }

    @Override
    public float CD(Hero hero) {
        return 0;
    }

    @Override
    public float rawCD() {
        return 45f;
    }

    @Override
    public int getMaxCharge() {
        return 6;
    }

    @Override
    protected boolean doCheckCell(Integer cell, Hero owner) {
        return false;
    }

    public int shootDamageMin(){
        int dmg = (int) (1 + owner.lvl/5 + Dungeon.depth);
        return Math.max(0, dmg);
    }

    public int shootDamageMax(){
        int dmg = 6 + (int)(Dungeon.hero.lvl/2.5f +  Dungeon.depth * 2);
        return Math.max(0, dmg);
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        if(attachment.attachType() == Attachment.AttachType.GUN_SIGHT || attachment.attachType() == Attachment.AttachType.BULLET ) return true;
        return false;
    }
}
