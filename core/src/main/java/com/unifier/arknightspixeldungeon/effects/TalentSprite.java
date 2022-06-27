package com.unifier.arknightspixeldungeon.effects;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

import java.util.HashMap;

public class TalentSprite extends Image {

    //FIXME It's a temporary mean to handle informing talent effects in a easier way.When buff icons and more be done some may be removed,but some can still have their place until there are better ways

    public enum Phase {
        FADE_IN, STATIC, FADE_OUT
    };

    private static final int SIZE	= 16;

    private static final float FADE_IN_TIME		= 0.2f;
    private static final float STATIC_TIME		= 0.8f;
    private static final float FADE_OUT_TIME	= 0.4f;

    private static TextureFilm film;

    private Char target;

    private Phase phase;
    private float duration;
    private float passed;

    private static HashMap<Char,TalentSprite> all = new HashMap<Char, TalentSprite>();

    public TalentSprite() {
        super(Assets.TALENT_ICONS);

        if (film == null) {
            film = new TextureFilm( texture, SIZE , SIZE);
        }

    }

    public void reset( Talent talent ) {

        frame(film.get(talent.icon()));

        origin.set( width / 2, height / 2 );

        phase = Phase.FADE_IN;

        duration = FADE_IN_TIME;
        passed = 0;
    }

    public void  reset( Talent talent,Phase phase){

        reset(talent);
        this.phase = phase;
    }

    @Override
    public void update() {
        super.update();

        if (target.sprite != null) {
            x = target.sprite.center().x - SIZE / 2;
            y = target.sprite.y - SIZE;
        }

        switch (phase) {
            case FADE_IN:
                alpha( passed / duration );
                scale.set( passed / duration );
                break;
            case STATIC:
                break;
            case FADE_OUT:
                alpha( 1 - passed / duration );
                break;
        }

        if ((passed += Game.elapsed) > duration) {
            switch (phase) {
                case FADE_IN:
                    phase = Phase.STATIC;
                    duration = STATIC_TIME;
                    break;
                case STATIC:
                    phase = Phase.FADE_OUT;
                    duration = FADE_OUT_TIME;
                    break;
                case FADE_OUT:
                    kill();
                    break;
            }

            passed = 0;
        }
    }

    @Override
    public void kill() {
        super.kill();
        all.remove( target );
    }

    public static void show( Char ch, Talent talent ,Phase phase) {

        if (!ch.sprite.visible) {
            return;
        }

        TalentSprite old = all.get( ch );
        if (old != null) {
            old.kill();
        }

        TalentSprite sprite = GameScene.talentSprite();
        sprite.revive();

        sprite.reset( talent );

        sprite.target = ch;
        all.put( ch,  sprite );

        sprite.phase = phase;
    }

}
