package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class Vector extends ExusiaiSkill {

    //FIXME Well it's a temporary variant for handling visual,may be changed later

    static int burstTemp;

    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.VECTOR,0,0,128,64);
    }

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
        if(attachment.attachType() == Attachment.AttachType.GUN_SIGHT
                || attachment.attachType() == Attachment.AttachType.FRONT_HANG
                || attachment.attachType() == Attachment.AttachType.BELOW_HANG
                || attachment.attachType() == Attachment.AttachType.AMMO_BOX ){
            return true;
        }
        return false;
    }

    @Override
    public boolean equippingAttachment(Attachment attachment) {
        if(attachment == this.getGUN_SIGHT() || attachment == this.getAMMO_BOX() || attachment == this.getFRONT_HANG() || attachment== this.getBELOW_HANG()){
            return true;
        }
        return false;
    }

    @Override
    public GunType getType() {
        return GunType.VECTOR;
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
        return 30f;
    }

    @Override
    public int getMaxCharge() {
        return 13;
    }

    public int burstNum(){
        return Math.min(Random.IntRange(3,5),charge);
    }

    protected Item ammoSprite(){
        return new Item(){
            {
                switch (burstTemp){
                    case 3:image = ItemSpriteSheet.ONE_BURST;break;
                    case 4:image = ItemSpriteSheet.TWO_BURST;break;
                    case 5:image = ItemSpriteSheet.THREE_BURST;break;
                    default:image = ItemSpriteSheet.THREE_BURST;break;
                }
            }

            @Override
            public boolean isBulletForEffect(){return true;}
        };
    }

    protected void doShoot(Hero owner,Integer cell){
        burstTemp = burstNum();
        super.doShoot(owner,cell);
    }

    protected void doEnemyCheck(int from, int to){
        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];

        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            startBurst( burstTemp , from , to , enemy );
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
            doCheckAfterShooting(burstTemp,false);
        }
    }
}
