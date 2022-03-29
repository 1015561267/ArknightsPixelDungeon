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

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.windows.WndInfoTalent;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Callback;

import static com.unifier.arknightspixeldungeon.Dungeon.hero;

public class TalentButton extends Button {

    public enum State{
        TEMPORARILY_UNAVAILABLE,ALREADY_UPGRADED,AVAILABLE,ON_SELECTED,ON_MUTEX,EXCLUDED;
    }


	public static final int WIDTH = 20;
	public static final int HEIGHT = 26;

	private SmartTexture icons;
	private TextureFilm film;

	private float warning = 0f;
    private float talentBlink = 10f;


	int tier;
	Talent talent;
	int pointsInTalent;
	boolean upgradeEnabled;

	Image icon;
	Image bg;

	public State state;

	ColorBlock fill;

	public TalentButton(int tier, Talent talent, int points, boolean upgradeEnabled,State state){
		super();
		hotArea.blockLevel = PointerArea.NEVER_BLOCK;

		this.tier = tier;
		this.talent = talent;
		this.pointsInTalent = points;
		this.upgradeEnabled = upgradeEnabled;
        this.state = state;

		bg.frame(20*(talent.maxPoints()-1), 0, WIDTH, HEIGHT);
		icon.frame( film.get( talent.icon() ) );

		ButtonUpgrade();
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		icons = TextureCache.get( Assets.TALENT_ICONS );
		film = new TextureFilm( icons, 16, 16 );

		fill = new ColorBlock(0, 4, 0xFFFFFF44);
		add(fill);

		bg = new Image(Assets.TALENT_BUTTON);
		add(bg);

		icon = new Image( icons );
		add(icon);
	}

	@Override
	protected void layout() {
		width = WIDTH;
		height = HEIGHT;

		super.layout();

		fill.x = x+2;
		fill.y = y + WIDTH - 1;
		fill.size( pointsInTalent/(float)talent.maxPoints() * (WIDTH-4), 5);

		bg.x = x;
		bg.y = y;

		icon.x = x + 2;
		icon.y = y + 2;
		PixelScene.align(icon);

        ButtonUpgrade();
	}

	@Override
	protected void onClick() {
		super.onClick();

		Talent.checkResult result = Talent.talentCheck(talent);

		if(state == State.ON_SELECTED || state == State.ALREADY_UPGRADED)
        {
            ArknightsPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, new Callback() {
                @Override
                public void call() {
                    state = State.ALREADY_UPGRADED;
                    upgradeTalent();
                }
            },result));
        }

		else if(state == State.ON_MUTEX)
        {
            if(this.parent.parent instanceof TalentsPane)
            {
                ((TalentsPane)this.parent.parent).updateButtons(talent);
            }
            state = State.ON_SELECTED;

            ArknightsPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, new Callback() {
                @Override
                public void call() {
                    state = State.ALREADY_UPGRADED;
                    upgradeTalent();
                }
            },result));
        }

		else if(state == State.AVAILABLE)
        {
            if(talent.Mutex()!= null )
            {
                if(this.parent.parent instanceof TalentsPane)
                {
                    ((TalentsPane)this.parent.parent).updateButtons(talent);//Note here we have a "allbuttons" as button group,all button add to this,and it added to TalentsPane
                }
                state = State.ON_SELECTED;
            }

            else ArknightsPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, new Callback() {
                @Override
                public void call() {
                    state = State.ALREADY_UPGRADED;
                    upgradeTalent();
                }
            },result));
        }
		else if(state == State.EXCLUDED || state == State.TEMPORARILY_UNAVAILABLE)
        {
            ArknightsPixelDungeon.scene().addToFront(new WndInfoTalent(talent, pointsInTalent, null,result));
        }
	}

	@Override
	protected void onPointerDown() {
		icon.brightness( 1.5f );
		bg.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}

	@Override
	protected void onPointerUp() {
		icon.resetColor();
		bg.resetColor();
	}

	public void enable( boolean value ) {
		active = value;
		icon.alpha( value ? 1.0f : 0.3f );
		bg.alpha( value ? 1.0f : 0.3f );
	}

	public void upgradeTalent(){
		if (hero.talentPointsAvailable(tier) > 0 && parent != null) {
			hero.upgradeTalent(talent);
			float oldWidth = fill.width();
			pointsInTalent++;
			layout();
			Sample.INSTANCE.play(Assets.SND_LEVELUP, 0.7f, 1.2f);
			Emitter emitter = (Emitter) parent.recycle(Emitter.class);
			emitter.revive();
			emitter.pos(fill.x + (fill.width() + oldWidth) / 2f, fill.y + fill.height() / 2f);
			emitter.burst(Speck.factory(Speck.STAR), 12);
		}
	}

	public void ButtonUpgrade(){
        stateUpdate();
        update();
    }

    public void stateUpdate() {
	    //if(state == State.ON_SELECTED || state == State.ON_SELECTED)

        Talent.checkResult result = Talent.talentCheck(talent);

        switch (result)
        {
            case NEED_PRECONDITION:
            case NEED_ITEM:
            case NOT_ENOUGH_POINTS_LOW_LEVEL:
            case NOT_ENOUGH_POINTS_RUN_OUT: state = State.TEMPORARILY_UNAVAILABLE;break;

            case NOT_ENOUGH_POINTS_ALREADY_UPGRADED:
            case ALREADY_UPGRADED_AVAILABLE:
            case ALREADY_FULL: state = State.ALREADY_UPGRADED;break;

            case AVAILABLE:state = State.AVAILABLE;break;
            case MUTEX_TALENT:state = State.EXCLUDED;break;
        }
	}

    @Override
    public void update(){
        if(state == State.ON_MUTEX)
        {
            warning -=  Game.elapsed;
            icon.tint(1, 0, 0, (float)Math.abs(Math.sin(2*warning)/2f));
            bg.tint(1, 0, 0, (float)Math.abs(Math.sin(2*warning)/2f));
        }
        else if(state == State.ON_SELECTED)
        {
            talentBlink -=  Game.elapsed;
            icon.tint(1, 1, 0, (float)Math.abs(Math.sin(2*talentBlink)/2f));
            bg.tint(1, 1, 0, (float)Math.abs(Math.sin(2*talentBlink)/2f));
        }

        else if (state == State.TEMPORARILY_UNAVAILABLE)
        {
            icon.tint(0x000000, 0.25f);
            bg.tint(0x000000, 0.25f);
        }
        else if (state == State.EXCLUDED)
        {
            icon.tint(0x000000, 0.75f);
            bg.tint(0x000000, 0.75f);
        }
        else {
            icon.resetColor();
            bg.resetColor();
        }
    }

    private static final int[] warningColors = new int[]{0x660000, 0xCC0000, 0x660000};
}
