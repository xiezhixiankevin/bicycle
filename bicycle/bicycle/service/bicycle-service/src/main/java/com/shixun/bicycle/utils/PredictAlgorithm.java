package com.shixun.bicycle.utils;

import cn.itcast.feign.pojo.Area;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.bicycle.mapper.AreaMapper;
import com.shixun.bicycle.pojo.LocationInfo;
import com.shixun.bicycle.service.AreaService;
import com.shixun.bicycle.service.impl.AreaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PredictAlgorithm {
    @Autowired
    AreaService areaService;

    private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //localhost为服务器地址，test2为数据库名称,可能需要修改
    private String DB_URL = "jdbc:mysql://121.4.113.134:3306/shixun?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    //为用户和密码
//    @Value("${spring.datasource.username}")
    private String USER = "root";
//    @Value("${spring.datasource.password}")
    private String PASS = "shiwo2002625";
    //存储相关表格信息
//    x_list 为存储相应x值
//    y_list 为存储相应y值
//    level_list存储信号强度
    static ArrayList<LocationInfo> locationInfo_List = new ArrayList<>();

    public int readlist(double jd,double wd){
        int area;
        Connection conn = null;
        locationInfo_List.clear();
        try
        {
            Class.forName(JDBC_DRIVER);
            System.out.println("驱动加载成功");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("数据库连接成功");
            String sql="select jd,wd,area from bicycle where state = 0;";
            System.out.println(sql);
            Statement st=(Statement)conn.createStatement();
            ResultSet rSet = st.executeQuery(sql);
            // 迭代打印出查询信息
            System.out.println("单车列表");
            while (rSet.next()) {
                LocationInfo locationInfo = new LocationInfo(Double.parseDouble(rSet.getString("jd")),Double.parseDouble(rSet.getString("wd")),Integer.parseInt(rSet.getString("area")));
                System.out.println("locationInfo : jd为：" + rSet.getString("jd") + " wd为：" + rSet.getString("wd") + " area为：" + rSet.getString("area"));
                locationInfo_List.add(locationInfo);
            }
            conn.close();
            rSet.close();
            st.close();
            //    ***************************************************************************************************匹配算法
            area = select_Kmin(jd,wd);
            return area;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //    匹配算法
    public int select_Kmin(double jd,double wd){

        double d = 999;
        int area;

        for(int i = 0 ; i < locationInfo_List.size() ; i++){
            d = Math.abs(Math.abs(jd) - Math.abs(locationInfo_List.get(i).getJd())) + Math.abs(Math.abs(wd) - Math.abs(locationInfo_List.get(i).getWd()));
//            d = (float)((float)((rrsi_1) * wifi_list.get(i).rrsi1 + (rrsi_2) * wifi_list.get(i).rrsi2 + (rrsi_3) * wifi_list.get(i).rrsi3))/((float) (Math.sqrt(rrsi_1*rrsi_1 + rrsi_2*rrsi_2 + rrsi_3*rrsi_3) * Math.sqrt(wifi_list.get(i).rrsi1*wifi_list.get(i).rrsi1 +wifi_list.get(i).rrsi2*wifi_list.get(i).rrsi2+ wifi_list.get(i).rrsi3*wifi_list.get(i).rrsi3)));
            locationInfo_List.get(i).setDistance(d);
        }

        Collections.sort(locationInfo_List, new LocationCompare());

        System.out.println("更新后");
        for (LocationInfo information : locationInfo_List) {
            System.out.println("information : wd为：" + information.getWd() + "jd为：" + information.getJd() + " area：" + information.getArea());
        }

        return locationInfo_List.get(0).getArea();
    }
}
