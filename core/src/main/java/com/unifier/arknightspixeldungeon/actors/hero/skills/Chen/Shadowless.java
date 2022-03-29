package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.ChenSlash;
import com.unifier.arknightspixeldungeon.items.KindOfWeapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shadowless extends HeroSkill {

    @Override
    public boolean activated() { return Dungeon.hero.hasTalent(Talent.SHADOWLESS); }

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.SHADOWLESS);
    }

    public String desc() { return Messages.get(this, "desc",range(),time());}

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
            if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY) {
                targets.add( mob );
            }
        }


        if (!targets.isEmpty())
        {
            GLog.i("Start");
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

        ChenSlash.hit(mob.pos, Random.Int(360),new Callback() {
            @Override
            public void call() {
                mob.damage(hero.damageRoll(), hero);

                if (!mob.isAlive()){
                    GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", mob.name)) );
                    int exp = Dungeon.hero.lvl <= mob.maxLvl ? mob.EXP : 0;
                    if (exp > 0) {
                        Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
                        Dungeon.hero.earnExp(exp);
                    }
                    targets.remove(mob);
                }

                if(t>=9 || targets.isEmpty()){
                    owner.sprite.visible=true;
                    ((HeroSprite)owner.sprite).setSkillCallbackAnimation(new Callback() {
                        @Override
                        public void call() {
                            ((HeroSprite) owner.sprite).setAfterSkillAnimation();
                            doAfterAction();
                            owner.spendAndNext(1f);
                        }
                    },HeroSprite.skillAnimationType.shadowless_over);
                }
                else { doSlash(targets,t+1); }
            }
        });
    }

    public int range(){
       return 3;
    }

    public int time(){
        return 10;
    }
}
