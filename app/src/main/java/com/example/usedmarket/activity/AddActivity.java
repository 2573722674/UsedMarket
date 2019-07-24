package com.example.usedmarket.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.usedmarket.Config;
import com.example.usedmarket.FunctionActivity;
import com.example.usedmarket.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddActivity extends AppCompatActivity {
    private EditText activityName,userName,startTime,endTime,activityDetail;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        activityName=findViewById(R.id.activityName);
        userName=findViewById(R.id.userName);
        startTime=findViewById(R.id.startTime);
        endTime=findViewById(R.id.endTime);
        activityDetail=findViewById(R.id.activityDetail);
        addBtn=findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("---------------------------"+activityName.getText().toString());
                ProgressDialog progressDialog=new ProgressDialog(AddActivity.this);
                progressDialog.setTitle("正在发布中");
                progressDialog.setMessage("Posting...");
                progressDialog.setCancelable(true);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        RequestBody requestBody=new FormBody.Builder()
                                .add("user_name",userName.getText().toString())
                                .add("activity_name",activityName.getText().toString())
                                .add("start_time",startTime.getText().toString())
                                .add("end_time",endTime.getText().toString())
                                .add("activity_detail",activityDetail.getText().toString())
                                .build();
                        Request request=new Request.Builder()
                                .url(Config.host+"/activity/post")
                                .post(requestBody)
                                .build();
                        Call call=client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                final String error=e.getMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddActivity.this,error,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseText=response.body().string();
                                try {
                                    JSONObject jsonObject=new JSONObject(responseText);
                                    if(jsonObject.getInt("code")==0){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent=new Intent(AddActivity.this, FunctionActivity.class);
                                                startActivity(intent);
                                                finish();
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

            }
        });

    }
}
