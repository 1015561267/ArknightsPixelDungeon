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
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.armor.ClassArmor;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class ArmorKit extends Item {

	private static final String TXT_UPGRADED		= "you applied the armor kit to upgrade your %s";
	
	private static final float TIME_TO_UPGRADE = 2;
	
	private static final String AC_APPLY = "APPLY";
	
	{
		image = ItemSpriteSheet.KIT;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action == AC_APPLY) {

			curUser = hero;
			GameScene.selectItem( itemSelector );
			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void upgrade( Armor armor ) {
		
		detach( curUser.belongings.backpack );
		
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.KIT ), 0.05f, 10 );
		curUser.spend( TIME_TO_UPGRADE );
		curUser.busy();
		
		GLog.w( Messages.get(this, "upgraded", armor.name()) );
		
		ClassArmor classArmor = ClassArmor.upgrade( curUser, armor );
		if (curUser.belongings.armor == armor) {
			
			curUser.belongings.armor = classArmor;
			((HeroSprite)curUser.sprite).updateArmor();
			classArmor.activate(curUser);
			
		} else {
			
			armor.detach( curUser.belongings.backpack );
			classArmor.collect( curUser.belongings.backpack );
			
		}
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_EVOKE );
	}

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(ArmorKit.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Armor;
        }

        @Override
		public void onSelect( Item item ) {
			if (item != null) {
				ArmorKit.this.upgrade( (Armor)item );
			}
		}
	};
}
