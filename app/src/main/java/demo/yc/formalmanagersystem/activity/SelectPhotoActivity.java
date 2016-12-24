package demo.yc.formalmanagersystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.contentvalues.PersonInfoContent;
import demo.yc.formalmanagersystem.contentvalues.SelectPhotoContent;
import demo.yc.formalmanagersystem.util.DialogUtil;
import demo.yc.formalmanagersystem.util.FileUtil;
import demo.yc.formalmanagersystem.util.VolleyUtil;
import demo.yc.formalmanagersystem.view.DialogCancelListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SelectPhotoActivity extends BaseActivity implements View.OnClickListener{

    ImageView backBtn;
   // CircleImageView headPhoto;
    Button gallery, camera;
    TextView saveTv,title;

    ImageView  headPhoto;
    String imageURl;
    String photoPath;

    Intent lastIntent;

    boolean isChange = false;
    boolean isCancel = false;

    private static final MediaType UPDATE_IMAGE_TYPE = MediaType.parse("image/jpeg");
    private final OkHttpClient client = new OkHttpClient();
    private static final int UPDATE_OK = 0x001;
    private static final int UPDATE_ERROR = 0x002;
    //接收头像路径
    //根据头像路径，创建临时头像路径
    //根据相机或者相册，选择图片到临时头像路径
    //确认返回,上传图片到后台。
    //取消，则什么也不返回。


    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        setUi();
        setListener();
        getMyIntent();
        pd = new ProgressDialog(this);
    }

    private void setUi()
    {

        headPhoto = (ImageView) findViewById(R.id.select_head);
        saveTv = (TextView) findViewById(R.id.select_save_btn);

        gallery = (Button) findViewById(R.id.select_gallery);
        camera = (Button) findViewById(R.id.select_camera);

        backBtn = (ImageView) findViewById(R.id.top_layout_menu);
        backBtn.setImageResource(R.drawable.back);

        title = (TextView) findViewById(R.id.top_layout_title);
        title.setText("修改头像");

    }

    private void setListener()
    {
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);

        saveTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void getMyIntent()
    {
        lastIntent  = getIntent();
        imageURl = lastIntent.getStringExtra(PersonInfoContent.CHANGE_PHOTO_TAG);
       // Glide.with(this).load(imageURl).fitCenter().into(headPhoto);

        Glide.with(this).load(imageURl).asBitmap().fitCenter().placeholder(R.drawable.error).into(new BitmapImageViewTarget(headPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                headPhoto.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private void dealLocalPhoto(Intent data)
    {
        if(data != null) {
            photoPath = data.getStringExtra("photo");
          //  Glide.with(this).load(photoPath).fitCenter().into(headPhoto);
            Glide.with(this).load(photoPath).asBitmap().fitCenter().placeholder(R.drawable.error).into(new BitmapImageViewTarget(headPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    headPhoto.setImageDrawable(circularBitmapDrawable);
                }
            });
            isChange = true;
        }
    }

    private void dealTakePhoto(Intent data)
    {
        if(data != null)
        {
            FileOutputStream fos = null;
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            try {
                fos = new FileOutputStream(photoPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
               // Glide.with(this).load(photoPath).fitCenter().into(headPhoto);
                Glide.with(this).load(photoPath).asBitmap().fitCenter().placeholder(R.drawable.error).into(new BitmapImageViewTarget(headPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        headPhoto.setImageDrawable(circularBitmapDrawable);
                    }
                });
                isChange = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(SelectPhotoActivity.this," data null",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.top_layout_menu:
                finish();
                break;
            case R.id.select_save_btn:
                if(isChange)
                {
                    //将图片传递给后台
                    //将图片添加到缓存中。
                    //再传给上一个activity
                    DialogUtil.showDialog(this,"正在保存").show();
                    DialogUtil.onCancelListener(new DialogCancelListener() {
                        @Override
                        public void onCancel() {
                            DialogUtil.dissmiss();
                            client.dispatcher().cancelAll();
                            isCancel = true;
                        }
                    });
//                    pd.setMessage("正在保存...");
//                    pd.show();
//                    pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialogInterface) {
//
//                            client.dispatcher().cancelAll();
//                            isCancel = true;
//                        }
//                    });
                    updateImage(photoPath);
                }else
                {
                    setResult(RESULT_CANCELED);
                    finish();
                }

                break;
            case R.id.select_gallery:
                Intent localIntent = new Intent(SelectPhotoActivity.this,PhotoSelectFromPhoneActivity.class);
                startActivityForResult(localIntent, SelectPhotoContent.LOCAL_PHOTO);
                break;
            case R.id.select_camera:
                photoPath = FileUtil.getPicturePath();
               // Toast.makeText(SelectPhotoActivity.this, "拍摄地址："+photoPath, Toast.LENGTH_SHORT).show();
                if(photoPath.equals("")) {
                    Toast.makeText(SelectPhotoActivity.this, "内存卡不能使用", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent takeIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(takeIntent,SelectPhotoContent.TAKE_PHOTO);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case SelectPhotoContent.LOCAL_PHOTO:
                    dealLocalPhoto(data);
                    break;
                case SelectPhotoContent.TAKE_PHOTO:
                    dealTakePhoto(data);
                    break;
            }
        }else
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //使用okhttp上传图片
    private void updateImage(String filePath)
    {

        File file = new File(filePath);
        if(!file.exists()) {
            mHandler.sendEmptyMessage(UPDATE_ERROR);
            return;
        }


        //封装数据类型，图片和用户名
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file",filePath, RequestBody.create(UPDATE_IMAGE_TYPE, file));
        builder.addFormDataPart("id",MyApplication.getPersonId());
        builder.addFormDataPart("role",MyApplication.getRole());

        //包含图片和参数的数据体
        MultipartBody requestBody = builder.build();
        //包装请求体
        final Request request = new Request.Builder()
                .url(VolleyUtil.ROOT_URL+"information/image/upload")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("file","上传图片failure......"+e.getLocalizedMessage());
                Looper.prepare();
                mHandler.sendEmptyMessage(UPDATE_ERROR);
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {
                    if(response.body().string().equals("1"))
                    {
                        Log.w("file","上传图片ok......");
                        mHandler.sendEmptyMessage(UPDATE_OK);
                    }else
                    {
                        Log.w("file","上传图片failure......"+response.body().string());
                        mHandler.sendEmptyMessage(UPDATE_ERROR);
                    }
                }else
                {
                    Log.w("file","上传图片failure......");
                    mHandler.sendEmptyMessage(UPDATE_ERROR);
                }
            }
        });
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            DialogUtil.dissmiss();
//            pd.dismiss();
            if (msg.what == UPDATE_ERROR)
            {
                if(!isCancel) {
                    Toast.makeText(SelectPhotoActivity.this, "图片保存失败...", Toast.LENGTH_SHORT).show();

                }else
                {
                    isCancel = false;
                    Toast.makeText(SelectPhotoActivity.this, "操作已取消...", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what == UPDATE_OK)
            {
                Toast.makeText(SelectPhotoActivity.this,"图片保存成功...",Toast.LENGTH_SHORT).show();
                FileUtil.compressImage(MyApplication.getUser().getId(),photoPath);
                MyApplication.setPersonHeadPath(photoPath);
                setResult(RESULT_OK,lastIntent);
                finish();
            }
        }
    };


}
