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

package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.Recipe;
import com.unifier.arknightspixeldungeon.items.food.Blandfruit;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.Dart;
import com.unifier.arknightspixeldungeon.levels.Terrain;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.plants.BlandfruitBush;
import com.unifier.arknightspixeldungeon.plants.Plant;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.tiles.DungeonTerrainTilemap;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.ItemSlot;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class WndAlchemy extends Window {
	
	private static WndBlacksmith.ItemButton[] inputs = new WndBlacksmith.ItemButton[3];
	private ItemSlot output;
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	
	private RedButton btnCombine;
	
	private static final int WIDTH_P = 116;
	private static final int WIDTH_L = 160;
	
	private static final int BTN_SIZE	= 28;
	
	public WndAlchemy(){
		
		int w = WIDTH_P;
		
		int h = 0;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon(DungeonTerrainTilemap.tile(0, Terrain.ALCHEMY));
		titlebar.label( Messages.get(this, "title") );
		titlebar.setRect( 0, 0, w, 0 );
		add( titlebar );
		
		h += titlebar.height() + 2;
		
		RenderedTextBlock desc = PixelScene.renderTextBlock(6);
		desc.text( Messages.get(this, "text") );
		desc.setPos(0, h);
		desc.maxWidth(w);
		add(desc);
		
		h += desc.height() + 6;

		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new WndBlacksmith.ItemButton() {
					@Override
					protected void onClick() {
						super.onClick();
						if (item != null) {
							if (!item.collect()) {
								Dungeon.level.drop(item, Dungeon.hero.pos);
							}
							item = null;
							slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
						}
						GameScene.selectItem(itemSelector);
					}
				};
				inputs[i].setRect(10, h, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				h += BTN_SIZE + 2;
			}
		}
		
		btnCombine = new RedButton(""){
			Image arrow;
			
			@Override
			protected void createChildren() {
				super.createChildren();
				
				arrow = Icons.get(Icons.RESUME);
				add(arrow);
			}
			
			@Override
			protected void layout() {
				super.layout();
				arrow.x = x + (width - arrow.width)/2f;
				arrow.y = y + (height - arrow.height)/2f;
				PixelScene.align(arrow);
			}
			
			@Override
			public void enable(boolean value) {
				super.enable(value);
				if (value){
					arrow.tint(1, 1, 0, 1);
					arrow.alpha(1f);
					bg.alpha(1f);
				} else {
					arrow.color(0, 0, 0);
					arrow.alpha(0.6f);
					bg.alpha(0.6f);
				}
			}
			
			@Override
			protected void onClick() {
				super.onClick();
				combine();
			}
		};
		btnCombine.enable(false);
		btnCombine.setRect((w-30)/2f, inputs[1].top()+5, 30, inputs[1].height()-10);
		add(btnCombine);
		
		output = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null){
					GameScene.show(new WndInfoItem(item));
				}
			}
		};
		output.setRect(w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
		
		ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
		outputBG.x = output.left();
		outputBG.y = output.top();
		add(outputBG);
		
		add(output);
		output.visible = false;
		
		bubbleEmitter = new Emitter();
		smokeEmitter = new Emitter();
		bubbleEmitter.pos(outputBG.x + (BTN_SIZE-16)/2f, outputBG.y + (BTN_SIZE-16)/2f, 16, 16);
		smokeEmitter.pos(bubbleEmitter.x, bubbleEmitter.y, bubbleEmitter.width, bubbleEmitter.height);
		bubbleEmitter.autoKill = false;
		smokeEmitter.autoKill = false;
		add(bubbleEmitter);
		add(smokeEmitter);
		
		h += 4;
		
		float btnWidth = (w-14)/2f;
		
		RedButton btnRecipes = new RedButton(Messages.get(this, "recipes_title")){
			@Override
			protected void onClick() {
				super.onClick();
				ArknightsPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndAlchemy.class, "recipes_text")));
			}
		};
		btnRecipes.setRect(5, h, btnWidth, 18);
		PixelScene.align(btnRecipes);
		add(btnRecipes);
		
		RedButton btnClose = new RedButton(Messages.get(this, "close")){
			@Override
			protected void onClick() {
				super.onClick();
				onBackPressed();
			}
		};
		btnClose.setRect(w - 5 - btnWidth, h, btnWidth, 18);
		PixelScene.align(btnClose);
		add(btnClose);
		
		h += btnClose.height();
		
		resize(w, h);
	}

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(WndAlchemy.class, "select");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return ((item instanceof Plant.Seed && !(item instanceof BlandfruitBush.Seed)) || (item instanceof Blandfruit && ((Blandfruit) item).potionAttrib == null) || (item.getClass() == Dart.class));
            //This is only adjusted for now as the alchemy is few in 0.6.5(And I don't want to add so damned much alchemy items
        }

        @Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item == null) {
							if (item instanceof Dart) {
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
					updateState();
				}
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item;
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		if (recipe != null){
			output.item(recipe.sampleOutput(ingredients));
			output.visible = true;
			btnCombine.enable(true);
			
		} else {
			btnCombine.enable(false);
			output.visible = false;
		}
		
	}

	private void combine(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		Item result = null;
		
		if (recipe != null){
			result = recipe.brew(ingredients);
		}
		
		if (result != null){
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.2f, 10 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
			
			output.item(result);
			if (!result.collect()){
				Dungeon.level.drop(result, Dungeon.hero.pos);
			}

			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item != null) {
						if (inputs[i].item.quantity() <= 0) {
							inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
							inputs[i].item = null;
						} else {
							inputs[i].slot.item(inputs[i].item);
						}
					}
				}
			}
			
			btnCombine.enable(false);
		}
		
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] != null && inputs[i].item != null) {
					if (!inputs[i].item.collect()) {
						Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
					}
				}
				inputs[i] = null;
			}
		}
		super.destroy();
	}

	private static final String ALCHEMY_INPUTS = "alchemy_inputs";

	public static void storeInBundle( Bundle b ){
		synchronized ( inputs ){
			ArrayList<Item> items = new ArrayList<>();
			for (WndBlacksmith.ItemButton i : inputs){
				if (i != null && i.item != null){
					items.add(i.item);
				}
			}
			if (!items.isEmpty()){
				b.put( ALCHEMY_INPUTS, items );
			}
		}
	}

	public static void restoreFromBundle( Bundle b, Hero h ){

		if (b.contains(ALCHEMY_INPUTS)){
			for (Bundlable item : b.getCollection(ALCHEMY_INPUTS)){

				//try to add normally, force-add otherwise.
				if (!((Item)item).collect(h.belongings.backpack)){
					h.belongings.backpack.items.add((Item)item);
				}
			}
		}

	}
}
