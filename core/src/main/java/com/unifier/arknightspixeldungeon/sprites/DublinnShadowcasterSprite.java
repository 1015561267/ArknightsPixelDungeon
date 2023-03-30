package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.mobs.DublinnShadowcaster;
import com.unifier.arknightspixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DublinnShadowcasterSprite extends MobSprite  {

    public DublinnShadowcasterSprite() {
        super();

        texture( Assets.DUBLINNSHADOWCASTER );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 1, 2, 3, 4 );

        run = new Animation( 8, true );
        run.frames( frames, 10, 11, 12, 13,14,15 );

        attack = new Animation( 10, false );
        attack.frames( frames, 20, 21, 22,23,24, 25,26 );

        zap = attack.clone();

        die = new Animation( 14, false );
        die.frames( frames, 30, 31, 32,33,34, 35,36,37,38 );

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((DublinnShadowcaster)ch).onZapComplete();
                    }
                } );

        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

}
