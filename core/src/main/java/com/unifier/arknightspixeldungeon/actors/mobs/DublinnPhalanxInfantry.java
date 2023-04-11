package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.sprites.DublinnPhalanxInfantrySprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class DublinnPhalanxInfantry extends Mob {

    //raw Slime

    {
        spriteClass = DublinnPhalanxInfantrySprite.class;

        HP = HT = 22;
        defenseSkill = 5;

        EXP = 4;
        maxLvl = 9;

        lootChance = 0.2f; //by default, see lootChance()
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return Random.NormalIntRange( 2, 6 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 6){
            //takes 6/7/8/9/10/11 dmg at 6/8/11/15/20/26 incoming dmg
            dmg = 5 + (int)(Math.sqrt(8*(dmg - 5) + 1) - 1)/2;
        }
        super.damage(dmg, src);
    }

    @Override
    public float lootChance(){
        //each drop makes future drops 1/3 as likely
        // so loot chance looks like: 1/5, 1/15, 1/45, 1/135, etc.
        return super.lootChance() * (float)Math.pow(1/3f, Dungeon.LimitedDrops.DUBLINNPHALANXINFANTRY_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.DUBLINNPHALANXINFANTRY_WEP.count++;
        Generator.Category c = Generator.Category.WEP_T2;
        MeleeWeapon w = (MeleeWeapon) Reflection.newInstance(c.classes[Random.chances(c.probs)]);
        w.random();
        w.level(0);
        return w;
    }

}
