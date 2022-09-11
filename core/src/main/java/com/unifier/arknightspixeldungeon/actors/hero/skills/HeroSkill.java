package com.unifier.arknightspixeldungeon.actors.hero.skills;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.RageTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ReflectTracker;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.items.KindOfWeapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.text.DecimalFormat;

public abstract class HeroSkill extends Buff {

    private static final String COOLDOWN = "cooldown";
    private static final String CHARGE = "charge";

    {
        actPriority = BUFF_PRIO; //low priority, towards the end of a turn
        cooldown = rawCD();
    }

    public Hero owner;
    protected float cooldown;
    public int charge = 0;

    abstract public boolean activated();

    abstract public Image skillIcon() ;

    public abstract float CD(Hero hero);

    public abstract float rawCD();

    public abstract int getMaxCharge();

    public float cooldownRatio() {//For now it means to handle cooldown for multi charge skill have problem when there are only one charge
        if(charge == getMaxCharge())
            return 0;
        else
        return cooldown / rawCD();
    }

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

    public boolean available(){ return charge > 0; }

    public void resetCD(Hero hero){ this.cooldown = CD(hero);}

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

    public String name() { return Messages.get(this, "name"); };

    public String otherInfo() {
        if (getMaxCharge() == charge) {
            return Messages.get(this, "chargeinfo",getMaxCharge(),charge);
        } else {
            return Messages.get(this, "cooldown", cooldown) + "\n\n" + Messages.get(this, "chargeinfo", getMaxCharge(), charge);
        }
    }

    public String desc() { return Messages.get(this, "desc");};

    public enum buffType {ACTIVE,PASSIVE};

    public buffType type = buffType.ACTIVE;

    public void doAction()
    {
        if(!available()){
            GLog.h(Messages.get(this, "unavailable"));
            return;
        }
    };

    public void doAfterAction(){
        charge--;

        if(owner.pointsInTalent(Talent.SKILLFUL_GUARD)==2){
            if(owner.buff(ReflectTracker.class)==null) {
                owner.buff(ReflectTracker.class).gainStack();
            }
        }
    }

    public boolean attachTo( Char hero ) {
        this.owner = (Hero)hero;
        this.target = hero;
        hero.add( this );
        return super.attachTo(hero);
    }

    protected String dispTurns(float input){
        return new DecimalFormat("#.##").format(input);
    }

    private static final String SKILL_PLACE = "skill_place_";

    public static final int MAX_SKILL_AMOUNT = 4;//start from 1

    public static void storeSkillsInBundle(Bundle bundle, Hero hero ) {
        bundle.put(SKILL_PLACE + 1 , hero.skill_1);
        bundle.put(SKILL_PLACE + 2 , hero.skill_2);
        bundle.put(SKILL_PLACE + 3, hero.skill_3);
    }

    public static void restoreSkillsFromBundle( Bundle bundle, Hero hero ) {
        if (hero != null)
        {
            if(bundle.contains(SKILL_PLACE + 1))
            {
                hero.skill_1 = (HeroSkill)bundle.get(SKILL_PLACE + 1 );
                hero.skill_1.attachTo(hero);

            }
            if(bundle.contains(SKILL_PLACE + 2))
            {
                hero.skill_2 = (HeroSkill)bundle.get(SKILL_PLACE + 2 );
                hero.skill_2.attachTo(hero);
            }
            if(bundle.contains(SKILL_PLACE + 3))
            {
                hero.skill_3 = (HeroSkill)bundle.get(SKILL_PLACE + 3 );
                hero.skill_3.attachTo(hero);
            }
        }
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COOLDOWN, cooldown );
        bundle.put( CHARGE, charge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt( COOLDOWN );
        charge = bundle.getInt(CHARGE);
    }
}