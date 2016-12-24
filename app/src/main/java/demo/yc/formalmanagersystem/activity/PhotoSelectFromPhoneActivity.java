package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.models.FileBean;

public class PhotoSelectFromPhoneActivity extends BaseActivity {

    GridView gridView;
    MyGridViewAdapter adapter;
    List<String> allImges = new ArrayList<>();
    View bottomLayout;
    TextView dirName ;
    TextView dirCount;

    File currentDir;

    List<FileBean> mFileBeanList = new ArrayList<>();

    ProgressDialog pd;

    int mMaxCount = 0;

    Intent lastIntent;


    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            pd.dismiss();
            sendDataToView();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select_from_phone);
        pd = new ProgressDialog(this);
        setUI();
        setData();
        setListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
    }

    private void setUI()
    {
        gridView = (GridView) findViewById(R.id.select_photo_gridView);
        bottomLayout = findViewById(R.id.select_photo_bottom);
        dirName = (TextView) findViewById(R.id.select_photo_fileName);
        dirCount = (TextView) findViewById(R.id.select_photo_fileCount);
    }

    private void setData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this,"当前存储卡不可用", Toast.LENGTH_LONG).show();
            return;
        }
        pd.show();
        new Thread()
        {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = getContentResolver().query(mImageUri,null,
                        MediaStore.Images.Media.MIME_TYPE +" = ? or " + MediaStore.Images.Media.MIME_TYPE+" = ? ",
                        new String[]{"image/jpeg","image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();

                while(cursor.moveToNext())
                {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                    allImges.add(path);

                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null)
                        continue;

                    String parentPath = parentFile.getAbsolutePath();
                    FileBean fileBean = null;

                    if(mDirPaths.contains(parentPath))
                        continue;
                    else
                    {
                        mDirPaths.add(parentPath);
                        fileBean = new FileBean();
                        fileBean.setDir(parentPath);
                        fileBean.setFirstFile(path);
                    }

                    if(parentFile.list() == null)
                        continue;

                    int fileCount = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            if(s.endsWith("jpg")||s.endsWith("jpeg")||s.endsWith("png"))
                                return true;
                            return false;
                        }
                    }).length;

                    fileBean.setCount(fileCount);
                    mFileBeanList.add(fileBean);

                    if(fileCount>mMaxCount)
                    {
                        mMaxCount = fileCount;
                        currentDir = parentFile;
                    }
                }
                cursor.close();

                mHandler.sendEmptyMessage(0x1112);
            }
        }.start();

    }

    private void setListener()
    {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastIntent  = getIntent();
                lastIntent.putExtra("photo",allImges.get(i));
                Log.w("photo","select photo path = "+allImges.get(i));
                setResult(RESULT_OK,lastIntent);
                finish();
            }
        });
    }

    private void sendDataToView() {

        if(currentDir == null)
        {
            Toast.makeText(PhotoSelectFromPhoneActivity.this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }
        for(FileBean f:mFileBeanList)
        {
            Log.w("Path",f.getDir()+"....."+f.getCount());
        }
        adapter = new MyGridViewAdapter(this,allImges);
        // adapter = new MyGridViewAdapter(this,mImgs,currentDir.getAbsolutePath());
        gridView.setAdapter(adapter);

        dirName.setText("所有图片");
        dirCount.setText(allImges.size()+"");
     //   dirCount.setText(mMaxCount+"");
    }


    private class MyGridViewAdapter extends BaseAdapter
    {

        private LayoutInflater inflater;
        private List<String> mDatas;
        private String currentDir;
        boolean flag = false;
        public MyGridViewAdapter(Context context, List<String> mDatas, String currentDir)
        {

            this.mDatas = mDatas;
            this.currentDir = currentDir;
            inflater = LayoutInflater.from(context);

        }
        public MyGridViewAdapter(Context context, List<String> mDatas)
        {
            this.mDatas = mDatas;
            flag = true;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if(view == null)
            {
                view = inflater.inflate(R.layout.phone_photo_grid_item,viewGroup,false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
                view.setTag(holder);
            }else

                holder= (ViewHolder) view.getTag();

            Glide.with(PhotoSelectFromPhoneActivity.this).load(R.drawable.nothing).into(holder.imageView);
           // holder.imageView.setImageResource(R.drawable.nothing);
            if(!flag)
                Glide.with(PhotoSelectFromPhoneActivity.this).load(currentDir+"/"+mDatas.get(i)).into(holder.imageView);
                //ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(currentDir+"/"+mDatas.get(i),holder.imageView);
            else
                Glide.with(PhotoSelectFromPhoneActivity.this).load(mDatas.get(i)).into(holder.imageView);
            //ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mDatas.get(i),holder.imageView);

            return view;
        }
    }

    static class ViewHolder
    {
        ImageView imageView;
    }
}
