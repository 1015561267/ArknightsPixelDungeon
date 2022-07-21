package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ComboTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.SwordRainTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.ChenSlash;
import com.unifier.arknightspixeldungeon.effects.Effects;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.Splash;
import com.unifier.arknightspixeldungeon.effects.particles.BloodParticle;
import com.unifier.arknightspixeldungeon.items.KindOfWeapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shadowless extends HeroSkill {

    private ArrayList<Image> images = new ArrayList<>();

    @Override
    public boolean activated() { return owner.hasTalent(Talent.SHADOWLESS); }

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.SHADOWLESS);
    }

    public String desc() {
        return Messages.get(this, "desc",range(),time());
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 20f;
    }

    @Override
    public int getMaxCharge() {
        if (owner.hasTalent(Talent.MOTION_ACCUMULATION)) {
            return 2;
        }
        return 1;
    }

    @Override
    public void doAction() {
        if(!available()){
            GLog.h(Messages.get(HeroSkill.class, "unavailable"));
            return;
        }

        ArrayList<Mob> targets =  new ArrayList<Mob>();

        KindOfWeapon curWep = Dungeon.hero.belongings.weapon;
        if (curWep == null) {
            GLog.i("You need a melee weapon to use this skill");
            return;
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(owner.pos,mob.pos) <= range() ) {
                targets.add( mob );
            }
        }


        if (!targets.isEmpty())
        {
            //GLog.i("Start");
            owner.busy();
            ((HeroSprite)owner.sprite).setSkillCallbackAnimation(
                    new Callback() {
                        @Override
                        public void call() {
                            owner.sprite.visible = false;
                            owner.sprite.idle();
                            doSlash(targets,0);
                        }
                    },HeroSprite.skillAnimationType.shadowless_start);
        }
        else {
            GLog.i("No enemy in sight");
        }
    }

    private void doSlash(ArrayList<Mob> targets, int t){
        Hero hero = Dungeon.hero;

        Mob mob=targets.get(Random.Int(targets.size()));

//        Emitter emitter = new Emitter();
//        emitter.autoKill = false;
//        emitter.pos(mob.sprite.center());
//        Group parent = Dungeon.hero.sprite.parent;
//        parent.add(emitter);
//        emitter.pour(BloodParticle.FACTORY, 0.3f);

//        mob.sprite.flash();
//        Random.Int(0,30)
        float angle = Random.Int(195,255);

        Image image = new Image(Effects.get(Effects.Type.CHEN_SLASH));
        image.origin.set(image.width / 2,image.height / 2);
        image.point(mob.sprite.center(image));
        image.angle = angle;


        ChenSlash.hit(mob,angle,new Callback() {
            @Override
            public void call() {

                hero.sprite.parent.add(image);
//                Game.scene().add(image);
                images.add(image);

                mob.damage(1, hero);
//                mob.damage(skillDamage(mob, false), hero);

                if (!mob.isAlive()){
                    GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", mob.name)) );
                    int exp = owner.lvl <= mob.maxLvl ? mob.EXP : 0;
                    if (exp > 0) {
                        owner.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
                        owner.earnExp(exp);
                    }
                    targets.remove(mob);
                }else{
                    if(hero.hasTalent(Talent.MORTAL_SKILL)){
                        Buff.affect(mob, ComboTracker.class).stack();
                    }
                }

                if(hero.hasTalent(Talent.SWORD_RAIN)){
                    Buff.affect(mob,SwordRainTracker.class).stack();
                }

                if(t>=time()-1 || targets.isEmpty()){

                    owner.sprite.visible=true;
                    ((HeroSprite)owner.sprite).setSkillCallbackAnimation(new Callback() {
                        @Override
                        public void call() {
                            ((HeroSprite) owner.sprite).setAfterSkillAnimation();

                            for (Image i : images) {
                                hero.sprite.parent.remove(i);
//                                Game.scene().remove(i);
                            }
                            images = new ArrayList<>();

                            doAfterAction();
                            owner.spendAndNext(1f);
                            if (owner.pointsInTalent(Talent.SWORD_RAIN) == 2 && t<10){
                                cooldown -=  rawCD() * 0.05f * (10 - t);
                            }

                        }
                    },HeroSprite.skillAnimationType.shadowless_over);
                }
                else { doSlash(targets,t+1); }
            }
        });
    }

    public int range(){
//        return 999;
        return 3 + 2 * Dungeon.hero.pointsInTalent(Talent.CRIMSON_EXTENSION);//3*3 at lvl 0,5*5 at lvl 1,7*7 at lvl 2
    }

    public int time(){
        int time = 10;
        if (Dungeon.hero.pointsInTalent(Talent.CRIMSON_EXTENSION) == 1) {
            time += 2;
        }
        if (Dungeon.hero.pointsInTalent(Talent.CRIMSON_EXTENSION) == 2) {
            time += 5;
        }
        return time;
//        return 999;
//        return 10 + 2 * Dungeon.hero.pointsInTalent(Talent.CRIMSON_EXTENSION) + Dungeon.hero.pointsInTalent(Talent.CRIMSON_EXTENSION) == 2 ? 0 : 1;//10 at lvl 0,12 at lvl 1,15 at lvl 2
    }

    public int skillDamage(Char enemy,boolean isMagic){

        int dmg = owner.rawdamageRoll(enemy,isMagic);
        float factor = 1;

        if(enemy.buff(SwordRainTracker.class)!=null){
            factor += 0.05f * enemy.buff(SwordRainTracker.class).stack;
        }

        if (owner.buff(Talent.SheathedStrikeTracker2.class) != null && owner.pointsInTalent(Talent.SHEATHED_STRIKE) == 2) {
            factor += 0.2;
        }

        if (cooldown < rawCD()) {
            factor += ((rawCD() - cooldown) / rawCD());
        }

        return (int)(dmg * factor);
    }

    @Override
    public void doAfterAction(){
        charge = 0;
        cooldown = rawCD();
    }

    @Override
    public String otherInfo() {
        String info;
        if (getMaxCharge() == charge) {
            info = Messages.get(this, "chargeinfo",getMaxCharge(),charge);
        } else {
            info = Messages.get(this, "cooldown", cooldown) + "\n\n" + Messages.get(this, "chargeinfo", getMaxCharge(), charge);
        }

        if (charge > 0 && cooldown < rawCD()) {
            info += "\n\n额外的充能将使你下一次绝影伤害提高" + "_" + (int)((rawCD() - cooldown) / rawCD() * 100) + "_" + "%";
        }
        return info;
    }
}
