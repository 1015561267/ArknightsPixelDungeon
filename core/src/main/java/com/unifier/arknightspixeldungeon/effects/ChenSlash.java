package com.unifier.arknightspixeldungeon.effects;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.utils.Callback;

public class ChenSlash extends Wound {
    public ChenSlash() {
        super( Effects.get( Effects.Type.WOUND ));
    }
    public ChenSlash(Callback callback)
    {
        super( Effects.get( Effects.Type.WOUND ),callback);
    }
    protected static float fadeTime(){ return 0.5f; }

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
        w.time = fadeTime();//0.8f is too slow,and even more if you have to wait it end then start the next
        w.angle = angle;
        w.callback=callback;
    }
}