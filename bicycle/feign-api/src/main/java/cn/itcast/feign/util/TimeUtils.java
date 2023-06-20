package cn.itcast.feign.util;

import java.util.Date;

/**
 * <Description> TimeUtils
 *
 * @author 26802
 * @version 1.0
 */
public class TimeUtils {

    public static int getDifferHour(Date startDate, Date endDate) {
        long dayM = 1000 * 24 * 60 * 60;
        long hourM = 1000 * 60 * 60;
        long differ = endDate.getTime() - startDate.getTime();
        long hour = differ % dayM / hourM;
        return Integer.parseInt(String.valueOf(hour));
    }

}
