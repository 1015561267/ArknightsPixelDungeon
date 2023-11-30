package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class GopnikSprite extends MobSprite{

    private Animation showoff;

    public GopnikSprite() {
        super();

        texture( Assets.GOPNIK );

        TextureFilm frames = new TextureFilm( texture, 40, 40 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 0, 1, 1, 2, 3, 4 );

        run = new Animation( 12, true );
        run.frames( frames, 16, 17, 18, 19, 20, 21,22);

        attack = new Animation( 20, false );
        attack.frames( frames, 32,33,33,34,34,35,35,36,37,38,38,38,39,39,40,41);

        showoff = new Animation( 8, false );
        showoff.frames( frames, 42,43,43,44,44,45,45,46);

        die = new Animation( 10, false );
        die.frames( frames, 48, 49, 50, 51, 52, 53 , 54 , 55 , 56 );

        play( idle );
    }

    @Override
    public void onComplete( Animation anim ) {
        //super.onComplete(anim);
        if (animCallback != null) {
            Callback executing = animCallback;
            animCallback = null;
            executing.call();
        }else {
            if (anim == attack) {
                Camera.main.shake( GameMath.gate( 3, Random.Int(3,7), 7), 0.3f );
                ch.onAttackComplete(Char.rangeType.Dismiss);
                play(showoff);
            } else if (anim == showoff) {
                idle();
            }else super.onComplete(anim);
        }
    }
}
