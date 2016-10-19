package demo.yc.formalmanagersystem.util;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class PersonUtil
{
    public static String getPositonName(String code)
    {
        if(code.contains("1"))
            return "前端";
        else if(code.contains("2"))
            return "后端";
        else if(code.contains("3"))
            return "Android";
        else
            return "其他";
    }

    public static String getPositionCode(String name)
    {
        if(name.contains("前端"))
            return "1";
        else if(name.contains("后端"))
            return "2";
        else if(name.contains("Android"))
            return "3";
        else
            return "0";
    }


}
