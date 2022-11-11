package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.items.EquipableItem;
import com.unifier.arknightspixeldungeon.items.Gold;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.items.bags.Bag;
import com.unifier.arknightspixeldungeon.items.wands.Wand;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.audio.Sample;

public class InventorySlot extends ItemSlot {

    private static final int NORMAL		= 0x9953564D;
    private static final int EQUIPPED	= 0x9991938C;

    private ColorBlock bg;

    public InventorySlot( Item item ) {

        super( item );
    }

    @Override
    protected void createChildren() {
        bg = new ColorBlock( 1, 1, NORMAL );
        add( bg );

        super.createChildren();
    }

    @Override
    protected void layout() {
        bg.size(width, height);
        bg.x = x;
        bg.y = y;

        super.layout();
    }

    @Override
    public void alpha(float value) {
        super.alpha(value);
        bg.alpha(value);
    }

    @Override
    public void item( Item item ) {

        super.item( item );

        bg.visible = !(item instanceof Gold || item instanceof Bag);

        if (item != null) {

            boolean equipped = item.isEquipped(Dungeon.hero) ||
                    item == Dungeon.hero.belongings.weapon ||
                    item == Dungeon.hero.belongings.armor ||
                    item == Dungeon.hero.belongings.artifact ||
                    item == Dungeon.hero.belongings.misc ||
                    item == Dungeon.hero.belongings.ring;

            bg.texture( TextureCache.createSolid( equipped ? EQUIPPED : NORMAL ) );
            bg.resetColor();
            if (item.cursed && item.cursedKnown) {
                bg.ra = +0.3f;
                bg.ga = -0.15f;
            } else if (!item.isIdentified()) {
                if ((item instanceof EquipableItem || item instanceof Wand) && item.cursedKnown){
                    bg.ba = 0.3f;
                } else {
                    bg.ra = 0.3f;
                    bg.ba = 0.3f;
                }
            }

            if (item.name() == null) {
                enable( false );
            }
            //else if (Dungeon.hero.buff(LostInventory.class) != null
            //        && !item.keptThoughLostInvent){
            //    enable(false);
            //}
        } else {
            bg.texture( TextureCache.createSolid( NORMAL ) );
            bg.resetColor();
        }
    }

    public Item item(){
        return item;
    }

    @Override
    protected void onPointerDown() {
        bg.brightness( 1.5f );
        Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
    }

    protected void onPointerUp() {
        bg.brightness( 1.0f );
    }

}
