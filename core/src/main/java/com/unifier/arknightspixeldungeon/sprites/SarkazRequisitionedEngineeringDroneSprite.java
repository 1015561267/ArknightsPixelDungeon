package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SarkazRequisitionedEngineeringDroneSprite extends MobSprite {

    public SarkazRequisitionedEngineeringDroneSprite() {
        super();

        texture( Assets.SARKAZREQUISITIONEDENGINEERINGDRONE );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3 ,4,5,6 ,7);

        run = new Animation( 8, true );
        run.frames( frames,   8, 9, 10 ,11,12,13,14,15);

        attack = new Animation( 16, false );
        attack.frames( frames, 16, 17 ,18,19,20,21,22,23 );

        die = new Animation( 10, false );
        die.frames( frames, 24,25,26,27,28 );

        play(idle);
    }


}
