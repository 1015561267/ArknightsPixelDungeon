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

package com.unifier.arknightspixeldungeon.scenes;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.mobs.Mob;
import com.unifier.arknightspixeldungeon.items.Heap;
import com.unifier.arknightspixeldungeon.tiles.DungeonTilemap;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ScrollArea;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class CellSelector extends ScrollArea {

	public Listener listener = null;
	
	public boolean enabled;
	
	private float dragThreshold;
	
	public CellSelector( DungeonTilemap map ) {
		super( map );
		camera = map.camera();
		
		dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;
	}

	@Override
	protected void onClick( PointerEvent event ) {
		if (dragging) {
			
			dragging = false;
			
		} else {

			PointF p = Camera.main.screenToCamera( (int)event.current.x, (int)event.current.y );
            //Prioritizes a sprite if it and a tile overlap, so long as that sprite isn't more than 4 pixels into another tile.
            //The extra check prevents large sprites from blocking the player from clicking adjacent tiles

            //hero first
            if (Dungeon.hero.sprite != null && Dungeon.hero.sprite.overlapsPoint( p.x, p.y )){
                PointF c = DungeonTilemap.tileCenterToWorld(Dungeon.hero.pos);
                if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
                    select(Dungeon.hero.pos);
                    return;
                }
            }

            //then mobs
            for (Char mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob.sprite != null && mob.sprite.overlapsPoint( p.x, p.y )){
                    PointF c = DungeonTilemap.tileCenterToWorld(mob.pos);
                    if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
                        select(mob.pos);
                        return;
                    }
                }
            }

			for (Heap heap : Dungeon.level.heaps.values()){
				if (heap.sprite != null && heap.sprite.overlapsPoint( p.x, p.y)){
					select( heap.pos );
					return;
				}
			}

            select( ((DungeonTilemap)target).screenToTile(
                    (int) event.current.x,
                    (int) event.current.y,
                    true ) );
		}
	}

	private float zoom( float value ) {

		value = GameMath.gate( PixelScene.minZoom, value, PixelScene.maxZoom );
		PDSettings.zoom((int) (value - PixelScene.defaultZoom));
		camera.zoom( value );

		//Resets character sprite positions with the new camera zoom
		//This is important as characters are centered on a 16x16 tile, but may have any sprite size
		//This can lead to none-whole coordinate, which need to be aligned with the zoom
		for (Char c : Actor.chars()){
			if (c.sprite != null && !c.sprite.isMoving){
				c.sprite.point(c.sprite.worldToCamera(c.pos));
			}
		}

		return value;
	}
	
	public void select( int cell ) {
		if (enabled && listener != null && cell != -1) {
			
			listener.onSelect( cell );
			GameScene.ready();
			
		} else {
			
			GameScene.cancel();
			
		}
	}
	
	private boolean pinching = false;
	private PointerEvent another;
	private float startZoom;
	private float startSpan;
	
	@Override
	protected void onPointerDown( PointerEvent event ) {

		if (event != curEvent && another == null) {
					
			if (!curEvent.down) {
                curEvent = event;
                onPointerDown( event );
				return;
			}
			
			pinching = true;

			another = event;
			startSpan = PointF.distance( curEvent.current, another.current );
			startZoom = camera.zoom;

			dragging = false;
		} else if (event != curEvent) {
			reset();
		}
	}

    @Override
    protected void onPointerUp( PointerEvent event ) {
        if (pinching && (event == curEvent || event == another)) {

            pinching = false;

            zoom(Math.round( camera.zoom ));

            dragging = true;
            if (event == curEvent) {
                curEvent = another;
            }
            another = null;
            lastPos.set( curEvent.current );
        }
    }
	
	private boolean dragging = false;
	private PointF lastPos = new PointF();

    @Override
    protected void onDrag( PointerEvent event ) {

        if (pinching) {

            float curSpan = PointF.distance( curEvent.current, another.current );
            float zoom = (startZoom * curSpan / startSpan);
            camera.zoom( GameMath.gate(
                    PixelScene.minZoom,
                    zoom - (zoom % 0.1f),
                    PixelScene.maxZoom ) );

        } else {

            if (!dragging && PointF.distance( event.current, event.start ) > dragThreshold) {

                dragging = true;
                lastPos.set( event.current );

            } else if (dragging) {
                camera.shift( PointF.diff( lastPos, event.current ).invScale( camera.zoom ) );
                lastPos.set( event.current );
            }
        }

    }

	public void cancel() {
		
		if (listener != null) {
			listener.onSelect( null );
		}
		
		GameScene.ready();
	}

	@Override
	public void reset() {
		super.reset();
		another = null;
		if (pinching){
			pinching = false;

			zoom( Math.round( camera.zoom ) );
		}
	}

	public void enable(boolean value){
		if (enabled != value){
			enabled = value;
		}
	}


	public interface Listener {
		void onSelect( Integer cell );
		String prompt();
	}
}
