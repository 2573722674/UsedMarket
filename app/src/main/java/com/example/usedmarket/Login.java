package com.example.usedmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Login extends AppCompatActivity implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.TV_3:
                intent=new Intent(this,ForgetPassword.class);
                break;
            case R.id.TV_4:
                intent=new Intent(this,Register.class);
                break;
            case R.id.loginBtn:
                intent=new Intent(this, FunctionActivity.class);
                break;
        }
        startActivity(intent);
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
