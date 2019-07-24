package com.example.usedmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Show1Activity extends AppCompatActivity {

    private TextView activityName,userName,startTime,endTime,activityDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show1);
        activityName=findViewById(R.id.activityName);
        userName=findViewById(R.id.userName);
        startTime=findViewById(R.id.startTime);
        endTime=findViewById(R.id.endTime);
        activityDetail=findViewById(R.id.activityDetail);

        Intent intent=getIntent();
        activityName.setText(intent.getStringExtra("activityName"));
        userName.setText(intent.getStringExtra("userName"));
        startTime.setText(intent.getStringExtra("startTime"));
        endTime.setText(intent.getStringExtra("endTime"));
        activityDetail.setText(intent.getStringExtra("activityDetail"));

    }
}
