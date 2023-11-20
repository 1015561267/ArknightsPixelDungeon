package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class SarkazSniperSprite extends MobSprite {

    private Animation cast;

    public SarkazSniperSprite() {
        super();

        texture( Assets.SARKAZSNIPER );

        TextureFilm frames = new TextureFilm( texture, 38, 30 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 3 ,4 ,5 ,6 );

        run = new Animation( 12, true );
        run.frames( frames, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 ,18 );

        attack = new Animation( 24, false );
        attack.frames( frames, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 ,31 );

        cast = attack.clone();

        die = new Animation( 8, false );
        die.frames( frames, 32, 33, 34, 35, 36, 37 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( ch.pos, cell, new ParalyticDart(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete(Char.rangeType.SarkazSniper);
                        }
                    } );

            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }


}
