package demo.yc.formalmanagersystem.models;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public class FileBean
{
    private String dir ;
    private String name;
    private int count;
    private String firstFile;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        String name = dir.substring(dir.lastIndexOf("/"));
        this.name = name;
    }

    public String getFirstFile() {
        return firstFile;
    }

    public void setFirstFile(String firstFile) {
        this.firstFile = firstFile;
    }

    public String getName() {
        return name;
    }


}
