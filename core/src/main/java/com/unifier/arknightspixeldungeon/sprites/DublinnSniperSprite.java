package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class DublinnSniperSprite extends MobSprite {

    public DublinnSniperSprite() {
        super();

        texture(Assets.DUBLINNSNIPER);

        TextureFilm frames = new TextureFilm(texture, 40, 40);

        idle = new Animation(5, true);
        idle.frames(frames, 0,1,2,3,4);

        run = new Animation(12, true);
        run.frames(frames, 9,10,11,12,13,14);

        attack = new Animation(9, false);
        attack.frames(frames, 18, 19, 20, 21, 22, 23, 24,25,26);

        die = new Animation(10, false);
        die.frames(frames, 27, 28, 29, 30, 31);

        play(idle);
    }

    private Animation cast;

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( ch.pos, cell, new Item(){
                    {
                        image = ItemSpriteSheet.ONE_BURST;
                    }
                    @Override
                    public boolean isBulletForEffect(){return true;}
                    }, new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete(Char.rangeType.DublinnSniper);
                        }
                    });
            play( cast );
            turnTo( ch.pos , cell );
        } else {
            super.attack( cell );
        }
    }
}
