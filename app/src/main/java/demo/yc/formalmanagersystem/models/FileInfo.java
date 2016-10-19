package demo.yc.formalmanagersystem.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class FileInfo implements Serializable{

    private String fileName;
    private String fileURL;

    public FileInfo(String fileName, String fileURL) {
        this.fileName = fileName;
        this.fileURL = fileURL;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

}
