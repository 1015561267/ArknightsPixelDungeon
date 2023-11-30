package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.effects.particles.PoisonParticle;
import com.watabou.noosa.TextureFilm;

public class SarkazWithererSprite extends MobSprite
{
    public SarkazWithererSprite() {
        super();

        texture( Assets.SARKAZWITHERER );

        TextureFilm frames = new TextureFilm( texture, 32, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5, 6,7,8,9 );

        run = new Animation( 15, true );
        run.frames( frames, 10, 11, 12, 13, 14, 15 ,16,17,18 );

        attack = new Animation( 15, false );
        attack.frames( frames, 19, 20, 21,22,23,24,25 );

        die = new Animation( 12, false );
        die.frames( frames, 26,27,28,29,30,31,32,33);

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        if (Dungeon.level.heroFOV[ch.pos]) {
            CellEmitter.center(this.ch.pos).burst( PoisonParticle.SPLASH, 10 );
            emitter().burst( Speck.factory( Speck.TOXIC ), 12 );
        }
    }
}
