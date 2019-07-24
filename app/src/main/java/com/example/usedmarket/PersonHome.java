package com.example.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PersonHome extends AppCompatActivity {

    private ImageView user_image;
    private TextView user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_home);
        user_image=findViewById(R.id.user_image);
        user_name=findViewById(R.id.user_name);
        user_name.setText(getBaseContext().getSharedPreferences("zhigu",Context.MODE_PRIVATE).getString("user_name",""));

        Intent intent =getIntent();
        String image=intent.getStringExtra("user_image");
        Glide.with(getApplicationContext()).load(image).into(user_image);

    }
}
