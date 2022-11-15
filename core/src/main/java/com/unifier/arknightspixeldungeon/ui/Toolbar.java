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
import com.unifier.arknightspixeldungeon.PDAction;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.actors.hero.Belongings;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.tiles.DungeonTerrainTilemap;
import com.unifier.arknightspixeldungeon.windows.WndBag;
import com.unifier.arknightspixeldungeon.windows.WndKeyBindings;
import com.unifier.arknightspixeldungeon.windows.WndMessage;
import com.unifier.arknightspixeldungeon.windows.WndQuickBag;
import com.unifier.arknightspixeldungeon.windows.WndUseItem;
import com.watabou.input.ControllerHandler;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;
	
	private PickedUpItem pickedUp;
	
	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

    public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}
	
	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}

    @Override
    public synchronized void destroy() {
        super.destroy();
        if (instance == this) instance = null;
    }

    @Override
	protected void createChildren() {
		
		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    examining = false;
                    Dungeon.hero.rest(false);
                }
			}

            @Override
            public GameAction keyAction() {
                return PDAction.WAIT;
            }

            @Override
            public GameAction secondaryTooltipAction() {
                return PDAction.WAIT_OR_PICKUP;
            }

            @Override
            protected String hoverText() {
                return Messages.titleCase(Messages.get(WndKeyBindings.class, "wait"));
            }

            @Override
            protected boolean onLongClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    examining = false;
                    Dungeon.hero.rest(true);
                }
				return true;
			}

			;
		});
		
		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
                if (Dungeon.hero.ready) {
                    if (!examining && !GameScene.cancel()) {
                        GameScene.selectCell(informer);
                        examining = true;
                    } else {
                        informer.onSelect(null);
                        Dungeon.hero.search(true);
                    }
                }
			}

            @Override
            public GameAction keyAction() {
                return PDAction.EXAMINE;
            }

            @Override
            protected String hoverText() {
                return Messages.titleCase(Messages.get(WndKeyBindings.class, "examine"));
            }

            @Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});

		btnQuick = new QuickslotTool[4];

		add( btnQuick[3] = new QuickslotTool( 64, 0, 22, 24, 3) );

		add( btnQuick[2] = new QuickslotTool( 64, 0, 22, 24, 2) );

		add(btnQuick[1] = new QuickslotTool(64, 0, 22, 24, 1));

		add(btnQuick[0] = new QuickslotTool(64, 0, 22, 24, 0));
		
		add(btnInventory = new Tool(0, 0, 24, 26) {
			//private GoldIndicator gold;

            private CurrencyIndicator ind;

            private Image arrow;

            @Override
            protected void onClick() {
                if (Dungeon.hero.ready || !Dungeon.hero.isAlive()) {
                    if (PDSettings.interfaceSize() == 2) {
                        GameScene.toggleInvPane();
                    } else {
                        if (!GameScene.cancel()) {
                            GameScene.show(new WndBag(Dungeon.hero.belongings.backpack));
                        }
                    }
                }
            }

            @Override
            public GameAction keyAction() {
                return PDAction.INVENTORY;
            }

            @Override
            public GameAction secondaryTooltipAction() {
                return PDAction.INVENTORY_SELECTOR;
            }

            @Override
            protected String hoverText() {
                return Messages.titleCase(Messages.get(WndKeyBindings.class, "inventory"));
            }

            @Override
            protected boolean onLongClick() {
                GameScene.show(new WndQuickBag(null));
                return true;
            }

            @Override
            protected void createChildren() {
                super.createChildren();
                arrow = Icons.get(Icons.COMPASS);
                arrow.originToCenter();
                arrow.visible = PDSettings.interfaceSize() == 2;
                arrow.tint(0x3D2E18, 1f);
                add(arrow);

                ind = new CurrencyIndicator();
                add(ind);
            }

            @Override
            protected void layout() {
                super.layout();
                ind.fill(this);

                arrow.x = left() + (width - arrow.width())/2;
                arrow.y = bottom()-arrow.height-1;
                arrow.angle = bottom() == camera().height ? 0 : 180;
            }
		});

		//hidden buttons

        //hidden button for quickslot selector keybind
        add(new Button(){
            @Override
            protected void onClick() {
                if (QuickSlotButton.targetingSlot != -1){
                    int cell = QuickSlotButton.autoAim(QuickSlotButton.lastTarget, Dungeon.quickslot.getItem(QuickSlotButton.targetingSlot));

                    if (cell != -1){
                        GameScene.handleCell(cell);
                    } else {
                        //couldn't auto-aim, just target the position and hope for the best.
                        GameScene.handleCell( QuickSlotButton.lastTarget.pos );
                    }
                    return;
                }

                if (Dungeon.hero.ready && !GameScene.cancel()) {

                    String[] slotNames = new String[6];
                    Image[] slotIcons = new Image[6];
                    for (int i = 0; i < 6; i++){
                        Item item = Dungeon.quickslot.getItem(i);

                        if (item != null && !Dungeon.quickslot.isPlaceholder(i) &&
                                //(Dungeon.hero.buff(LostInventory.class) == null || item.keptThoughLostInvent)){
                                //(Dungeon.hero.buff(LostInventory.class) == null ||
                                        item.keptThoughLostInvent){
                            slotNames[i] = Messages.titleCase(item.name());
                            slotIcons[i] = new ItemSprite(item);
                        } else {
                            slotNames[i] = Messages.get(Toolbar.class, "quickslot_assign");
                            slotIcons[i] = new ItemSprite(ItemSpriteSheet.SOMETHING);
                        }
                    }

                    String info = "";
                    if (ControllerHandler.controllerActive){
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_select") + "\n";
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.RIGHT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_assign") + "\n";
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_cancel");
                    } else {
                        info += Messages.get(WndKeyBindings.class, PDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "quickslot_select") + "\n";
                        info += Messages.get(WndKeyBindings.class, PDAction.RIGHT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "quickslot_assign") + "\n";
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "quickslot_cancel");
                    }

                    Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "quickslot_prompt"), info, slotNames, slotIcons) {
                        @Override
                        public void onSelect(int idx, boolean alt) {
                            Item item = Dungeon.quickslot.getItem(idx);

                            if (item == null || Dungeon.quickslot.isPlaceholder(idx)
                                    //|| (Dungeon.hero.buff(LostInventory.class) != null && !item.keptThoughLostInvent)
                                    || !item.keptThoughLostInvent
                                    || alt){
                                //TODO would be nice to use a radial menu for this too
                                // Also a bunch of code could be moved out of here into subclasses of RadialMenu
                                GameScene.selectItem(new WndBag.ItemSelector() {
                                    @Override
                                    public String textPrompt() {
                                        return Messages.get(QuickSlotButton.class, "select_item");
                                    }

                                    @Override
                                    public boolean itemSelectable(Item item) {
                                        return item.defaultAction != null;
                                    }

                                    @Override
                                    public void onSelect(Item item) {
                                        if (item != null) {
                                            Dungeon.quickslot.setSlot( idx , item );
                                            QuickSlotButton.refresh();
                                        }
                                    }
                                });
                            } else {

                                item.execute(Dungeon.hero);
                                if (item.usesTargeting) {
                                    QuickSlotButton.useTargeting(idx);
                                }
                            }
                            super.onSelect(idx, alt);
                        }
                    });
                }
            }

            @Override
            public GameAction keyAction() {
                if (btnWait.active) return PDAction.QUICKSLOT_SELECTOR;
                else				return null;
            }
        });


        //hidden button for rest keybind
        add(new Button(){
            @Override
            protected void onClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    examining = false;
                    Dungeon.hero.rest(true);
                }
            }

            @Override
            public GameAction keyAction() {
                if (btnWait.active) return PDAction.REST;
                else				return null;
            }
        });

        //hidden button for wait / pickup keybind
        add(new Button(){
            @Override
            protected void onClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    //Dungeon.hero.waitOrPickup = true;
                    if ((Dungeon.level.heaps.get(Dungeon.hero.pos) != null //|| Dungeon.hero.isStandingOnTrampleableGrass())
                            && Dungeon.hero.handle(Dungeon.hero.pos))){
                        //trigger hold fast here, even if the hero didn't specifically wait
                        //if (Dungeon.hero.hasTalent(Talent.HOLD_FAST)){
                        //    Buff.affect(Dungeon.hero, HoldFast.class);
                        //}
                        Dungeon.hero.next();
                    } else {
                        examining = false;
                        Dungeon.hero.rest(false);
                    }
                }
            }

            protected boolean onLongClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    examining = false;
                    Dungeon.hero.rest(true);
                }
                return true;
            }

            @Override
            public GameAction keyAction() {
                if (btnWait.active) return PDAction.WAIT_OR_PICKUP;
                else				return null;
            }
        });

        //hidden button for inventory selector keybind
        add(new Button(){
            @Override
            protected void onClick() {
                if (Dungeon.hero.ready && !GameScene.cancel()) {
                    ArrayList<Bag> bags = Dungeon.hero.belongings.getBags();
                    String[] names = new String[bags.size()];
                    Image[] images = new Image[bags.size()];
                    for (int i = 0; i < bags.size(); i++){
                        names[i] = Messages.titleCase(bags.get(i).name());
                        images[i] = new ItemSprite(bags.get(i));
                    }
                    String info = "";
                    if (ControllerHandler.controllerActive){
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "container_select") + "\n";
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "container_cancel");
                    } else {
                        info += Messages.get(WndKeyBindings.class, PDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "container_select") + "\n";
                        info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "container_cancel");
                    }

                    Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "container_prompt"), info, names, images){
                        @Override
                        public void onSelect(int idx, boolean alt) {
                            super.onSelect(idx, alt);
                            Bag bag = bags.get(idx);
                            ArrayList<Item> items = (ArrayList<Item>) bag.items.clone();

                            for(Item i : bag.items){
                                if (i instanceof Bag) items.remove(i);
                                if (//Dungeon.hero.buff(LostInventory.class) != null &&
                                        !i.keptThoughLostInvent) items.remove(i);
                            }

                            if (idx == 0){
                                Belongings b = Dungeon.hero.belongings;
                                if (b.ring() != null) items.add(0, b.ring());
                                if (b.misc() != null) items.add(0, b.misc());
                                if (b.artifact() != null) items.add(0, b.artifact());
                                if (b.armor() != null) items.add(0, b.armor());
                                if (b.weapon() != null) items.add(0, b.weapon());
                            }

                            if (items.size() == 0){
                                GameScene.show(new WndMessage(Messages.get(Toolbar.class, "container_empty")));
                                return;
                            }

                            String[] itemNames = new String[items.size()];
                            Image[] itemIcons = new Image[items.size()];
                            for (int i = 0; i < items.size(); i++){
                                itemNames[i] = Messages.titleCase(items.get(i).name());
                                itemIcons[i] = new ItemSprite(items.get(i));
                            }

                            String info = "";
                            if (ControllerHandler.controllerActive){
                                info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "item_select") + "\n";
                                info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.RIGHT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "item_use") + "\n";
                                info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "item_cancel");
                            } else {
                                info += Messages.get(WndKeyBindings.class, PDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "item_select") + "\n";
                                info += Messages.get(WndKeyBindings.class, PDAction.RIGHT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "item_use") + "\n";
                                info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "item_cancel");
                            }

                            Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "item_prompt"), info, itemNames, itemIcons){
                                @Override
                                public void onSelect(int idx, boolean alt) {
                                    super.onSelect(idx, alt);
                                    Item item = items.get(idx);
                                    if (alt && item.defaultAction != null) {
                                        item.execute(Dungeon.hero);
                                    } else {
                                        Game.scene().addToFront(new WndUseItem(null, item));
                                    }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public GameAction keyAction() {
                if (btnWait.active) return PDAction.INVENTORY_SELECTOR;
                else				return null;
            }
        });

        add(pickedUp = new PickedUpItem());

    }
	
	@Override
	protected void layout() {

        float right = width;

        int quickslotsToShow = 4;

        int[] visible = new int[4];
		int slots = PDSettings.quickSlots();

		for(int i = 0; i <= 3; i++)
			visible[i] = (int)((slots > i) ? y+2 : y+25);

		for(int i = 0; i <= 3; i++) {
			btnQuick[i].visible = btnQuick[i].active = slots > i;
			//decides on quickslot layout, depending on available screen size.
			if (slots == 4 && width < 152){
				if (width < 138){
					if ((PDSettings.flipToolbar() && i == 3) ||
							(!PDSettings.flipToolbar() && i == 0)) {
						btnQuick[i].border(0, 0);
						btnQuick[i].frame(88, 0, 17, 24);
					} else {
						btnQuick[i].border(0, 1);
						btnQuick[i].frame(88, 0, 18, 24);
					}
				} else {
					if (i == 0 && !PDSettings.flipToolbar() ||
						i == 3 && PDSettings.flipToolbar()){
						btnQuick[i].border(0, 2);
						btnQuick[i].frame(106, 0, 19, 24);
					} else if (i == 0 && PDSettings.flipToolbar() ||
							i == 3 && !PDSettings.flipToolbar()){
						btnQuick[i].border(2, 1);
						btnQuick[i].frame(86, 0, 20, 24);
					} else {
						btnQuick[i].border(0, 1);
						btnQuick[i].frame(88, 0, 18, 24);
					}
				}
			} else {
				btnQuick[i].border(2, 2);
				btnQuick[i].frame(64, 0, 22, 24);
			}

		}

        if (PDSettings.interfaceSize() > 0){
            btnInventory.setPos(right - btnInventory.width(), y);
            btnWait.setPos(btnInventory.left() - btnWait.width(), y);
            btnSearch.setPos(btnWait.left() - btnSearch.width(), y);


            right = btnSearch.left();
  //          for(int i = endingSlot; i >= startingSlot; i--) {
//                if (i == endingSlot){
                for(int i = 3; i >= 0; i--) {
                if (i == 0){
                btnQuick[i].border(0, 2);
                    btnQuick[i].frame(106, 0, 19, 24);
                } else if (i == 0){
                    btnQuick[i].border(2, 1);
                    btnQuick[i].frame(86, 0, 20, 24);
                } else {
                    btnQuick[i].border(0, 1);
                    btnQuick[i].frame(88, 0, 18, 24);
                }
                btnQuick[i].setPos(right-btnQuick[i].width(), y+2);
                right = btnQuick[i].left();
            }

            //swap button never appears on larger interface sizes

            return;
        }

        float shift = 0;
        //Dote:Damn evan place the judgement of Android and Desktop into PixelScene.landscape() and involve in PDSettings.interfaceSize()
		switch(Mode.valueOf(PDSettings.toolbarMode())){
			case SPLIT:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				btnQuick[0].setPos(btnInventory.left() - btnQuick[0].width(), visible[0]);
				btnQuick[1].setPos(btnQuick[0].left() - btnQuick[1].width(), visible[1]);
				btnQuick[2].setPos(btnQuick[1].left() - btnQuick[2].width(), visible[2]);
				btnQuick[3].setPos(btnQuick[2].left() - btnQuick[3].width(), visible[3]);
				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				float toolbarWidth = btnWait.width() + btnSearch.width() + btnInventory.width();
				for(Button slot : btnQuick){
					if (slot.visible) toolbarWidth += slot.width();
				}
				right = (width + toolbarWidth)/2;

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);

				btnQuick[0].setPos(btnInventory.left() - btnQuick[0].width(), visible[0]);
				btnQuick[1].setPos(btnQuick[0].left() - btnQuick[1].width(), visible[1]);
				btnQuick[2].setPos(btnQuick[1].left() - btnQuick[2].width(), visible[2]);
				btnQuick[3].setPos(btnQuick[2].left() - btnQuick[3].width(), visible[3]);
				break;
		}
		right = width;

		if (PDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnSearch.setPos( (right - btnSearch.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);

			for(int i = 0; i <= 3; i++) {
				btnQuick[i].setPos( right - btnQuick[i].right(), visible[i]);
			}

		}

    }

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (lastEnabled != (Dungeon.hero.ready && Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready && Dungeon.hero.isAlive());
			
			for (Gizmo tool : members) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}
		
		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}

    public void setOnlyBagPos(float left, float v) {//We just change inventoryPane itself instead,as  remains

    }


    //void alpha( float value ){
    //    btnWait.alpha( value );
    //    btnSearch.alpha( value );
    //    btnInventory.alpha( value );
     //   for (QuickslotTool tool : btnQuick){
     //       tool.alpha(value);
     //   }
     //   btnSwap.alpha( value );
    //}


    public void pickup( Item item, int cell ) {
		pickedUp.reset( item,
			cell,
			btnInventory.centerX(),
			btnInventory.centerY());
	}
	
	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
            if (instance != null) {
                instance.examining = false;
                GameScene.examineCell(cell);
            }
        }
		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};
	
	private static class Tool extends Button {
		
		private static final int BGCOLOR = 0x7B8073;
		
		private Image base;
		
		public Tool( int x, int y, int width, int height ) {
			super();
			//hotArea.blockWhenInactive = true;
            hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
            frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			base = new Image( Assets.TOOLBAR );
			add( base );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}

        public void alpha( float value ){
            base.alpha(value);
        }

        @Override
		protected void onPointerDown() {
			base.brightness( 1.4f );
		}
		
		@Override
		protected void onPointerUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}
		
		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}
	
	private static class QuickslotTool extends Tool {
		
		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;
		
		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}
		
		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + borderLeft, y + 2, width - borderLeft-borderRight, height - 4 );
			//slot.setRect( x, y, width, height );
            //slot.slotMargins(borderLeft, 2, borderRight, 2);
        }

        @Override
        public void alpha(float value) {
            super.alpha(value);
            slot.alpha(value);
        }

        @Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
	}
	
	public static class PickedUpItem extends ItemSprite {
		
		private static final float DURATION = 0.5f;
		
		private float startScale;
		private float startX, startY;
		private float endX, endY;
		private float left;
		
		public PickedUpItem() {
			super();
			
			originToCenter();
			
			active =
			visible =
				false;
		}
		
		public void reset( Item item, int cell, float endX, float endY ) {
			view( item );
			
			active =
			visible =
				true;
			
			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);
			
			x = this.startX = start.x - ItemSprite.SIZE / 2;
			y = this.startY = start.y - ItemSprite.SIZE / 2;
			
			this.endX = endX - ItemSprite.SIZE / 2;
			this.endY = endY - ItemSprite.SIZE / 2;
			left = DURATION;
			
			scale.set( startScale = Camera.main.zoom / camera().zoom );
			
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((left -= Game.elapsed) <= 0) {
				
				visible =
				active =
					false;
				if (emitter != null) emitter.on = false;
				
			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );
				
				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}
