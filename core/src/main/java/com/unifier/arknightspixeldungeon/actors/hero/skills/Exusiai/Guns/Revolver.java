package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.watabou.noosa.Image;

public class Revolver extends ExusiaiSkill {

    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.REVOLVER,0,0,128,64);
    }

    @Override
    public int shootDamageMin(){
        return (int) (1 + owner.lvl/5f + Dungeon.depth);
    }

    @Override
    public int shootDamageMax(){
        return (int)(owner.lvl/2.5f +  Dungeon.depth * 2);
    }

    @Override
    protected boolean checkAttachmentType(Attachment attachment) {
        if(attachment.attachType() == Attachment.AttachType.GUN_SIGHT || attachment.attachType() == Attachment.AttachType.BULLET ) return true;
        return false;
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if(attachment == this.getGUN_SIGHT() || attachment == this.getBULLET()){
            return true;
        }
        return false;
    }

    @Override
    public GunType getType() { return GunType.REVOLVER; }

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
        return 45f;
    }

    @Override
    public int getMaxCharge() {
        return 6;
    }

    protected Item ammoSprite(){
        return new Item(){
            {
                image = ItemSpriteSheet.SPIRIT_ARROW;
            }

            @Override
            public boolean isBulletForEffect(){return true;}
        };
    };
}
