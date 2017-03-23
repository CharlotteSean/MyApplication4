package tan.myapplication.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import tan.myapplication.Data.MyUser;
import tan.myapplication.R;
import tan.myapplication.Util.CacheUtils;
import tan.myapplication.Util.ImageTools;
import tan.myapplication.Util.Util;


/**
 * Created by TAN on 2016/4/24.
 * 注册
 */
public class Aty_register extends Activity {
    
    public Util util;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;

    private static final int SCALE = 5;//照片缩小比例

    private EditText register_username, register_password, register_rpasswrod;
    private Button btn_register;
    private ImageView picture = null;

    private Button btn_ChoosePic;

    public String targeturl = null;
    String dateTime;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this, "2d3b9cf00c834afce97de57a3485e074");
        
        util = new Util();
        register_username = (EditText) findViewById(R.id.editText_register_username);
        register_password = (EditText) findViewById(R.id.editText_register_psw);
        register_rpasswrod = (EditText) findViewById(R.id.editText_register_rpsw);

        btn_ChoosePic = (Button) findViewById(R.id.btn_ChoosePic);

        picture = (ImageView) findViewById(R.id.picture);
        
        btn_register = (Button) findViewById(R.id.btn_register);
        
        Resources res = getBaseContext().getResources();
        Bitmap b = BitmapFactory.decodeResource(res, R.drawable.icon);
        targeturl = saveToSdCard(b);
        picture.setBackgroundDrawable(new BitmapDrawable(b));

        btn_ChoosePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPicturePicker(Aty_register.this, true);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DoRegister();
            }
        });
        
    }               //end OnCreate
    
    private void DoRegister() {
        
        final BmobFile figureFile = new BmobFile(new File(targeturl));
        figureFile.upload(Aty_register.this, new UploadFileListener() {
            
            public void onProgress(Integer integer) {
                
            }
            
            public void onSuccess() {
                String name = register_username.getText().toString().trim();
                String psw = register_password.getText().toString().trim();
                String rpsw = register_rpasswrod.getText().toString().trim();
                
                Log.v("TTTTTT", name + "---" + psw + "---" + rpsw + "---" + figureFile);

                if (name.equals("") || psw.equals("") || rpsw.equals("")) {
                    Toast.makeText(Aty_register.this, "输入不能为空", Toast.LENGTH_LONG).show();
                } else if (!psw.equals(rpsw)) {
                    Toast.makeText(Aty_register.this, "密码输入不一致，请重新输入", Toast.LENGTH_LONG).show();
                } else {
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(psw);
                    user.setIcon(figureFile);

                    user.signUp(Aty_register.this, new SaveListener() {
                        public void onSuccess() {
                            Toast.makeText(Aty_register.this, "Register OK", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        public void onFailure(int i, String s) {
                            Toast.makeText(Aty_register.this, "注册失败，失败原因：" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            
            public void onFailure(int i, String s) {
                Toast.makeText(Aty_register.this, "注册失败，失败原因：" + s, Toast.LENGTH_SHORT).show();
            }
        });
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
                    picture.setImageBitmap(newBitmap);
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
                            picture.setImageBitmap(smallBitmap);
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
                    Toast.makeText(Aty_register.this, "截取图片后", Toast.LENGTH_LONG).show();
                    picture.setImageBitmap(photo);
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
                        Toast.makeText(Aty_register.this, "拍照逻辑执行", Toast.LENGTH_SHORT).show();
                        Date date1 = new Date(System.currentTimeMillis());
                        dateTime = date1.getTime() + "";
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
                        Toast.makeText(Aty_register.this, "相册逻辑执行", Toast.LENGTH_SHORT).show();
                        Date date2 = new Date(System.currentTimeMillis());
                        dateTime = date2.getTime() + "";
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
        String files = CacheUtils.getCacheDirectory(this, true, "pic") + "_"
                                           + dateTime + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }                    //saveToSdCard
    
}
