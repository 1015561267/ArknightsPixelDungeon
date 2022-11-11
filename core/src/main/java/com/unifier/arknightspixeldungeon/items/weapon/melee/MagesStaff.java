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

package com.unifier.arknightspixeldungeon.items.weapon.melee;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Badges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.HeroSubClass;
import com.unifier.arknightspixeldungeon.effects.particles.ElmoParticle;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.items.bags.MagicalHolster;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfRecharging;
import com.unifier.arknightspixeldungeon.items.wands.Wand;
import com.unifier.arknightspixeldungeon.items.wands.WandOfCorrosion;
import com.unifier.arknightspixeldungeon.items.wands.WandOfCorruption;
import com.unifier.arknightspixeldungeon.items.wands.WandOfDisintegration;
import com.unifier.arknightspixeldungeon.items.wands.WandOfRegrowth;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndOptions;
import com.unifier.arknightspixeldungeon.windows.WndUseItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagesStaff extends MeleeWeapon {

	private Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	{
		image = ItemSpriteSheet.MAGES_STAFF;

		tier = 1;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
		bones = false;
	}

	public MagesStaff() {
		wand = null;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //8 base damage, down from 10
				lvl*(tier+1);   //scaling unaffected
	}

	public MagesStaff(Wand wand){
		this();
		wand.identify();
		wand.cursed = false;
		this.wand = wand;
		wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
		wand.curCharges = wand.maxCharges;
		name = Messages.get(wand, "staff_name");
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (wand!= null && wand.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void activate( Char ch ) {
		if(wand != null) wand.charge( ch, STAFF_SCALE_FACTOR );
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_IMBUE)) {

			curUser = hero;
            GameScene.selectItem(itemSelector);

		} else if (action.equals(AC_ZAP)){

			if (wand == null) {
				GameScene.show(new WndUseItem(null, this));
				return;
			}

			wand.execute(hero, AC_ZAP);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (wand != null && Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE) {
			if (wand.curCharges < wand.maxCharges) wand.partialCharge += 0.33f;
			ScrollOfRecharging.charge((Hero)attacker);
			wand.onHit(this, attacker, defender, damage);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = super.reachFactor(owner);
		if (owner instanceof Hero
				&& wand instanceof WandOfDisintegration
				&& ((Hero)owner).subClass == HeroSubClass.BATTLEMAGE){
			reach++;
		}
		return reach;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null && wand != null) {
				wand.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		if (wand != null) wand.stopCharging();
	}

	public Item imbueWand(Wand wand, Char owner){

		wand.cursed = false;
		this.wand = null;

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level(), wand.level());

		//if the staff's level is being overridden by the wand, preserve 1 upgrade
		if (wand.level() >= this.level() && this.level() > 0) targetLevel++;

		int staffLevelDiff = targetLevel - this.level();
		if (staffLevelDiff > 0)
			this.upgrade(staffLevelDiff);
		else if (staffLevelDiff < 0)
			this.degrade(Math.abs(staffLevelDiff));

		int wandLevelDiff = targetLevel - wand.level();
		if (wandLevelDiff > 0)
			wand.upgrade(wandLevelDiff);
		else if (wandLevelDiff < 0)
			wand.degrade(Math.abs(wandLevelDiff));

		this.wand = wand;
		wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
		wand.curCharges = wand.maxCharges;
		wand.identify();
		if (owner != null) wand.charge(owner);

		name = Messages.get(wand, "staff_name");

		//This is necessary to reset any particles.
		//FIXME this is gross, should implement a better way to fully reset quickslot visuals
		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			Dungeon.quickslot.clearSlot(slot);
			updateQuickslot();
			Dungeon.quickslot.setSlot( slot, this );
			updateQuickslot();
		}
		
		Badges.validateItemLevelAquired(this);

		return this;
	}
	
	public void gainCharge( float amt ){
		if (wand != null){
			wand.gainCharge(amt);
		}
	}

	public Class<?extends Wand> wandClass(){
		return wand != null ? wand.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.upgrade();
			//gives the wand one additional charge
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			wand.curCharges = Math.min(wand.curCharges + 1, 10);
			updateQuickslot();
		}

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.degrade();
			//gives the wand one additional charge
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			wand.curCharges = curCharges-1;
			updateQuickslot();
		}

		return this;
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String info() {
		String info = super.info();

		if (wand == null){
			info += "\n\n" + Messages.get(this, "no_wand");
		} else {
			info += "\n\n" + Messages.get(this, "has_wand", Messages.get(wand, "name")) + " " + wand.statsDesc();
		}

		return info;
	}

	@Override
	public Emitter emitter() {
		if (wand == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(12.5f, 3);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.1f);
		return emitter;
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		if (wand != null) {
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			name = Messages.get(wand, "staff_name");
		}
	}

	@Override
	public int price() {
		return 0;
	}

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(MagesStaff.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return MagicalHolster.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Wand;
        }

        @Override
        public void onSelect( final Item item ) {
            if (item != null) {

                if (!item.isIdentified()) {
                    GLog.w(Messages.get(MagesStaff.class, "id_first"));
                    return;
                } else if (item.cursed){
                    GLog.w(Messages.get(MagesStaff.class, "cursed"));
                    return;
                }

                if (wand == null){
                    applyWand((Wand)item);
                } else {
                    int newLevel;
                    int itemLevel = item.trueLevel();
                    if (itemLevel >= trueLevel()){
                        if (trueLevel() > 0)    newLevel = itemLevel + 1;
                        else                    newLevel = itemLevel;
                    } else {
                        newLevel = trueLevel();
                    }

                    String bodyText = Messages.get(MagesStaff.class, "imbue_desc", newLevel);

                    //int preservesLeft = Dungeon.hero.hasTalent(Talent.WAND_PRESERVATION) ? 5 : 0;

                    //if (Dungeon.hero.buff(Talent.WandPreservationCounter.class) != null){
                    //    preservesLeft -= Dungeon.hero.buff(Talent.WandPreservationCounter.class).count();
                    //}
                    //if (Dungeon.hero.hasTalent(Talent.WAND_PRESERVATION)){
                    //    int preserveChance = Dungeon.hero.pointsInTalent(Talent.WAND_PRESERVATION) == 1 ? 67 : 100;
                     //   bodyText += "\n\n" + Messages.get(MagesStaff.class, "imbue_talent", preserveChance, preservesLeft);
                    //} else {
                    //    bodyText += "\n\n" + Messages.get(MagesStaff.class, "imbue_lost");
                    //}

                    GameScene.show(
                            new WndOptions(new ItemSprite(item),
                                    Messages.titleCase(item.name()),
                                    bodyText,
                                    Messages.get(MagesStaff.class, "yes"),
                                    Messages.get(MagesStaff.class, "no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        applyWand((Wand)item);
                                    }
                                }
                            }
                    );
                }
            }
        }

        private void applyWand(Wand wand){
            Sample.INSTANCE.play(Assets.SND_BURNING);
            curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
            evoke(curUser);

            Dungeon.quickslot.clearItem(wand);

            wand.detach(curUser.belongings.backpack);

            GLog.p( Messages.get(MagesStaff.class, "imbue", wand.name()));
            imbueWand( wand, curUser );

            updateQuickslot();
        }
    };

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((wand instanceof WandOfDisintegration)
					|| (wand instanceof WandOfCorruption)
					|| (wand instanceof WandOfCorrosion)
					|| (wand instanceof WandOfRegrowth));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}
