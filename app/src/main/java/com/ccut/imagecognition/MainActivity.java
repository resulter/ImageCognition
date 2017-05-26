package com.ccut.imagecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ccut.imagecognition.view.SpinerPopWindow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mTextViewTittleSpinner;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> mSpinnerList;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences sp;

    private ImageView mImageViewExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_main_to_test).setOnClickListener(this);
        initToolbar();
        initView();
        initLeftMenu();
    }

    private void initToolbar() {
        mSpinnerList = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            mSpinnerList.add("test:" + i);
        }

        mTextViewTittleSpinner = (TextView) findViewById(R.id.tv_toolbar_spinner);
        mTextViewTittleSpinner.setOnClickListener(this);
        mSpinerPopWindow = new SpinerPopWindow<String>(MainActivity.this, mSpinnerList,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
    }

    private void initView() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        mTextViewTittleSpinner = (TextView) findViewById(R.id.tv_toolbar_spinner);

    }

    private void initLeftMenu() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("标题");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        mDrawerLayout = (DrawerLayout) findViewById(R.id.fl_content);
        mImageViewExit = (ImageView) findViewById(R.id.iv_left_menu_exit);
        mImageViewExit.setOnClickListener(this);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setDrawerLeftEdgeSize(this, mDrawerLayout, 0.3f);
    }
    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setTextImage(R.mipmap.icon_down);
        }
    };
    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            mTextViewTittleSpinner.setText(mSpinnerList.get(position));
            Toast.makeText(MainActivity.this, "点击了:" + mSpinnerList.get(position),Toast.LENGTH_LONG).show();
        }
    };



    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        mTextViewTittleSpinner.setCompoundDrawables(null, null, drawable, null);
    }
    /**
     * 2  * 抽屉滑动范围控制
     * 3  * 利用反射修改drawerLayout滑动边距
     * 4  * @param drawerLayout
     * 5  * @param displayWidthPercentage 占全屏的份额0~1
     * 6
     */
    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mLeftDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            showTips();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left_menu_exit:
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("ISCHECK").remove("AUTO_ISCHECK");
                editor.commit();
                break;
            case R.id.bt_main_to_test:
                startActivity(new Intent(this,TestActivityNoUse.class));
                break;
            case R.id.tv_toolbar_spinner:
                mSpinerPopWindow.setWidth(mTextViewTittleSpinner.getWidth());
                mSpinerPopWindow.showAsDropDown(mTextViewTittleSpinner);
                setTextImage(R.mipmap.icon_up);
                break;
        }
    }

    private void showTips() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提醒")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                }).setNegativeButton("取消",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create(); // 创建对话框
        alertDialog.show(); // 显示对话框
    }
}
