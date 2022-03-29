package com.unifier.arknightspixeldungeon.effects.particles;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class ArcParticle extends PixelParticle {

    public enum ArcType
    {
        //ROUND_LEFT,ROUND_RIGHT,LINER_LEFT,LINER_RIGHT,LINGER_MIDDLE
        LINER
    }

    public static float startingLine = 0f;

    private ArcType type;//For now we have two type:one slide on a circular orbit which center at the middle of screen
    //while another spawn from lower part and rise with randomly changed x

    private float radius;//with round type we use radius to update it's position
    private float distance;//with liner type,we use distance to calculate the y type

    private int accumulate;
    private boolean direction;

    private float offset;
    private float slope;
    private float delay;

    public float speed;
    public float acceleration;

    private float time;

    private float accumulator;

    public boolean roundedFuntion()
    {
        return false;
    }

    public ArcParticle() {
        super();
        type = ArcType.LINER;
    }

    public ArcParticle(ArcType type) {
        super();
    }

    public ArcParticle(float xOffset , float slope , float transparentdelay) {
        super();
        this.offset = xOffset;
        this.slope = slope;
        this.delay = transparentdelay;
    }

    //public void reset( float x, float y ) {
    public void reset() {
        revive();

        color( 0xFFFFFF );

        left = lifespan = 8f;

        size = 4;
        size(size);

        y = startingLine;
        x = offset;

        time = 0;
        accumulator = 0;
    }

    @Override
    public void update() {
        super.update();

        float variation = 0.1f * Random.Int(5,15);

        y -= variation;

        if(slope >= 1f || slope <= -1f)
        {
            x += variation * slope;
        }
        else
        {
            accumulator += slope;

            if(accumulator >= 1f || accumulator <= -1f)
            {
                x += variation * accumulator;
                accumulator = acceleration % 1f;
            }
        }

        am = Math.abs( time += Game.elapsed ) + delay  + 0.3f;

        if(am > 1) am = am % 1;
        else if(am < 0) am = 0;

        if (time >= 0.333f*Math.PI) {
            time = 0;
        }

        if( x <= 0 || y <= 0 || x >= Camera.main.width || y >= Camera.main.height )
        {
            this.reset();
        }
    }
}