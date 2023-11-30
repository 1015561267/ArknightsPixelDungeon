package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.blobs.Blob;
import com.unifier.arknightspixeldungeon.actors.blobs.ToxicGas;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.levels.features.Chasm;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.SarkazWithererSprite;
import com.unifier.arknightspixeldungeon.sprites.SkeletonSprite;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SarkazWitherer extends Mob {

    //raw Skeleton

    {
        spriteClass = SarkazWithererSprite.class;

        HP = HT = 20;
        defenseSkill = 8;

        EXP = 6;
        maxLvl = 13;

        loot = Generator.Category.WEAPON;
        lootChance = 0.125f;

        properties.add(Property.UNDEAD);
        properties.add(Property.INORGANIC);
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange(2, 8);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (cause == Chasm.class) return;

        GameScene.add( Blob.seed( pos, 30, ToxicGas.class ) );

        if (Dungeon.level.heroFOV[pos]) {
            //Sample.INSTANCE.play(Assets.SND_BONES);
        }

        //if (heroKilled) {
        //    Dungeon.fail(getClass());
        //    GLog.n(Messages.get(this, "explo_kill"));
        //}
    }

    @Override
    protected Item createLoot() {
        Item loot;
        do {
            loot = Generator.randomWeapon();
            //50% chance of re-rolling tier 4 or 5 melee weapons
        } while (((MeleeWeapon) loot).tier >= 4 && Random.Int(2) == 0);
        loot.level(0);
        return loot;
    }

    @Override
    public int attackSkill(Char target) {
        return 11;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }
}
