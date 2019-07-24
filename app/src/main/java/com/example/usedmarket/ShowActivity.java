package com.example.usedmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowActivity extends AppCompatActivity {

    private ImageView user_image;
    private TextView user_name;
    private TextView goods_price;
    private TextView goods_name;
    private ImageView goods_image;
    private TextView goods_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        goods_price = findViewById(R.id.goods_price);
        goods_name = findViewById(R.id.goods_name);
        goods_image = findViewById(R.id.goods_image);
        goods_detail = findViewById(R.id.goods_detail);

        Intent intent=getIntent();
        user_name.setText(intent.getStringExtra("user_name"));
        goods_price.setText(intent.getStringExtra("goods_price"));
        goods_name.setText(intent.getStringExtra("goods_name"));
        goods_detail.setText(intent.getStringExtra("goods_detail"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("user_image")).into(user_image);
        Glide.with(getApplicationContext()).load(intent.getStringExtra("goods_image")).into(goods_image);

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),PersonHome.class);
                startActivity(intent1);
            }
        });
    }
}
