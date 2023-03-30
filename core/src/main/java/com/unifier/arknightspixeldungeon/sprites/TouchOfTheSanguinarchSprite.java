package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class TouchOfTheSanguinarchSprite extends MobSprite {

    public TouchOfTheSanguinarchSprite() {
        super();

        texture( Assets.TOUCHOFTHESANGUINARCH );
        TextureFilm film = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 3, true );
        idle.frames( film, 0, 1, 2, 3, 4 );

        run = new Animation( 5, true );
        run.frames( film,  5, 6, 7,8,9 );

        die = new Animation( 10, false );
        die.frames( film,  10,11, 12, 13,14 );

        attack = new Animation( 8, false );
        attack.frames( film, 15,16,17,18 );

        idle();
    }
}
