package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class AssaultRifle extends ExusiaiSkill{

    public boolean burstFireMode = false;

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

    private static final String BURST_FIRE_MODE ="burst_fire_mode";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(BURST_FIRE_MODE, burstFireMode);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        burstFireMode = bundle.getBoolean(BURST_FIRE_MODE);
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

    public int burstNum(){
        if (burstFireMode){
            return Math.min( 3,charge);
        }else return 1;
    }

    public String otherInfo() {
        String otherInfo = super.otherInfo()+ "\n\n";
        otherInfo+= burstFireMode ? Messages.get(this, "burstfireon") : Messages.get(this, "burstfireoff");
        return otherInfo;
    }

    @Override
    protected Item ammoSprite(){
        return new Item(){
            {
                image = burstFireMode ? ItemSpriteSheet.THREE_BURST : ItemSpriteSheet.ONE_BURST;
            }
            @Override
            public boolean isBulletForEffect(){return true;}
        };
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

    protected void doEnemyCheck(int from, int to){

        int burstNum = burstNum();

        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];

        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            startBurst( burstNum , from , to , enemy );
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
            doCheckAfterShooting(burstNum,false);
        }
    }

    private static final String AC_SWITCH		= "SWITCH";
    //just work like item actions
    public ArrayList<String> windowActions() {
        ArrayList<String> actions = super.windowActions();
        actions.add(AC_SWITCH);
        return actions;
    }

    public void excuteActions(String action) {
        super.excuteActions(action );

        if (action.equals(AC_SWITCH)) {
            burstFireMode = !burstFireMode;
            GLog.i(Messages.get(this, "switch"));
            Dungeon.hero.sprite.operate(Dungeon.hero.pos);
        }
        return;
    }
}
