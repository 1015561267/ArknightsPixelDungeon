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

package com.unifier.arknightspixeldungeon.items.scrolls;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.buffs.Invisibility;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {

    private String inventoryTitle(){
        return Messages.get(this, "inv_title");
    }

    protected boolean usableOnItem( Item item ){
        return true;
    }

    protected Class<?extends Bag> preferredBag = null;

    @Override
	public void doRead() {
		
		if (!isKnown()) {
			setKnown();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		
		GameScene.selectItem( itemSelector );
	}
	
	private void confirmCancelation() {
		GameScene.show( new WndOptions( Messages.titleCase(name()), Messages.get(this, "warning"),
				Messages.get(this, "yes"), Messages.get(this, "no") ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
				case 0:
					curUser.spendAndNext( TIME_TO_READ );
					identifiedByUse = false;

					Talent.afterItemUse(curItem);

					break;
				case 1:
					GameScene.selectItem( itemSelector );
					break;
				}
			}
			public void onBackPressed() {};
		} );
	}
	
	protected abstract void onItemSelected( Item item );

	protected static boolean identifiedByUse = false;

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return inventoryTitle();
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
            public void onSelect( Item item ) {

                //FIXME this safety check shouldn't be necessary
                //it would be better to eliminate the curItem static variable.
                if (!(curItem instanceof InventoryScroll)){
                    return;
                }

                if (item != null) {

                    ((InventoryScroll)curItem).onItemSelected( item );
                    ((InventoryScroll)curItem).readAnimation();

                    Sample.INSTANCE.play( Assets.SND_READ );
                    Invisibility.dispel();

                    Talent.afterItemUse(curItem);

                } else if (identifiedByUse && !((Scroll)curItem).ownedByBook) {

                    ((InventoryScroll)curItem).confirmCancelation();

                } else if (!((Scroll)curItem).ownedByBook) {

                    curItem.collect( curUser.belongings.backpack );

                }
		}
	};
}
