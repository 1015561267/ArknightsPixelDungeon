package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class SkillIcons extends Image {

    private static TextureFilm film;
    private static final int SIZE = 16;

    //transparent icon
    public static final int NONE    = 31;

    public static final int SHEATHED_STRIKE   = 0;
    public static final int UNSHEATH   = 21;
    public static final int SHADOWLESS  = 40;

    public SkillIcons(int number){
        super( Assets.TALENT_ICONS );
        if (film == null){
            film = new TextureFilm(texture, SIZE, SIZE);
        }
        frame(film.get(number));
    }
}