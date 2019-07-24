package com.example.usedmarket.market;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.usedmarket.Config;
import com.example.usedmarket.R;
import com.example.usedmarket.db.Good;
import com.example.usedmarket.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TabFragment1 extends Fragment {


    private List<Good> goodList;

    private RecyclerView recyclerMarket;
    private List<FMBean> fmBeanList;
    private TabFragment1RecyclerAdapter tabFragment1RecyclerAdapter;

    private SwipeRefreshLayout refresh;

    private boolean flag=true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tab_fragment1,container,false);

        recyclerMarket=view.findViewById(R.id.recyclerMarket);




        goodList= LitePal.findAll(Good.class);

        if(goodList.size()>0){
            ArrayList<FMBean> fmBeans = new ArrayList<>();
            for(Good good:goodList){
                FMBean fmBean=new FMBean();
                fmBean.setUser_image(good.getUser_image());
                fmBean.setUser_name(good.getUser_name());
                fmBean.setGoods_price(good.getGoods_price());
                fmBean.setGoods_name(good.getGoods_name());
                fmBean.setGoods_image(good.getGoods_image());
                fmBean.setGoods_detail(good.getGoods_detail());
                fmBeans.add(fmBean);
            }
            fmBeanList=fmBeans;
        }else{
            HttpUtil.sendOkHttpRequest(Config.host+"/goods/selling", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    final String error=e.getMessage();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseText=response.body().string();
                    try {
                        JSONObject jsonObject1=new JSONObject(responseText);
                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            Good good=new Good();
                            good.setUser_image(jsonObject2.getString("goodsImageUrl"));
                            good.setUser_name(jsonObject2.getString("userName"));
                            good.setGoods_price(jsonObject2.getString("goodsPrice"));
                            good.setGoods_name(jsonObject2.getString("goodsName"));
                            good.setGoods_image(jsonObject2.getString("goodsImageUrl"));
                            good.setGoods_detail(jsonObject2.getString("goodsDetail"));
                            good.save();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                }
            });
            fmBeanList=initData();
        }



        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerMarket.setLayoutManager(layoutManager);
        tabFragment1RecyclerAdapter=new TabFragment1RecyclerAdapter(getContext(),fmBeanList);
        recyclerMarket.setAdapter(tabFragment1RecyclerAdapter);



        refresh=view.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LitePal.deleteAll(Good.class);
                HttpUtil.sendOkHttpRequest(Config.host+"/goods/selling", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        final String error=e.getMessage();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                                refresh.setRefreshing(false);

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseText=response.body().string();
                        try {
                            JSONObject jsonObject1=new JSONObject(responseText);
                            JSONArray jsonArray=jsonObject1.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                Good good=new Good();
                                good.setUser_image(jsonObject2.getString("goodsImageUrl"));
                                good.setUser_name(jsonObject2.getString("userName"));
                                good.setGoods_price(jsonObject2.getString("goodsPrice"));
                                good.setGoods_name(jsonObject2.getString("goodsName"));
                                good.setGoods_image(jsonObject2.getString("goodsImageUrl"));
                                good.setGoods_detail(jsonObject2.getString("goodsDetail"));
                                good.save();
                                if(jsonObject1.getInt("code")==0){

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            refresh.setRefreshing(false);
                                            tabFragment1RecyclerAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

        return view;
    }
    private ArrayList<FMBean> initData(){
       while (flag);
        ArrayList<FMBean> fmBeans = new ArrayList<>();
        for(Good good:goodList){
            FMBean fmBean=new FMBean();
            fmBean.setUser_image(good.getUser_image());
            fmBean.setUser_name(good.getUser_name());
            fmBean.setGoods_price(good.getGoods_price());
            fmBean.setGoods_name(good.getGoods_name());
            fmBean.setGoods_image(good.getGoods_image());
            fmBean.setGoods_detail(good.getGoods_detail());
            fmBeans.add(fmBean);
        }

        return fmBeans;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LitePal.deleteAll(Good.class);
    }
}
