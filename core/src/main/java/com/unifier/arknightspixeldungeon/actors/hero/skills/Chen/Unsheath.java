package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ComboTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.SharpJudgementTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.effects.Beam;
import com.unifier.arknightspixeldungeon.effects.Pushing;
import com.unifier.arknightspixeldungeon.effects.TalentSprite;
import com.unifier.arknightspixeldungeon.levels.Terrain;
import com.unifier.arknightspixeldungeon.levels.features.Door;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
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
    public boolean activated() { return owner.hasTalent(Talent.UNSHEATH); }

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
        return 100f;
    }

    @Override
    public int getMaxCharge() {
        return 1;
    }

    @Override
    public void doAction() {

        Buff.detach( owner, Talent.BoilingKenshinTracker.class );

        if(!available()){
            if(owner.hasTalent(Talent.BOILING_KENSHIN) && ( owner.HP > owner.HT * 0.4f ) ){
                Buff.affect(owner,Talent.BoilingKenshinTracker.class);
                GameScene.selectCell(unsheath_selector);
            }

            else if(owner.buff( Talent.FlowingWaterTracker.class ) != null || owner.buff( Talent.AnotherFlowingWaterTracker.class ) != null){
                GameScene.selectCell(unsheath_selector);
            }

            else{
                GLog.h(Messages.get(HeroSkill.class, "unavailable"));
                return;
            }
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
                Ballistica attack = new Ballistica(owner.pos, cell, Ballistica.STOP_TERRAIN);
                //Here we take a rather complex logic to make hero get the further,unoccupied but close to desired pos
                //For begin,if pointed position is stand-able then that's all,if not,
                //First,if the pointed position is too long then take the furthest pos
                //Second,if the pointed position is occupied then check if further pos is both unoccupied and stand-able until max range
                //Third,if second step cannot get,roll back to the pointed position,then get closer to check possible pos
                //Finally,if reach the zero pos,it represent finding failure.

                int result = cell;
                boolean dropedTracker = false;

                Char enemy;
                List<Integer> availablePath = attack.subPath(1, range());

                //GLog.w( "All path:");
                //for (int c : availablePath) {
               //     GLog.w( c + " ");
               // }

                int desired = availablePath.indexOf(cell);

                int maxTracker = Math.min(availablePath.indexOf(cell) < 0 ? availablePath.size() - 1 :availablePath.indexOf(cell) //get the so-called max range possible if pointed place is out of range
                        ,availablePath.size() - 1);

                //GLog.w(cell+" "+desired+" "+maxTracker);

                if(desired != -1) //represent pointed place is in range,otherwise dismiss further search cause we directly use the longest pos as start
                {
                    //GLog.w( "further:");
                    for (int c : availablePath) {
                        if(availablePath.indexOf(c) >= availablePath.indexOf(cell)){//search front to seek possible further drop place
                             GLog.w(String.valueOf(c));
                             enemy = Actor.findChar(c);

                            if (Dungeon.level.solid[c]) {
                                if(Dungeon.level.map[c] == Terrain.DOOR)
                                {
                                    result = c;
                                    //GLog.w( "on door:"+result);
                                    dropedTracker = true;
                                }else break;
                            }//We need to consider that former terrain can block further search so for now if there are a solid terrain expect unlocked door,just break and consider futher search as a failure

                            if (enemy == null && Dungeon.level.passable[c]) {
                                result = c;
                                //GLog.w( "further search:"+result);
                                dropedTracker = true;
                                break;
                            }
                        }
                    }
                }

                if (!dropedTracker) {//then search back
                    //List<Integer> reversePath = attack.subPath(maxTracker,1); Warning,Arraylist.sublist must have start<end,else throw exception,so reserve ergodic has had to take other ways
                    //GLog.w( "narrow:");
                    for(int i = maxTracker;i>=0;i--){
                        int c =availablePath.get(i);
                        enemy = Actor.findChar(c);
                        if (enemy == null && Dungeon.level.passable[c]) {
                            result = c;
                            //GLog.w( "narrow search:"+result);
                            dropedTracker = true;
                            break;
                        }
                    }
                }

                if (!dropedTracker || result == owner.pos) {
                    GLog.i("Unable to use");
                    return;
                }

                if(owner.buff(Talent.BoilingKenshinTracker.class)!=null){
                    owner.HP -= Math.max(owner.HP * 0.6f , owner.HT * 0.4f);
                    Buff.detach( owner, Talent.BoilingKenshinTracker.class );
                    TalentSprite.show(owner, Talent.BOILING_KENSHIN,TalentSprite.Phase.FADE_IN);
                }

                int dropPos = result;
                Ballistica real = new Ballistica(owner.pos, dropPos, Ballistica.DISMISS_CHAR);

                owner.busy();

                int finalDropPos = real.collisionPos;
                owner.sprite.turnTo(owner.pos,finalDropPos);

                ((HeroSprite) owner.sprite).setSkillCallbackAnimation(
                        new Callback() {
                            @Override
                            public void call() {
                                owner.sprite.visible = false;

                                ArrayList<Char> enemys = new ArrayList<>();

                                if(real.dist > 1) {
                                    for (int c : real.subPath(1, real.dist - 1)) {
                                        Char enemy = Actor.findChar(c);
                                        if (enemy == null) {
                                            if (owner.pointsInTalent(Talent.WIND_CUTTER) == 2) {
                                                for (int i : PathFinder.NEIGHBOURS8) {
                                                    Char dragged = Actor.findChar(c + i);
                                                    if (dragged != null && !enemys.contains(dragged) && dragged.alignment == Char.Alignment.ENEMY && !dragged.properties().contains(Char.Property.IMMOVABLE)) {
                                                        Actor.add(new Pushing(dragged, dragged.pos, c, new Callback() {
                                                            public void call() {
                                                                Dungeon.level.press(c, dragged, true);
                                                            }
                                                        }));
                                                        dragged.pos = c;
                                                        Dungeon.observe();
                                                        GameScene.updateFog();
                                                        enemys.add(dragged);
                                                        break;
                                                    }
                                                }
                                            }
                                        } else if (enemy != null && enemy.alignment == Char.Alignment.ENEMY) {
                                            enemys.add(enemy);
                                        }
                                    }
                                }

                                owner.sprite.parent.add(new Beam.UnSheathRay(owner.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(finalDropPos), new Callback() {

                                    public void call() {

                                        boolean killAny = false;
                                        boolean hitAny = !enemys.isEmpty();

                                        if(enemys.size()==1 && owner.hasTalent(Talent.SUN_CROSS)){
                                            Char instantKilled = enemys.get(0);
                                            if(!instantKilled.properties().contains(Char.Property.BOSS)){
                                                enemys.remove(instantKilled);
                                                instantKilled.die(Unsheath.class);
                                                killAny = true;
                                                TalentSprite.show(owner, Talent.SUN_CROSS,TalentSprite.Phase.FADE_IN);
                                            }
                                        }

                                        float factor = 1f;

                                        factor +=  owner.pointsInTalent(Talent.UNSHEATH) == 2 ? 0.2f : 0f;

                                        if (owner.buff(Talent.SheathedStrikeTracker2.class) != null && owner.pointsInTalent(Talent.SHEATHED_STRIKE) == 2) {
                                            factor += 0.2f;
                                        }

                                        if(owner.hasTalent(Talent.SHARP_JUDGEMENT)){
                                            int size = enemys.size();
                                            for (int n : PathFinder.NEIGHBOURS9) {
                                                int c = finalDropPos + n;
                                                Char ch = Actor.findChar(c);
                                                if (ch != null && enemys.contains(ch) && ch.alignment == Char.Alignment.ENEMY) {
                                                    size++;
                                                }
                                            }
                                            if(size>0){
                                                Buff.affect(owner, SharpJudgementTracker.class).setStack(size);
                                                TalentSprite.show(owner, Talent.SHARP_JUDGEMENT,TalentSprite.Phase.FADE_IN);
                                            }
                                        }

                                        for(Char enemy : enemys){
                                            int tracker = (int) (skillDamage(enemy,false) * factor);
                                            enemy.damage(tracker,owner);
                                            if(owner.hasTalent(Talent.MORTAL_SKILL)){
                                                ComboTracker comboTracker = enemy.buff(ComboTracker.class);
                                                if(comboTracker!=null && comboTracker.getStack()>=3 ){
                                                    owner.skill_3.getCoolDown(owner.skill_3.rawCD() * 0.25f);
                                                }
                                            }
                                            if (!enemy.isAlive()) {
                                                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                                killAny = true;
                                            }
                                        }

                                        if(owner.hasTalent(Talent.WIND_CUTTER)) {
                                            for (int n : PathFinder.NEIGHBOURS9) {
                                                int c = finalDropPos + n;
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

                                        boolean finalKillAny = killAny;

                                        ((HeroSprite) owner.sprite).setSkillCallbackAnimation(
                                                new Callback() {
                                                    @Override
                                                    public void call() {
                                                        ((HeroSprite) owner.sprite).setAfterSkillAnimation();
                                                        Dungeon.level.press(finalDropPos, owner);
                                                        float tempCooldown = cooldown;
                                                        doAfterAction();

                                                        if(owner.buff(Talent.BoilingKenshinTracker.class)!=null) {
                                                            Buff.detach( owner, Talent.BoilingKenshinTracker.class );//TODO Not decided this effect will save previous cd yet
                                                            cooldown = tempCooldown;
                                                        }

                                                            if(owner.pointsInTalent(Talent.SUN_CROSS) == 2 && finalKillAny){
                                                            owner.skill_1.getCoolDown(owner.skill_1.rawCD());
                                                        }


                                                        if( owner.hasTalent(Talent.FLOWING_WATER) && hitAny){
                                                            if(owner.buff( Talent.FlowingWaterTracker.class ) != null){
                                                                Buff.detach( owner, Talent.FlowingWaterTracker.class );
                                                                Buff.affect(owner, Talent.AnotherFlowingWaterTracker.class , 5f);
                                                                cooldown = tempCooldown;
                                                            }else if(owner.buff( Talent.AnotherFlowingWaterTracker.class ) != null){
                                                                Buff.detach( owner, Talent.AnotherFlowingWaterTracker.class );
                                                                cooldown = tempCooldown;
                                                            }
                                                            else Buff.affect(owner, Talent.FlowingWaterTracker.class , 5f);




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
