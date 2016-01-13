package com.lh.scroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by liuhui on 2016/1/11.
 *
 */
public class ScrollViewFra extends Fragment
{
    private View mScrollView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mScrollView=inflater.inflate(R.layout.scrollview_fra,container,false);
        return mScrollView;
    }

    public View getScrollView()
    {
        return mScrollView;
    }

}
