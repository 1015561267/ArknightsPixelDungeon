package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.Statistics;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Vertigo;
import com.unifier.arknightspixeldungeon.items.food.MysteryMeat;
import com.unifier.arknightspixeldungeon.levels.RegularLevel;
import com.unifier.arknightspixeldungeon.levels.rooms.Room;
import com.unifier.arknightspixeldungeon.levels.rooms.special.PoolRoom;
import com.unifier.arknightspixeldungeon.sprites.NetherseaPredatorSprite;
import com.unifier.arknightspixeldungeon.sprites.PiranhaSprite;
import com.watabou.utils.Random;

public class NetherseaPredator extends Mob{

    //raw Piranha

    {
        spriteClass = NetherseaPredatorSprite.class;

        baseSpeed = 2f;

        EXP = 0;

        loot = MysteryMeat.class;
        lootChance = 1f;

        HUNTING = new NetherseaPredator.Hunting();

        properties.add(Property.BLOB_IMMUNE);
    }

    public NetherseaPredator() {
        super();

        HP = HT = 10 + Dungeon.depth * 5;
        defenseSkill = 10 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {

        if (!Dungeon.level.water[pos]) {
            die( null );
            sprite.killAndErase();
            return true;
        } else {
            return super.act();
        }
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( Dungeon.depth, 4 + Dungeon.depth * 2 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20 + Dungeon.depth * 2;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth);
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        Statistics.piranhasKilled++;
        Badges.validatePiranhasKilled();
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findStep( this, pos, target,
                Dungeon.level.water,
                fieldOfView );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, pos, target,
                Dungeon.level.water,
                fieldOfView );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    {
        immunities.add( Burning.class );
        immunities.add( Vertigo.class );
    }

    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            boolean result = super.act(enemyInFOV, justAlerted);
            //this causes piranha to move away when a door is closed on them in a pool room.
            if (state == WANDERING && Dungeon.level instanceof RegularLevel){
                Room curRoom = ((RegularLevel)Dungeon.level).room(pos);
                if (curRoom instanceof PoolRoom) {
                    target = Dungeon.level.pointToCell(curRoom.random(1));
                }
            }
            return result;
        }
    }
}
