package com.ccut.imagecognition.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccut.imagecognition.MainActivity;
import com.ccut.imagecognition.R;
import com.ccut.imagecognition.config.Config;
import com.ccut.imagecognition.utils.WebUtil;
import com.ccut.imagecognition.view.DeletableEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private DeletableEditText mEtUsername, mEtpassword;
    private Button mBtLogin;
    private LinearLayout mLlLoadingLogin;
    private CheckBox mCheckBoxRemPass,mCheckBoxAutoLogin;
    private TextView mTextViewToLogin;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        autoLogin();
    }

    private void initView() {
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        mEtUsername = (DeletableEditText) findViewById(R.id.et_login_username);
        mEtpassword = (DeletableEditText) findViewById(R.id.et_login_password);
        mLlLoadingLogin = (LinearLayout) findViewById(R.id.ll_loading_login);
        mCheckBoxRemPass = (CheckBox) findViewById(R.id.cb_login_rem_password);
        mCheckBoxAutoLogin = (CheckBox) findViewById(R.id.cb_login_auto_login);
        mTextViewToLogin = (TextView) findViewById(R.id.tv_login_to_register);
        mBtLogin = (Button) findViewById(R.id.btn_login);
        mLlLoadingLogin.setVisibility(View.INVISIBLE);
        mBtLogin.setVisibility(View.VISIBLE);
        mBtLogin.setOnClickListener(this);

    }
    private void autoLogin() {
        //判断记住密码多选框的状态
        if (sp.getBoolean("ISCHECK", false)) {
            //设置默认是记录密码状态
            mCheckBoxRemPass.setChecked(true);
            mEtUsername.setText(sp.getString("USER_NAME", ""));
            mEtpassword.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态
                mCheckBoxAutoLogin.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();

            }
        }

        //监听记住密码多选框按钮事件
        mCheckBoxRemPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCheckBoxRemPass.isChecked()) {
                    Log.d("wang","记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                } else {
                    Log.d("wang","记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        mCheckBoxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCheckBoxAutoLogin.isChecked()) {
                    Log.d("wang","自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    Log.d("wang","自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });


    }
    private void executeLogin(String username, String password) {
        new LoginTask().execute(username, password);
    }

    private void onLoginComplete(Integer userId) {
        if (userId == null || userId == -1) {//如果没有获取到用户ID，说明登录失败
            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT)
                    .show();

            return;
        }
        startActivity(new Intent(this, MainActivity.class));
        mLlLoadingLogin.setVisibility(View.INVISIBLE);
        mBtLogin.setVisibility(View.VISIBLE);
        //如果成功获取到返回的用户ID，说明登录成功，跳转到HelloActivity
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
//        HelloActivity.actionStart(LoginActivity.this, userId, etUsername.getText().toString());
    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //进行登录验证时，显示登录进度条
            mBtLogin.setVisibility(View.INVISIBLE);
            mLlLoadingLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub
            Integer result = null;
            JSONArray reqValue;
            try {
                //将用户名和密码封装到JSONArray中，进行HTTP通信
                reqValue = new JSONArray().put(new JSONObject().put("username",
                        params[0]).put("password", params[1]));
                JSONArray rec = WebUtil.getJSONArrayByWeb(Config.METHOD_LOGIN,
                        reqValue);
                if (rec != null) {//如果成功获取用户ID
                    result = rec.getJSONObject(0).getInt("score");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //回调
            onLoginComplete(result);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = mEtUsername.getText().toString();
                String password = mEtpassword.getText().toString();
                if("".equals(username)){
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(password)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //记住用户名、密码、
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", username);
                editor.putString("PASSWORD", password);
                editor.putBoolean("isLogin", true);
                editor.commit();
                //如果已经填写了用户名和密码，执行登录操作
                executeLogin(username, password);
                break;
        }
    }
}
