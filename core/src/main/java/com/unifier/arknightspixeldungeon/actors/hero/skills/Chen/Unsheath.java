package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.FormationBreakerTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.WindCutterTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.Beam;
import com.unifier.arknightspixeldungeon.levels.Terrain;
import com.unifier.arknightspixeldungeon.levels.features.Door;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.List;

import static com.unifier.arknightspixeldungeon.Dungeon.hero;

public class Unsheath extends HeroSkill {
    @Override
    public boolean activated() { return hero.hasTalent(Talent.UNSHEATH); }

    public String desc() { return Messages.get(this, "desc",range());}

    private int range() { return 4 + hero.pointsInTalent(Talent.FLASH);}

    public int repeattedTime = 0;
    private static final String REPEATTEDTIME = "repeattedtime";

    @Override
    public Image skillIcon() {
        return new SkillIcons(SkillIcons.UNSHEATH);
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 10f;
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
        GameScene.selectCell(unsheath_selector);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( REPEATTEDTIME, repeattedTime );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        repeattedTime = bundle.getInt( REPEATTEDTIME );
    }

    protected final CellSelector.Listener unsheath_selector = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell != null && owner != null) {
                Ballistica attack = new Ballistica(owner.pos, cell, Ballistica.DISMISS_CHAR);

                int maxDistance = Math.min(range(), attack.dist);

                int result = attack.collisionPos;

                if (result == owner.pos) {
                    GLog.i("Unable to use");
                    return;
                }

                Char enemy;
                int dropPos = result;

                List<Integer> path = attack.subPath(maxDistance, 1);

                for (int c : path) {
                    if (path.indexOf(c) == path.size() - 1) {
                        GLog.i("Full of enemy");
                        return;
                    }

                    enemy = Actor.findChar(c);
                    if (enemy == null) {
                        dropPos = c;
                        break;
                    }
                }

                Ballistica real = new Ballistica(owner.pos, dropPos, Ballistica.DISMISS_CHAR);

                owner.busy();

                int finalDropPos = real.collisionPos;
                owner.sprite.turnTo(owner.pos,finalDropPos);

                ((HeroSprite) owner.sprite).setSkillCallbackAnimation(
                        new Callback() {
                            @Override
                            public void call() {
                                owner.sprite.visible = false;

                                owner.sprite.parent.add(new Beam.UnSheathRay(owner.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(finalDropPos), new Callback() {

                                    public void call() {

                                        ArrayList<Char> enemys = new ArrayList<>();

                                        for(int c : real.subPath(1,real.dist)){
                                            Char enemy = Actor.findChar(c);
                                            if (enemy != null && enemy.alignment == Char.Alignment.ENEMY) {
                                                enemys.add(enemy);
                                            }
                                        }

                                        float factor = 1f;

                                        if(owner.hasTalent(Talent.FLOWING_WATER)){
                                            if(enemys.size()>=3){ factor += 1f;}
                                        }else if(owner.hasTalent(Talent.FORMATION_BREAKER)){
                                            factor += 0.25f;
                                        }

                                        factor += owner.pointsInTalent(Talent.FLASH) * 0.1f;
                                        factor +=  owner.pointsInTalent(Talent.UNSHEATH) == 2 ? 0.2f : 0f;

                                        int tracker = 0;

                                        for(Char enemy : enemys){
                                            if(owner.hasTalent(Talent.HEART_STRIKER) && enemys.indexOf(enemy) == enemys.size() -1 ){
                                                tracker = (int) (skillDamage(enemy,false) * factor + 0.25f);
                                            }
                                            else tracker = (int) (skillDamage(enemy,false) * factor);

                                            enemy.damage(tracker,owner);

                                            if (!enemy.isAlive()) {
                                                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                                int exp = hero.lvl <= ((Mob) enemy).maxLvl ? ((Mob) enemy).EXP : 0;
                                                if (exp > 0) {
                                                    hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
                                                    hero.earnExp(exp);
                                                }
                                            } else if(owner.hasTalent(Talent.WIND_CUTTER)){
                                                Buff.affect(enemy, WindCutterTracker.class).set((int) (tracker * 0.5f));
                                            }
                                        }

                                        if(owner.pointsInTalent(Talent.HEART_STRIKER)==2) {
                                            for (int n : PathFinder.NEIGHBOURS9) {
                                                int c = owner.pos + n;
                                                Char ch = Actor.findChar(c);
                                                if (ch != null && enemys.contains(ch) && ch.alignment == Char.Alignment.ENEMY) {
                                                    ch.damage((int) (skillDamage(ch,false) * 0.5f),owner);
                                                }
                                            }
                                        }

                                        if(Dungeon.level.map[finalDropPos] == Terrain.DOOR)
                                        {
                                            Door.enter( finalDropPos );
                                        }

                                        owner.pos = finalDropPos;
                                        owner.sprite.interruptMotion();
                                        owner.sprite.place(finalDropPos);
                                        owner.sprite.visible = true;
                                        Dungeon.observe();
                                        GameScene.updateFog();

                                        ((HeroSprite) owner.sprite).setSkillCallbackAnimation(
                                                new Callback() {
                                                    @Override
                                                    public void call() {
                                                        ((HeroSprite) owner.sprite).setAfterSkillAnimation();
                                                        Dungeon.level.press(finalDropPos, owner);
                                                        doAfterAction();
                                                        if(enemys.size()>=3 && owner.hasTalent(Talent.FORMATION_BREAKER) || (!enemys.isEmpty() && hero.hasTalent(Talent.FLOWING_WATER))){
                                                            if(repeattedTime<3) charge++;
                                                            else repeattedTime = 0;
                                                        }

                                                        if(owner.pointsInTalent(Talent.FORMATION_BREAKER) == 2){
                                                            Buff.affect(owner, FormationBreakerTracker.class).set(enemys.size());
                                                        }

                                                        owner.spendAndNext(1f);
                                                    }
                                                }, HeroSprite.skillAnimationType.unsheath_over);
                                    }
                                }));
                            }
                        },  HeroSprite.skillAnimationType.unsheath_start);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(CellSelector.class, "prompt");
        }
    };

    public int skillDamage(Char enemy, boolean isMagic){

        return owner.rawdamageRoll(enemy,isMagic);
    }
}
