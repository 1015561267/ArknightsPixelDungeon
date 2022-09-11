package com.unifier.arknightspixeldungeon.actors.buffs;

import com.unifier.arknightspixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class KineticDamage extends Buff {

    {
        type = buffType.POSITIVE;
    }

    @Override
    public int icon() {
        return super.icon();
        //return BuffIndicator.WEAPON;
    }

    @Override
    public void tintIcon(Image icon) {
        super.tintIcon(icon);
        /*
        if (preservedDamage >= 10){
            icon.hardlight(1f, 0f, 0f);
        } else if (preservedDamage >= 5) {
            icon.hardlight(1f, 1f - (preservedDamage - 5f)*.2f, 0f);
        } else {
            icon.hardlight(1f, 1f, 1f - preservedDamage*.2f);
        }*/
    }

    public String iconTextDisplay() {
        return Integer.toString(damageBonus());
    }

    private float preservedDamage;

    public void setBonus(int bonus){
        preservedDamage = Math.max(bonus,preservedDamage);
    }

    public int damageBonus(){
        return (int)Math.ceil(preservedDamage);
    }

    @Override
    public boolean act() {
        preservedDamage -= Math.max(preservedDamage*.025f, 0.1f);
        if (preservedDamage <= 0) detach();

        spend(TICK);
        return true;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", damageBonus());
    }

    private static final String PRESERVED_DAMAGE = "preserve_damage";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PRESERVED_DAMAGE, preservedDamage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(PRESERVED_DAMAGE)){
            preservedDamage = bundle.getFloat(PRESERVED_DAMAGE);
        } else {
            preservedDamage = cooldown()/10;
            spend(cooldown());
        }
    }

}
