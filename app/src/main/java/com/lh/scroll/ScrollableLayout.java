package com.lh.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by liuhui on 2016/1/11.
 * ScrollableLayout
 */
public class ScrollableLayout extends ViewGroup
{
    private float lastY;
    private int maximumFlingVelocity;
    private int minimumFlingVelocity;
    private int touchSlop;
    private int maxScrollY;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private Fragment scrollableView;

//    private boolean scrollStatue;

    public ScrollableLayout(Context context)
    {
        super(context);
        initView();
    }

    public ScrollableLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public ScrollableLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView()
    {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        maximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        touchSlop = viewConfiguration.getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int actWidth = 0;
        int actHeight = 0;
        if (getChildCount() == 2)
        {
            View headView = getChildAt(0);
            View contentView = getChildAt(1);
            headView.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(headView.getLayoutParams().height, MeasureSpec.EXACTLY));
            contentView.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
            actHeight = headView.getMeasuredHeight() + contentView.getMeasuredHeight();
            actWidth = Math.max(headView.getMeasuredWidth(), contentView.getMeasuredWidth());
        }
        setMeasuredDimension(actWidth, actHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        View headView = getChildAt(0);
        View contentView = getChildAt(1);
        headView.layout(0, 0, headView.getMeasuredWidth(), getMeasuredWidth());
        contentView.layout(0, headView.getMeasuredHeight(), getMeasuredWidth(),
                headView.getMeasuredHeight() + contentView.getMeasuredHeight());
        maxScrollY = getChildAt(0).getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            lastY = ev.getRawY();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if(ev.getAction()==MotionEvent.ACTION_DOWN)
        {
            lastY=ev.getRawY();
        }
        else if(ev.getAction()==MotionEvent.ACTION_MOVE)
        {
            float currY = ev.getRawY();
            float distanceY = currY - lastY;
            lastY = currY;
            if (distanceY > 0) //下滑
            {
                if(isTop(scrollableView))
                {
                    Log.e("scroll","childTop");
                    return true;
                }
                else
                {

                    return false;
                }
            }
            else
            {
                if(isTopHide())
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        obtainVelocityTracker(event);
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
//            Log.e("scroll","滑");
            if (!mScroller.isFinished())
            {
                mScroller.abortAnimation();
            }
            float currY = event.getRawY();
            float distanceY = currY - lastY;
//            Log.e("scroll",distanceY+"");
            lastY = currY;
            if (Math.abs(distanceY) > 0)//滑动
            {
                if (distanceY > 0) //下滑
                {
                    //Log.e("scroll","下滑");
                    if (getScrollY() - distanceY < 0)
                    {
//                        Log.e("scroll","to 0 0");
                        scrollTo(0, 0);
                    }
                    else
                    {
//                        Log.e("scroll","by 0 "+distanceY);
                        scrollBy(0, (int) -distanceY);
                    }
                }
                else
                {
                   // Log.e("scroll","上滑");
                    if (getScrollY() - distanceY > maxScrollY)
                    {
//                        Log.e("scroll","to 0 "+maxScrollY);
                        scrollTo(0, maxScrollY);
                    }
                    else
                    {
//                        Log.e("scroll","by 0 "+distanceY);
                        scrollBy(0, (int) -distanceY);
                    }
                }
                return true;
            }
            else
            {
                return false;
            }

        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            mVelocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            float yVelocity = mVelocityTracker.getYVelocity();
            releaseVelocityTracker();
            if(Math.abs(yVelocity)>minimumFlingVelocity)
            {
                mScroller.fling(0, getScrollY(), 0, -(int) yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                invalidate();
                return true;
            }
        }
        return true;
    }

    private boolean isTopHide()
    {
        if(getScrollY()>=maxScrollY)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            int currY = mScroller.getCurrY();
            if (currY > maxScrollY)
            {
                currY = maxScrollY;
            }
            else if (currY < 0)
            {
                currY = 0;
            }
            scrollTo(0, currY);
            postInvalidate();
        }
    }

    private void obtainVelocityTracker(MotionEvent event)
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setScrollableView(Fragment scrollableView)
    {
        this.scrollableView = scrollableView;
    }

    private boolean isTop(Fragment f)
    {
        View view=((ScrollViewFra)f).getScrollView();
        if (view instanceof ScrollView)
        {
            return ((ScrollView) view).getScrollY() == 0 ? true : false;
        }
        //else if()
        return false;
    }
}
