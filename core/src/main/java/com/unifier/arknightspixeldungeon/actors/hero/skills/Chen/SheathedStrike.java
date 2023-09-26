package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.CounterStrikeTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.WellPreparedTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TimeBubble;
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

import java.util.ArrayList;
import java.util.Collections;

public class SheathedStrike extends ChenSkill {

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
        return 50f;
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

        if (owner.hasTalent(Talent.SEIZE_OPPORTUNITY)) {
            Buff.affect(owner,Talent.SeizeOpportunityTracker.class);
            Buff.affect(owner,TimeBubble.class).reset(2f);
        }

        if(owner.hasTalent(Talent.SHEATH_THROW))
        {
            GameScene.selectCell(sheath_throw_selector);
        }
        else if(owner.hasTalent(Talent.REPRIMAND))
        {
            GameScene.selectCell(reprimand_selector);
        }
        else {
            Buff.affect(owner,Talent.SheathedStrikeTracker1.class);
            Buff.affect(owner,Talent.SheathedStrikeTracker2.class,3f);
            if (owner.hasTalent(Talent.PARRY)) {
                Buff.affect(owner,Talent.ParryTrackerPrepare.class);
            }
            if (owner.hasTalent(Talent.COUNTER_STRIKE)) {
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

                    owner.busy();
                    doThrow(enemy,ballistica,1);

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
        int from = ba.sourcePos;
        int to = ba.collisionPos;
        ((MissileSprite)owner.sprite.parent.recycle(MissileSprite.class)).reset(from,to,new ThrowingStone(),callback);
//        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    protected void doThrow(Char enemy,Ballistica ballistica,int t) {
        SheathedStrike.this.fx(ballistica, new Callback() {
            @Override
            public void call() {
                int dmg = 0;

                if (owner.pointsInTalent(Talent.SHEATH_THROW) == 1) {
                    dmg = 1;
                } else if (owner.pointsInTalent(Talent.SHEATH_THROW) == 2) {
                    dmg = 9999;
                }

                int From = enemy.pos;
                Mob To = null;
                ArrayList<Mob> targets =  new ArrayList<Mob>();

                for (Mob mob : Dungeon.level.mobs) {
                    if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(From,mob.pos) <= 99) {
                        targets.add(mob);
                    }
                }
                enemy.damage(dmg,owner);

                if (owner.hasTalent(Talent.SHEATH_BOUNCE)) {

//                    Mob mob = targets.get(Random.Int(targets.size()));
                    if (targets.size() != 0) {
                        Collections.shuffle(targets);
                        for (Mob mob : targets) {
                            To = mob;
                            Ballistica ba = new Ballistica(From,To.pos,Ballistica.PROJECTILE);
                            if (ba.collisionPos != To.pos || !To.isAlive() || To.pos == From) {
                                To = null;
                            } else {
                                break;
                            }
                        }
                    }

                    if (To != null && t <= 100) {
                        Ballistica ba1 = new Ballistica(From,To.pos,Ballistica.PROJECTILE);
                        doThrow(To,ba1,t+1);
                    } else {
                        Ballistica ba2 = new Ballistica(From,owner.pos,Ballistica.PROJECTILE);
                        if (ba2.collisionPos == owner.pos) {
                            SheathedStrike.this.fx(ba2, new Callback() {
                                public void call() {
                                    doAfterAction();
                                    owner.spendAndNext(1f);
                                }
                            });
                        } else {
                            doAfterAction();
                            owner.spendAndNext(1f);
                        }
                    }
                } else {
                    doAfterAction();
                    owner.spendAndNext(1f);
                }
            }
        });

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

                        if (owner.pointsInTalent(Talent.WELL_PREPARED) == 1) {
                            Buff.affect(owner, WellPreparedTracker.class,3f).setTime(1);
                        }

                        if (owner.pointsInTalent(Talent.WELL_PREPARED) == 2) {
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
