package demo.yc.formalmanagersystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/23 0023.
 */
public class DownloadAttachementFile extends Thread{

    private int threadNum;
    private int threadLength;
    private RandomAccessFile threadFile;
    private int startPosition;
    private String path;

    public DownloadAttachementFile(int threadNum,int startPosition,RandomAccessFile threadFile,
                                   int threadLength,String path)
    {
        this.path = path;
        this.startPosition = startPosition;
        this.threadLength = threadLength;
        this.threadNum = threadNum;
        this.threadFile = threadFile;
    }

    public DownloadAttachementFile(){}

    @Override
    public void run() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Range","bytes="+startPosition+"-");
            if(conn.getResponseCode() == 206)
            {
                InputStream is = conn.getInputStream();
                byte[] buf = new byte[1024];
                int len;
                int length =0;
                while( (len = is.read(buf))!=-1 && length<threadLength)
                {
                    threadFile.write(buf,0,len);
                    length+=len;
                }
                threadFile.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn != null)
                conn.disconnect();
        }
    }
}
