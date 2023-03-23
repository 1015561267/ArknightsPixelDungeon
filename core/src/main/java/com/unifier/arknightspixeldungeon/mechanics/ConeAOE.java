package com.unifier.arknightspixeldungeon.mechanics;

import com.unifier.arknightspixeldungeon.Dungeon;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

//a cone made of up several ballisticas scanning in an arc
public class ConeAOE {

    public Ballistica coreRay;

    public ArrayList<Ballistica> outerRays = new ArrayList<>();
    public ArrayList<Ballistica> rays = new ArrayList<>();
    public HashSet<Integer> cells = new HashSet<>();

    public ConeAOE( Ballistica core, float degrees ){
        this( core, Float.POSITIVE_INFINITY, degrees, core.collisionProperties );
    }

    public ConeAOE( Ballistica core, float maxDist, float degrees, int ballisticaParams ){

        coreRay = core;

        //we want to use true coordinates for our trig functions, not game cells
        // so get the center of from and to as points
        PointF fromP = new PointF(Dungeon.level.cellToPoint(core.sourcePos));
        fromP.x += 0.5f;
        fromP.y += 0.5f;

        PointF toP = new PointF(Dungeon.level.cellToPoint(core.collisionPos));
        toP.x += 0.5f;
        toP.y += 0.5f;

        //clamp distance of cone to maxDist (in true distance, not game distance)
        if (PointF.distance(fromP, toP) > maxDist){
            toP = PointF.inter(fromP, toP, maxDist/PointF.distance(fromP, toP) );
        }

        //now we can get the circle's radius. We bump it by 0.5 as we want the cone to reach
        // The edge of the target cell, not the center.
        float circleRadius = PointF.distance(fromP, toP);
        circleRadius += 0.5f;

        //Now we find every unique cell along the outer arc of our cone.
        PointF scan = new PointF();
        Point scanInt = new Point();
        float initalAngle = PointF.angle(fromP, toP)/PointF.G2R;
        //want to preserve order so that our collection of rays is going clockwise
        LinkedHashSet<Integer> targetCells = new LinkedHashSet<>();
        LinkedHashSet<Integer> outerCells = new LinkedHashSet<>();

        //cast a ray every 0.5 degrees in a clockwise arc, to find cells along the cone's outer arc
        for (float a = initalAngle+degrees/2f; a >= initalAngle-degrees/2f; a-=0.5f){
            scan.polar(a * PointF.G2R, circleRadius);
            scan.offset(fromP);
            scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
            scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
            scanInt.set(
                    (int) GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                    (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
            targetCells.add(Dungeon.level.pointToCell(scanInt));
            outerCells.add(Dungeon.level.pointToCell(scanInt));
            //if the cone is large enough, also cast rays to cells just inside of the outer arc
            // this helps fill in any holes when casting rays
            if (circleRadius >= 4) {
                scan.polar(a * PointF.G2R, circleRadius - 1);
                scan.offset(fromP);
                scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
                scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
                scanInt.set(
                        (int)GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                        (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
                targetCells.add(Dungeon.level.pointToCell(scanInt));
            }
        }

        //cast a ray to each found cell, these make up the cone
        //we don't add the core ray as its collision properties may differ from the cone
        for( int c : targetCells ){
            Ballistica ray = new Ballistica(core.sourcePos, c, ballisticaParams);
            cells.addAll(ray.subPath(1, ray.dist));
            rays.add(ray);
            if (outerCells.contains(c)){
                outerRays.add(ray);
            }
        }

    }

    public ConeAOE( int from,int maxDist,float angle,int degrees,int ballisticaParams)
    //this use a start point ,maxDist,degrees and a center angle(relatively ,take right up ahead as 0) to make a similar coneAoe
    {
        PointF pointF = new PointF(Dungeon.level.cellToPoint(from));
        pointF.x += 0.5f;
        pointF.y += 0.5f;

        PointF temp = new PointF();
        temp.polar(angle * PointF.G2R, maxDist);
        temp.offset(pointF);
        temp.x += (pointF.x > temp.x ? +0.5f : -0.5f);
        temp.y += (pointF.y > temp.y ? +0.5f : -0.5f);
        temp = PointF.inter(pointF, temp, maxDist/PointF.distance(pointF, temp) );

        Point middle = new Point();
        middle.set(
                (int) GameMath.gate(0, (int)Math.floor(temp.x), Dungeon.level.width()-1),
                (int)GameMath.gate(0, (int)Math.floor(temp.y), Dungeon.level.height()-1));

        int target = Dungeon.level.pointToCell(middle);

        Ballistica core = new Ballistica(from,target,Ballistica.STOP_TARGET);

        //-- get that Ballistica,then almost same as what it do in past

        coreRay = core;

        //we want to use true coordinates for our trig functions, not game cells
        // so get the center of from and to as points
        PointF fromP = new PointF(Dungeon.level.cellToPoint(core.sourcePos));
        fromP.x += 0.5f;
        fromP.y += 0.5f;

        PointF toP = new PointF(Dungeon.level.cellToPoint(core.collisionPos));
        toP.x += 0.5f;
        toP.y += 0.5f;

        //clamp distance of cone to maxDist (in true distance, not game distance)
        if (PointF.distance(fromP, toP) > maxDist){
            toP = PointF.inter(fromP, toP, maxDist/PointF.distance(fromP, toP) );
        }

        //now we can get the circle's radius. We bump it by 0.5 as we want the cone to reach
        // The edge of the target cell, not the center.
        float circleRadius = PointF.distance(fromP, toP);
        circleRadius += 0.5f;

        //Now we find every unique cell along the outer arc of our cone.
        PointF scan = new PointF();
        Point scanInt = new Point();
        float initalAngle = PointF.angle(fromP, toP)/PointF.G2R;
        //want to preserve order so that our collection of rays is going clockwise
        LinkedHashSet<Integer> targetCells = new LinkedHashSet<>();
        LinkedHashSet<Integer> outerCells = new LinkedHashSet<>();

        //cast a ray every 0.5 degrees in a clockwise arc, to find cells along the cone's outer arc
        for (float a = initalAngle+degrees/2f; a >= initalAngle-degrees/2f; a-=0.5f){
            scan.polar(a * PointF.G2R, circleRadius);
            scan.offset(fromP);
            scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
            scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
            scanInt.set(
                    (int) GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                    (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
            targetCells.add(Dungeon.level.pointToCell(scanInt));
            outerCells.add(Dungeon.level.pointToCell(scanInt));
            //if the cone is large enough, also cast rays to cells just inside of the outer arc
            // this helps fill in any holes when casting rays
            if (circleRadius >= 4) {
                scan.polar(a * PointF.G2R, circleRadius - 1);
                scan.offset(fromP);
                scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
                scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
                scanInt.set(
                        (int)GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
                        (int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
                targetCells.add(Dungeon.level.pointToCell(scanInt));
            }
        }

        //cast a ray to each found cell, these make up the cone
        //we don't add the core ray as its collision properties may differ from the cone
        for( int c : targetCells ){
            Ballistica ray = new Ballistica(core.sourcePos, c, Ballistica.STOP_TARGET);
            cells.addAll(ray.subPath(1, ray.dist));
            rays.add(ray);
            if (outerCells.contains(c)){
                outerRays.add(ray);
            }
        }

    }
}