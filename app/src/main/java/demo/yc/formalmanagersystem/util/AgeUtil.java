package demo.yc.formalmanagersystem.util;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class AgeUtil {

    public final static int[] getAge()
    {
        int[] ages = new int[100];
        for(int i=0;i<100;++i)
            ages[i] = (i+1);

        return ages;
    }
}
