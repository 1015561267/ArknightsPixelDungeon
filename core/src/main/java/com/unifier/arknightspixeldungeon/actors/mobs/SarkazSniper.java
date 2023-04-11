package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.blobs.Blob;
import com.unifier.arknightspixeldungeon.actors.blobs.Fire;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Burning;
import com.unifier.arknightspixeldungeon.actors.buffs.Poison;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.MissileWeapon;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.sprites.SarkazSniperSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SarkazSniper extends Mob{

    //raw GnollTrickster,but it cannot extend DublinnScout(changed gnoll)as they have magic resist

    {
        spriteClass = SarkazSniperSprite.class;

        HP = HT = 20;
        defenseSkill = 5;

        EXP = 5;

        state = WANDERING;

        //at half quantity, see createLoot()
        loot = Generator.Category.MISSILE;
        lootChance = 1f;

        properties.add(Property.MINIBOSS);
    }

    private int combo = 0;

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 1, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos && buff(Talent.ReprimandTracker.class) == null;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        //The gnoll's attacks get more severe the more the player lets it hit them
        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >=6 && enemy.buff(Burning.class) == null){

                if (Dungeon.level.flamable[enemy.pos])
                    GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
                Buff.affect(enemy, Burning.class).reignite( enemy );

            } else
                Buff.affect( enemy, Poison.class).set((effect-2) );

        }
        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        combo = 0; //if he's moving, he isn't attacking, reset combo.
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    protected Item createLoot() {
        MissileWeapon drop = (MissileWeapon)super.createLoot();
        //half quantity, rounded up
        drop.quantity((drop.quantity()+1)/2);
        return drop;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );

        FrostNovaQuestPlot.Quest.process();
    }

    private static final String COMBO = "combo";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }
}
