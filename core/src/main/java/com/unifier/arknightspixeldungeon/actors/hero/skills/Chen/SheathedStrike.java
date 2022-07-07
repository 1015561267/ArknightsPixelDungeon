package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Blindness;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

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
                Buff.affect(owner,Talent.CounterStrikeTracker.class,10f);
            }
        }
//            enable = true;//PARRY effect can be found at damage roll :
        doAfterAction();
    }

    protected CellSelector.Listener sheath_throw_selector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (owner != null)
            {
                Ballistica attack = new Ballistica( owner.pos, cell, Ballistica.PROJECTILE);
                int result =  attack.collisionPos;
                Char enemy = Actor.findChar( result );
                if(enemy != null && owner.alignment == Char.Alignment.ENEMY)
                {

                }else {

                }
            }
            else {

            }
        }

        @Override
        public String prompt() {
            return null;
        }
    };

    protected CellSelector.Listener reprimand_selector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (owner != null)
            {
                Char enemy = Actor.findChar( cell );
                if(enemy != null && enemy.alignment == Char.Alignment.ENEMY)
                {
                    enemy.fieldOfView = new boolean[Dungeon.level.length()];
                    Dungeon.level.updateFieldOfView(enemy,enemy.fieldOfView);
                    if(enemy.charInView(owner)) {
                        Buff.affect(enemy,Talent.ReprimandTracker.class,7f);
                        if (((Mob)enemy).state == ((Mob) enemy).SLEEPING) {
                            ((Mob)enemy).state = ((Mob) enemy).HUNTING;
                        }
                        ((Mob)enemy).target = owner.pos;

                        if (owner.pointsInTalent(WELL_PREPARED) == 1) {
                            Buff.affect(owner,Talent.WellPrepared.class,3f).setTime(1);
                        }

                        if (owner.pointsInTalent(WELL_PREPARED) == 2) {
                            Buff.affect(owner,Talent.WellPrepared.class,3f).setTime(3);
                        }

                    }

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
