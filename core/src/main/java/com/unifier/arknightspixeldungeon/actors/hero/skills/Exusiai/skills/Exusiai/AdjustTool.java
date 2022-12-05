package com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.skills.Exusiai;

import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.scenes.GameScene;

import java.util.ArrayList;

public class AdjustTool extends Item {

    public static final String AC_ADJUST	= "ADJUST";

    {
        defaultAction = AC_ADJUST;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ADJUST);
        return actions;
    }

    @Override
    public void execute(Hero hero,String action) {
        if (action.equals(AC_ADJUST)) {
            GameScene.show(new WndAdjustGun(1));
        }
    }

    @Override
    public String desc() {
        return "枪支调整工具";
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
