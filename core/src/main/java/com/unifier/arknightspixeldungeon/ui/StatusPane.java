/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.PDAction;
import com.unifier.arknightspixeldungeon.Statistics;
import com.unifier.arknightspixeldungeon.actors.hero.skills.HeroSkill;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.windows.WndHero;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.ColorMath;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	private float warning;

    public static final float FLASH_RATE = (float)(Math.PI*1.5f); //1.5 blinks per second

    private int lastTier = 0;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private Image exp;

	private BossHealthBar bossHP;

	private int lastLvl = -1;

	private BitmapText level;
	//private BitmapText depth;

	//private DangerIndicator danger;
	private BuffIndicator buffs;
	private Compass compass;

	//private JournalButton btnJournal;
	//private MenuButton btnMenu;

	private Toolbar.PickedUpItem pickedUp;

    //private BitmapText version;

    private SkillLoader loader1;
    private SkillLoader loader2;
    private SkillLoader loader3;

	@Override
	protected void createChildren() {

		bg = new NinePatch( Assets.STATUS, 0, 0, 128, 36, 85, 0, 45, 0 );
		add( bg );

        add( new Button(){
            @Override
            protected void onClick () {
                Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
                GameScene.show( new WndHero() );
            }

            @Override
            public GameAction keyAction() {
                return PDAction.HERO_INFO;
            }
        }.setRect( 0, 1, 30, 30 ));

		//btnJournal = new JournalButton();
		//add( btnJournal );

		//btnMenu = new MenuButton();
		//add( btnMenu );

		avatar = HeroSprite.Portrait(Dungeon.hero.heroClass, lastTier);
		add( avatar );

		compass = new Compass( Statistics.amuletObtained ? Dungeon.level.entrance : Dungeon.level.exit );
		add( compass );

		rawShielding = new Image( Assets.SHLD_BAR );
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image( Assets.SHLD_BAR );
		add(shieldedHP);

		hp = new Image( Assets.HP_BAR );
		add( hp );

		exp = new Image( Assets.XP_BAR );
		add( exp );

		bossHP = new BossHealthBar();
		add( bossHP );

		level = new BitmapText( PixelScene.pixelFont);
		level.hardlight( 0xFFEBA4 );
		add( level );

		//depth = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.pixelFont);
		//depth.hardlight( 0xCACFC2 );
		//depth.measure();
		//add( depth );

		//danger = new DangerIndicator();
		//add( danger );

		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );

		add( pickedUp = new Toolbar.PickedUpItem());

        //version = new BitmapText(  Game.version, PixelScene.pixelFont);
        //version.alpha( 0.5f );
        //add(version);

        loader1 = new SkillLoader(Dungeon.hero.skill_1 != null ? Dungeon.hero.skill_1 : null);
        add(loader1);

        loader2 = new SkillLoader(Dungeon.hero.skill_2 != null ? Dungeon.hero.skill_2 : null);
        add(loader2);

        loader3 = new SkillLoader(Dungeon.hero.skill_3 != null ? Dungeon.hero.skill_3 : null);
        add(loader3);
	}

	@Override
	protected void layout() {

		height = 32;

		bg.size( width, bg.height );

		avatar.x = bg.x + 15 - avatar.width / 2f;
		avatar.y = bg.y + 16 - avatar.height / 2f;
		PixelScene.align(avatar);

		compass.x = avatar.x + avatar.width / 2f - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2f - compass.origin.y;
		PixelScene.align(compass);

		hp.x = shieldedHP.x = rawShielding.x = 30;
		hp.y = shieldedHP.y = rawShielding.y = 3;

		bossHP.setPos( 6 + (width - bossHP.width())/2, 20);

		//depth.x = width - 35.5f - depth.width() / 2f;
		//depth.y = 8f - depth.baseLine() / 2f;
		//PixelScene.align(depth);

		//danger.setPos( width - danger.width(), 20 );

		buffs.setPos( 31, 9 );

		//btnJournal.setPos( width - 42, 1 );

		//btnMenu.setPos( width - btnMenu.width(), 1 );

        //version.scale.set(PixelScene.align(0.5f));
        //version.measure();
        //version.x = width - version.width();
        //version.y = btnMenu.bottom() + (4 - version.baseLine());
        //PixelScene.align(version);

        loader1.setPos(avatar.x,avatar.y + avatar.height() + loader1.height() / 4);
        loader2.setPos(avatar.x,avatar.y + avatar.height() + loader1.height() / 4 + loader1.height() + loader1.height() / 4);
        loader3.setPos(avatar.x,avatar.y + avatar.height() + loader1.height() / 4 + loader1.height() + loader1.height() / 4 + loader2.height() + loader2.height() / 4);
    }
	
	private static final int[] warningColors = new int[]{0x660000, 0xCC0000, 0x660000};

	@Override
	public void update() {
		super.update();

		float health = Dungeon.hero.HP;
		float shield = Dungeon.hero.SHLD;
		float max = Dungeon.hero.HT;

		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health/max) < 0.3f) {
			warning += Game.elapsed * 5f *(0.4f - (health/max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, warningColors), 0.5f );
		} else {
			avatar.resetColor();
		}

		hp.scale.x = Math.max( 0, (health-shield)/max);
		shieldedHP.scale.x = health/max;
		rawShielding.scale.x = shield/max;

		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				Emitter emitter = (Emitter)recycle( Emitter.class );
				emitter.revive();
				emitter.pos( 27, 27 );
				emitter.burst( Speck.factory( Speck.STAR ), 12 );
			}

			lastLvl = Dungeon.hero.lvl;
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = 27.5f - level.width() / 2f;
			level.y = 28.0f - level.baseLine() / 2f;
			PixelScene.align(level);
		}

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy(HeroSprite.Portrait(Dungeon.hero.heroClass, lastTier));
			//avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}

	public void loadSkill(int index, HeroSkill skill)
    {

        switch (index){
            case 1:this.loader1 = new SkillLoader(skill); break;
            case 2:this.loader2 = new SkillLoader(skill);break;
            case 3:this.loader3 = new SkillLoader(skill);break;
            default:return;
        }
        layout();
    }
}
