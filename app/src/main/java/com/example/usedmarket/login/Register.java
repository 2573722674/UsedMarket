package com.example.usedmarket.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usedmarket.Config;
import com.example.usedmarket.Login;
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

public class Register extends AppCompatActivity {

    private EditText userName,passWord;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName=findViewById(R.id.userName);
        passWord=findViewById(R.id.passWord);
        registerBtn=findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            OkHttpClient client=new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("user_name",userName.getText().toString())
                                    .add("password",passWord.getText().toString())
                                    .build();
                            Request request=new Request.Builder()
                                    .url(Config.host+"/user/register")
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
                                            Toast.makeText(Register.this,error,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseText=response.body().string();
                                    try {
                                        final JSONObject jsonObject=new JSONObject(responseText);
                                        if(jsonObject.getInt("code")==0){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(Register.this, Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }else{
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
            }


        });

    }
}
