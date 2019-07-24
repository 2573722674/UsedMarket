package com.example.usedmarket.market;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<Fragment> list;
    private String [] title;


    public TabFragmentAdapter(FragmentManager fm,String [] title,List<Fragment> list) {
        super(fm);
        this.manager=fm;
        this.title=title;
        this.list=list;

    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];
    }
}
