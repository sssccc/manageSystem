package demo.yc.formalmanagersystem.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import demo.yc.formalmanagersystem.MyApplication;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class FileUtil {

    private static String dir = "";
    private static String fileDir = "";


    private static void getRootDir()
    {
        dir = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/809/manage/head/";
        File dirFile = new File(dir);
        if(!dirFile.exists())
            dirFile.mkdir();
    }

    private static void getFileDir()
    {

        if (isExternalStorageOk()) {
            File tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/809/manage/");
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            fileDir = tempFile.getAbsolutePath();
            Toast.makeText(MyApplication.getContext(),"filedir = "+fileDir,Toast.LENGTH_SHORT).show();

        }

    }

    //获取存储用户头像的路径
    public static String getImagePath(String fileName)
    {
        if(dir.equals(""))
            getRootDir();
        Log.w("path","....."+dir+"......."+fileName+".jpg");
        File file = new File(dir,fileName+".jpg");
        if(file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file.getAbsolutePath();
    }


    public static boolean isExternalStorageOk()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }



    //获取用户拍摄的图片的路径
    public static String getPicturePath()
    {
        if(fileDir.equals(""))
            getFileDir();
        File file = new File(fileDir,System.currentTimeMillis()+".jpg");
        Toast.makeText(MyApplication.getContext(),"filedir = "+fileDir,Toast.LENGTH_SHORT).show();
        return file.getAbsolutePath();
    }

    //获取下载附件的路径
    public static String getEnclosurePath(String fileName)
    {
        if(fileDir.equals(""))
            getFileDir();
        File file = new File(fileDir,fileName);
        return file.getAbsolutePath();
    }


    //将选择的照片（拍摄或者选自手机） 替换掉原本在缓存的用户头像
    public static boolean compressImage(String oldName, String newName)
    {
        File newFile = new File(newName);
        Log.w("file","+++++++++++++newFile----"+newFile.getAbsolutePath());
        Log.w("file","+++++++++++++newFile----"+newFile.length());


        File oldFile = new File(getImagePath(oldName));
        Log.w("file","+++++++++++++oldFile----"+oldFile.getAbsolutePath());
        Log.w("file","+++++++++++++oldFile----"+ oldFile.length());

        Bitmap bitmap = null;

        FileOutputStream fos = null;
        try{
            bitmap = BitmapFactory.decodeFile(newName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(oldFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
               fos.close();
                Log.w("file","+++++++++++++oldFile----"+ new File(oldName).length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String getUserImagePath(String userId)
    {
        if(dir.equals(""))
            getRootDir();
        return dir+userId+".jpg";
    }








}
