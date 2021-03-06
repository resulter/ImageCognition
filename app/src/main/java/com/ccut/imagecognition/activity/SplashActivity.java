package com.ccut.imagecognition.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.ccut.imagecognition.R;

public class SplashActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mLinearLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLinearLayoutRoot = (LinearLayout) findViewById(R.id.ll_splash_root);
        startAnim();
    }

    /**
     * 开启动画
     */
    private void startAnim() {

        // 动画集合
        AnimationSet set = new AnimationSet(false);

        // 旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(1000);// 动画时间
        rotate.setFillAfter(true);// 保持动画状态

        // 缩放动画
        ScaleAnimation scale = new ScaleAnimation(0.9f, 1, 0.9f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(1000);// 动画时间
        scale.setFillAfter(true);// 保持动画状态

        // 渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);// 动画时间
        alpha.setFillAfter(true);// 保持动画状态

//		set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        // 设置动画监听
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            // 动画执行结束
            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }
        });

        mLinearLayoutRoot.startAnimation(set);
    }

    /**
     * 跳转下一个页面
     */
    private void jumpNextPage() {
        // 判断之前有没有显示过新手引导
       /* boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed",
                false);

        if (!userGuide) {
            // 跳转到新手引导页
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {

            boolean isLogin = PrefUtils.getBoolean(this, "isLogin", false);
            if (isLogin) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }

        }
*/
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }


}
