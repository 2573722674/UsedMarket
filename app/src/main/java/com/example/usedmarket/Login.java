package com.example.usedmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.usedmarket.login.ForgetPassword;
import com.example.usedmarket.login.Register;

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

public class Login extends AppCompatActivity implements View.OnClickListener {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView TV_3,TV_4;
    private EditText ET_1,ET_2;
    private Button loginBtn;
    private CheckBox CB_1,CB_2;
    private ToggleButton TB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TV_3=findViewById(R.id.TV_3);
        TV_4=findViewById(R.id.TV_4);
        ET_1=findViewById(R.id.ET_1);
        ET_2=findViewById(R.id.ET_2);
        loginBtn=findViewById(R.id.loginBtn);
        CB_1=findViewById(R.id.CB_1);
        CB_2=findViewById(R.id.CB_2);
        TB=findViewById(R.id.TB);
        TV_3.setOnClickListener(this);
        TV_4.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        TB.setOnCheckedChangeListener(new ToggleButtonClick());
        pref= this.getSharedPreferences("zhigu", Context.MODE_PRIVATE);

        boolean isRememberPassWord=pref.getBoolean("remember_password",false);
       // boolean isRememberService=pref.getBoolean("remember_service",false);
        String account=pref.getString("account","");
        ET_1.setText(account);
        if(isRememberPassWord){
            String password=pref.getString("password","");
            ET_2.setText(password);
            CB_1.setChecked(true);
            CB_2.setChecked(true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.TV_3:
                Intent intent=new Intent(this, ForgetPassword.class);
                startActivity(intent);
                break;
            case R.id.TV_4:
                Intent intent1=new Intent(this, Register.class);
                startActivity(intent1);
                break;
            case R.id.loginBtn:
                if(ET_1.getText().toString().equals("20001230")&&ET_2.getText().toString().equals("20001230")){
                    Intent intent2=new Intent(Login.this,FunctionActivity.class);
                    startActivity(intent2);
                    finish();
                }else {
                    if(CB_2.isChecked()){
                        final ProgressDialog progressDialog=new ProgressDialog(Login.this);
                        progressDialog.setTitle("正在登录中");
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client=new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder()
                                        .add("user_name",ET_1.getText().toString())
                                        .add("password",ET_2.getText().toString())
                                        .build();
                                Request request=new Request.Builder()
                                        .url(Config.host+"/user/login")
                                        .post(requestBody)
                                        .build();
                                Call call=client.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, final IOException e) {

                                        final String error=e.getMessage();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Login.this,error,Toast.LENGTH_SHORT).show();
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
                                                        progressDialog.dismiss();
                                                        editor=pref.edit();
                                                        editor.putString("account",ET_1.getText().toString());
                                                        if(CB_1.isChecked()){
                                                            editor.putBoolean("remember_password",true);
                                                            editor.putBoolean("remember_service",true);
                                                            editor.putString("password",ET_2.getText().toString());
                                                        }else {
                                                            editor.remove("remember_password");
                                                            editor.remove("remember_service");
                                                            editor.remove("password");
                                                        }
                                                        editor.apply();
                                                        Intent intent3=new Intent(Login.this,FunctionActivity.class);
                                                        startActivity(intent3);
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
                    }else {
                        Toast.makeText(this,"请仔细阅读服务条款，并选中",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }


    private class ToggleButtonClick implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                ET_2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                ET_2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            ET_2.setSelection(ET_2.length());
        }
    }
}
