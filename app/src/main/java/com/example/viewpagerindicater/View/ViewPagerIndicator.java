package com.example.viewpagerindicater.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viewpagerindicater.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔡大爷 on 2016/8/18.
 */
public class ViewPagerIndicator extends LinearLayout {

    private ViewPager mViewPager;
    private PageOnChangerListener mListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;


    private int selectedPos;

    private int mTriangleHeight = 20;
    private int mTriangleWidth = 40;
    private int mTabVisiableCount;
    /*
    **用于指示器跟随手指的移动
    * @param mTranslationX
    * 用于将指示器画布移到Tab正中间
    * @param mIndicatorTransX
    *
     */
    private int mTranslationX;
    private int mIndicatorTransX;

    /*
    * 根据tab换算三角形指示器的宽度 ，0.2
    ** @param TAB_WIDTH_CHANGE_TRIANGLE
    * 默认指示器的宽度,默认为4
    * @param DEFAULT_TAB_COUNT
    * 指示器的文本颜色，默认为白色
    * @param TEXT_COLOR_NORMAL
     */
    public static final int DEFAULT_TAB_COUNT = 4;
    public static final double TAB_WIDTH_CHANGE_TRIANGLE = 0.2;
    public static int TEXT_COLOR_NORMAL = 0x77000000;
    public static int TEXT_COLOR_HIGHLIGHT = 0x77489159;

    private List<String> mTitles = new ArrayList<>();


    public ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

        mTabVisiableCount = a.getInt(R.styleable.ViewPagerIndicator_visiable_tab_count, DEFAULT_TAB_COUNT);
        if (mTabVisiableCount < 0) {

            mTabVisiableCount = DEFAULT_TAB_COUNT;
        }
        a.recycle();

    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    ** 设置可见的tab 数量 ，该方法需要在setTabItemTitles()方法前调用
    * @param count
     */
    public void setVisiableTabCount(int count) {
        mTabVisiableCount = count;
    }

    /*
    * 设置 PageOnChangerListener
    * @param listener
     */

    public void setOnPageChangeListener(PageOnChangerListener listener) {
        mListener = listener;
    }


    public void openItemClickEvent(boolean isOpen) {

        if (isOpen) {
            setItemClickEvent();
        }
    }

    private void setItemClickEvent() {

        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

     /*
     * 设置 ViewPager
     * @param viewPager
     */

    public void setViewPager(ViewPager viewPager, final int pos) {

        mViewPager = viewPager;

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageSelected(int position) {

                selectedPos = position;
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }

                setHighLightColor(selectedPos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        };
        mViewPager.setCurrentItem(pos);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
    }

    /*
    ** 重置textView 的颜色
     */
    private void resetTextViewColor() {

        for (int child = 0; child < getChildCount(); child++) {

            setTextColor(child, TEXT_COLOR_NORMAL);
        }
    }

    /**
     * 设置高亮时的颜色
     */
    private void setHighLightColor(int selectedPos) {

        resetTextViewColor();
        setTextColor(selectedPos, TEXT_COLOR_HIGHLIGHT);

    }

    /*
    ** 设置TextView 的颜色
     */
    private void setTextColor(int child, int color) {
        View view = getChildAt(child);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    /*
    ** 设置TabItemTitles
    * @param titles
     */
    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
//            for (String title : mTitles) {
//                addView(generateTextView(title));
//            }

            for (int cureentPos = 0; cureentPos < mTitles.size(); cureentPos++) {
                addView(generateTextView(mTitles.get(cureentPos), cureentPos));
            }
        }
//        setItemClickEvent();
    }

    /*
    ** generateTextView  装载 TextView
    * @param title
     */
    private View generateTextView(String title, int cureentPos) {


        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = getScreenWidth() / mTabVisiableCount;
        lp.height = 80;
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(lp);
        if (selectedPos == cureentPos) {
            tv.setTextColor(TEXT_COLOR_HIGHLIGHT);
        } else {
            tv.setTextColor(TEXT_COLOR_NORMAL);
        }
        return tv;

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
/*
** 只通过布局文件指定是才调用下面代码
 */

//        int childCount = getChildCount();
//        if (childCount == 0) {
//            return;
//        }
//        for (int i = 0; i < childCount; i++) {
//            View view = getChildAt(i);
//            LayoutParams lp = (LayoutParams) view.getLayoutParams();
//            lp.weight = 0;
//            lp.width = getScreenWidth() / mTabVisiableCount;
//            view.setLayoutParams(lp);
//        }

    }

    /*
    ** 获得屏幕宽度
     */
    private int getScreenWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) ((w / mTabVisiableCount) * TAB_WIDTH_CHANGE_TRIANGLE);
        mIndicatorTransX = (w / mTabVisiableCount) / 2 - mTriangleWidth / 2;

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        drawIndicator(canvas);

        super.dispatchDraw(canvas);
    }


    private void drawIndicator(Canvas canvas) {

        Path path = new Path();
        Paint mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);

        path.moveTo(0, getHeight());
        path.lineTo(mTriangleWidth / 2, getHeight() - mTriangleHeight);
        path.lineTo(mTriangleWidth, getHeight());
        path.close();

        canvas.save();
        canvas.translate(mTranslationX, 0);
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }


    public void scroll(int position, float positionOffset) {

        int tabWidth = getWidth() / mTabVisiableCount;

        mTranslationX = (int) (tabWidth * (positionOffset + position)) + mIndicatorTransX;

        if (position >= (mTabVisiableCount - 1) && position > 0 && getChildCount() > mTabVisiableCount) {

            int dx = (int) ((position - (mTabVisiableCount - 1)) * tabWidth + (int) tabWidth * positionOffset);
            this.scrollTo(dx, 0);

            Log.e("TestIndicator", "positionOffset" + positionOffset);

        }
        invalidate();

    }


    public interface PageOnChangerListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);

    }

}
