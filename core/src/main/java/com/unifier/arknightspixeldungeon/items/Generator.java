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

package com.unifier.arknightspixeldungeon.items;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.armor.ClothArmor;
import com.unifier.arknightspixeldungeon.items.armor.LeatherArmor;
import com.unifier.arknightspixeldungeon.items.armor.MailArmor;
import com.unifier.arknightspixeldungeon.items.armor.PlateArmor;
import com.unifier.arknightspixeldungeon.items.armor.ScaleArmor;
import com.unifier.arknightspixeldungeon.items.artifacts.AlchemistsToolkit;
import com.unifier.arknightspixeldungeon.items.artifacts.Artifact;
import com.unifier.arknightspixeldungeon.items.artifacts.CapeOfThorns;
import com.unifier.arknightspixeldungeon.items.artifacts.ChaliceOfBlood;
import com.unifier.arknightspixeldungeon.items.artifacts.CloakOfShadows;
import com.unifier.arknightspixeldungeon.items.artifacts.DriedRose;
import com.unifier.arknightspixeldungeon.items.artifacts.EtherealChains;
import com.unifier.arknightspixeldungeon.items.artifacts.HornOfPlenty;
import com.unifier.arknightspixeldungeon.items.artifacts.LloydsBeacon;
import com.unifier.arknightspixeldungeon.items.artifacts.MasterThievesArmband;
import com.unifier.arknightspixeldungeon.items.artifacts.SandalsOfNature;
import com.unifier.arknightspixeldungeon.items.artifacts.TalismanOfForesight;
import com.unifier.arknightspixeldungeon.items.artifacts.TimekeepersHourglass;
import com.unifier.arknightspixeldungeon.items.artifacts.UnstableSpellbook;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.items.food.Food;
import com.unifier.arknightspixeldungeon.items.food.MysteryMeat;
import com.unifier.arknightspixeldungeon.items.food.Pasty;
import com.unifier.arknightspixeldungeon.items.potions.Potion;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfExperience;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfFrost;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfHealing;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfInvisibility;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfLevitation;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfLiquidFlame;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfMight;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfMindVision;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfParalyticGas;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfPurity;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfStrength;
import com.unifier.arknightspixeldungeon.items.potions.PotionOfToxicGas;
import com.unifier.arknightspixeldungeon.items.rings.Ring;
import com.unifier.arknightspixeldungeon.items.rings.RingOfAccuracy;
import com.unifier.arknightspixeldungeon.items.rings.RingOfElements;
import com.unifier.arknightspixeldungeon.items.rings.RingOfEnergy;
import com.unifier.arknightspixeldungeon.items.rings.RingOfEvasion;
import com.unifier.arknightspixeldungeon.items.rings.RingOfForce;
import com.unifier.arknightspixeldungeon.items.rings.RingOfFuror;
import com.unifier.arknightspixeldungeon.items.rings.RingOfHaste;
import com.unifier.arknightspixeldungeon.items.rings.RingOfMight;
import com.unifier.arknightspixeldungeon.items.rings.RingOfSharpshooting;
import com.unifier.arknightspixeldungeon.items.rings.RingOfTenacity;
import com.unifier.arknightspixeldungeon.items.rings.RingOfWealth;
import com.unifier.arknightspixeldungeon.items.scrolls.Scroll;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfIdentify;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfLullaby;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRage;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRecharging;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfTerror;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.unifier.arknightspixeldungeon.items.wands.Wand;
import com.unifier.arknightspixeldungeon.items.wands.WandOfBlastWave;
import com.unifier.arknightspixeldungeon.items.wands.WandOfCorrosion;
import com.unifier.arknightspixeldungeon.items.wands.WandOfCorruption;
import com.unifier.arknightspixeldungeon.items.wands.WandOfDisintegration;
import com.unifier.arknightspixeldungeon.items.wands.WandOfFireblast;
import com.unifier.arknightspixeldungeon.items.wands.WandOfFrost;
import com.unifier.arknightspixeldungeon.items.wands.WandOfLightning;
import com.unifier.arknightspixeldungeon.items.wands.WandOfMagicMissile;
import com.unifier.arknightspixeldungeon.items.wands.WandOfPrismaticLight;
import com.unifier.arknightspixeldungeon.items.wands.WandOfRegrowth;
import com.unifier.arknightspixeldungeon.items.wands.WandOfTransfusion;
import com.unifier.arknightspixeldungeon.items.weapon.melee.AssassinsBlade;
import com.unifier.arknightspixeldungeon.items.weapon.melee.BattleAxe;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Crossbow;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Dagger;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Dirk;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Flail;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Gauntlet;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Glaive;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Greataxe;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Greatshield;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Greatsword;
import com.unifier.arknightspixeldungeon.items.weapon.melee.HandAxe;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Knuckles;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Longsword;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Mace;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MagesStaff;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Quarterstaff;
import com.unifier.arknightspixeldungeon.items.weapon.melee.RoundShield;
import com.unifier.arknightspixeldungeon.items.weapon.melee.RunicBlade;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Sai;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Scimitar;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Shortsword;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Spear;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Sword;
import com.unifier.arknightspixeldungeon.items.weapon.melee.WarHammer;
import com.unifier.arknightspixeldungeon.items.weapon.melee.Whip;
import com.unifier.arknightspixeldungeon.items.weapon.melee.WornShortsword;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Bolas;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.FishingSpear;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Javelin;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.MissileWeapon;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Shuriken;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Tomahawk;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.Trident;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.Dart;
import com.unifier.arknightspixeldungeon.plants.BlandfruitBush;
import com.unifier.arknightspixeldungeon.plants.Blindweed;
import com.unifier.arknightspixeldungeon.plants.Dreamfoil;
import com.unifier.arknightspixeldungeon.plants.Earthroot;
import com.unifier.arknightspixeldungeon.plants.Fadeleaf;
import com.unifier.arknightspixeldungeon.plants.Firebloom;
import com.unifier.arknightspixeldungeon.plants.Icecap;
import com.unifier.arknightspixeldungeon.plants.Plant;
import com.unifier.arknightspixeldungeon.plants.Rotberry;
import com.unifier.arknightspixeldungeon.plants.Sorrowmoss;
import com.unifier.arknightspixeldungeon.plants.Starflower;
import com.unifier.arknightspixeldungeon.plants.Stormvine;
import com.unifier.arknightspixeldungeon.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 6,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		POTION	( 20,   Potion.class ),
		SCROLL	( 20,   Scroll.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		SEED	( 0,    Plant.Seed.class ),
		
		FOOD	( 0,    Food.class ),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfIdentify.class,
					ScrollOfTeleportation.class,
					ScrollOfRemoveCurse.class,
					ScrollOfUpgrade.class,
					ScrollOfRecharging.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfTerror.class,
					ScrollOfLullaby.class,
					ScrollOfMagicalInfusion.class,
					ScrollOfPsionicBlast.class,
					ScrollOfMirrorImage.class };
			SCROLL.probs = new float[]{ 30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10 };
			
			POTION.classes = new Class<?>[]{
					PotionOfHealing.class,
					PotionOfExperience.class,
					PotionOfToxicGas.class,
					PotionOfParalyticGas.class,
					PotionOfLiquidFlame.class,
					PotionOfLevitation.class,
					PotionOfStrength.class,
					PotionOfMindVision.class,
					PotionOfPurity.class,
					PotionOfInvisibility.class,
					PotionOfMight.class,
					PotionOfFrost.class };
			POTION.probs = new float[]{ 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10 };
			
			//TODO: add last ones when implemented
			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					//WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					//WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Knuckles.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					Dart.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 1, 1 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 4, 3 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 4, 3 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class
			};
			MIS_T4.probs = new float[]{ 4, 3 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class
			};
			MIS_T5.probs = new float[]{ 4, 3 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
			
			SEED.classes = new Class<?>[]{
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Blindweed.Seed.class,
					Sungrass.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Rotberry.Seed.class,
					BlandfruitBush.Seed.class,
					Dreamfoil.Seed.class,
					Stormvine.Seed.class,
					Starflower.Seed.class};
			SEED.probs = new float[]{ 10, 10, 10, 10, 10, 10, 10, 0, 2, 10, 10, 1 };
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		try {
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			ArknightsPixelDungeon.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			ArknightsPixelDungeon.reportException(e);
			return null;
			
		}
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			ArknightsPixelDungeon.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
			MeleeWeapon w = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			ArknightsPixelDungeon.reportException(e);
			return null;
		}
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		try {
			Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
			MissileWeapon w = (MissileWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			ArknightsPixelDungeon.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}
			
			Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

			if (removeArtifact(art)) {
				Artifact artifact = art.newInstance();
				
				artifact.random();
				
				return artifact;
			} else {
				return null;
			}

		} catch (Exception e) {
			ArknightsPixelDungeon.reportException(e);
			return null;
		}
	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		if (bundle.contains(SPAWNED_ARTIFACTS)){
			for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
				removeArtifact(artifact);
			}
		//pre-0.6.1 saves
		} else if (bundle.contains("artifacts")) {
			String[] names = bundle.getStringArray("artifacts");
			Category cat = Category.ARTIFACT;

			for (String artifact : names)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
