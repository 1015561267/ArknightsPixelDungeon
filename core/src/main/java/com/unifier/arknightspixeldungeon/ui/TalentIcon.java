package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class TalentIcon extends Image {

    private static TextureFilm film;
    private static final int SIZE = 16;

    public TalentIcon(Talent talent) {
        this(talent.icon());
    }

    public TalentIcon(int icon) {
        super(Assets.TALENT_ICONS);
        if (film == null) film = new TextureFilm(texture, SIZE, SIZE);
        frame(film.get(icon));
    }
}
