package tan.myapplication.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import tan.myapplication.Data.Boring_info;
import tan.myapplication.Data.Busy_info;
import tan.myapplication.R;
import tan.myapplication.Util.CacheUtils;
import tan.myapplication.Util.ImageTools;
import tan.myapplication.Util.Utils;


public class Aty_addinfo extends Activity implements AMapLocationListener, View.OnClickListener {
    
    EditText editText_Title, editText_content;
    ImageView imageView_Picture = null;
    Button btn_Add_pic, btn_publish;
    String dateTime;
    public String targeturl = null;

    AMapLocationClient locationClient = null;
    AMapLocationClientOption locationClientOption = null;
    TextView tv_location_result;
    Button btn_start_location;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;
    
    private static final int SCALE = 5;//照片缩小比例
    
    String STR_Title;
    String STR_Content;
    String Publish_type;

    String locationstr = "未获取定位信息";

    SharedPreferences sp;
    String user;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_info);
        Bmob.initialize(this, "2d3b9cf00c834afce97de57a3485e074");

        tv_location_result = (TextView) findViewById(R.id.tv_location_result);
        editText_Title = (EditText) findViewById(R.id.editText_Title);
        editText_content = (EditText) findViewById(R.id.editText_Content);
        imageView_Picture = (ImageView) findViewById(R.id.imageView_Picture);
        btn_Add_pic = (Button) findViewById(R.id.btn_Add_Pic);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        btn_start_location = (Button) findViewById(R.id.btn_start_location);


        sp = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        user = sp.getString("username", "未登录");


        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationClientOption.setOnceLocation(true);
        locationClientOption.setNeedAddress(true);
        locationClient.setLocationListener(this);


        Resources res = getBaseContext().getResources();
        Bitmap b = BitmapFactory.decodeResource(res, R.drawable.icon);
        targeturl = saveToSdCard(b);
        imageView_Picture.setBackgroundDrawable(new BitmapDrawable(b));

        Intent i = getIntent();
        Publish_type = i.getStringExtra("PUBLISH_TYPE");

        btn_start_location.setOnClickListener(this);

        btn_Add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker(Aty_addinfo.this, true);
            }
        });

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Publish(Publish_type);
            }
        });


    }//OnCreate
    
    private void InitStr() {
        STR_Title = editText_Title.getText().toString().trim();
        STR_Content = editText_content.getText().toString().trim();
    }

    private void Publish(String publish_type) {
        final BmobFile figureFile = new BmobFile(new File(targeturl));
        if (publish_type.equals("BO")) {
            figureFile.upload(Aty_addinfo.this, new UploadFileListener() {
                public void onSuccess() {
                    InitStr();
                    Boring_info boringinfo = new Boring_info();
                    boringinfo.setLocation("hahah");
                    boringinfo.setTitle(STR_Title);
                    boringinfo.setUsername(user);
                    boringinfo.setLocation(locationstr);
                    boringinfo.setContent(STR_Content);
                    boringinfo.setPic(figureFile);
                    boringinfo.save(Aty_addinfo.this, new SaveListener() {
                        public void onSuccess() {
                            Toast.makeText(Aty_addinfo.this, "PUBLISH OK", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        
                        public void onFailure(int i, String s) {
                            Toast.makeText(Aty_addinfo.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                    
                }
                
                public void onFailure(int i, String s) {
                    Toast.makeText(Aty_addinfo.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (publish_type.equals("BU")) {
            figureFile.upload(Aty_addinfo.this, new UploadFileListener() {
                
                @Override
                public void onSuccess() {
                    InitStr();
                    Busy_info busyInfo = new Busy_info();
                    busyInfo.setTitle(STR_Title);
                    busyInfo.setContent(STR_Content);
                    busyInfo.setLocation("kkkkk");
                    busyInfo.setUsername(user);
                    busyInfo.setPic(figureFile);
                    busyInfo.save(Aty_addinfo.this, new SaveListener() {
                        public void onSuccess() {
                            Toast.makeText(Aty_addinfo.this, "PUBLISH OK", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        
                        public void onFailure(int i, String s) {
                            Toast.makeText(Aty_addinfo.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                
                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(Aty_addinfo.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Aty_addinfo.this, "参数传递错误。" + "传递值为" + publish_type, Toast.LENGTH_SHORT).show();
        }
        
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    
                    //将处理过的图片显示在界面上，并保存到本地
                    imageView_Picture.setImageBitmap(newBitmap);
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                    targeturl = Environment.getExternalStorageDirectory().getAbsolutePath();
                    
                    break;
                
                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            targeturl = saveToSdCard(photo);
                            imageView_Picture.setImageBitmap(smallBitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                
                case CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        System.out.println("Data");
                    } else {
                        System.out.println("File");
                        String fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    }
                    cropImage(uri, 500, 500, CROP_PICTURE);
                    break;
                
                case CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap) extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    targeturl = saveToSdCard(photo);
                    Toast.makeText(Aty_addinfo.this, "截取图片后", Toast.LENGTH_LONG).show();
                    imageView_Picture.setImageBitmap(photo);
                    break;
                default:
                    break;
            }
        }
    }
    
    
    public void showPicturePicker(Context context, boolean isCrop) {
        final boolean crop = isCrop;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (crop) {
                            REQUEST_CODE = CROP;
                            //删除上一次截图的临时文件
                            SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                            ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
                            
                            //保存本次截图临时文件名字
                            fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("tempName", fileName);
                            editor.commit();
                        } else {
                            REQUEST_CODE = TAKE_PICTURE;
                            fileName = "image.jpg";
                        }
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, REQUEST_CODE);
                        break;
                    
                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (crop) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;
                    
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }
    
    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }
    
    
    //将图片的bitmap存入sd卡,返回存储的路径
    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(this, true, "pic")
                                           + dateTime + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }                    //saveToSdCard

    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationClientOption = null;
        }
    }

    Handler mhandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case Utils.MSG_LOCATION_START:
                    tv_location_result.setText("正在定位...");
                    break;
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
//                    String result = Utils.getLocationStr(loc);
                    String result = loc.getAddress();
                    locationstr = result;
                    tv_location_result.setText(result);
                    break;
                case Utils.MSG_LOCATION_STOP:
                    tv_location_result.setText("定位停止");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = mhandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mhandler.sendMessage(msg);
        }
    }


    public void onClick(View v) {
        if (v.getId() == R.id.btn_start_location) {
            if (btn_start_location.getText().equals("开始定位")) {
                btn_start_location.setText("停止定位");
                // 设置定位参数
                locationClient.setLocationOption(locationClientOption);
                // 启动定位
                locationClient.startLocation();
                mhandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
            } else {
                btn_start_location.setText("开始定位");
                // 停止定位
                locationClient.stopLocation();
                mhandler.sendEmptyMessage(Utils.MSG_LOCATION_STOP);
            }
        }
    }

}
