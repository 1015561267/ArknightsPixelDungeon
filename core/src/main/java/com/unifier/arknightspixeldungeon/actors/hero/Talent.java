package com.unifier.arknightspixeldungeon.actors.hero;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Bleeding;
import com.unifier.arknightspixeldungeon.actors.buffs.Bless;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Cripple;
import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.unifier.arknightspixeldungeon.actors.buffs.Healing;
import com.unifier.arknightspixeldungeon.actors.buffs.Hex;
import com.unifier.arknightspixeldungeon.actors.buffs.KineticDamage;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Regeneration;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.BladeStormTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.DragonScaleTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.RageTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.RallyForceTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.ReflectTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.TalentRelatedTracker.SharpJudgementTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.Vertigo;
import com.unifier.arknightspixeldungeon.actors.buffs.Vulnerable;
import com.unifier.arknightspixeldungeon.actors.buffs.Weakness;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.TalentSprite;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.artifacts.ChaliceOfBlood;
import com.unifier.arknightspixeldungeon.items.food.Food;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfMight;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfStrength;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.levels.features.Chasm;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import static com.unifier.arknightspixeldungeon.Dungeon.hero;

public enum Talent {
    //Chen
    //Chen Tier1
    SHEATHED_STRIKE(0 , 1),FAST_RECOVERY(1, 1), PREEMPTIVE_STRIKE(2 ,1), ARM_INTUITION(3, 1),
    //Chen Tier2
    SHEATH_THROW(16,2) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATHED_STRIKE);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(REPRIMAND);
                add(PARRY);
            }};
        }
    },
    REPRIMAND(17,2) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATHED_STRIKE);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(SHEATH_THROW);
                add(PARRY);
            }};
        }
    },
    PARRY(18,2) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATHED_STRIKE);
        }
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(SHEATH_THROW);
                add(REPRIMAND);
            }};
        }
    },
    VIGILANCE(19 , 2){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(FAST_RECOVERY);
        }
    },LAST_CHANCE(20,1,2){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(FAST_RECOVERY);
        }
    }, DRAGON_SCALE(21,2){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(FAST_RECOVERY);
        }
    },
    UNSHEATH(22,2), FLASH(23,2) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(UNSHEATH);
        }
    },
    REFLECT(24 , 2),
    CONTINUOUS_ASSAULT(25, 1, 2) {
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(RED_RAGE);
            }};
        }
    },
    RED_RAGE(26, 1,2) {
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(CONTINUOUS_ASSAULT);
            }};
        }
    },
    WEAPON_ADAPT(27 , 2),
    //Chen Tier3
    SHEATH_BOUNCE(32,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATH_THROW);
        }
    },
    WELL_PREPARED(33,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(REPRIMAND);
        }
    },
    COUNTER_STRIKE(34,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(PARRY);
        }
    },
    RALLY_FORCE(35,3){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(VIGILANCE) || Dungeon.hero.hasTalent(DRAGON_SCALE);
        }
    },
    SUN_CROSS(36, 3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(UNSHEATH);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(WIND_CUTTER);
            }};
        }
    },
    WIND_CUTTER(37, 3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(UNSHEATH);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(SUN_CROSS);
            }};
        }
    },
    SKILLFUL_GUARD(38,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(REFLECT);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(EYE_FOR_EYE);
            }};
        }
    },
    EYE_FOR_EYE(39,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(REFLECT);
        }

        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(SKILLFUL_GUARD);
            }};
        }
    },
    DEADLY_COMBO(40, 1,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(CONTINUOUS_ASSAULT);
        }
    },
    SCARLET_MOMENTUM(41, 1,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RED_RAGE);
        }
    },
    SHADOWLESS(42,3),
    LIGHT_WEAPON_MASTERY(43,3){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(WEAPON_ADAPT);
        }
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(HEAVY_WEAPON_MASTERY);
            }};
        }
    },
    SWORD_WEAPON_MASTERY(44,1,3){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(WEAPON_ADAPT);
        }
    },
    HEAVY_WEAPON_MASTERY(45,3){
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(WEAPON_ADAPT);
        }
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(LIGHT_WEAPON_MASTERY);
            }};
        }
    },
    //Chen Tier4
    WEAPON_THROW(48,1,4), SEIZE_OPPORTUNITY(49,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATHED_STRIKE);
        }
    }, FRUGALITY(50,1,4), BOILING_KENSHIN(51,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(UNSHEATH);
        }
    },
    BOTHSIDE_ATTACK(52,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(CONTINUOUS_ASSAULT);
        }
    },
    CRIMSON_RAMPAGE(53,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RED_RAGE);
        }
    },
    SONIC_CUTTING(54,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHADOWLESS);
        }
    },
    SWORD_RAIN(55,1,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHADOWLESS);
        }
    },

    //Chen Tier5
    FLOWING_WATER(64, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SUN_CROSS);
        }
    },
    SHARP_JUDGEMENT(65, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(WIND_CUTTER);
        }
    },
    DECISIVENESS(66, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(REFLECT);
        }
    },
    MORTAL_SKILL(67, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(CONTINUOUS_ASSAULT);
        }
    }, SURPASS_LIMIT(68, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RED_RAGE);
        }
    }, CLOUD_CRACK(69, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHADOWLESS);
        }
    },BLADE_STORM(70, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(LIGHT_WEAPON_MASTERY);
        }
    },
    FULL_SUPPRESSION(71, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(HEAVY_WEAPON_MASTERY);
        }
    };

    public static int[] tierLevelThresholds = new int[]{0, 1, 7, 15, 21, 27, 31};//Note that you have one point at level 1,
    // tier 4 point gained at level 22,24 and 26 while tier 5 point gained at level 28 and 30

    int icon;
    int maxPoints;
    int tier;

    Talent(int icon , int tier) {
        this(icon, 2, tier,false);
    }

    Talent(int icon, int maxPoints , int tier) {
        this(icon, maxPoints, tier ,false);
    }

    Talent(int icon, int maxPoints, int tier ,boolean needItem) {
        this.icon = icon;
        this.maxPoints = maxPoints;
        this.tier = tier;
    }

    public static void onItemCollected(Hero hero, Item item) {
    }

    public static void onItemIdentified(Hero hero, Item item) {
    }

    public int tier()
    {
        return tier;
    }

    public boolean needItem()
    {
        return false;
    }

    public boolean PreconditionFulfilled() {
        return true;
    }

    public ArrayList<Talent> Mutex() {
        return null;
    }

    public int icon() {
        return icon;
    }

    public int maxPoints() {
        return maxPoints;
    }

    public String title(){
        //TODO translate this
        return Messages.get(this, name() + ".title");
    }

    public String desc(){
        return Messages.get(this, name() + ".desc");
    }

    public enum checkResult {
        NOT_ENOUGH_POINTS_ALREADY_UPGRADED,
        NOT_ENOUGH_POINTS_RUN_OUT,
        NOT_ENOUGH_POINTS_LOW_LEVEL,
        NEED_ITEM,
        NEED_PRECONDITION,
        MUTEX_TALENT,
        AVAILABLE,
        ALREADY_UPGRADED_AVAILABLE,
        ALREADY_FULL
    }

    public static checkResult talentCheck(Talent talent) {

        if (hero.pointsInTalent(talent) == talent.maxPoints())
        {
            return checkResult.ALREADY_FULL;
        }

        if (!talent.PreconditionFulfilled()) {
            return checkResult.NEED_PRECONDITION;
        } else if (talent.Mutex() != null) {
            for (Talent t : talent.Mutex()) {
                if (Dungeon.hero.hasTalent(t)) {
                    return checkResult.MUTEX_TALENT;
                }
            }
        }

        if(Dungeon.hero.talentPointsAvailable(talent.tier) <= 0)
        {
            if(hero.hasTalent(talent))
            return checkResult.NOT_ENOUGH_POINTS_ALREADY_UPGRADED;
            else if(hero.reachTalentThresholds(talent))
                return checkResult.NOT_ENOUGH_POINTS_LOW_LEVEL;
            else return checkResult.NOT_ENOUGH_POINTS_RUN_OUT;
        }

        //if(talent.needItem())
        //{
        //    TomeOfMastery tomeOfMastery = hero.belongings.getItem( TomeOfMastery.class );
        //    if(tomeOfMastery == null) {
        //        return checkResult.NEED_ITEM;
        //    }
        //}

        if(hero.hasTalent(talent))
            return checkResult.ALREADY_UPGRADED_AVAILABLE;

        return checkResult.AVAILABLE;
    }

    public static final int MAX_TALENT_TIERS = 5;

    private static final String TALENT_TIER = "talents_tier_";

    public static void storeTalentsInBundle(Bundle bundle, Hero hero ){
        for (int i = 0; i < MAX_TALENT_TIERS; i++){
            if(hero.talents.isEmpty()){
                initClassTalents(hero);
            }
            LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
            Bundle tierBundle = new Bundle();
            for (Talent talent : tier.keySet()){
                if (tier.get(talent) > 0){
                    tierBundle.put(talent.name(), tier.get(talent));
                }
                if (tierBundle.contains(talent.name())){
                    tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
                }
            }
            bundle.put(TALENT_TIER+(i+1), tierBundle);
        }
    }

    public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
        if (hero.heroClass != null)     initClassTalents(hero);

        for (int i = 0; i < MAX_TALENT_TIERS; i++){
            LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
            Bundle tierBundle = bundle.contains(TALENT_TIER+(i+1)) ? bundle.getBundle(TALENT_TIER+(i+1)) : null;

            if (tierBundle != null){
                for (Talent talent : tier.keySet()){
                    if (tierBundle.contains(talent.name())){
                        tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
                    }
                }
            }
        }
    }

    public static void initClassTalents( Hero hero ){
        initClassTalents( hero.heroClass, hero.talents );
    }

    public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
        while (talents.size() < MAX_TALENT_TIERS){
            talents.add(new LinkedHashMap<>());
        }
        ArrayList<Talent> tierTalents = new ArrayList<>();
        for(int i=0 ; i <MAX_TALENT_TIERS ; i++)
        {
            switch (cls){
                case WARRIOR: default:
                    initChenTalents(tierTalents,i);
                    break;
            /*case MAGE:
              Collections.addAll(tierTalents, EMPOWERING_MEAL, SCHOLARS_INTUITION, TESTED_HYPOTHESIS, BACKUP_BARRIER);
              break;
            case ROGUE:
                Collections.addAll(tierTalents, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS);
                break;
            case HUNTRESS:
                Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID);
                break;*/
            }
            for (Talent talent : tierTalents){
                talents.get(i).put(talent, 0);
            }
            tierTalents.clear();
        }
    }

    private static void initChenTalents(  ArrayList<Talent> tierTalents , int tier){
        switch (tier)
        {
            case 0:Collections.addAll(tierTalents, SHEATHED_STRIKE,FAST_RECOVERY,PREEMPTIVE_STRIKE,ARM_INTUITION);break;
            case 1:Collections.addAll(tierTalents, SHEATH_THROW,REPRIMAND,PARRY,VIGILANCE,LAST_CHANCE, DRAGON_SCALE,UNSHEATH,FLASH, REFLECT,CONTINUOUS_ASSAULT, RED_RAGE, WEAPON_ADAPT);break;
            case 2:Collections.addAll(tierTalents, SHEATH_BOUNCE,WELL_PREPARED,COUNTER_STRIKE,RALLY_FORCE, SUN_CROSS, WIND_CUTTER,SKILLFUL_GUARD,EYE_FOR_EYE,DEADLY_COMBO, SCARLET_MOMENTUM,SHADOWLESS,LIGHT_WEAPON_MASTERY,SWORD_WEAPON_MASTERY,HEAVY_WEAPON_MASTERY);break;
            case 3:Collections.addAll(tierTalents, WEAPON_THROW, SEIZE_OPPORTUNITY, FRUGALITY, BOILING_KENSHIN,BOTHSIDE_ATTACK,CRIMSON_RAMPAGE,SWORD_RAIN, SONIC_CUTTING);break;
            case 4:Collections.addAll(tierTalents, FLOWING_WATER, SHARP_JUDGEMENT, DECISIVENESS,MORTAL_SKILL,SURPASS_LIMIT, CLOUD_CRACK,BLADE_STORM,FULL_SUPPRESSION);break;
            default:break;
        }
        //Mostly weapon damage modify can be found at MeleeWeapon.damageRoll
        //WEAPON_ADAPT have hit chance modify at MeleeWeapon.accuracyFactor
    }

    public static void onTalentUpgraded(Hero hero, Talent talent) {
        if(talent == DRAGON_SCALE) {
            if(hero.buff(DragonScaleTracker.class)!=null) {
                hero.buff(DragonScaleTracker.class).upgrade();
            } else Buff.affect(hero,DragonScaleTracker.class);
        }//see class file in buff for more info
        else if(talent == LAST_CHANCE) { Buff.affect(hero,LastChanceTracker.class); }
        else if(talent == REFLECT){
            if(hero.buff(ReflectTracker.class)==null) {
                Buff.affect(hero, ReflectTracker.class);
            }
        }
        else if(talent == RALLY_FORCE)
        {
            if(hero.buff(RallyForceTracker.class)!=null) {
                hero.buff(RallyForceTracker.class).doubledSpeed = true;
            } else Buff.affect(hero,RallyForceTracker.class);
        }
//        else if(talent == RED_RAGE){ Buff.affect(hero, RageTracker.class); }
    }

    public static class LastChanceTracker extends Buff{}

    public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
        if (hero.hasTalent( FAST_RECOVERY )){
            //10%/20% gradual HP healed,another instant 10% when below 30% health
            if (hero.HP <= hero.HT * 0.3f)
            {
                hero.heal(FAST_RECOVERY, (int) Math.ceil(hero.HT * 0.1f));
            }
            Buff.affect( hero, Healing.class ).stackHeal((int) (Math.ceil(hero.HT) * 0.1f * hero.pointsInTalent(FAST_RECOVERY)), 0.333f, 0);
            TalentSprite.show(hero, Talent.FAST_RECOVERY,TalentSprite.Phase.FADE_IN);
        }
        if (hero.hasTalent(VIGILANCE)){
            if (hero.cooldown() > 0) {
                Buff.affect(hero, VigilanceModifier.class, Food.TIME_TO_EAT);
                TalentSprite.show(hero, Talent.VIGILANCE,TalentSprite.Phase.FADE_IN);
            }
        }
    }

    public static class VigilanceModifier extends FlavourBuff {//See hero.damage
        { actPriority = HERO_PRIO+1; }
    }

    public static boolean onItemEquipped( Hero hero, Item item ){

        if(item instanceof Weapon || item instanceof Armor)
        {
            if(hero.pointsInTalent(ARM_INTUITION) == 2 && !item.isIdentified() && item.cursed && Random.Float() <= 0.3f)
            {
                TalentSprite.show(hero, Talent.ARM_INTUITION,TalentSprite.Phase.FADE_IN);
                item.cursedKnown = true;
                return false;
            }
            else if (hero.hasTalent(ARM_INTUITION))
            {
                item.identify();
                return true;
            }
        }

        return true;
    }

    public static int onAttackProc(Hero hero, Char enemy, int dmg ){

        if (enemy.buff(ReprimandTracker.class) != null) {
            if (hero.pointsInTalent(REPRIMAND) == 2) {
                dmg *= 1.5;
            }
            enemy.buff(ReprimandTracker.class).detach();
        }

        if (hero.hasTalent(Talent.PREEMPTIVE_STRIKE) && enemy instanceof Mob && enemy.HP >= Math.floor(enemy.HT * (0.9f - 0.2f * hero.pointsInTalent(PREEMPTIVE_STRIKE))) && enemy.buff(PreemptiveStrikeUsedTracker.class) == null ){// Have 30% possibility when enemy above 70%HP at level 1 and 50% possibility when enemy above 50%HP at level 2
            if(Random.Float() < 0.1f + 0.2f * hero.pointsInTalent(PREEMPTIVE_STRIKE)) {
                Buff.affect(Dungeon.hero, PreemptiveStrikeActiveTracker.class);//See Hero.attackDelay()
            }
            Buff.affect(enemy, PreemptiveStrikeUsedTracker.class);
        }

        if(hero.hasTalent(Talent.BLADE_STORM)){
            BladeStormTracker bladeStormTracker = hero.buff(BladeStormTracker.class);
            if (bladeStormTracker != null) {
                bladeStormTracker.refresh();
            }
            else Buff.affect(hero,BladeStormTracker.class);
        }

        if (hero.hasTalent(Talent.SHEATHED_STRIKE) && hero.buff(SheathedStrikeTracker1.class) != null) {
            dmg /= 2;
            Buff.affect(enemy,Vertigo.class,3f);
        }

        if (hero.hasTalent(Talent.SURPASS_LIMIT)) {
            float factor = Math.max(0,0.75f - (float)(hero.HP / hero.HT));
            dmg *= (1 + factor);
            int heal = (int)(dmg * (factor/2));
            hero.heal(null,heal);
            hero.sprite.emitter().burst(Speck.factory(Speck.HEALING),1);
        }

        return dmg;
    }
    //Well damn the onAttackProc() cannot return things like you can have extra turns,I have to make two buffs,one show if the talent should work and the other inform that the talent effect should be in use
    public static class PreemptiveStrikeUsedTracker extends Buff{}
    public static class PreemptiveStrikeActiveTracker extends Buff{}

    public static class SheathedStrikeTracker1 extends Buff{}
    public static class SheathedStrikeTracker2 extends FlavourBuff{}

    public static class ReprimandTracker extends FlavourBuff{}

    public static class ParryTrackerPrepare extends Buff{}
    public static class ParryTrackerUsing extends FlavourBuff{}

    public static class SeizeOpportunityTracker extends Buff{}

    public static class LightWeaponMasteryTracker extends Buff{}//because it affect enemy's defense,check MeleeWeapon.damageRoll for more info

    public static class BoilingKenshinTracker extends Buff{}//check Unsheath.java for more info

    public static class FlowingWaterTracker extends FlavourBuff{}
    public static class AnotherFlowingWaterTracker extends FlavourBuff{}

    public static void doAfterDamage(Hero hero, Char enemy, int effectiveDamage) {

        if(hero.pointsInTalent(HEAVY_WEAPON_MASTERY) == 2)
        {
            float acuRoll = Random.Float( hero.attackSkill( enemy ) / 2 );
            float defRoll = Random.Float( enemy.defenseSkill( hero ) );
            if (hero.buff(Bless.class) != null) acuRoll *= 1.25f;
            if (hero.buff(Hex.class) != null) acuRoll *= 0.8f;
            if (enemy.buff(Bless.class) != null) defRoll *= 1.25f;
            if (enemy.buff(Hex.class) != null) defRoll *= 0.8f;//FIXME Well may improve it with char.hit() later

            if(acuRoll > defRoll){
                int result = Random.Int(100);//20% Weakness,20% Vulnerable,20% Cripple,20% Bleeding,10% Hex,10% Paralysis
                if(result <= 20)
                {
                    Buff.prolong( enemy, Weakness.class, (int)Math.sqrt(effectiveDamage) + 5f );
                }else if(result <= 40){
                    Buff.prolong( enemy, Vulnerable.class, (int)Math.sqrt(effectiveDamage) + 5f );
                }else if(result <= 60){
                    Buff.prolong( enemy, Cripple.class, (int)Math.sqrt(effectiveDamage) + 5f );
                }else if(result <= 80){
                    Buff.affect( enemy, Bleeding.class).set(effectiveDamage/4);
                }else if(result <= 90){
                    Buff.prolong( enemy, Hex.class, (int)Math.sqrt(effectiveDamage) + 3f );
                }else{
                    Buff.prolong( enemy, Paralysis.class, (int)Math.sqrt(effectiveDamage) );
                }
            }
        }

        SheathedStrikeTracker1 sheathedStrikeTracker = hero.buff(SheathedStrikeTracker1.class);
        if (sheathedStrikeTracker != null) {
            sheathedStrikeTracker.detach();
        }

        if (!enemy.isAlive() && hero.hasTalent(Talent.CRIMSON_RAMPAGE)) {
            RageTracker rageTracker = Buff.affect(hero,RageTracker.class);
            rageTracker.rage = rageTracker.rage + 0.1f;
            rageTracker.rageLossBuffer = Math.max(rageTracker.rageLossBuffer,5);
        }
    }

    public static void onDodge() {
        if(hero.pointsInTalent(SKILLFUL_GUARD) == 2){
            if(hero.buff(ReflectTracker.class)!=null) {
                hero.buff(ReflectTracker.class).getCoolDown(10f);
            }
        }
    }


    public static int onDefenseProc(Hero hero,Object source,int damage)//it should be before damage taken,so check Hero.damage for it
    {
        if (source instanceof Chasm || source instanceof ChaliceOfBlood) {
            return damage;
        }

        if (hero.buff(Talent.VigilanceModifier.class) != null){
            if (hero.pointsInTalent(Talent.VIGILANCE) == 1)       damage = Math.round(damage*0.50f);
            else if (hero.pointsInTalent(Talent.VIGILANCE) == 2)  damage = Math.round(damage*0.25f);
        }

        if(hero.buff(DragonScaleTracker.class)!=null)
        {
            damage = hero.buff(DragonScaleTracker.class).affect(damage,source);
        }

        //if (hero.pointsInTalent(CRIMSON_RAMPAGE) == 2) {
        //    if (hero.buff(RageTracker.class) != null && hero.buff(RageTracker.class).rage > 0.8f) {
        //        damage = Math.round(damage * (1.8f - hero.buff(RageTracker.class).rage));
        //    }
        //}

        return damage;
    }

    public static void onHealthGain(Hero hero,Object source, int amount) {
        if(hero.buff(RallyForceTracker.class)!=null){
            hero.buff(RallyForceTracker.class).getCharge(source instanceof Regeneration ? amount * 2 : amount);//doubled amount for auto-regeneration
        }
    }

    public static int onHealthLose(Hero hero,Object source, int damage) {
        if(hero.buff(RageTracker.class)!=null) {

        }

        if(hero.buff(DragonScaleTracker.class)!=null){
            hero.buff(DragonScaleTracker.class).stack(damage,source);
        }

        if(hero.buff(SharpJudgementTracker.class)!=null && source instanceof Mob){
           Buff.detach(hero,SharpJudgementTracker.class);
        }


        return damage;
    }

    public static void afterReflectKill() {

        if(hero.hasTalent(Talent.EYE_FOR_EYE)){
            if(hero.buff(ReflectTracker.class)!=null) {
                hero.buff(ReflectTracker.class).getCoolDown(25f);
            }
        }

        if(hero.hasTalent(Talent.DECISIVENESS)){

        }
    }

    public static void doAfterReflect(int effectiveDamage) {

        if(hero.pointsInTalent(REFLECT) == 2){
            Buff.affect(hero, KineticDamage.class).setBonus(effectiveDamage/2);
        }

        if(hero.hasTalent(Talent.SKILLFUL_GUARD)){
            ArrayList<HeroSkill> pool = new ArrayList<>();

            if( hero.skill_1.charge < hero.skill_1.getMaxCharge() ){ pool.add(hero.skill_1); }
            if( hero.skill_2.charge < hero.skill_2.getMaxCharge() ){ pool.add(hero.skill_2); }
            if( hero.skill_3.charge < hero.skill_3.getMaxCharge()){ pool.add(hero.skill_3); }

            HeroSkill picked = Random.element(pool);

            if(picked!=null) picked.getCoolDown(picked.rawCD() * 0.2f);
        }

        if(hero.hasTalent(Talent.DECISIVENESS)){

        }
    }

    public static void afterItemUse(Item item) {
        if(hero.pointsInTalent(FRUGALITY) == 2){
            if(item instanceof PotionOfStrength || item instanceof PotionOfMight || item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion){
                return;
            }else {
                if(Random.Int(100)<25){
                    item.collect();
                }
            }
        }
    }

}