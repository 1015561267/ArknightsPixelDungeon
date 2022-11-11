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

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.hero.Belongings;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndItem;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class BrokenSeal extends Item {

	public static final String AC_AFFIX = "AFFIX";

	//only to be used from the quickslot, for tutorial purposes mostly.
	public static final String AC_INFO = "INFO_WINDOW";

	{
		image = ItemSpriteSheet.SEAL;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;

		defaultAction = AC_INFO;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_AFFIX);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_AFFIX)){
			curItem = this;
			GameScene.selectItem(armorSelector);
		} else if (action.equals(AC_INFO)) {
			GameScene.show(new WndItem(null, this));
		}
	}

	@Override
	//scroll of upgrade can be used directly once, same as upgrading armor the seal is affixed to then removing it.
	public boolean isUpgradable() {
		return level() == 0;
	}

    protected static WndBag.ItemSelector armorSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return  Messages.get(BrokenSeal.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Armor;
        }

        @Override
        public void onSelect( Item item ) {
            BrokenSeal seal = (BrokenSeal) curItem;
            if (item != null && item instanceof Armor) {
                Armor armor = (Armor)item;
                if (!armor.levelKnown){
                    GLog.w(Messages.get(BrokenSeal.class, "unknown_armor"));

                }

                //else if (armor.cursed && (seal.getGlyph() == null || !seal.getGlyph().curse())){
                //    GLog.w(Messages.get(BrokenSeal.class, "cursed_armor"));

                //} else if (armor.glyph != null && seal.getGlyph() != null
                //        && armor.glyph.getClass() != seal.getGlyph().getClass()) {
                //    GameScene.show(new WndOptions(new ItemSprite(seal),
                //            Messages.get(BrokenSeal.class, "choose_title"),
                //           Messages.get(BrokenSeal.class, "choose_desc"),
                //            armor.glyph.name(),
                //seal.getGlyph().name()){
                //      @Override
                //    protected void onSelect(int index) {
                //      if (index == 0) seal.setGlyph(null);
                //if index is 1, then the glyph transfer happens in affixSeal

                //    GLog.p(Messages.get(BrokenSeal.class, "affix"));
                //  Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                //Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
                //   armor.affixSeal(seal);
                //   seal.detach(Dungeon.hero.belongings.backpack);
                GLog.p(Messages.get(BrokenSeal.class, "affix"));
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                Sample.INSTANCE.play(Assets.SND_UNLOCK);
                armor.affixSeal((BrokenSeal)curItem);
                curItem.detach(Dungeon.hero.belongings.backpack);
            }
            //});

            //       } else {

        }
    };








	public static class WarriorShield extends Buff {

		private Armor armor;
		private float partialShield;

		@Override
		public synchronized boolean act() {
			if (armor == null) detach();
			else if (armor.isEquipped((Hero)target)) {
				if (target.SHLD < maxShield()){
					partialShield += 1/(35*Math.pow(0.885f, (maxShield() - target.SHLD - 1)));
				}
			}
			while (partialShield >= 1){
				target.SHLD++;
				partialShield--;
			}
			spend(TICK);
			return true;
		}

		public synchronized void setArmor(Armor arm){
			armor = arm;
		}

		public synchronized int maxShield() {
			return 1 + armor.tier + armor.level();
		}
	}
}
