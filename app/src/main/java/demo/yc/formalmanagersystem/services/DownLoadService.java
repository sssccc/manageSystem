package demo.yc.formalmanagersystem.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import demo.yc.formalmanagersystem.models.FileInfo;
import demo.yc.formalmanagersystem.util.FileUtil;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class DownLoadService extends Service
{
    public static final String ACTION_CHANGE_IMAGE = "ACTION_CHANGE_IMAGE";
    public static final String ACTION_DOWNLAOD_IMAGE = "ACTION_DOWNLAOD_IMAGE";
    public static final String ACTION_DOWNLAOD_ENCLOSURE = "ACTION_DOWNLAOD_ENCLOSURE";
    public static final String ACTION_PROPERTY_NOTIFY = "ACTION_PROPERTY_NOTIFY";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("file","registerReceiver.......");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_DOWNLAOD_IMAGE))
        {
            Log.w("file","service....下载图片");
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("image");
            new DownLoadThread(fileInfo).start();
        }else if(intent.getAction().equals(ACTION_PROPERTY_NOTIFY))
        {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        Intent intent1 = new Intent(ACTION_PROPERTY_NOTIFY);
                        intent1.putExtra("message","购买电脑成功");
                        sendBroadcast(intent1);
                        Log.w("file","service .... notify");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }else if(intent.getAction().equals(ACTION_DOWNLAOD_ENCLOSURE))
        {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("enclosure");
            new DownLoadThread(fileInfo).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("file","unregisterReceiver.......");
    }
}

class DownLoadThread extends Thread
{
    private FileInfo fileInfo;
    public DownLoadThread(FileInfo fileInfo)
    {
        this.fileInfo = fileInfo;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/809temp.jpg");
        //Toast.makeText(MyApplication.getContext(),file.getAbsolutePath(),Toast.LENGTH_SHORT).show();

        Log.w("file","tempFile path = "+file.getAbsolutePath());
        Log.w("file",fileInfo.getFileURL()+"....");
        try {
            URL url = new URL(fileInfo.getFileURL());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if(file == null) {
                Log.w("file","file is null。。。。");
                return;
            }

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                os = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int len ;
                while((len = is.read(bytes))!= -1)
                {
                    os.write(bytes,0,len);
                }
                FileUtil.compressImage(fileInfo.getFileName(),file.getAbsolutePath());
            }else
            {
                Log.w("file","下载失败");
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try {
                os.close();
                conn.disconnect();
                file.delete();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

