package com.unifier.arknightspixeldungeon.actors.mobs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.items.weapon.enchantments.Grim;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.journal.Notes;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.sprites.GopnikSprite;
import com.unifier.arknightspixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gopnik extends Mob{

    //raw Statue

    {
        spriteClass = GopnikSprite.class;

        EXP = 0;
        state = PASSIVE;

        properties.add(Char.Property.INORGANIC);
    }

    protected Weapon weapon;

    public Gopnik() {
        super();

        do {
            weapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
        } while (weapon.cursed);

        weapon.enchant( Weapon.Enchantment.random() );

        HP = HT = 15 + Dungeon.depth * 5;
        defenseSkill = 4 + Dungeon.depth;
    }

    private static final String WEAPON	= "weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WEAPON, weapon );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        weapon = (Weapon)bundle.get( WEAPON );
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Notes.add( Notes.Landmark.STATUE );
        }
        return super.act();
    }

    @Override
    public int damageRoll(Char enemy, boolean isMagic) {
        return weapon.damageRoll(this,enemy,isMagic);
    }

    @Override
    public int attackSkill( Char target ) {
        return (int)((9 + Dungeon.depth) * weapon.accuracyFactor(this));
    }

    @Override
    protected float attackDelay() {
        return weapon.speedFactor( this );
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Dungeon.level.distance( pos, enemy.pos ) <= weapon.reachFactor(this);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth + weapon.defenseFactor(this));
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.damage( dmg, src );
    }

    @Override
    public void multipleDamage(ArrayList<Boolean> burstArray, ArrayList<Integer> damageArray, Object src, int hittedTime){

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.multipleDamage(burstArray,damageArray,src,hittedTime);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        return weapon.proc( this, enemy, damage );
    }

    @Override
    public void beckon( int cell ) {
        // Do nothing
    }

    @Override
    public void die( Object cause ) {
        weapon.identify();
        Dungeon.level.drop( weapon, pos ).sprite.drop();
        super.die( cause );
    }

    @Override
    public void destroy() {
        Notes.remove( Notes.Landmark.STATUE );
        super.destroy();
    }

    @Override
    public boolean reset() {
        state = PASSIVE;
        return true;
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", weapon.name());
    }

    {
        resistances.add(Grim.class);
    }
}
