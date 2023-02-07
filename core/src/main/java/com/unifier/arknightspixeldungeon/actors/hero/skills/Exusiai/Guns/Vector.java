package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Doom;
import com.unifier.arknightspixeldungeon.actors.buffs.Frost;
import com.unifier.arknightspixeldungeon.actors.buffs.MagicalSleep;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Vector extends ExusiaiSkill {

    //FIXME Well it's a temporary variant for handling,may be changed later

    static int burstTemp;

    @Override
    public Image bluePrintPicture() {
        return new Image(Assets.VECTOR,0,0,128,64);
    }

    public int shootDamageMin(){
        int dmg = (int) (1 + owner.lvl/5f + Dungeon.depth);
        return Math.max(0, dmg);
    }

    public int shootDamageMax(){
        int dmg = 3 + (int)(owner.lvl/5f +  Dungeon.depth);
        return Math.max(0, dmg);
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
    };

    protected void doShoot(Hero owner,Integer cell){
        int from = owner.pos;
        int to = cell;

        Ballistica ballistica = new Ballistica(owner.pos, cell, Ballistica.PROJECTILE);
        int result = ballistica.collisionPos;

        burstTemp = burstNum();

        ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(from, result, ammoSprite() , new Callback() {
            @Override
            public void call() {
                doEnemyCheck(from,result);
            }
        });
    }

    protected void doEnemyCheck(int from, int to){
        Char enemy = Char.findChar(to);
        boolean visibleFight = Dungeon.level.heroFOV[to];

        int burst = burstTemp;

        if(enemy != null && enemy.alignment == Char.Alignment.ENEMY){
            startBurst( burst , from , to , enemy );
        }else {
            if (visibleFight) {
                Splash.at(to, 0xCCFFC800, 1);
            }
            doCheckAfterShooting(burst,false);
        }
    }

    private void startBurst(int burst, int from, int to, Char enemy) {

        boolean visibleFight = Dungeon.level.heroFOV[to];

        ArrayList<Boolean> burstArray = new ArrayList<>();

        String defense = enemy.defenseVerb();

        Boolean everHitted = false;

        while (burst>0){

            if(doHitCheck(from,to,enemy)){
                everHitted = true;
                burstArray.add(true);
            }else {
                //enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
                burstArray.add(false);
            }
            burst --;
        }

        if(everHitted){
            doDamageCalculation(from,to,enemy,burstArray);

            if (enemy.buff(Frost.class) != null){
                Buff.detach( enemy, Frost.class );
            }
            if (enemy.buff(MagicalSleep.class) != null){
                Buff.detach(enemy, MagicalSleep.class);
            }

        }else {
            Splash.at( to, 0xCCFFC800, 1 );
            Sample.INSTANCE.play(Assets.SND_MISS);
            enemy.sprite.showStatus( CharSprite.NEUTRAL,defense);
        }
    }

    protected void doDamageCalculation(int from, int to, Char enemy, ArrayList<Boolean> burstArray){

        ArrayList<Integer> damageArray = new ArrayList<>();

        //String verbArray = "";

        int i=0;

        for(Boolean record : burstArray){

            if(record){
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

                damageArray.add(effectiveDamage);
            }
            else {
                damageArray.add(Integer.MIN_VALUE);
            }
        }

        damageArray = enemy.multipleDefenseProc(owner,damageArray);

        Integer totalDamage = 0;

        boolean visibleFight = Dungeon.level.heroFOV[owner.pos] || Dungeon.level.heroFOV[enemy.pos];

        for(Integer record : damageArray){
            i++;
            //GLog.i(record.toString());
            if (!enemy.isAlive()){
                break;
            }else {
                if(record == Integer.MIN_VALUE){
                    if (visibleFight) {
                        //verbArray += enemy.defenseVerb();
                        String defense = enemy.defenseVerb();
                        enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                    }
                }else {
                    if (record < 0) {
                        continue;
                    }

                    if(enemy.isInvulnerable(this.getClass())){
                        enemy.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
                        break;
                    }

                    if (enemy.buff(Doom.class) != null){
                        record *= 2;
                    }

                    Class<?> srcClass = enemy.getClass();
                    if (enemy.isImmune( srcClass )) {
                        record = 0;
                    } else {
                        record = Math.round( record * enemy.resist( srcClass ));
                    }

                    if (enemy.buff( Paralysis.class ) != null) {
                        enemy.buff( Paralysis.class ).processDamage(record);
                    }
                    if (enemy.SHLD == 0){
                        enemy.HP -= record;
                    }
                    if (enemy.SHLD >= record){
                        enemy.SHLD -= record;
                    } else if (enemy.SHLD > 0) {
                        enemy.HP -= (record - enemy.SHLD);
                        enemy.SHLD = 0;
                    }

                    if(enemy instanceof Hero) {
                        Talent.onHealthLose((Hero) enemy, this, Math.min(record, enemy.HP));
                    }

                    if (enemy.HP < 0) enemy.HP = 0;
                }

                //in order to simplify the process of doing multiple times damage,this is only a demo.should be move to char.java instead of vector special usage

                if (visibleFight) {
                    enemy.sprite.showStatus(enemy.HP > enemy.HT / 2 ? CharSprite.WARNING : CharSprite.NEGATIVE,record.toString());
                }
            }
        }

        if(totalDamage>0) {
            enemy.sprite.bloodBurstA(enemy.sprite.center(), totalDamage);
            enemy.sprite.flash();
        }

        if (!enemy.isAlive()) {
            enemy.die( this );
            GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())) );

            if (visibleFight) {
                while (i < damageArray.size()) {
                    if (damageArray.get(i) == Integer.MIN_VALUE) {
                        if (visibleFight) {
                            //verbArray += enemy.defenseVerb();
                            String defense = enemy.defenseVerb();
                            enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                        }
                    } else {
                        enemy.sprite.showStatus(enemy.HP > enemy.HT / 2 ? CharSprite.WARNING : CharSprite.NEGATIVE, damageArray.get(i).toString());
                    }
                    i++;
                }
            }
        }

        doCheckAfterShooting(burstArray.size(),false);
    }

}
