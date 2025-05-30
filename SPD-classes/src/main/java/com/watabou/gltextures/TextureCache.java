/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.watabou.gltextures;

import static com.watabou.noosa.Scene.landscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashMap;

public class TextureCache {

    private static HashMap<Object, SmartTexture> all = new HashMap<>();

    public synchronized static SmartTexture createSolid(int color) {
        final String key = "1x1:" + color;

        if (all.containsKey(key)) {

            return all.get(key);

        } else {

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            // In the rest of the code ARGB is used
            pixmap.setColor((color << 8) | (color >>> 24));
            pixmap.fill();
            SmartTexture tx = new SmartTexture(pixmap);
            all.put(key, tx);

            return tx;
        }
    }

    public synchronized static SmartTexture createGradient(int... colors) {

        final String key = "" + colors;

        if (all.containsKey(key)) {

            return all.get(key);

        } else {
            Pixmap pixmap = new Pixmap(colors.length, 1, Pixmap.Format.RGBA8888);

            for (int i = 0; i < colors.length; i++) {
                // In the rest of the code ARGB is used
                pixmap.drawPixel(i, 0, (colors[i] << 8) | (colors[i] >>> 24));
            }
            SmartTexture tx = new SmartTexture(pixmap);

            tx.filter(Texture.LINEAR, Texture.LINEAR);
            tx.wrap(Texture.CLAMP, Texture.CLAMP);

            all.put(key, tx);
            return tx;
        }

    }

    public synchronized static SmartTexture createCircle( int r ,int ...colors )
    {
        final String key = "cir" + colors;

        final int RADIUS	= r;

        if (all.containsKey(key)) {

            return all.get(key);

        } else {

            Pixmap pixmap = new Pixmap(2*RADIUS+1, 2*RADIUS+1, Pixmap.Format.RGBA8888);

            //int color = 0x000001E;
            int color = 0x00000FF;

            for (int i = 0; i < RADIUS; i+=2 ) {
                //pixmap.setColor((colors[i] << 8) | (colors[i] >>> 24));
                //color += landscape() ? 0x00000008 : 0x0000000C;

                color -= landscape() ? 1f : 1.5f;

                //pixmap.setColor(color);
                //pixmap.drawCircle(RADIUS , RADIUS, RADIUS - i);
                pixmap.setColor(color);
                pixmap.drawCircle(RADIUS , RADIUS, RADIUS - i);
            }

            SmartTexture tx = new SmartTexture(pixmap);
            tx.filter(Texture.NEAREST, Texture.NEAREST);
            all.put(key, tx);

            return tx;
        }
    }

    public synchronized static Image createNewCircle(int r , int ...colors )
    {
        ArrayList<PointF> POINTS = new ArrayList<>();

        POINTS.add( new PointF( 0, 0 ) );

        float radius = 10f;
        for (float i=-radius; i<=radius; i++) {
            //POINTS.add( new PointF( 0, i) );
            //POINTS.add( new PointF( i, 0) );
        }

        for (int angleDeg = 0; angleDeg < 360; angleDeg += 3) {
            float angleRad = (float) Math.toRadians(angleDeg); // 도 → 라디안
            float x = radius * (float) Math.cos(angleRad);
            float y = radius * (float) Math.sin(angleRad);
            POINTS.add( new PointF( x, y ) );
        }

       return null;
    }

    //texture is created at given size, but size is not enforced if it already exists
    //texture contents are also not enforced, make sure you know the texture's state!
    public synchronized static SmartTexture create( Object key, int width, int height ) {

        if (all.containsKey( key )) {

            return all.get( key );

        } else {

            SmartTexture tx = new SmartTexture(new Pixmap( width, height, Pixmap.Format.RGBA8888 ));

            tx.filter( Texture.LINEAR, Texture.LINEAR );
            tx.wrap( Texture.CLAMP, Texture.CLAMP );

            all.put( key, tx );

            return tx;
        }
    }

    public synchronized static void remove(Object key) {
        SmartTexture tx = all.get(key);
        if (tx != null) {
            all.remove(key);
            tx.delete();
        }
    }

    public synchronized static SmartTexture get(Object src) {

        if (all.containsKey(src)) {

            return all.get(src);

        } else if (src instanceof SmartTexture) {

            return (SmartTexture) src;

        } else {

            SmartTexture tx = new SmartTexture(getBitmap(src));
            all.put(src, tx);
            return tx;
        }

    }

    public synchronized static void clear() {

        for (Texture txt : all.values()) {
            txt.delete();
        }
        all.clear();

    }

    public synchronized static void reload() {
        for (SmartTexture tx : all.values()) {
            tx.reload();
        }
    }

    public static Pixmap getBitmap(Object src) {

        try {
            if (src instanceof Integer) {

                //LibGDX does not support android resource integer handles, and they were
                //never used by the game anyway, should probably remove this entirely
                return null;

            } else if (src instanceof String) {

                return new Pixmap(Gdx.files.internal((String) src));

            } else if (src instanceof Pixmap) {

                return (Pixmap) src;

            } else {

                return null;

            }
        } catch (Exception e) {

            Game.reportException(e);
            return null;

        }
    }

    public synchronized static boolean contains(Object key) {
        return all.containsKey(key);
    }

}
