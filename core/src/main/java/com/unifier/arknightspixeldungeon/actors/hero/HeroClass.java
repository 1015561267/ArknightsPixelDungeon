/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.unifier.arknightspixeldungeon.actors.hero;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Challenges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.Shadowless;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.SheathedStrike;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.Unsheath;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Revolver;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.SniperRifle;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Vector;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.skills.Exusiai.AdjustTool;
import com.unifier.arknightspixeldungeon.items.BrokenSeal;
import com.unifier.arknightspixeldungeon.items.Generator;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.LevelTeleporter;
import com.unifier.arknightspixeldungeon.items.TomeOfMastery;
import com.unifier.arknightspixeldungeon.items.Torch;
import com.unifier.arknightspixeldungeon.items.armor.ClothArmor;
import com.unifier.arknightspixeldungeon.items.artifacts.Artifact;
import com.unifier.arknightspixeldungeon.items.artifacts.CloakOfShadows;
import com.unifier.arknightspixeldungeon.items.bags.PotionBandolier;
import com.unifier.arknightspixeldungeon.items.bags.ScrollHolder;
import com.unifier.arknightspixeldungeon.items.bags.VelvetPouch;
import com.unifier.arknightspixeldungeon.items.food.Food;
import com.unifier.arknightspixeldungeon.items.food.SmallRation;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfExperience;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfHealing;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfMindVision;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfStrength;
import com.unifier.arknightspixeldungeon.items.rings.Ring;
import com.unifier.arknightspixeldungeon.items.rings.RingOfHaste;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.unifier.arknightspixeldungeon.items.wands.WandOfMagicMissile;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Dagger;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Knuckles;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MagesStaff;
import com.unifier.arknightspixeldungeon.items.weapon.melee.WornShortsword;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Boomerang;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingStone;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.ui.TalentIcon;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import static com.unifier.arknightspixeldungeon.actors.hero.Talent.CONTINUOUS_ASSAULT;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.FAST_RECOVERY;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.REFLECT;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.SHADOWLESS;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.SHEATHED_STRIKE;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.UNSHEATH;
import static com.unifier.arknightspixeldungeon.actors.hero.Talent.WEAPON_ADAPT;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.WARDEN, HeroSubClass.SNIPER );

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}

        for(int j=0;j<3;j++) {
            Artifact artifact = Generator.randomArtifact();
            artifact.identify().collect();
        }

        Ring ring= new RingOfHaste();
		ring.level(6);
		ring.identify().collect();

        for(int j=0;j<10;j++) {
            /*new ScrollOfMagicalInfusion().identify().collect();
            new StoneOfAugmentation().collect();
            new ScrollOfUpgrade().identify().collect();
            new ScrollOfMagicMapping().identify().collect();
            new ScrollOfMagicMapping().identify().collect();
            new ScrollOfRage().identify().collect();
            new SmallRation().collect();
            new ScrollOfRemoveCurse().identify().collect();*/
            new PotionOfStrength().identify().collect();
            new ScrollOfMagicMapping().identify().collect();
        }

		for(int j=0;j<30;j++) {
			new PotionOfExperience().identify().collect();
		}

		//SandalsOfNature sandalsOfNature = new SandalsOfNature();
		//sandalsOfNature.identify().collect();

        //for(int j=0;j<100;j++) {
       //     Item seed = Generator.random(Generator.Category.SEED);
      //      seed.collect();
        //}


        //new ScrollOfRage().identify().collect();

        //new TomeOfMastery().collect();
        new Torch().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.identify().quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}
		
		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		new PotionOfHealing().identify();

		hero.skill_1 = new SheathedStrike();
		hero.skill_1.attachTo(hero);

		hero.skill_2 = new Unsheath();
        hero.skill_2.attachTo(hero);

        hero.skill_3 = new Shadowless();
        hero.skill_3.attachTo(hero);

        new LevelTeleporter().identify().collect();
        new ScrollOfMirrorImage().identify().collect();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;
		
		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		new ScrollOfUpgrade().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new ScrollOfMagicMapping().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Knuckles()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new PotionOfMindVision().identify();

		new TomeOfMastery().identify().collect();

		new AdjustTool().collect();

		hero.skill_1 = new Revolver();
        //((Revolver) hero.skill_1).doAttach(Attachment.RED_DOT_SIGHT);
        //((Revolver) hero.skill_1).doAttach(Attachment.BLACKJACK_SOLITAIRE);
		hero.skill_1.attachTo(hero);

		hero.skill_2 = new Vector();
        //((Vector) hero.skill_2).doAttach(Attachment.SILENCER);
        //((Vector) hero.skill_2).doAttach(Attachment.TACTICAL_FLASHLIGHT);
        //((Vector) hero.skill_2).doAttach(Attachment.ORIGINUMS_REFINING_CLIP);
        hero.skill_2.attachTo(hero);

		hero.skill_3  = new SniperRifle();
        //((Revolver) hero.skill_3).setGUN_SIGHT(Attachment.CLOSE_COMBAT_OPTICAL_SIGHT);
        //((Revolver) hero.skill_3).setBULLET(Attachment.HOLLOW_POINT_BULLET);
		hero.skill_3.attachTo(hero);
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		return subClasses;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
	}

    public String splashArt(){
        switch (this) {
            case WARRIOR: default:
                return Assets.WARRIORSPLASH;
            case MAGE:
                return Assets.MAGESPLASH;
            case ROGUE:
                return Assets.ROGUESPLASH;
            case HUNTRESS:
                return Assets.HUNTRESSSPLASH;
        }
    }

	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return new String[]{
					Messages.get(HeroClass.class, "warrior_perk1"),
					Messages.get(HeroClass.class, "warrior_perk2"),
					Messages.get(HeroClass.class, "warrior_perk3"),
					Messages.get(HeroClass.class, "warrior_perk4"),
					Messages.get(HeroClass.class, "warrior_perk5"),
			};
		case MAGE:
			return new String[]{
					Messages.get(HeroClass.class, "mage_perk1"),
					Messages.get(HeroClass.class, "mage_perk2"),
					Messages.get(HeroClass.class, "mage_perk3"),
					Messages.get(HeroClass.class, "mage_perk4"),
					Messages.get(HeroClass.class, "mage_perk5"),
			};
		case ROGUE:
			return new String[]{
					Messages.get(HeroClass.class, "rogue_perk1"),
					Messages.get(HeroClass.class, "rogue_perk2"),
					Messages.get(HeroClass.class, "rogue_perk3"),
					Messages.get(HeroClass.class, "rogue_perk4"),
					Messages.get(HeroClass.class, "rogue_perk5"),
			};
		case HUNTRESS:
			return new String[]{
					Messages.get(HeroClass.class, "huntress_perk1"),
					Messages.get(HeroClass.class, "huntress_perk2"),
					Messages.get(HeroClass.class, "huntress_perk3"),
					Messages.get(HeroClass.class, "huntress_perk4"),
					Messages.get(HeroClass.class, "huntress_perk5"),
			};
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}

    public boolean isUnlocked() {
            //always unlock on debug builds
            if (DeviceCompat.isDebug()) return true;

            switch (this){
                case WARRIOR: default:
                    return true;
                case MAGE:
                    //return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
                    return true;
                case ROGUE:
                   // return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
                return true;
                case HUNTRESS:
                    //return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
                return true;
            }
    }

    public String unlockMsg() {
        switch (this){
            case WARRIOR: default:
                return "";
            case MAGE:
                return Messages.get(HeroClass.class, "mage_unlock");
            case ROGUE:
                return Messages.get(HeroClass.class, "rogue_unlock");
            case HUNTRESS:
                return Messages.get(HeroClass.class, "huntress_unlock");
        }
    }

    public String desc(){
        return Messages.get(HeroClass.class, name()+"_desc");
    }

    public String shortDesc() {
        return Messages.get(HeroClass.class, name()+"_desc_short");
    }

    public String skillDesc(HeroClass heroClass){

	    String message="";

	    switch (heroClass){
            case WARRIOR:
            case MAGE:
            case ROGUE:
            case HUNTRESS:
                for(int i=1;i<=getSkillSize();i++){
                    message += Messages.get(HeroClass.class, name()+"_skill"+i+"_desc");
                    message += "\n\n";
                }
        }

        return message;
    }

    public String specificDesc(HeroClass heroClass){
        String message="";

        switch (heroClass){
            case WARRIOR:
            case MAGE:
            case ROGUE:
            case HUNTRESS:
                for(int i=1;i<=getSpecificSize();i++){
                    message += Messages.get(HeroClass.class, name()+"_specific"+i+"_desc");
                    message += "\n\n";
                }
        }

        return message;    }

    public Integer getSkillSize() {
        switch (this){
            case WARRIOR: default:
                return 3;
            case MAGE:
                return 3;
            case ROGUE:
                return 3;
            case HUNTRESS:
                return 3;
        }
    }


    public Image[] getSkillIcons() {
        switch (this){
            case WARRIOR: default:
                return new Image[]{
                    new TalentIcon(SHEATHED_STRIKE),new TalentIcon(UNSHEATH),new TalentIcon(SHADOWLESS)
                };
            case MAGE:
                return new Image[]{
                        new TalentIcon(SHEATHED_STRIKE),new TalentIcon(UNSHEATH),new TalentIcon(SHADOWLESS)
                };
                case ROGUE:
                return new Image[]{
                        new TalentIcon(SHEATHED_STRIKE),new TalentIcon(UNSHEATH),new TalentIcon(SHADOWLESS)
                };
                case HUNTRESS:
                return new Image[]{
                        new TalentIcon(SHEATHED_STRIKE),new TalentIcon(UNSHEATH),new TalentIcon(SHADOWLESS)
                };
        }
    }

    public Integer getSpecificSize() {
        switch (this){
            case WARRIOR: default:
                return 4;
            case MAGE:
                return 1;
            case ROGUE:
                return 1;
            case HUNTRESS:
                return 1;
        }
    }


    public Image[] getSpecificIcons() {
        switch (this){
            case WARRIOR: default:
                return new Image[]{
                        new TalentIcon(FAST_RECOVERY),new TalentIcon(WEAPON_ADAPT),new TalentIcon(REFLECT),new TalentIcon(CONTINUOUS_ASSAULT)
                };
            case MAGE:
                return new Image[]{
                        new TalentIcon(FAST_RECOVERY),new TalentIcon(WEAPON_ADAPT),new TalentIcon(REFLECT)
                };
            case ROGUE:
                return new Image[]{
                        new TalentIcon(FAST_RECOVERY),new TalentIcon(WEAPON_ADAPT),new TalentIcon(REFLECT)
                };
            case HUNTRESS:
                return new Image[]{
                        new TalentIcon(FAST_RECOVERY),new TalentIcon(WEAPON_ADAPT),new TalentIcon(REFLECT)
                };
        }
    }

    public Image[] getSkillDetailIcons(int order) {
        switch (this){
            case WARRIOR: default:
                switch (order){
                    case 1: return new Image[]{
                            new TalentIcon(Talent.SHEATH_THROW),new TalentIcon(Talent.REPRIMAND),new TalentIcon(Talent.PARRY),new TalentIcon(Talent.SEIZE_OPPORTUNITY)
                        };
                    case 2:  return new Image[]{
                            new TalentIcon(Talent.FLASH),new TalentIcon(Talent.SUN_CROSS),new TalentIcon(Talent.WIND_CUTTER),new TalentIcon(Talent.BOILING_KENSHIN)
                    };
                    case 3:  return new Image[]{
                            new TalentIcon(Talent.SONIC_CUTTING),new TalentIcon(Talent.SWORD_RAIN),new TalentIcon(Talent.CLOUD_CRACK)
                    };
                }

                return new Image[]{
                        new TalentIcon(SHEATHED_STRIKE),new TalentIcon(UNSHEATH),new TalentIcon(SHADOWLESS)
                };
            case MAGE:
                return new Image[]{

                };
            case ROGUE:
                return new Image[]{

                };
            case HUNTRESS:
                return new Image[]{

                };
        }
    }

}
