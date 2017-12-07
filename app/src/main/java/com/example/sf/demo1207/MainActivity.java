package com.example.sf.demo1207;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Random;

// 背景动态变换颜色的引导页效果

/**
 * 元博客地址 https://github.com/jgilfelt/SystemBarTint
 * http://www.jianshu.com/p/a0dde1ebf600   使用ArgbEvaluator 进行颜色值渐变替换了类ColorShades 的复杂使用。
 * http://www.jianshu.com/p/c795ad5bf6f4   第一版采用ColorShades
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private SystemBarTintManager mTintManager;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FrameLayout root;
    private ViewPager viewPager;
    private Context mContext;
    //少用枚举 写到Values array 文件里面
    int[] images = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,};
    int[] colors = new int[]{android.R.color.holo_blue_bright, android.R.color.holo_green_dark, android.R.color.holo_red_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.vp);

        viewPager.setAdapter(getAdapter());

        viewPager.addOnPageChangeListener(this);
    }

    //设置状态栏颜色
    private void applyTintColor(int color) {
        mTintManager.setTintColor(color);
    }

    private void init() {
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        applyTintColor(colors[0]);
    }


    private PagerAdapter getAdapter() {
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public View instantiateItem(ViewGroup container, int position) {
                //View view = View.inflate(mContext,R.layout.vp_item,null);
                View view =getLayoutInflater().inflate(R.layout.vp_item, container, false);
                root = view.findViewById(R.id.root);
                ImageView imageView = view.findViewById(R.id.iv);
                imageView.setImageResource(images[position]);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //这一句必须注释掉 否则报错
                //super.destroyItem(container, position, object);
                container.removeView((View) object);
            }
        };
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e(TAG, position + "====" + positionOffset + "*****" + positionOffsetPixels);
        ColorShades shades = new ColorShades();
        shades.setFromColor(colors[position % images.length])
                .setToColor(colors[(position + 1) % images.length])
                .setShade(positionOffset);
        //getWindow().setBackgroundDrawable(new ColorDrawable(shades.generate()));
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
        //root.setBackgroundColor(shades.generate());
        int color = Color.RED+new Random().nextInt(128);
        Log.e(TAG,shades.generate()+"");
        //applyTintColor(shades.generate());
        //root.setBackgroundColor(color);
        //上面ColorShades 动态生成的颜色有问题，只有黑色背景
        //随机生成的颜色 渐变不够细腻，会闪烁
        getWindow().setBackgroundDrawable(new ColorDrawable(color));
        applyTintColor(color);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
