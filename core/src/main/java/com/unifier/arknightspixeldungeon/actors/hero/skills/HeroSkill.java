package com.unifier.arknightspixeldungeon.actors.hero.skills;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.windows.WndHeroSkill;
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

    @Override
    public abstract boolean act();

    public abstract void doAction();

    public abstract void doAfterAction();

    public abstract void getCoolDown(float amount);

    public float cooldownRatio() {//For now it means to handle cooldown for multi charge skill have problem when there are only one charge
        if(charge == getMaxCharge())
            return 0;
        else if(charge >= 0 ){
            return charge / getMaxCharge();
        }
        else
        return cooldown / rawCD();
    }

    public abstract boolean available();

    public String name() { return Messages.get(this, "name"); };

    public String otherInfo() {
        if (getMaxCharge() == charge) {
            return Messages.get(this, "chargeinfo",getMaxCharge(),charge);
        } else {
            return Messages.get(this, "cooldown", cooldown) + "\n\n" + Messages.get(this, "chargeinfo", getMaxCharge(), charge);
        }
    }

    public String desc() { return Messages.get(this, "desc");}

    public boolean useTargetting(){return false;}

    public void handleLongClick() {
        GameScene.show(new WndHeroSkill(this));
    }

    public enum buffType {ACTIVE,PASSIVE};

    public buffType type = buffType.ACTIVE;

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