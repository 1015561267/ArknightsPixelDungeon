package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
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
import com.watabou.utils.Callback;

import java.util.List;

import static com.unifier.arknightspixeldungeon.Dungeon.hero;

public class Unsheath extends HeroSkill {
    @Override
    public boolean activated() { return hero.hasTalent(Talent.UNSHEATH); }

    public String desc() { return Messages.get(this, "desc",range());}

    private int range() { return 4;}

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

                                        for (int c : real.subPath(1, real.dist)) {
                                            Char enemy = Actor.findChar(c);
                                            if (enemy != null && enemy.alignment == Char.Alignment.ENEMY) {
                                                enemy.damage(owner.damageRoll(), owner);

                                                if (!enemy.isAlive()) {
                                                    GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                                    int exp = hero.lvl <= ((Mob) enemy).maxLvl ? ((Mob) enemy).EXP : 0;
                                                    if (exp > 0) {
                                                        hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "exp", exp));
                                                        hero.earnExp(exp);
                                                    }
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
}
