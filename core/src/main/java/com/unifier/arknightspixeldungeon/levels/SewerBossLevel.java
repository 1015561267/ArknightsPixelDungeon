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

package com.unifier.arknightspixeldungeon.levels;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Bones;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.actors.mobs.SarkazCenturion;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.levels.painters.Painter;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.HashSet;

public class SewerBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

    private static final int WIDTH = 13;
    private static final int HEIGHT = 24;

    private static final Rect entry = new Rect(1, 13, 12, 24);
    private static final Rect arena = new Rect(1, 1, 12, 12);

    private static final int bottomDoor = 6 + (arena.bottom) * WIDTH;
    private static final int topDoor = 6;

    private boolean triggered = false;

    private static final String TRIGGERED	= "triggered";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( TRIGGERED, triggered );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        triggered = bundle.getBoolean( TRIGGERED );
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);

        //entrance room
        Painter.fill(this, entry, Terrain.WALL);
        Painter.fill(this, entry, 1, Terrain.BOOKSHELF);
        Painter.fill(this, entry, 2, Terrain.EMPTY);

        Painter.fill(this, entry.left+3, entry.top+3, 1, 5, Terrain.WALL);
        Painter.fill(this, entry.right-4, entry.top+3, 1, 5, Terrain.WALL);

        Point c = entry.center();

        Painter.fill(this, c.x-1, c.y-2, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x-1, c.y, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x-1, c.y+2, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x, entry.top+1, 1, 6, Terrain.EMPTY_SP);

        Painter.set(this, c.x, entry.top, Terrain.EMPTY);
        Painter.set(this, c.x, entry.top - 1, Terrain.DOOR);


        int entrance = c.x + (c.y+3)*width();
        Painter.set(this, entrance, Terrain.ENTRANCE);
        this.entrance = entrance;

        //Arena room
        Painter.fill(this, arena,Terrain.EMPTY);

        Painter.fill(this, arena, 4, Terrain.EMPTY_SP);

        c = arena.center();
        Painter.set(this, c.x-3, c.y - 3, Terrain.WALL);
        Painter.set(this, c.x, c.y - 3, Terrain.WALL_DECO);
        Painter.set(this, c.x+3, c.y - 3, Terrain.WALL);

        Painter.set(this, c.x-3, c.y, Terrain.WALL_DECO);
        Painter.set(this, c.x+3, c.y, Terrain.WALL_DECO);

        Painter.set(this, c.x-3, c.y + 3, Terrain.WALL);
        Painter.set(this, c.x, c.y + 3, Terrain.WALL_DECO);
        Painter.set(this, c.x+3, c.y + 3, Terrain.WALL);

        //Painter.set(this, c.x, arena.top, Terrain.LOCKED_DOOR);
        exit = topDoor;
        map[exit] = Terrain.LOCKED_EXIT;

        return true;
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_SEWERS;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_SEWERS;
    }


    @Override
    public Mob createMob() {
        return null;
    }

    public Actor respawner() {
		return null;
	}

    @Override
    protected void createMobs() {} //do nothing
	@Override
	protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos = randomRespawnCell();
            } while (pos == entrance);
            drop( item, pos ).type = Heap.Type.REMAINS;
        }
	}

    @Override
    public int randomRespawnCell() {
        int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
        while (!passable[cell]){
            cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
        }
        return cell;
    }
	
	public void seal() {
		if (entrance != 0) {

			super.seal();
			
			set( entrance, Terrain.EMPTY );
			GameScene.updateMap( entrance );
			//GameScene.ripple( entrance );

            set( bottomDoor, Terrain.LOCKED_DOOR );
            GameScene.updateMap( bottomDoor );
            Dungeon.observe();
		}
	}
	
	public void unseal() {
        super.unseal();

        set( entrance, Terrain.ENTRANCE );
        GameScene.updateMap( entrance );

        set( bottomDoor, Terrain.DOOR );
        GameScene.updateMap( bottomDoor );

        for (Mob m : mobs){
            if(m.isDerivative()){
                m.die(null);
            }
        }

        Dungeon.observe();
	}

    @Override
    public void press( int cell, Char ch ) {

        super.press( cell, ch );

        if (!triggered && map[bottomDoor] != Terrain.LOCKED_DOOR //&& map[topDoor] == Terrain.LOCKED_DOOR
                && cell == arena.center().x + arena.center().y * WIDTH && ch == Dungeon.hero) {

            triggered = true;

            seal();

            SarkazCenturion boss = new SarkazCenturion();

            boss.state = boss.WANDERING;
            boss.pos = getSummoningPos();
            GameScene.add( boss );
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.yell( "开战yell,待替换" );
                boss.sprite.alpha( 0 );
                boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
            }
        }
    }

	public void onBerserkBegin()
    {
        for(int i=0;i<2;i++){
            Mob rat = new SarkazCenturion.DerivativeRat();
            rat.state = rat.WANDERING;
            rat.pos = getSummoningPos();
            GameScene.add( rat );
            rat.beckon(Dungeon.hero.pos);
            if (heroFOV[rat.pos]) {
                rat.notice();
                rat.sprite.alpha( 0 );
                rat.sprite.parent.add( new AlphaTweener( rat.sprite, 1, 0.1f ) );
            }
        }
    }

    public void onBerserkEnd()
    {
        for(int i=0;i<2;i++){
            Mob dublinnScout = new SarkazCenturion.DerivativeDublinnScout();
            dublinnScout.state = dublinnScout.WANDERING;
            dublinnScout.pos = getSummoningPos();
            GameScene.add( dublinnScout );
            dublinnScout.beckon(Dungeon.hero.pos);
            if (heroFOV[dublinnScout.pos]) {
                dublinnScout.notice();
                dublinnScout.sprite.alpha( 0 );
                dublinnScout.sprite.parent.add( new AlphaTweener( dublinnScout.sprite, 1, 0.1f ) );
            }
        }
    }

    static final HashSet<Integer> positions = new HashSet<>();

    static {
        int k;
        for (int i = 1;i < 11;i++) {
            //1,1 to 10,1
            k = i + 1 * WIDTH;
            positions.add(k);

            //11,1 to 11,10
            k = 11 + i * WIDTH;
            positions.add(k);

            //1,2 to 1,11
            k = 1 + (i + 1) * WIDTH;
            positions.add(k);

            //2,11 to 11,11
            k =(i + 1) + 11 * WIDTH;
            positions.add(k);
        }

    }

    public int getSummoningPos(){

        for(int i = 0;i<10;i++)
        {
            int temp = Random.element(positions);
            if(Actor.findChar(temp)==null){
                return temp;
            }
        }
            return -1;//this should not happen
    }
}
