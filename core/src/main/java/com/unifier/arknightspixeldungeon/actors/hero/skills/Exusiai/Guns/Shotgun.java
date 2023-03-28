package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.mechanics.ConeAOE;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.ui.SkillLoader;
import com.watabou.noosa.Image;

public class Shotgun extends ExusiaiSkill {
    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.REVOLVER,0,0,128,64);
    }

    @Override
    public int shootDamageMin() {
        return 0;
    }

    @Override
    public int shootDamageMax() {
        return 0;
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

                        Ballistica ballistica = new Ballistica(owner.pos,cell,Ballistica.PROJECTILE);
                        ConeAOE coneAOE = new ConeAOE(ballistica,initDistance(),degrees(), Ballistica.PROJECTILE);


                    }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SniperRifle.class, "prompt");
        }
    };

    private int initDistance() {
        return 3;
    }

    private int degrees() {
        return 45;
    }

    private int bullets() {
        return 7;
    }
}
