package com.example.usedmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.usedmarket.db.Good;
import com.example.usedmarket.function.FragmentAdapter;
import com.example.usedmarket.function.FragmentMarket;
import com.example.usedmarket.function.FragmentPersonalCenter;
import com.example.usedmarket.function.FragmentPutactivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class FunctionActivity extends AppCompatActivity {

    private BottomNavigationBar bottomNav;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private FragmentAdapter fragmentAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        bottomNav=findViewById(R.id.bottomNav);
        viewPager=findViewById(R.id.viewPager);

        fragmentList=new ArrayList<>();
        fragmentList.add(new FragmentMarket());
        fragmentList.add(new FragmentPutactivity());
        fragmentList.add(new FragmentPersonalCenter());
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNav.selectTab(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomNav
                .addItem(new BottomNavigationItem(R.drawable.ic_market,"市场"))
                .addItem(new BottomNavigationItem(R.drawable.ic_putactivity,"活动"))
                .addItem(new BottomNavigationItem(R.drawable.ic_personal_center,"我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNav.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });






    }
}
