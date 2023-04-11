package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Corruption;
import com.unifier.arknightspixeldungeon.actors.buffs.Poison;
import com.unifier.arknightspixeldungeon.effects.Pushing;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfHealing;
import com.unifier.arknightspixeldungeon.levels.Terrain;
import com.unifier.arknightspixeldungeon.levels.features.Door;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.SarkazRequisitionedEngineeringDroneSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SarkazRequisitionedEngineeringDrone extends Mob {

    //raw Swarm

    {
        spriteClass = SarkazRequisitionedEngineeringDroneSprite.class;

        HP = HT = 50;
        defenseSkill = 4;

        EXP = 3;
        maxLvl = 12;

        flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see rollToDropLoot()
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    private static final float SPLIT_DELAY	= 1f;

    int generation	= 0;

    private static final String GENERATION	= "generation";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
        if (generation > 0) EXP = 0;
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {

        if (HP >= damage + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();
            boolean[] solid = Dungeon.level.solid;

            int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (!solid[n] && Actor.findChar( n ) == null) {
                    candidates.add( n );
                }
            }

            if (candidates.size() > 0) {

                SarkazRequisitionedEngineeringDrone clone = split();
                clone.HP = (HP - damage) / 2;
                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                    Door.enter( clone.pos );
                }

                GameScene.add( clone, SPLIT_DELAY );
                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                HP -= clone.HP;
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public ArrayList<Integer> multipleDefenseProc(Char enemy, ArrayList<Integer> damage, ArrayList<Boolean> burstArray, int hittedTime) {

        Integer total = 0;
        for(Integer record : damage){
            total += record;
        }

        if (HP >= total + 2 && !damage.isEmpty()) {
            ArrayList<Integer> candidates = new ArrayList<>();
            boolean[] solid = Dungeon.level.solid;

            int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (!solid[n] && Actor.findChar( n ) == null) {
                    candidates.add( n );
                }
            }

            if (candidates.size() > 0) {

                SarkazRequisitionedEngineeringDrone clone = split();
                clone.HP = (HP - total) / 2;
                clone.pos = Random.element( candidates );
                clone.state = clone.HUNTING;

                if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                    Door.enter( clone.pos );
                }

                GameScene.add( clone, SPLIT_DELAY );
                Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                HP -= clone.HP;
                //GLog.i("splited!");
            }
        }

        return super.multipleDefenseProc(enemy, damage, burstArray, hittedTime);
    }

    private SarkazRequisitionedEngineeringDrone split() {
        SarkazRequisitionedEngineeringDrone clone = new SarkazRequisitionedEngineeringDrone();
        clone.generation = generation + 1;
        clone.EXP = 0;
        if (buff( Burning.class ) != null) {
            Buff.affect( clone, Burning.class ).reignite( clone );
        }
        if (buff( Poison.class ) != null) {
            Buff.affect( clone, Poison.class ).set(2);
        }
        if (buff(Corruption.class ) != null) {
            Buff.affect( clone, Corruption.class);
        }
        return clone;
    }

    @Override
    public void rollToDropLoot() {
        lootChance = 1f/(6 * (generation+1) );
        lootChance *= (5f - Dungeon.LimitedDrops.SWARM_HP.count) / 5f;
        super.rollToDropLoot();
    }

    @Override
    protected Item createLoot(){
        Dungeon.LimitedDrops.SWARM_HP.count++;
        return super.createLoot();
    }
}
