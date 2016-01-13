package com.lh.scroll;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by liuhui on 2016/1/11.
 *
 */
public class ViewPagerAdapter extends FragmentPagerAdapter
{
    public ArrayList<Fragment> list;

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
        list=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "ScrollView";
    }
}
