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

package com.unifier.arknightspixeldungeon.actors.mobs.npcs;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.Paralysis;
import com.unifier.arknightspixeldungeon.actors.buffs.Roots;
import com.unifier.arknightspixeldungeon.effects.CellEmitter;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scripts.NPCPlot.FrostNovaQuestPlot;
import com.unifier.arknightspixeldungeon.sprites.GhostSprite;
import com.unifier.arknightspixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Ghost extends NPC {

	{
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;
	}
	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}

	@Override
	protected boolean act() {
		if (FrostNovaQuestPlot.Quest.processed())
			target = Dungeon.hero.pos;
		return super.act();
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public float speed() {
		return FrostNovaQuestPlot.Quest.processed() ? 2f : 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );

        Sample.INSTANCE.play( Assets.SND_GHOST );

        if (c != Dungeon.hero){
            return super.interact(c);
        }

        FrostNovaQuestPlot plot = new FrostNovaQuestPlot();

        FrostNovaQuestPlot.Quest.setGhost(this);

		if (FrostNovaQuestPlot.Quest.given) {
			if (FrostNovaQuestPlot.Quest.weapon != null) {
				if (FrostNovaQuestPlot.Quest.processed) {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndDialog(plot,false));//because there are initial function,so declare process at here is unnecessary
                        }
                    });
				} else {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndDialog(plot,false));
                        }
                    });

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell();
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {
						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.level.heroFOV[pos];
					}
				}
			}
		} else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
		}

		return false;
	}
	
	{
		immunities.add( Paralysis.class );
		immunities.add( Roots.class );
	}

}
