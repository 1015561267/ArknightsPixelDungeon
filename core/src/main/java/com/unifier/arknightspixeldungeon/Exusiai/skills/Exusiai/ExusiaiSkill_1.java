package com.unifier.arknightspixeldungeon.Exusiai.skills.Exusiai;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.ui.SkillIcons;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

public class ExusiaiSkill_1 extends HeroSkill {

    @Override
    public boolean activated() {
        return true;
    }

    @Override
    public Image skillIcon() {
        if (Dungeon.hero.hasTalent(Talent.SHADOWLESS))
        return new SkillIcons(SkillIcons.SHADOWLESS);
        else if (Dungeon.hero.hasTalent(Talent.UNSHEATH))
        return new SkillIcons(SkillIcons.UNSHEATH);
        else if (Dungeon.hero.hasTalent(Talent.SHEATHED_STRIKE))
        return new SkillIcons(SkillIcons.SHEATHED_STRIKE);
        else return new SkillIcons(SkillIcons.NONE);
    }

    @Override
    public float CD(Hero hero) {
        return rawCD();
    }

    @Override
    public float rawCD() {
        return 3f;
    }

    @Override
    public int getMaxCharge() {
        return 1;
    }

    @Override
    public void doAction() {
        if(!available()){
            GLog.h(Messages.get(HeroSkill.class, "unavailable"));
            return;
        }
        doAfterAction();
    }
}
