package demo.yc.formalmanagersystem.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.broadcastreceiver.MyReceiver;
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

    MyReceiver receiver;
    LocalBroadcastManager manager ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PROPERTY_NOTIFY);
        receiver = new MyReceiver();
        registerReceiver(receiver,filter);
        Log.w("file","registerReceiver.......");


    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_DOWNLAOD_IMAGE))
        {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("image");
            new DownLoadThread(fileInfo).start();
        }else if(intent.getAction().equals(ACTION_PROPERTY_NOTIFY))
        {
            Intent intent1 = new Intent(ACTION_PROPERTY_NOTIFY);
            intent.putExtra("message","购买电脑成功");
            manager.sendBroadcast(intent1);
            Log.w("file","service .... notify");
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
        unregisterReceiver(receiver);
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
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp.jpg");
        Toast.makeText(MyApplication.getContext(),file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        Log.w("file","tempFile path = "+file.getAbsolutePath());
        try {
            URL url = new URL(fileInfo.getFileURL());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if(file == null)
                return;
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
            }
            FileUtil.compressImage(fileInfo.getFileName(),file.getAbsolutePath());
            file.delete();
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            try {
                os.close();
                conn.disconnect();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

