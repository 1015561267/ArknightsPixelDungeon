package com.unifier.arknightspixeldungeon.sprites.sheep_sprite;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class BestBuddySprite extends MobSprite {

    public BestBuddySprite() {
        super();

        texture( Assets.BESTBUDDY );

        TextureFilm frames = new TextureFilm( texture, 32,  PDSettings.flyingSheep() ? 32 : 26 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 0, 1, 1, 2, 2, 3, 3);

        run = idle.clone();
        attack = idle.clone();

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
        curFrame = Random.Int( curAnim.frames.length );
    }

}
