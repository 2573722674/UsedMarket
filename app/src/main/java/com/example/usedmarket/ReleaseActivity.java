package com.example.usedmarket;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;
    private EditText goods_name, goods_price, goods_detail;
    private RecyclerView photoRecyclerView;
    private ImageView takePhotos;
    private Button issue;
    private List<PhotoBean> photoBeanList;
    private PhotoAdapter photoAdapter;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        goods_name = findViewById(R.id.goods_name);
        goods_price = findViewById(R.id.goods_price);
        goods_detail = findViewById(R.id.goods_detail);
        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        takePhotos = findViewById(R.id.takePhotos);
        issue = findViewById(R.id.issue);

        photoBeanList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photoRecyclerView.setLayoutManager(layoutManager);
        photoAdapter = new PhotoAdapter(photoBeanList);
        photoRecyclerView.setAdapter(photoAdapter);
        takePhotos.setOnClickListener(this);
        issue.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    photoBeanList.add(new PhotoBean(bitmap));
                    photoAdapter.notifyDataSetChanged();

                    File file = new File(getExternalCacheDir(), "image.jpg");//将要保存图片的路径
                    try {
                        Log.e("ReleaseA...", "run:1 ");
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.takePhotos:
                PackageManager pm = getPackageManager();
                // FEATURE_CAMERA - 后置相机
                // FEATURE_CAMERA_FRONT - 前置相机
                if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                        && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                    Toast.makeText(getApplicationContext(), "未检测到相机", Toast.LENGTH_SHORT).show();
                } else {
                    List<String> permissionList = new ArrayList<>();
                    if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!permissionList.isEmpty()) {
                        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                        ActivityCompat.requestPermissions(ReleaseActivity.this, permissions, 1);
                    } else {
                        File outputImage = new File(getExternalCacheDir(), "image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(v.getContext(), "com.example.usedmarket.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                }
                break;
            case R.id.issue:

                ProgressDialog progressDialog=new ProgressDialog(ReleaseActivity.this);
                progressDialog.setTitle("正在发布中");
                progressDialog.setMessage("Posting...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(getExternalCacheDir(), "image.jpg");//将要保存图片的路径

                        String user=getSharedPreferences("zhigu", Context.MODE_PRIVATE).getString("account", "");
                        OkHttpClient client = new OkHttpClient();
                       // MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        // MediaType.parse() 里面是上传的文件类型。
                        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                        // 参数分别为， 请求key ，文件名称 ， RequestBody
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("user_name",user)
                                .addFormDataPart("goods_name", goods_name.getText().toString())
                                .addFormDataPart("goods_price", goods_price.getText().toString())
                                .addFormDataPart("goods_detail", goods_detail.getText().toString())
                                .addFormDataPart("goods_image", getExternalCacheDir()+"image.jpg", body)
                                .build();


                        Request request = new Request.Builder()
                                .url(Config.host + "/goods/post")
                                .post(requestBody)
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "发布失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseText = response.body().string();
                                try {
                                    final JSONObject jsonObject = new JSONObject(responseText);
                                    if (jsonObject.getInt("code") == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                Intent intent = new Intent(getApplicationContext(), FunctionActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }).start();
                break;
        }

    }


}
