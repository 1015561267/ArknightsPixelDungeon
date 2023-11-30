package com.unifier.arknightspixeldungeon.sprites.sheep_sprite;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.sprites.MobSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class WanderLandStellarCasterSprite extends MobSprite {
    public WanderLandStellarCasterSprite() {
        super();

        texture( Assets.WANDERLANDSTELLARCASTER );

        TextureFilm frames = new TextureFilm( texture, 32,  PDSettings.flyingSheep() ? 32 : 26 );

        idle = new MovieClip.Animation( 8, true );
        idle.frames( frames, 0, 0, 1, 1, 2, 2, 3, 3);

        run = idle.clone();
        attack = idle.clone();

        die = new MovieClip.Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
        curFrame = Random.Int( curAnim.frames.length );
    }



}
