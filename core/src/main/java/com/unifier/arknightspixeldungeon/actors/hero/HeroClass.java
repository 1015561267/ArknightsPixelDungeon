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
import com.unifier.arknightspixeldungeon.Exusiai.AdjustTool;
import com.unifier.arknightspixeldungeon.Exusiai.skills.Exusiai.ExusiaiSkill_1;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.Shadowless;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.SheathedStrike;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Chen.Unsheath;
import com.unifier.arknightspixeldungeon.items.BrokenSeal;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.LevelTeleporter;
import com.unifier.arknightspixeldungeon.items.TestItem;
import com.unifier.arknightspixeldungeon.items.TomeOfMastery;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.armor.ClothArmor;
import com.unifier.arknightspixeldungeon.items.artifacts.CloakOfShadows;
import com.unifier.arknightspixeldungeon.items.bags.PotionBandolier;
import com.unifier.arknightspixeldungeon.items.bags.ScrollHolder;
import com.unifier.arknightspixeldungeon.items.bags.VelvetPouch;
import com.unifier.arknightspixeldungeon.items.food.Food;
import com.unifier.arknightspixeldungeon.items.food.SmallRation;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfExperience;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfHealing;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfInvisibility;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfMindVision;
import com.unifier.arknightspixeldungeon.items.rings.Ring;
import com.unifier.arknightspixeldungeon.items.rings.RingOfHaste;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRage;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.unifier.arknightspixeldungeon.items.stones.StoneOfAugmentation;
import com.unifier.arknightspixeldungeon.items.wands.WandOfMagicMissile;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Dagger;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Flail;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Knuckles;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MagesStaff;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Sai;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Spear;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Whip;
import com.unifier.arknightspixeldungeon.items.weapon.melee.WornShortsword;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Boomerang;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingStone;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

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


        Ring ring= new RingOfHaste();
		ring.level(6);
		ring.identify().collect();

        for(int j=0;j<10;j++) {
            new ScrollOfMagicalInfusion().identify().collect();
            new StoneOfAugmentation().collect();
            new ScrollOfUpgrade().identify().collect();
            new ScrollOfMagicMapping().identify().collect();
            new ScrollOfMagicMapping().identify().collect();
            new ScrollOfRage().identify().collect();
            new SmallRation().collect();
            new ScrollOfRemoveCurse().identify().collect();
        }

		for(int j=0;j<30;j++) {
			new PotionOfExperience().identify().collect();
		}

        //new ScrollOfRage().identify().collect();

        //new TomeOfMastery().collect();

        WornShortsword sword = new WornShortsword();
        sword.upgrade();
        sword.cursed = true;
        sword.enchant(Weapon.Enchantment.randomCurse());
        sword.identify().collect();

        ClothArmor armor = new ClothArmor();
        armor.upgrade();
        armor.cursed = true;
        armor.inscribe(Armor.Glyph.randomCurse());
        armor.identify().collect();

        Weapon weapon = new Flail();
        weapon.identify();
        weapon.collect();

        weapon = new Spear();
        weapon.identify();
        weapon.collect();

        weapon = new Whip();
        weapon.identify();
        weapon.collect();

        weapon = new Sai();
        weapon.identify();
        weapon.collect();
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
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

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

		hero.skill_1 = new ExusiaiSkill_1();
		hero.skill_1.attachTo(hero);

		hero.skill_2 = new Unsheath();
		hero.skill_2.attachTo(hero);

		hero.skill_3 = new Shadowless();
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
}
