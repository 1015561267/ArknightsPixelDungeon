package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DublinnSnifferHoundSprite extends MobSprite {

    public DublinnSnifferHoundSprite() {
        super();

        texture( Assets.DUBLINNSNIFFERHOUND );

        TextureFilm frames = new TextureFilm( texture, 32, 21 );

        idle = new Animation( 8, true );
        idle.frames( frames,7, 8, 9, 10 ,11,12,13);

        run = new Animation( 12, true );
        run.frames( frames,  0, 1, 2, 3 ,4,5,6);

        attack = new Animation( 14, false );
        attack.frames( frames, 14, 15, 16, 17 ,18,19,20 );

        die = new Animation( 14, false );
        die.frames( frames, 21, 22, 23, 24 ,25,26,27 );

        play(idle);
    }

}

