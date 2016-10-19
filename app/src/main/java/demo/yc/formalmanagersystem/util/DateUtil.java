package demo.yc.formalmanagersystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class DateUtil {

    public static String getTodayName() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        return format.format(d);
    }

    public static String getWeekDayName(int code) {
        switch (code) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "error";
        }


    }

    public static int getWeekDayCode(String name) {
        if(name.contains("一") || name.contains("Mon"))
            return 1;
        else if(name.contains("二") || name.contains("Tue"))
            return 2;
        else if(name.contains("三") || name.contains("Wed"))
            return 3;
        else if(name.contains("四") || name.contains("Thu"))
            return 4;
        else if(name.contains("五") || name.contains("Fri"))
            return 5;
        else if(name.contains("六") || name.contains("Sat"))
            return 6;
        else if(name.contains("日") || name.contains("Sun"))
            return 7;
        else
            return 0;

//            case "星期二":
//                return 2;
//            case "星期三":
//                return 3;
//            case "星期四":
//                return 4;
//            case "星期五":
//                return 5;
//            case "星期六":
//                return 6;
//            case "星期日":
//                return 7;
//            default:
//                return 0;
        }

    public static String getDayTimeName(int code) {
        switch (code) {
            case 1:
                return "1,2";
            case 2:
                return "3,4";
            case 3:
                return "5,6";
            case 4:
                return "7,8";
            case 5:
                return "9,10";
            case 6:
                return "11,12,13";
            default:
                return "error";
        }
    }

    public static String getPlanCateName(int code) {
        switch (code) {
            case 0:
                return "课程";
            case 1:
                return "项目";
            case 2:
                return "个人";
            default:
                return "error";
        }
    }

    public static String getDateFromMillions(String mill)
    {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date(Long.parseLong(mill));
        return format.format(date);
    }


}
