package com.unifier.arknightspixeldungeon.sprites;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class SkillSprite extends CharSprite {

    public enum skillAnimation{
        unsheath_start,unsheath_over,shadowless_start,shadowless_over
    }

    public SkillSprite(skillAnimation skill,Callback callback) {

        super();

        link( Dungeon.hero );

        Object file = new Object();

        switch (skill)
        {
            case unsheath_start:
            case unsheath_over:file = Assets.UNSHEATH;break;

            case shadowless_start:
            case shadowless_over:file = Assets.SHADOWLESS;break;
        }
        texture(file);

        visible = false;
    }

    private void getSkill(skillAnimation skillAnimation,Callback callback) {

        TextureFilm frames = new TextureFilm( texture, 16, 17 );

        Animation animation;

        switch (skillAnimation)
        {
            case unsheath_start:
                animation = new Animation(10,false);
                animation.frames(frames,0,1,2,3,4);
                break;
            case unsheath_over:
                animation = new Animation(8,false);
                animation.frames(frames,5,6,7,8);
                break;
            case shadowless_start:
                animation = new Animation(9,false);
                animation.frames(frames,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33);
                break;
            case shadowless_over:
                animation = new Animation(6,false);
                animation.frames(frames,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + skillAnimation);
        }

        visible = true;
        animCallback = callback;
        play(animation);
    }

    public void setSkillCallbackAnimation(skillAnimation skillAnimation,Callback callback, Animation animation)
    {
        //getSkill();

        switch (skillAnimation)
        {
            default:break;
            case shadowless_start:

                break;
            case shadowless_over:
                break;
        }

        animCallback = callback;
        play( animation ,true );
    }
}
