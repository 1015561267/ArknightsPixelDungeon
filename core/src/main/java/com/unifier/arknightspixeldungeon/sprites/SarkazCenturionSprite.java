package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.mobs.SarkazCenturion;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.particles.ElmoParticle;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SarkazCenturionSprite extends MobSprite {

    private Emitter spray;
    private ArrayList<Emitter> pumpUpEmitters = new ArrayList<>();

    public SarkazCenturionSprite() {

        texture(Assets.SARKAZCENTURION);

        TextureFilm frames = new TextureFilm(texture, 32, 28);

        idle = new Animation(4, true);
        idle.frames(frames, 0, 1, 2, 3);

        run = new Animation(12, true);
        run.frames(frames, 4, 5, 6, 7, 8);

        attack = new Animation(18, false);
        attack.frames(frames, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);

        operate = new Animation(12, false);
        operate.frames(frames, 22, 23, 24, 25, 26, 27, 28, 29);

        //fade = new Animation(12,false);
        //fade.frames(frames, 22, 23, 24, 25, 26 );

        die = new Animation(5, false);
        die.frames(frames, 30, 31, 32, 33, 34, 35, 36, 37);

        spray = centerEmitter();
        spray.autoKill = false;
        spray.pour( BlackParticle.FACTORY, 0.04f );
        spray.on = false;
    }

    @Override
    public void play(Animation anim) {
        //if (anim != operate){
       //     clearEmitters();
        //}
        super.play(anim);
    }

    public void clearEmitters(){
        for (Emitter e : pumpUpEmitters){
            e.on = false;
        }
        pumpUpEmitters.clear();
    }

    public void triggerEmitters(){
        for (Emitter e : pumpUpEmitters){
            e.burst(ElmoParticle.FACTORY, 10);
        }
        Sample.INSTANCE.play( Assets.BURNING );
        pumpUpEmitters.clear();
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete(anim);

        if (anim == operate) {
            triggerEmitters();
            idle();
            ch.next();
            ((SarkazCenturion)ch).doAbility();
        } else if (anim == die) {
            spray.killAndErase();
        }
    }

    public void performAbility() { play(operate); }

    public void showWarn( int warnDist ) {
        if (warnDist == 0){
            clearEmitters();
        } else {
            //play(pump);
            //Sample.INSTANCE.play( Assets., 1f, warnDist == 1 ? 0.8f : 1f );
            for (int i = 0; i < Dungeon.level.length(); i++){
                if (ch.fieldOfView != null && ch.fieldOfView[i]
                        && Dungeon.level.distance(i, ch.pos) <= warnDist
                        && new Ballistica( ch.pos, i, Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN ).collisionPos == i
                        && new Ballistica( i, ch.pos, Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN).collisionPos == ch.pos){
                    Emitter e = CellEmitter.get(i);
                    e.pour(BlackParticle.FACTORY, 0.04f);
                    pumpUpEmitters.add(e);
                }
            }
        }
    }

    public static class BlackParticle extends PixelParticle.Shrinking {

        public static final Emitter.Factory FACTORY = new Emitter.Factory() {
            @Override
            public void emit( Emitter emitter, int index, float x, float y ) {
                ((BlackParticle)emitter.recycle( BlackParticle.class )).reset( x, y );
            }
        };

        public BlackParticle() {
            super();

            color( 0x000000 );
            lifespan = 0.3f;

            acc.set( 0, +50 );
        }

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan;

            size = 4;
            speed.polar( -Random.Float( PointF.PI ), Random.Float( 32, 48 ) );
        }

        @Override
        public void update() {
            super.update();
            float p = left / lifespan;
            am = p > 0.5f ? (1 - p) * 2f : 1;
        }
    }
}
