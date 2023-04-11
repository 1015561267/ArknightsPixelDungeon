package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class OriginiumSlugSprite extends MobSprite {

    public OriginiumSlugSprite() {
        super();

        texture( Assets.ORIGINIUMSLUG );

        TextureFilm frames = new TextureFilm( texture, 30, 20 );

        idle = new Animation( 6, true );
        idle.frames( frames,0, 1, 2, 3 );

        run = new Animation( 18, true );
        run.frames( frames,  4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

        attack = new Animation( 14, false );
        attack.frames( frames, 17, 18, 19, 20, 21, 22, 23);

        die = new Animation( 12, false );
        die.frames( frames, 24, 25, 26, 27, 28, 29);

        play(idle);
    }

}
