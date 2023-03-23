package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.ui.TalentIcon;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndInfoHeroAbility extends Window {

    public enum Type{
        Skill,Specific
    }

    public WndInfoHeroAbility(HeroClass heroClass,Type type,int order){
        super();
    }

    public Image wndIcon(HeroClass heroClass,Type type,int order){
        switch (type){
            case Skill:
                switch (heroClass){
                    case WARRIOR:
                        switch (order){
                            case 1:return new TalentIcon(Talent.SHEATHED_STRIKE);
                            case 2:return new TalentIcon(Talent.UNSHEATH);
                            case 3:return new TalentIcon(Talent.SHADOWLESS);
                        }
                    case MAGE:
                    case ROGUE:
                    case HUNTRESS:
                }
            case Specific:
        }
        return new TalentIcon(Talent.REFLECT);
    }
}