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

import static com.unifier.arknightspixeldungeon.scenes.PixelScene.landscape;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;

public class Archs extends Component {

	private static final float SCROLL_SPEED	= 20f;

	private SkinnedBlock arcsBg;
	private SkinnedBlock arcsFg;
    private Image darkness;
    private LoginElemet loginElement;

	private static float offsB = 0;
	private static float offsF = 0;

	public boolean reversed = false;

	@Override
	protected void createChildren() {
		arcsBg = new SkinnedBlock( 1, 1, Assets.ARCS_BG ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//arch bg has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		arcsBg.autoAdjust = true;
		arcsBg.offsetTo( 0,  offsB );
		add( arcsBg );

		arcsFg = new SkinnedBlock( 1, 1, Assets.ARCS_FG ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}
		};
		arcsFg.autoAdjust = true;
		arcsFg.offsetTo( 0,  offsF );
		add( arcsFg );

        //loginElement.origin.set(loginElement.width / 2, loginElement.height / 2);

        if(landscape()) {
            loginElement = new LoginElemet();
            loginElement.visible = true;
            add(loginElement);
        }
        else {
            darkness= new Image(TextureCache.createCircle(80));
            darkness.origin.set(darkness.width / 2, darkness.height / 2);
            add(darkness);
        }
    }


	@Override
	protected void layout() {
		arcsBg.size( width, height );
		arcsBg.offset( arcsBg.texture.width / 4 - (width % arcsBg.texture.width) / 2, 0 );

		arcsFg.size( width, height );
		arcsFg.offset( arcsFg.texture.width / 4 - (width % arcsFg.texture.width) / 2, 0 );

        if(landscape()) {
            loginElement.x = width / 2f - (loginElement.width() / 2f);
            loginElement.y = 0;//-(loginElement.height() / 2f);
        }
        else {
            darkness.x = width/2f - (darkness.width() / 2f);
            darkness.y = -(darkness.height() / 2f);
            PixelScene.align(darkness);
        }

	}

	@Override
	public void update() {

		super.update();

		float shift = Game.elapsed * SCROLL_SPEED;
		if (reversed) {
			shift = -shift;
		}

		arcsBg.offset( 0, shift );
        arcsFg.offset( -shift*2, -shift * 2 );

		offsB = arcsBg.offsetY();
		offsF = arcsFg.offsetY();

        if(!landscape()) {
            darkness.angle += shift * 2;
        }
	}
}