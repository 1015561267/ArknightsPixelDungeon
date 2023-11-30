package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Blindness;
import com.unifier.arknightspixeldungeon.actors.mobs.SarkazCenturion;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.particles.BloodParticle;
import com.unifier.arknightspixeldungeon.effects.particles.ElmoParticle;
import com.unifier.arknightspixeldungeon.levels.SewerBossLevel;
import com.unifier.arknightspixeldungeon.mechanics.Ballistica;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
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

    public Animation charging;
    public Animation berserk;
    public Animation abilityAttack;

    private static TextureFilm temp;

    //use sprite instead of char class as notice may sound weird
    public SarkazCenturionSprite() {
        super();

        //WARNING this mob have spray,and link() will use it,so if not created before link() there would be NPE
        //Although it's caught,but it would cause sprite itself to be null
        spray = centerEmitter();
        spray.autoKill = false;
        spray.pour( BloodParticle.FACTORY, 0.04f );
        spray.on = false;
        spray.visible = false;

        texture(Assets.SARKAZCENTURION);

        updateBerserk(false);

    }

    @Override
    public void update() {
        super.update();
        spray.pos(this);
    }

    public void updateBerserk(boolean isBerserking) {

        TextureFilm frames = new TextureFilm( texture, 32, 28 );

        int c = isBerserking ? 39 : 0;

        idle = new Animation(4, true);
        idle.frames(frames, c+0, c+1, c+2, c+3);

        run = new Animation(10, true);
        run.frames(frames, c+4, c+5, c+6, c+7, c+8);

        attack = new Animation(15, false);
        attack.frames(frames, c+9, c+10, c+11, c+12, c+13, c+14, c+15, c+16, c+17, c+18, c+19, c+20, c+21);

        abilityAttack = attack.clone();

        charging = new Animation(4,true);
        charging.frames(frames, c+22);

        berserk = new Animation(12, false);
        berserk.frames(frames, c+22, c+23, c+24, c+25, c+26, c+27, c+28, c+29);

        die = new Animation(5, false);
        die.frames(frames, c+30, c+31, c+32, c+33, c+34, c+35, c+36, c+37);

        if (curAnim != berserk) play(idle);
    }

    private TextureFilm temp() {
        if (temp == null) {
            SmartTexture texture = TextureCache.get(Assets.SARKAZCENTURION);
            temp = new TextureFilm(texture, texture.width, 28);
        }
        return temp;
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch.buff(SarkazCenturion.CenturionBerserk.class)!=null) {
            spray(true);
            updateBerserk(true);
        }
    }


    @Override
    public void play(Animation anim) {
        if(anim == berserk) {
            GLog.i("play assigned");
        }
        if (anim != charging && anim!=abilityAttack){
            clearEmitters();
        }
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

        if (anim == berserk) {
            GLog.i("berserk Complete");
            showStatus(CharSprite.WARNING, "狂暴化！");
            ((SarkazCenturion)ch).afterBerserk();
            GLog.i("berserk onComplete");
        }

        if (anim == abilityAttack){
            triggerEmitters();
            ((SarkazCenturion)ch).doAbility();
            idle();
        }

        super.onComplete(anim);

        if (anim == die) {
            spray.killAndErase();
        }
    }

    public void spray(boolean on){
        spray.on = on;
        spray.visible = on;
    }

    public void chargingAbility(){
        play(charging,true);
        showWarn(1);
    }

    public void performAbility() { play(abilityAttack); }

    public void playBerserk() { play(berserk); }


    public void showWarn( int warnDist ) {
        if (warnDist == 0){
            clearEmitters();
        } else {
            if (ch.fieldOfView == null || ch.fieldOfView.length != Dungeon.level.length()){
                ch.fieldOfView = new boolean[Dungeon.level.length()];
                Dungeon.level.updateFieldOfView( ch, ch.fieldOfView );
            }
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
