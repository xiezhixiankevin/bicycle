package com.shixun.bicycle.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.awt.geom.Point2D;

/**
 * <Description> DistanceUtil
 *
 * @author 26802
 * @version 1.0
 * @see com.shixun.bicycle.utils
 */
public class DistanceUtil {

    private static final double EARTH_RADIUS = 6371393; // 平均半径,单位：m

    /**
     * 通过AB点经纬度获取距离
     * @return 距离(单位：米)
     */
//    public static double getDistance(Double Ajd,Double Awd,Double Bjd,Double Bwd) {
//
//        Point2D pointA = new Point2D.Double(Ajd,Awd);
//        Point2D pointB = new Point2D.Double(Bjd,Bwd);
//
//        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
//        double radiansAX = Math.toRadians(pointA.getX()); // A经弧度
//        double radiansAY = Math.toRadians(pointA.getY()); // A纬弧度
//        double radiansBX = Math.toRadians(pointB.getX()); // B经弧度
//        double radiansBY = Math.toRadians(pointB.getY()); // B纬弧度
//
//        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
//        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
//                + Math.sin(radiansAY) * Math.sin(radiansBY);
////        System.out.println("cos = " + cos); // 值域[-1,1]
//        double acos = Math.acos(cos); // 反余弦值
////        System.out.println("acos = " + acos); // 值域[0,π]
////        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
//        return EARTH_RADIUS * acos; // 最终结果
//    }
    public static double getDistance(Double Ajd,Double Awd,Double Bjd,Double Bwd) {
        {
            GlobalCoordinates gpsFrom = new GlobalCoordinates(Ajd, Awd);
            GlobalCoordinates gpsTo = new GlobalCoordinates(Bjd, Bwd);

            //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
            GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, gpsFrom, gpsTo);

            return geoCurve.getEllipsoidalDistance();
        }

    }
}
