package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class SentryAgentSprite extends MobSprite{
    public SentryAgentSprite() {
        super();

        texture( Assets.SENTRYAGENT);

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0,1,2,3,4,5,6,7,8,9,10,11 );

        run = new MovieClip.Animation( 6, true );
        run.frames( frames, 14,15,16,17,18,19);

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 22,23,24,25,26,27);

        die = new MovieClip.Animation( 15, false );
        die.frames( frames, 28,29,30,31,32,33,34,35 );

        play( idle );
    }

    //@Override
    //public int blood() {
    //    return 0x88000000;
    //}
}
