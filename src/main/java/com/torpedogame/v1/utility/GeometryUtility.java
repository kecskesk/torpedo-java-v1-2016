package com.torpedogame.v1.utility;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by Dombi Soma on 06/11/2016.
 */
public class GeometryUtility {
    /**
     * This is an Adapter function for the Angle.angle(Coordinate p0, Coordinate p1) function.
     * The result interval is moved from [ -Pi, Pi ] to [0 , 360)
     *
     * @return The angle of the vector from p0 to p1 in degree.
     */
    public static double getDegree(Coordinate p0, Coordinate p1) {
        double angle = Angle.toDegrees(Angle.angle(p0, p1));
        if (angle < 0) {
            angle = 360 + angle;
        }
        return angle;
    }

    public static double sumDegrees (double d1, double d2) {
        if (d1 + d2 > 360)
            return d1 + d2 -360;
        else if (d1 + d2 < 0)
            return d1 + d2 + 360;
        else return d1 + d2;
    }
}
