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

package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.armor.Armor;
import com.unifier.arknightspixeldungeon.items.potions.Potion;
import com.unifier.arknightspixeldungeon.items.scrolls.Scroll;
import com.unifier.arknightspixeldungeon.items.weapon.Weapon;
import com.unifier.arknightspixeldungeon.items.weapon.melee.MeleeWeapon;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.utils.Rect;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int FADED       = 0x999999;
	public static final int WARNING		= 0xFF8800;
    public static final int ENHANCED	= 0x3399FF;
    public static final int MASTERED	= 0xFFFF44;
    public static final int CURSE_INFUSED	= 0x8800FF;
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

    private Rect margin = new Rect();


    //Evan changed the name of these parameters,well I wonder how he remember which one place where...
    // bottomRightIcon -> itemIcon
    // topLeft -> status
    // topRight -> extra
    // bottomRight -> level

	protected ItemSprite sprite;
	protected Item       item;
    protected BitmapText topLeft;
    //FIXME it is called extra for now but I thought the old is better and able to support more condition
	//protected BitmapText topRight;
    protected BitmapText extra;
    protected BitmapText bottomRight;
	protected Image      bottomRightIcon;
	protected boolean    iconVisible = true;

    //protected BitmapText extra;
	
	private static final String TXT_STRENGTH	= ":%d";
	private static final String TXT_TYPICAL_STR	= "%d?";
	private static final String TXT_KEY_DEPTH	= "\u007F%d";

	private static final String TXT_LEVEL	= "%+d";
	private static final String TXT_CURSED    = "";//"-";

	// Special "virtual items"
	public static final Item CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CHEST; };
	};
	public static final Item LOCKED_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.LOCKED_CHEST; };
	};
	public static final Item CRYSTAL_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CRYSTAL_CHEST; };
	};
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; };
	};
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; };
	};
	public static final Item REMAINS = new Item() {
		public int image() { return ItemSpriteSheet.REMAINS; };
	};
	
	public ItemSlot() {
		super();
        sprite.visible(false);
		enable(false);
	}
	
	public ItemSlot( Item item ) {
		this();
		item( item );
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();

        sprite = new ItemSprite();
		add( sprite );
		
		topLeft = new BitmapText( PixelScene.pixelFont);
		add( topLeft );
		
		extra = new BitmapText( PixelScene.pixelFont);
		add(extra);
		
		bottomRight = new BitmapText( PixelScene.pixelFont);
		add( bottomRight );
	}
	
	@Override
	protected void layout() {
		super.layout();

        sprite.x = x + (width - sprite.width) / 2f;
        sprite.y = y + (height - sprite.height) / 2f;
		PixelScene.align(sprite);
		
		if (topLeft != null) {
			topLeft.measure();
			if (topLeft.width > width){
				topLeft.scale.set(PixelScene.align(0.8f));
			} else {
				topLeft.scale.set(1f);
			}
			topLeft.x = x;
			topLeft.y = y;
			PixelScene.align(topLeft);
		}

        if (extra != null) {
            extra.x = x + (width - extra.width()) - margin.right;
            extra.y = y + margin.top;
            PixelScene.align(extra);

            if ((topLeft.width() + extra.width()) > width){
                extra.visible = false;
            } else if (item != null) {
                extra.visible = true;
            }
        }
		
		//if (extra != null) {
		//	extra.x = x + (width - extra.width());
		//	extra.y = y;
		//	PixelScene.align(extra);
		//}
		
		if (bottomRight != null) {
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height());
			PixelScene.align(bottomRight);
		}

		if (bottomRightIcon != null) {
			bottomRightIcon.x = x + (width - bottomRightIcon.width()) -1;
			bottomRightIcon.y = y + (height - bottomRightIcon.height());
			PixelScene.align(bottomRightIcon);
		}
	}

    public void alpha( float value ){
        if (!active) value *= 0.3f;
        if (sprite != null)     sprite.alpha(value);
        if (topLeft != null)      topLeft.alpha(value);
        if (extra != null)     extra.alpha(value);
        if (bottomRight != null)   bottomRight.alpha(value);
        if (bottomRightIcon != null) bottomRightIcon.alpha(value);
    }

    public void clear(){
        item(null);
        enable(true);
        sprite.visible(true);
        sprite.view(ItemSpriteSheet.SOMETHING, null);
        layout();
    }
	
	public void item( Item item ) {
		if (this.item == item) {
			if (item != null) {
                sprite.view( item );
            }
			updateText();
			return;
		}

		this.item = item;

		if (item == null) {

			enable(false);
            sprite.visible(false);

			updateText();
			
		} else {
			
			enable(true);
            sprite.visible(true);

            sprite.view( item );
			updateText();
		}
	}

	private void updateText(){

		if (bottomRightIcon != null){
			remove(bottomRightIcon);
			bottomRightIcon = null;
		}

		if (item == null){
			topLeft.visible = extra.visible = bottomRight.visible = false;
			return;
		} else {
			topLeft.visible = extra.visible = bottomRight.visible = true;
		}

		topLeft.text( item.status() );

		boolean isArmor = item instanceof Armor;
		boolean isWeapon = item instanceof Weapon;
		if (isArmor || isWeapon) {

			if (item.levelKnown || (isWeapon && !(item instanceof MeleeWeapon))) {

				int str = isArmor ? ((Armor)item).STRReq() : ((Weapon)item).STRReq();
				extra.text( Messages.format( TXT_STRENGTH, str ) );
				if (str > Dungeon.hero.STR()) {
					extra.hardlight( DEGRADED );
				} else {
					extra.resetColor();
				}

			} else {

				extra.text( Messages.format( TXT_TYPICAL_STR, isArmor ?
						((Armor)item).STRReq(0) :
						((Weapon)item).STRReq(0) ) );
				extra.hardlight( WARNING );

			}
			extra.measure();

		} //else if (item instanceof Key && !(item instanceof SkeletonKey)) {
		//	topRight.text(Messages.format(TXT_KEY_DEPTH, ((Key) item).depth));
		//	topRight.measure();
		//}
		else {
			extra.text( null );
		}

		int level = item.visiblyUpgraded();

		if (level != 0) {
			bottomRight.text( item.levelKnown ? Messages.format( TXT_LEVEL, level ) : TXT_CURSED );
			bottomRight.measure();
			bottomRight.hardlight( level > 0 ? UPGRADED : DEGRADED );
		} else if (item instanceof Scroll || item instanceof Potion) {
			bottomRight.text( null );

			Integer iconInt;
			if (item instanceof Scroll){
				iconInt = ((Scroll) item).initials();
			} else {
				iconInt = ((Potion) item).initials();
			}
			if (iconInt != null && iconVisible) {
				bottomRightIcon = new Image(Assets.CONS_ICONS);
				int left = iconInt*7;
				int top = item instanceof Potion ? 0 : 8;
				bottomRightIcon.frame(left, top, 7, 8);
				add(bottomRightIcon);
			}

		} else {
			bottomRight.text( null );
		}

		layout();
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;
        sprite.alpha( alpha );
		topLeft.alpha( alpha );
		extra.alpha( alpha );
		bottomRight.alpha( alpha );
		if (bottomRightIcon != null) bottomRightIcon.alpha( alpha );
	}

    public void showExtraInfo( boolean show ){

        if (show){
            add(extra);
        } else {
            remove(extra);
        }

    }

    public void setMargins( int left, int top, int right, int bottom){
        margin.set(left, top, right, bottom);
        layout();
    }

    @Override
    protected String hoverText() {
        if (item != null && item.name() != null) {
            return Messages.titleCase(item.name());
        } else {
            return super.hoverText();
        }
    }

}
