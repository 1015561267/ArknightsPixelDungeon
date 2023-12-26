package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class NewMimicSprite extends MobSprite {

    public NewMimicSprite() {
        super();

        texture(Assets.NEW_MIMIC);

        TextureFilm frames = new TextureFilm(texture,38,36);

        idle = new Animation(5,true);
        idle.frames(frames,0);

        run = new Animation(5,true);
        run.frames( frames,0);

        attack = new Animation(10,false);
        attack.frames( frames,0,1,2,3,4);

        die = new Animation(5,false);
        die.frames( frames,5,6,7,8,9);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFFcb9700;
    }
}
