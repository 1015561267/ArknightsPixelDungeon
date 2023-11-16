package com.unifier.arknightspixeldungeon.effects;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.utils.Callback;

public class ChenSlash extends Wound {
    public ChenSlash() {
        super( Effects.get( Effects.Type.CHEN_SLASH ));
        hardlight(1f, 0f, 0f);
    }
    public ChenSlash(Callback callback)
    {
        super( Effects.get( Effects.Type.CHEN_SLASH ),callback);
        hardlight(1f, 0f, 0f);
    }
    protected static float fadeTime(){ return 0.2f; }
    private boolean theLastOne = false;

    @Override
    public void update() {
        updateMotion();
        if ((time -= Game.elapsed) <= 0) {
            kill();
        } else {
            float p = time / fadeTime();
            alpha( p );
            scale.x = 1.5f + p;
            if(callback != null&&time<=fadeTime()/2) {//call it right away when half at anime,to accelerate it
                callback.call();
                callback = null;
            }
        }
    }

    public static void hit( int pos ) {
        hit( pos, 0 );
    }

    public static void hit( int pos, float angle ) {
        Group parent = Dungeon.hero.sprite.parent;
        ChenSlash w = (ChenSlash)parent.recycle( ChenSlash.class );
        parent.bringToFront( w );
        w.reset( pos );
        w.angle = angle;
    }
    public static void hit(int pos,float angle ,Callback callback) {
        Group parent = Dungeon.hero.sprite.parent;
        ChenSlash w = (ChenSlash)parent.recycle( ChenSlash.class);
        parent.bringToFront( w );
        w.reset( pos );
        w.time = fadeTime();
        w.angle = angle;
        w.callback=callback;
    }

    public static void hit( Char ch ) {
        hit( ch, 0 );
    }

    public static void hit(Char ch, float angle ) {
        if (ch.sprite.parent != null) {
            ChenSlash w = (ChenSlash) ch.sprite.parent.recycle(ChenSlash.class);
            ch.sprite.parent.bringToFront(w);
            w.reset(ch.sprite);
            w.angle = angle;
        }
    }

    public static void hit(Char ch, float angle ,Callback callback) {
        if (ch.sprite.parent != null) {
            ChenSlash w = (ChenSlash) ch.sprite.parent.recycle(ChenSlash.class);
            ch.sprite.parent.bringToFront(w);
            w.reset(ch.sprite);
            w.time = fadeTime();//0.8f is too slow,and even more if you have to wait it end then start the next
            w.angle = angle;
            w.callback=callback;
        }
    }

    public static void lastHit(Char ch, float angle ,Callback callback){

    }

}