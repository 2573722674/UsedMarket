package com.example.usedmarket;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usedmarket.R;
import com.example.usedmarket.db.Good;
import com.example.usedmarket.objects.ObjectAdapter;
import com.example.usedmarket.objects.ObjectBean;
import com.example.usedmarket.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.litepal.LitePalApplication.getContext;

public class Objects extends AppCompatActivity {


    private RecyclerView objectsRecycler;
    private List<ObjectBean> objectBeanList;
    private ObjectAdapter objectAdapter;

    public static List<Integer> integerList;

    private List<String> goods_name, goods_price, goods_detail, goods_image;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);
        objectsRecycler = findViewById(R.id.objectsRecycler);

        goods_name = new ArrayList<>();
        goods_price = new ArrayList<>();
        goods_detail = new ArrayList<>();
        goods_image = new ArrayList<>();
        integerList = new ArrayList<>();

        HttpUtil.sendOkHttpRequest(Config.host + "/goods/buyuser_name=" + getSharedPreferences("zhigu", Context.MODE_PRIVATE).getString("account", ""), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                try {
                    JSONObject jsonObject1 = new JSONObject(responseText);
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        integerList.add(jsonObject2.getInt("goodsId"));
                        goods_price.add(jsonObject2.getString("goodsPrice"));
                        goods_name.add(jsonObject2.getString("goodsName"));
                        goods_image.add(jsonObject2.getString("goodsImageUrl"));
                        goods_detail.add(jsonObject2.getString("goodsDetail"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                flag = false;

            }
        });

        objectBeanList = initData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        objectsRecycler.setLayoutManager(layoutManager);
        objectAdapter = new ObjectAdapter(getContext(), objectBeanList);
        objectsRecycler.setAdapter(objectAdapter);
    }

    private ArrayList<ObjectBean> initData() {
        while (flag) ;
        ArrayList<ObjectBean> objectBeans = new ArrayList<>();
        for (int i = 0; i < goods_name.size(); i++) {
            ObjectBean objectBean = new ObjectBean();
            objectBean.setGoods_price(goods_price.get(i));
            objectBean.setGoods_name(goods_name.get(i));
            objectBean.setGoods_image(goods_image.get(i));
            objectBean.setGoods_detail(goods_detail.get(i));
            objectBeans.add(objectBean);
        }

        return objectBeans;
    }

}
