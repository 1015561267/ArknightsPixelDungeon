package com.unifier.arknightspixeldungeon.actors.hero;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.EmergencyRecoveryTracker;
import com.unifier.arknightspixeldungeon.actors.buffs.FlavourBuff;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.TomeOfMastery;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.food.Food;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
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
    },EMERGENCY_RECOVERY(21,2){
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
        public boolean needItem(){return true;}
        @Override
        public ArrayList<Talent> Mutex() {
            return new ArrayList<Talent>() {{
                add(RESENTMENT);
            }};
        }
    },
    RESENTMENT(26, 1,2) {
        @Override
        public boolean needItem(){return true;}
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
            return Dungeon.hero.hasTalent(VIGILANCE) || Dungeon.hero.hasTalent(EMERGENCY_RECOVERY);
        }
    },
    FORMATION_BREAKER(36, 3) {
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
                add(FORMATION_BREAKER);
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
                add(FAULTLESS_DEFENSE);
            }};
        }
    },
    FAULTLESS_DEFENSE(39,3) {
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
    EVIL_ABHORRENCE(41, 1,3) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RESENTMENT);
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
    WEAPON_THROW(48,4), SEIZE_OPPORTUNITY(49,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHEATHED_STRIKE);
        }
    },CONSISTENT_PRINCIPLE(50,4), HEART_STRIKER(51,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(UNSHEATH);
        }
    },
    BOTHSIDE_ATTACK(52,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(CONTINUOUS_ASSAULT);
        }
    },
    CRIMSON_RAGE(53,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RESENTMENT);
        }
    },
    CRIMSON_EXTENSION(54,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHADOWLESS);
        }
    },
    SWORD_RAIN(55,4) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(SHADOWLESS);
        }
    },

    //Chen Tier5
    FLOWING_WATER(64, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(FORMATION_BREAKER);
        }
    },
    SLASH_ECHO(65, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(WIND_CUTTER);
        }
    },
    LIGHTNING_REFLEXES(66, 1,5) {
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
    }, FURY_RAMPAGE(68, 1,5) {
        @Override
        public boolean PreconditionFulfilled() {
            return Dungeon.hero.hasTalent(RESENTMENT);
        }
    }, MOTION_ACCUMULATION(69, 1,5) {
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

    public static int[] tierLevelThresholds = new int[]{0, 1, 7, 15, 21, 27, 31};//Note that you have one point at level 1,and tier 5 point only gained at level 28 and 30

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

        if(talent.needItem())
        {
            TomeOfMastery tomeOfMastery = hero.belongings.getItem( TomeOfMastery.class );
            if(tomeOfMastery == null) {
                return checkResult.NEED_ITEM;
            }
        }

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
            case 1:Collections.addAll(tierTalents, SHEATH_THROW,REPRIMAND,PARRY,VIGILANCE,LAST_CHANCE,EMERGENCY_RECOVERY,UNSHEATH,FLASH, REFLECT,CONTINUOUS_ASSAULT,RESENTMENT, WEAPON_ADAPT);break;
            case 2:Collections.addAll(tierTalents, SHEATH_BOUNCE,WELL_PREPARED,COUNTER_STRIKE,RALLY_FORCE,FORMATION_BREAKER, WIND_CUTTER,SKILLFUL_GUARD, FAULTLESS_DEFENSE,DEADLY_COMBO,EVIL_ABHORRENCE,SHADOWLESS,LIGHT_WEAPON_MASTERY,SWORD_WEAPON_MASTERY,HEAVY_WEAPON_MASTERY);break;
            case 3:Collections.addAll(tierTalents, WEAPON_THROW, SEIZE_OPPORTUNITY,CONSISTENT_PRINCIPLE,HEART_STRIKER,BOTHSIDE_ATTACK,CRIMSON_RAGE,SWORD_RAIN,CRIMSON_EXTENSION);break;
            case 4:Collections.addAll(tierTalents, FLOWING_WATER, SLASH_ECHO, LIGHTNING_REFLEXES,MORTAL_SKILL,FURY_RAMPAGE,MOTION_ACCUMULATION,BLADE_STORM,FULL_SUPPRESSION);break;
            default:break;
        }
    }

    public static void onTalentUpgraded(Hero hero, Talent talent) {
        if(talent == EMERGENCY_RECOVERY)
        {
            Buff.affect(hero, EmergencyRecoveryTracker.class);
        }

        else if(talent == LAST_CHANCE)
        {
            Buff.affect(hero,LastChanceTracker.class);
        }
    }

    public static class LastChanceTracker extends Buff{};

    public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
        if (hero.hasTalent( FAST_RECOVERY )){
            //10%/20% HP healed,another 10% when below 30% health
            int recovery = 0;
            if (hero.HP <= hero.HT * 0.3f)
            {
                recovery += Math.ceil(hero.HT * 0.1f);
            }
            recovery += Math.ceil(hero.HT) * 0.1f * hero.pointsInTalent(FAST_RECOVERY);
            hero.heal(FAST_RECOVERY,recovery);
        }
        if (hero.hasTalent(VIGILANCE)){
            if (hero.cooldown() > 0) {
                Buff.affect(hero, VigilanceModifier.class, Food.TIME_TO_EAT);
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
                item.cursedKnown = true;
                return false;
            }
            else {
                item.identify();
                return true;
            }
        }

        return true;
    }

    public static int onAttackProc(Hero hero, Char enemy, int dmg ){
        if (hero.hasTalent(Talent.PREEMPTIVE_STRIKE) && Random.Float() < 0.1f + 0.2f * hero.pointsInTalent(PREEMPTIVE_STRIKE)
                && enemy instanceof Mob && enemy.HP >= Math.floor(enemy.HT * (0.9f - 0.2f * hero.pointsInTalent(PREEMPTIVE_STRIKE)))
                && enemy.buff(PreemptiveStrikeUsedTracker.class) == null) // Have 30% possibility when enemy above 70%HP at level 1 and 50% possibility when enemy above 50%HP at level 2
             {
                Buff.affect(enemy, PreemptiveStrikeUsedTracker.class);
                Buff.affect(Dungeon.hero, PreemptiveStrikeActiveTracker.class);//See Hero.attackDelay()
             }
        return dmg;
    }
    //Well damn the onAttackProc() cannot return things like you can have extra turns,I have to make two buffs,one show if the talent should work and the other inform that the talent effect should be in use
    public static class PreemptiveStrikeUsedTracker extends Buff{};
    public static class PreemptiveStrikeActiveTracker extends Buff{};
}