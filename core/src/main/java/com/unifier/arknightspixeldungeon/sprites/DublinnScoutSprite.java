package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DublinnScoutSprite extends MobSprite {

    public DublinnScoutSprite() {
        super();

        texture( Assets.DUBLINNSCOUT );

        TextureFilm frames = new TextureFilm( texture, 32, 31 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5 );

        run = new Animation( 12, true );
        run.frames( frames, 6, 7, 8 , 9, 10, 11, 12, 13);

        attack = new Animation( 18, false );
        attack.frames( frames,  14, 15, 16, 17, 18, 19, 20);

        die = new Animation( 7, false );
        die.frames( frames, 21, 22, 23, 24, 25, 26, 27 );

        play(idle);
    }

}
