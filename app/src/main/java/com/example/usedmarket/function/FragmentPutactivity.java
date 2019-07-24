package com.example.usedmarket.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.usedmarket.Config;
import com.example.usedmarket.R;
import com.example.usedmarket.activity.AddActivity;
import com.example.usedmarket.activity.PutAdapter;
import com.example.usedmarket.activity.PutBean;
import com.example.usedmarket.db.Activity_db;
import com.example.usedmarket.market.TabFragment1RecyclerAdapter;
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

public class FragmentPutactivity extends Fragment {

    private List<Activity_db> activityList;


    private RecyclerView putRecycler;
    private PutAdapter putAdapter;
    private List<PutBean> putBeanList ;

    private SwipeRefreshLayout putRefresh;
    private boolean flag=true;

    private Button putBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_putacitvity,container,false);

        putRecycler =view.findViewById(R.id.putRecycler);
        putRefresh=view.findViewById(R.id.putRefresh);

        activityList= LitePal.findAll(Activity_db.class);
        if(activityList.size()>0){
            ArrayList<PutBean> putBeans = new ArrayList<>();
            for(Activity_db activity_db:activityList){
                PutBean putBean=new PutBean();
                putBean.setActivityName(activity_db.getActivityName());
                putBean.setUserName(activity_db.getUserName());
                putBean.setStartTime(activity_db.getStartTime());
                putBean.setEndTime(activity_db.getEndTime());
                putBean.setActivityDetail(activity_db.getActivityDetail());
                putBeans.add(putBean);
            }
                putBeanList=putBeans;
        }else{
            HttpUtil.sendOkHttpRequest(Config.host+"/activity/active", new Callback() {
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
                            Activity_db activity_db=new Activity_db();
                            activity_db.setActivityName(jsonObject2.getString("activityName"));
                            activity_db.setUserName(jsonObject2.getString("userName"));
                            activity_db.setStartTime(jsonObject2.getString("startTime"));
                            activity_db.setEndTime(jsonObject2.getString("endTime"));
                            activity_db.setActivityDetail(jsonObject2.getString("activityDetail"));
                            activity_db.save();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                }
            });
            putBeanList=initData();
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        putRecycler.setLayoutManager(layoutManager);
        putAdapter=new PutAdapter(getContext(),putBeanList);
        putRecycler.setAdapter(putAdapter);


        putRefresh.setColorSchemeResources(R.color.colorPrimary);
        putRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LitePal.deleteAll(Activity_db.class);
                HttpUtil.sendOkHttpRequest(Config.host+"/activity/active", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        final String error=e.getMessage();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                                putRefresh.setRefreshing(false);
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
                                Activity_db activity_db=new Activity_db();
                                activity_db.setActivityName(jsonObject2.getString("activityName"));
                                activity_db.setUserName(jsonObject2.getString("userName"));
                                activity_db.setStartTime(jsonObject2.getString("startTime"));
                                activity_db.setEndTime(jsonObject2.getString("endTime"));
                                activity_db.setActivityDetail(jsonObject2.getString("activityDetail"));
                                activity_db.save();
                                if(jsonObject1.getInt("code")==0){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            putRefresh.setRefreshing(false);
                                            putAdapter.notifyDataSetChanged();
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



        putBtn=view.findViewById(R.id.putBtn);
        putBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private ArrayList<PutBean> initData(){
        while (flag);
        ArrayList<PutBean> putBeans = new ArrayList<>();
        for(Activity_db activity_db:activityList){
            PutBean putBean=new PutBean();
            putBean.setActivityName(activity_db.getActivityName());
            putBean.setUserName(activity_db.getUserName());
            putBean.setStartTime(activity_db.getStartTime());
            putBean.setEndTime(activity_db.getEndTime());
            putBean.setActivityDetail(activity_db.getActivityDetail());
            putBeans.add(putBean);
        }
        return putBeans;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LitePal.deleteAll(Activity_db.class);
    }
}
