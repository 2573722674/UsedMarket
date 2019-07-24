package com.example.usedmarket.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usedmarket.Config;
import com.example.usedmarket.R;
import com.example.usedmarket.ReleaseActivity;
import com.example.usedmarket.db.Good;
import com.example.usedmarket.market.TabFragment1;
import com.example.usedmarket.market.TabFragment2;
import com.example.usedmarket.market.TabFragment3;
import com.example.usedmarket.market.TabFragmentAdapter;
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

public class FragmentMarket extends Fragment implements View.OnClickListener{
    private ViewPager marketVP;
    private TabLayout tabLayout;
    private List<Fragment> list;
    private String [] title={"首页","服装","食品"};
    private TabFragmentAdapter tabFragmentAdapter;
    private ImageView add;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        for(String tab:title){
            tabLayout.addTab(tabLayout.newTab().setText(tab));//.setIcon(R.drawable.dfj)设置图标
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });







        marketVP=view.findViewById(R.id.marketVP);
        list=new ArrayList<>();
        list.add(new TabFragment1());
        list.add(new TabFragment2());
        list.add(new TabFragment3());
        tabFragmentAdapter=new TabFragmentAdapter(getChildFragmentManager(),title,list);
        marketVP.setAdapter(tabFragmentAdapter);
        tabLayout.setupWithViewPager(marketVP);


        add=view.findViewById(R.id.add);
        add.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), ReleaseActivity.class);
        startActivity(intent);
    }
}
