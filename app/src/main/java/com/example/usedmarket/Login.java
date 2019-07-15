package com.example.usedmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView TV_3,TV_4,TV_5;
    private EditText ET_1,ET_2;
    private Button loginBtn;
    private CheckBox CB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TV_3=findViewById(R.id.TV_4);
        TV_4=findViewById(R.id.TV_3);
        TV_5=findViewById(R.id.TV_4);
        ET_1=findViewById(R.id.ET_1);
        ET_2=findViewById(R.id.ET_2);
        loginBtn=findViewById(R.id.loginBtn);
        CB=findViewById(R.id.CB_2);
        TV_3.setOnClickListener(this);
        TV_4.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.TV_4:
                intent=new Intent(this,Register.class);
                break;
            case R.id.TV_3:
                intent=new Intent(this,ForgetPassword.class);
                break;
            case R.id.loginBtn:
                intent=new Intent(this, FunctionActivity.class);
                break;
        }
        startActivity(intent);
    }
}
