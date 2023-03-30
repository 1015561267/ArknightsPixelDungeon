package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SewerBossSprite extends MobSprite {

    public SewerBossSprite() {

        texture(Assets.SewerBoss);

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

    }
}
