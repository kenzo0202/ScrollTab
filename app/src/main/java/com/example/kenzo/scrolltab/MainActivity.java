package com.example.kenzo.scrolltab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int INDICATOR_OFFSET = 48;
    private int mIndicatorOffset;

    private HorizontalScrollView mTrackScroller;
    private ViewGroup mTrack;
    private View mIndicator;
    private int mScrollingState = ViewPager.SCROLL_STATE_IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //端末の解像度を取得
        final float density = getResources().getDisplayMetrics().density;
        mIndicatorOffset = (int)(INDICATOR_OFFSET * density);

        mTrackScroller = (HorizontalScrollView)findViewById(R.id.track_scroller);
        mTrack = (ViewGroup)findViewById(R.id.track);
        mIndicator = findViewById(R.id.indicator);




        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        final ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIndicatorPosition(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                //スクロール中はonPageScrolledで描画するため、ここではしない
                //SCROLL_STATE_IDLEは静止している時
                if(mScrollingState == ViewPager.SCROLL_STATE_IDLE){
                    updateIndicatorPosition(position,0);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                mScrollingState = state;
            }

            private void updateIndicatorPosition(int position, float positionOffset) {
                //positionOffset : ドラッグ量。0以上1未満の数値。


                //現在位置のタブのView
                final View view = mTrack.getChildAt(position);

                //現在位置の次のタブのView,現在の位置が最後のタブの時はnull
                final View view2 = position == (mTrack.getChildCount()-1)? null : mTrack.getChildAt(position + 1);

                int left = view.getLeft();

                int width = view.getWidth();

                int width2 = view2 == null ? width : view2.getWidth();

                //インディケータの幅 positionOffsetには0~1の値が入っている
                int indicatorWidth = (int)(width2 * positionOffset + width * (1 - positionOffset));

                //インディケータの左端の位置
                int indicatorLeft = (int)(left + positionOffset * width);

                //インディケータの幅と左端の位置をセット
                final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();
                layoutParams.width = indicatorWidth;
                layoutParams.setMargins(indicatorLeft,0,0,0);
                mIndicator.setLayoutParams(layoutParams);


                mTrackScroller.scrollTo(indicatorLeft - mIndicatorOffset, 0);
            }


        });



        LayoutInflater inflater = LayoutInflater.from(this);
        for(int i = 0 ; i< adapter.getCount();i++){
            final int position = i;
            TextView tv = (TextView)inflater.inflate(R.layout.tab_item,mTrack,false);
            tv.setText(adapter.getPageTitle(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pager.setCurrentItem(position);
                    Log.d("title","こんにちは");
                }
            });

            mTrack.addView(tv);
        }

    }

    //adapterの設定

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        // タブの項目
        private static final String[] sTabs = { "Mercury", "Venus", "Earth" };

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("title","こんにちは");
            switch (position){
                case 0:
                    return new fragment1();
                case 1:
                    return  new fragment2();
                case 2:
                    return new fragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return sTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTabs[position];
        }
    }
}
