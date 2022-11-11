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

package com.unifier.arknightspixeldungeon.actors.mobs.npcs;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.particles.ElmoParticle;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ShopkeeperSprite;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		flee();
	}
	
	@Override
	public void add( Buff buff ) {
		flee();
	}
	
	public void flee() {
		for (Heap heap: Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				heap.destroy();
			}
		}
		
		destroy();
		
		sprite.killAndErase();
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
	}
	
	@Override
	public boolean reset() {
		return true;
	}

    //shopkeepers are greedy!
    public static int sellPrice(Item item){
        return item.price() * 5 * (Dungeon.depth / 5 + 1);
    }

    public static WndBag sell() {
        return GameScene.selectItem( itemSelector );
    }

    public static boolean canSell(Item item){
        if (item.price() <= 0)                                              return false;
        if (item.unique && !item.stackable)                                 return false;
        if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
        if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
        return true;
    }

    private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(Shopkeeper.class, "sell");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return Shopkeeper.canSell(item);
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show( new WndTradeItem( item, parentWnd ) );
            }
        }
    };

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                sell();
            }
        });
        return true;
    }

}
