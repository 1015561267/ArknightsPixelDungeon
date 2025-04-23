package com.unifier.arknightspixeldungeon.effects.particles;

import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

class ShootParticle extends SnipeParticle {
    public static Emitter.Factory factory(Callback callback) {
        return new Emitter.Factory() {
            @Override
            public void emit(Emitter emitter, int index, float x, float y) {
                ((ShootParticle) emitter.recycle(ShootParticle.class)).reset(x, y, callback);
            }
        };
    }

    public ShootParticle() {
        super();
        color(0x000000);
        am = 0;
    }

    boolean shoot; //총 발사 여부
    Char target = null;
    int tier = 1;
    int lvl = 0;
    Callback callback;
    //총 발사 후 쉬는 구간 이후 마지막 구간 사이는 아무것도 하지 않음

    public void reset(float x, float y, Callback callback) {
        reset(x, y);

        size(2f);

        shoot = false;

        this.callback = callback;
    }

    @Override
    public void update() {
        super.update();

        if (!shoot && left <= lifespan - FIRST - MIDDLE_REST) { //left가 총 발사 타이밍과 완벽하게 일치하지 않아서 범위로 지정
            //Gun gun = Gun.getGun(SR.class, this.tier, this.lvl);
            //Gun.Bullet bullet = gun.knockBullet();
            //bullet.throwSound();
            //bullet.shoot(this.target.pos, false);

            CellEmitter.center(this.target.pos).burst(BlastParticle.FACTORY, 4);
            shoot = true;
        }

        if (left <= 0) {
            this.callback.call();
        }
    }
}
