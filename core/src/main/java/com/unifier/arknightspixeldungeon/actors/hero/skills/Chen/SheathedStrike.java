package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.CounterStrikeTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.WellPreparedTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingStone;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.MissileSprite;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

import static com.unifier.arknightspixeldungeon.actors.hero.Talent.COUNTER_STRIKE;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.PARRY;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.REPRIMAND;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.SHEATH_THROW;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.WELL_PREPARED;

public class SheathedStrike extends HeroSkill {

//    public boolean enable = false;

    @Override
    public boolean activated() { return owner.hasTalent(Talent.SHEATHED_STRIKE); }//I'd like to use owner but it somehow doesn't work

    @Override
    public Image skillIcon() {

        if (owner.hasTalent(Talent.SHEATH_THROW)) {
            return new SkillIcons(SkillIcons.SHEATH_THROW);
        } else if (owner.hasTalent(Talent.REPRIMAND)) {
            return new SkillIcons(SkillIcons.REPRIMAND);
        } else if (owner.hasTalent(Talent.PARRY)) {
            return new SkillIcons(SkillIcons.PARRY);
        }

        return new SkillIcons(SkillIcons.SHEATHED_STRIKE);
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 5f;
    }

    @Override
    public int getMaxCharge() {
        return 1;
    }

    @Override
    public void doAction() {

        if(!available()){
            GLog.w(Messages.get(HeroSkill.class, "unavailable"));
            return;
        }

        if(owner.hasTalent(SHEATH_THROW))
        {
            GameScene.selectCell(sheath_throw_selector);
        }
        else if(owner.hasTalent(REPRIMAND))
        {
            GameScene.selectCell(reprimand_selector);
        }
        else {
            Buff.affect(owner,Talent.SheathedStrikeTracker1.class);
            Buff.affect(owner,Talent.SheathedStrikeTracker2.class,3f);
            if (owner.hasTalent(PARRY)) {
                Buff.affect(owner,Talent.ParryTrackerPrepare.class);
            }
            if (owner.hasTalent(COUNTER_STRIKE)) {
                Buff.affect(owner,CounterStrikeTracker.class,10f);
            }
            doAfterAction();
        }
//            enable = true;//PARRY effect can be found at damage roll :
    }

    protected CellSelector.Listener sheath_throw_selector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (owner != null && cell != null) {
                Ballistica ballistica = new Ballistica(owner.pos,cell,Ballistica.PROJECTILE);
                int result =  ballistica.collisionPos;
                Char enemy = Actor.findChar(result);
                if (enemy != null && enemy.alignment == Char.Alignment.ENEMY) {
                    SheathedStrike.this.fx(ballistica, new Callback() {
                        @Override
                        public void call() {
                            int dmg = 0;

                            if (owner.pointsInTalent(SHEATH_THROW) == 1) {
                                dmg = 1;
                            } else if (owner.pointsInTalent(SHEATH_THROW) == 2) {
                                dmg = 9999;
                            }

                            enemy.damage(dmg,owner);
                        }
                    });

                    doAfterAction();
                    owner.spendAndNext(1f);
                } else {
                }
            } else {
            }
        }

        @Override
        public String prompt() {
            return Messages.get(CellSelector.class, "prompt");
        }
    };

    protected void fx(Ballistica ba, Callback callback) {
        int cell = ba.collisionPos;
        ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(owner.sprite,cell,new ThrowingStone(),callback);
//        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    protected CellSelector.Listener reprimand_selector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (owner != null && cell != null) {
                Char enemy = Actor.findChar(cell);

                if(enemy != null && enemy.alignment == Char.Alignment.ENEMY) {

                    enemy.fieldOfView = new boolean[Dungeon.level.length()];
                    Dungeon.level.updateFieldOfView(enemy,enemy.fieldOfView);

                    if(enemy.charInView(owner)) {
                        Buff.affect(enemy,Talent.ReprimandTracker.class,7f);
                        if (((Mob)enemy).state == ((Mob) enemy).SLEEPING) {
                            ((Mob)enemy).state = ((Mob) enemy).HUNTING;
                        }
                        ((Mob)enemy).target = owner.pos;

                        if (owner.pointsInTalent(WELL_PREPARED) == 1) {
                            Buff.affect(owner, WellPreparedTracker.class,3f).setTime(1);
                        }

                        if (owner.pointsInTalent(WELL_PREPARED) == 2) {
                            Buff.affect(owner, WellPreparedTracker.class,3f).setTime(3);
                        }

                    }

                    doAfterAction();
                    owner.spendAndNext(1f);
                } else {

                }
            }
            else{

            }
        }

        @Override
        public String prompt() {
            return Messages.get(CellSelector.class, "prompt");
        }
    };
 }
