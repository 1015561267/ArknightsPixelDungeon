/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.ui.skilltreelayouter.ChenSkillTreeLayouter;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.unifier.arknightspixeldungeon.PDSettings.landscape;

public class TalentsPane extends ScrollPane {

	public ArrayList<ColorBlock> separators = new ArrayList<>();
	public ArrayList<ArrayList<TalentButton>> allbuttons = new ArrayList<ArrayList<TalentButton>>();

    ArrayList<TalentButton> buttons = new ArrayList<>();;

    public ArrayList<BitmapText> points = new ArrayList<>();;

	public TalentsPane( boolean canUpgrade, ArrayList<LinkedHashMap<Talent, Integer>> talents ,int tiers) {
		super(new Component());

		for(int i=0;i<tiers;i++)
        {
            BitmapText point = new BitmapText( PixelScene.pixelFont);
            point.text(Dungeon.hero.talentPointsAvailable(i+1) + "/" + Dungeon.hero.talentAllPossible(i+1));

            points.add(point);
            content.add(point);

            ArrayList<TalentButton> buttons = new ArrayList<>();

            for (Talent talent : talents.get(i).keySet()){
                int finalI = i;

                TalentButton btn = new TalentButton(finalI +1, talent, talents.get(finalI).get(talent), canUpgrade , TalentButton.State.AVAILABLE){
                    @Override
                    public void upgradeTalent() {
                        super.upgradeTalent();

                        for(ArrayList<TalentButton> button: allbuttons)
                        {
                            for(TalentButton talentButton : button)
                            {
                                talentButton.stateUpdate();
                                talentButton.update();
                            }
                        }

                        if (parent != null) {
                            setupPoints(finalI);//the HUD use tier different from how it works in talent itself
                            layout();
                        }
                    }
                };
                buttons.add(btn);
                content.add(btn);
            }

            allbuttons.add(buttons);

            if(i!=tiers-1) {
                ColorBlock sep = new ColorBlock(0, 1, 0xFF000000);
                separators.add(sep);
                content.add(sep);

                ColorBlock blocker = new ColorBlock(0, 0, 0xFF222222);
                content.add(blocker);
            }
        }
	}

    private void setupPoints(int tier){
	    points.get(tier).text(Dungeon.hero.talentPointsAvailable(tier+1) + "/" + Dungeon.hero.talentAllPossible(tier+1));
    }

	@Override
	protected void layout() {
		super.layout();

        switch (Dungeon.hero.heroClass) {
            default:
            case WARRIOR:
                content.setSizeWithoutLayouter(ChenSkillTreeLayouter.ContentWidth, ChenSkillTreeLayouter.ContentHeight);//FIXME:Yeah I wish I could find a easier way to layout this thing but for now it looks like only can be done manually.
                //Check that class for more info
                break;
        }

        littlefinger.visible = width < content.width();
        if (littlefinger.visible) {
            littlefinger.scale.set(  width * width / content.width(), 2 );
            littlefinger.x = 0;
            littlefinger.y = 0;
        }

        thumb.visible = height < content.height();
        if (thumb.visible) {
            thumb.scale.set( 2, height * height / content.height() );
            thumb.x = right() - thumb.width();
            thumb.y = y + height * content.camera.scroll.y / content.height();
        }

        switch (Dungeon.hero.heroClass) {
            default:
            case WARRIOR:
                ChenSkillTreeLayouter.layoutSkillTree(this);//FIXME:Yeah I wish I could find a easier way to layout this thing but for now it looks like only can be done manually.
                //Check that class for more info
                break;
        }
    }

    @Override
    public float xSensitivity()
    {
        return 50f;
    }

    @Override
    public float ySensitivity()
    {
        if(landscape())
        {
            return 10000f;
        }

        return 5000f;
    }

    public void updateButtons(Talent talent) {

	    for(ArrayList<TalentButton> button: allbuttons)
        {
            for(TalentButton talentButton : button)
            {
                talentButton.stateUpdate();
                if(talent.Mutex().contains(talentButton.talent))
                {
                    talentButton.state = TalentButton.State.ON_MUTEX;
                    talentButton.update();
                }
            }
        }
    }

    public void reset()
    {
        for(ArrayList<TalentButton> button: allbuttons)
        {
            for(TalentButton talentButton : button)
            {
                talentButton.stateUpdate();
                talentButton.update();
            }
        }
    }

    public float middleLine()
    {
        return points.get(2).x + points.get(2).width()/2;
    }
}