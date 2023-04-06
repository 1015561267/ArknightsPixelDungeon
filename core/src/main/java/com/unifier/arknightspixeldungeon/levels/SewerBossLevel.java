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
import com.unifier.arknightspixeldungeon.actors.mobs.DublinnScout;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.actors.mobs.Rat;
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

import java.util.ArrayList;
import java.util.HashSet;

public class SewerBossLevel extends Level {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

    private static final String STAIRS	= "stairs";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
    }

    private static final int WIDTH = 13;
    private static final int HEIGHT = 24;

    private static final Rect entry = new Rect(1, 13, 12, 24);
    private static final Rect arena = new Rect(1, 1, 12, 12);

    private static final int bottomDoor = 6 + (arena.bottom) * WIDTH;
    //private static final int topDoor = 7 + arena.top * WIDTH;

    private static final int topDoor = 6;

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
        //transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

        //Arena room
        Painter.fill(this, arena,Terrain.EMPTY);

        Painter.fill(this, arena, 4, Terrain.EMPTY_SP);

        c = arena.center();
        Painter.set(this, c.x-3, c.y - 3, Terrain.STATUE);
        Painter.set(this, c.x, c.y - 3, Terrain.STATUE);
        Painter.set(this, c.x+3, c.y - 3, Terrain.STATUE);

        Painter.set(this, c.x-3, c.y, Terrain.STATUE);
        Painter.set(this, c.x+3, c.y, Terrain.STATUE);

        Painter.set(this, c.x-3, c.y + 3, Terrain.STATUE);
        Painter.set(this, c.x, c.y + 3, Terrain.STATUE);
        Painter.set(this, c.x+3, c.y + 3, Terrain.STATUE);

        //Painter.set(this, c.x, arena.top, Terrain.LOCKED_DOOR);

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

            set( bottomDoor, Terrain.DOOR );
            GameScene.updateMap( bottomDoor );

            set(topDoor,Terrain.EXIT);
            GameScene.updateMap( topDoor );

            Dungeon.observe();
	}

    @Override
    public void press( int cell, Char ch ) {

        super.press( cell, ch );

        if (map[bottomDoor] != Terrain.LOCKED_DOOR //&& map[topDoor] == Terrain.LOCKED_DOOR
                && cell == arena.center().x + arena.center().y * WIDTH && ch == Dungeon.hero) {

            seal();

            SarkazCenturion boss = new SarkazCenturion();

            boss.state = boss.WANDERING;
            boss.pos = getSummoningPos();
            GameScene.add( boss );
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.notice();
                boss.sprite.alpha( 0 );
                boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
            }

            set( bottomDoor, Terrain.LOCKED_DOOR );
            GameScene.updateMap( bottomDoor );
            Dungeon.observe();
        }
    }

	public void onBerserkBegin()
    {
        for(int i=0;i<2;i++){
            Mob rat = new Rat();
            rat.state = rat.WANDERING;
            rat.pos = pointToCell(arena.center());
            GameScene.add( rat );
            rat.beckon(Dungeon.hero.pos);
        }
    }

    public void onBerserkEnd()
    {
        for(int i=0;i<2;i++){
            Mob dublinnScout = new DublinnScout();
            dublinnScout.state = dublinnScout.WANDERING;
            dublinnScout.pos = pointToCell(arena.center());
            GameScene.add( dublinnScout );
            dublinnScout.beckon(Dungeon.hero.pos);
        }
    }

    static final HashSet<Integer> positions = new HashSet<>();

    static {
        Point c = arena.center();

        for(int i=0;i<WIDTH-2;i++)
        {
            positions.add(c.x - 5 + (c.y - 5 ) * WIDTH + i);//top
            positions.add(c.x - 5 + (c.y + 5 ) * WIDTH + i);//bottom
            positions.add(c.x - 5 + (c.y - 5 ) * WIDTH + i * WIDTH);//left
            positions.add(c.x - 5 + (c.y + 5 ) * WIDTH + i * WIDTH);//right
        }
    }

    public int getSummoningPos(){

//        Point c = arena.center();
//
//        ArrayList<Integer> temp = new ArrayList<>();
//        for(Integer record : positions){
//            if(Actor.findChar(record)==null){
//                temp.add(record);
//            }
//        }

        ArrayList<Integer> temp = new ArrayList<>();

        int k;
        for (int i = 1;i < 11;i++) {
            //1,1 to 10,1
            k = i + 1 * WIDTH;
            temp.add(k);

            //11,1 to 11,10
            k = 11 + i * WIDTH;
            temp.add(k);

            //1,2 to 1,11
            k = 1 + (i + 1) * WIDTH;
            temp.add(k);

            //2,11 to 11,11
            k =(i + 1) + 11 * WIDTH;
            temp.add(k);
        }

        if (temp.isEmpty()){
            return -1;//this should not happen
        } else {
            return Random.element(temp);
        }
    }
}
