package com.lh.scroll;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private ViewPager mViewpager;
    private TabLayout mTabLayout;
    private ScrollableLayout mScrollableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager= (ViewPager) findViewById(R.id.viewpager);
        mTabLayout= (TabLayout) findViewById(R.id.tabLayout);
        mScrollableLayout= (ScrollableLayout) findViewById(R.id.scrollable);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.list.add(new ScrollViewFra());
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
        mScrollableLayout.setScrollableView(adapter.list.get(0));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }
}
