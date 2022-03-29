package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.messages.Messages;

public class WndHeroSkill extends WndTitledMessage {

    public WndHeroSkill(HeroSkill skill) {

        super( skill.skillIcon(), skill.name(), skill.activated() ? skill.desc() + "\n\n" +skill.otherInfo() : Messages.get(HeroSkill.class, "locked") + "\n\n" + skill.desc() );

    }
}
