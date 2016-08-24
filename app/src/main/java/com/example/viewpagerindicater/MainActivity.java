package com.example.viewpagerindicater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.viewpagerindicater.Fragment.ViewPagerFragment;
import com.example.viewpagerindicater.View.ViewPagerIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPagerIndicator mViewPagerIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mFragmetList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
//    List<String> mTitles = Arrays.asList("上网","聊天","唱歌");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();

        mViewPagerIndicator.setTabItemTitles(mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPagerIndicator.setViewPager(mViewPager, 0);
        mViewPagerIndicator.openItemClickEvent(true);

    }

    private void initDatas() {

        mTitles.add("聊天");
        mTitles.add("上网");
        mTitles.add("唱歌");
        mTitles.add("游戏");
        mTitles.add("直播");
        mTitles.add("音乐");
        mTitles.add("看书");
        mTitles.add("电影");
        mTitles.add("奥运");
        mTitles.add("游泳");
        mTitles.add("睡觉");


        for (String s : mTitles) {

            ViewPagerFragment fragment = ViewPagerFragment.newInstance(s);
            mFragmetList.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmetList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmetList.size();
            }
        };


    }

    private void initViews() {

        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.viewPagerIndicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

    }


}
