package com.unifier.arknightspixeldungeon.effects;

import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Halo;

public class ThermalImaging extends Halo {

    private float phase;

    public ThermalImaging(CharSprite sprite ) {

        //rectangular sprite to circular radius. Pythagorean theorem
        super( (float)Math.sqrt(Math.pow(sprite.width()/2f, 2) + Math.pow(sprite.height()/2f, 2)), 0xBBAACC, 1f );

        am = -0.33f;
        aa = +0.33f;

        //target = sprite;

        phase = 1;
    }
}
