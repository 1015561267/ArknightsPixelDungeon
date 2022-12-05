package com.unifier.arknightspixeldungeon.actors.hero.skills.Chen;

import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.RageTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ReflectTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.items.KindOfWeapon;
import com.watabou.noosa.Image;

public abstract class ChenSkill extends HeroSkill {
    @Override
    public abstract boolean activated();

    @Override
    public abstract Image skillIcon();

    @Override
    public abstract float CD(Hero hero);

    @Override
    public abstract float rawCD();
    @Override
    public abstract int getMaxCharge();

    @Override
    public boolean act() {
        spend(TICK);

        float factor = 1;
        if(activated()) {
            if (owner.hasTalent(Talent.SWORD_WEAPON_MASTERY) && owner.belongings.weapon != null && owner.belongings.weapon.weaponType().contains(KindOfWeapon.type.SWORD)) {
                factor += 0.2;
            }

            RageTracker rageTracker = owner.buff(RageTracker.class);
            if (owner.hasTalent(Talent.SCARLET_MOMENTUM) && rageTracker != null && rageTracker.rage > 0) {
                factor += rageTracker.rage;
            }

            getCoolDown(TICK * factor);
        }
        return true;
    }

    @Override
    public void getCoolDown(float amount)
    {
        if (charge < getMaxCharge())
        {
            cooldown -= amount;
            if(cooldown <= 0)
            {
                charge ++;
                if(charge < getMaxCharge())
                {
                    cooldown = CD( this.owner) - (cooldown);
                }
                else cooldown = CD( this.owner);
            }
        }
    }


    @Override
    public void doAfterAction(){
        charge--;

        if(owner.pointsInTalent(Talent.SKILLFUL_GUARD)==2){
            if(owner.buff(ReflectTracker.class)!=null) {
                owner.buff(ReflectTracker.class).gainStack();
            }
        }
    }

    public boolean available(){ return charge > 0; }

}
