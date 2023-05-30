package cn.itcast.feign.util;


import java.util.HashMap;

//@Component
public class Constant {
    public static final int CITY_BEIJING=1;

    public static final int CITY_SHANGHAI=2;

    public static final int CITY_KUNMING=3;

    public static final int CITY_GUANGZHOU=4;

    public static final int CITY_FUZHOU=5;

    public static final int CITY_WULUMUQI=6;

    public static final int CITY_GUIYANG=7;

    public static final int CITY_XIAN=8;

    public static final int CITY_NANCHANG=9;

    public static final HashMap<String,Integer> CITY_TO_ID_MAP;
    static {
        CITY_TO_ID_MAP=new HashMap<>();
        CITY_TO_ID_MAP.put("北京", CITY_BEIJING);
        CITY_TO_ID_MAP.put("上海", CITY_SHANGHAI);
        CITY_TO_ID_MAP.put("昆明", CITY_KUNMING);
        CITY_TO_ID_MAP.put("广州", CITY_GUANGZHOU);
        CITY_TO_ID_MAP.put("福州", CITY_FUZHOU);
        CITY_TO_ID_MAP.put("乌鲁木齐", CITY_WULUMUQI);
        CITY_TO_ID_MAP.put("贵阳", CITY_GUIYANG);
        CITY_TO_ID_MAP.put("西安", CITY_XIAN);
        CITY_TO_ID_MAP.put("南昌", CITY_NANCHANG);
    }

    public static final HashMap<Integer, String> ID_TO_CITY_MAP;
    static {
        ID_TO_CITY_MAP = new HashMap<>();
        ID_TO_CITY_MAP.put(CITY_BEIJING, "北京");
        ID_TO_CITY_MAP.put(CITY_SHANGHAI, "上海");
        ID_TO_CITY_MAP.put(CITY_KUNMING, "昆明");
        ID_TO_CITY_MAP.put(CITY_GUANGZHOU, "广州");
        ID_TO_CITY_MAP.put(CITY_FUZHOU, "福州");
        ID_TO_CITY_MAP.put(CITY_WULUMUQI, "乌鲁木齐");
        ID_TO_CITY_MAP.put(CITY_GUIYANG, "贵阳");
        ID_TO_CITY_MAP.put(CITY_XIAN, "西安");
        ID_TO_CITY_MAP.put(CITY_NANCHANG, "南昌");
    }


    //运力的当前状态
    public static final int TRANSPORTATION_STATUS_WAITING =1;   //等待发车
    public static final int TRANSPORTATION_STATUS_IN_TRANSIT =2;//正在路上
    public static final int TRANSPORTATION_STATUS_FINISH =3;    //已经送达

    public static final int TRANSPORTATION_REFRIGERATED =1;//运力有冷冻功能
    public static final int TRANSPORTATION_NOT_REFRIGERATED =0;//运力没有冷冻功能

    //运力的类型
    public static final int TRANSPORTATION_TYPE_SMALL_TRUCK =1;//小货车
    public static final int TRANSPORTATION_TYPE_BIG_TRUCK =2;//大货车
    public static final int TRANSPORTATION_TYPE_AIRCRAFT =3;//飞机

    //运力的最大承载量
    public static final double TRANSPORTATION_MAX_CAPACITY_SMALL_TRUCK =3000;//小货车
    public static final double TRANSPORTATION_MAX_CAPACITY_BIG_TRUCK =18000;//大货车
    public static final double TRANSPORTATION_MAX_CAPACITY_AIRCRAFT =100000;//飞机


    //用户身份
    public static final int USER_IDENTITY_OWNER =0;//货主
    public static final int USER_IDENTITY_CARRIER =1;//承运商
    public static final int USER_IDENTITY_ADMIN =2;//管理员

    // 订单状态
    public static final Integer STATE_PUBLISH = 0;
    public static final Integer STATE_NEGOTIATE = 1;
    public static final Integer STATE_WAITING = 2;
    public static final Integer STATE_TRANSPORT = 3;
    public static final Integer STATE_ARRIVED = 4;
    public static final Integer STATE_COMPLETED = 5;




}
