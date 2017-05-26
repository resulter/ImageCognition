package com.ccut.imagecognition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ccut.imagecognition.config.Config;
import com.ccut.imagecognition.utils.WebUtil;
import com.ccut.imagecognition.view.DeletableEditText;
import com.ccut.imagecognition.view.SpinerPopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class TestActivityNoUse extends AppCompatActivity  {
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<String> mSpinnerList;
    private TextView tvValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();

        tvValue = (TextView) findViewById(R.id.tv_value);
        tvValue.setOnClickListener(clickListener);
        mSpinerPopWindow = new SpinerPopWindow<String>(this, mSpinnerList,itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);
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
            tvValue.setText(mSpinnerList.get(position));
            Toast.makeText(TestActivityNoUse.this, "点击了:" + mSpinnerList.get(position),Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 显示PopupWindow
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_value:
                    mSpinerPopWindow.setWidth(tvValue.getWidth());
                    mSpinerPopWindow.showAsDropDown(tvValue);
                    setTextImage(R.mipmap.icon_up);
                    break;
            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        mSpinnerList = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            mSpinnerList.add("test:" + i);
        }
    }

    /**
     * 给TextView右边设置图片
     * @param resId
     */
    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
        tvValue.setCompoundDrawables(null, null, drawable, null);
    }
}
