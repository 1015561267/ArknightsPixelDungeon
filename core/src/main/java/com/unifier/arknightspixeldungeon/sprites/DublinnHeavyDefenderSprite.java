package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DublinnHeavyDefenderSprite extends MobSprite {

    public DublinnHeavyDefenderSprite() {
        super();
        texture(Assets.DUBLINNHEAVYDEFENDER);

        TextureFilm frames = new TextureFilm(texture, 29, 29);

        idle = new Animation(8, true);
        idle.frames(frames, 0, 1, 2, 3, 4, 5, 6);

        run = new Animation(23, true);
        run.frames(frames, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        attack = new Animation(18, false);
        attack.frames(frames, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);

        die = new Animation(8, false);
        die.frames(frames, 28, 29, 30, 31, 32, 33);

        play(idle);
    }

}
