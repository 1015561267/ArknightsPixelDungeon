package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.watabou.noosa.TextureFilm;

public class NetherseaPredatorSprite extends MobSprite{
    public NetherseaPredatorSprite() {
        super();

        //renderShadow = false;
        //perspectiveRaise = 0.2f;

        texture( Assets.NETHERSEAPREDATOR );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3 ,4);

        run = new Animation( 8, true );
        run.frames( frames, 5, 6, 7, 8 );

        attack = new Animation( 10, false );
        attack.frames( frames, 9,10,11,12,13);

        die = new Animation( 4, false );
        die.frames( frames, 14, 15, 16 , 17 );

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        //renderShadow = false;
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        if (anim == attack) {
            GameScene.ripple( ch.pos );
        }
    }
}
