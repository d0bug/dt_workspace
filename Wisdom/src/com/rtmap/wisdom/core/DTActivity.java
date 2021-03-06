package com.rtmap.wisdom.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;
import com.rtmap.wisdom.util.DTLog;

/**
 * 所有activity都要继承的基类
 * @author dingtao
 *
 */
public abstract class DTActivity extends FragmentActivity {

	public ProgressDialog mLoadDialog;// 加载框

	/** 记录处于前台的Activity */
	private static DTActivity mForegroundActivity = null;
	public Gson mGson = new Gson();
	public SharedPreferences mShare = DTApplication.getInstance().getShare();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DTLog.e("getTaskId = " + getTaskId());
		DTApplication.getInstance().addActivity(this);
		initLoad();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mForegroundActivity = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mForegroundActivity = this;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DTApplication.getInstance().removeActivity(this);
	}

	/**
	 * 设置页面名字
	 */
	public abstract String getPageName();

	public void onViewClick(View v) {

	}

	/**
	 * 初始化加载框
	 */
	private void initLoad() {
		mLoadDialog = new ProgressDialog(this);// 加载框
		mLoadDialog.setCanceledOnTouchOutside(false);
		mLoadDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (mLoadDialog.isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
					cancelLoadDialog();
					mLoadDialog.cancel();
				}
				return false;
			}
		});
	}

	public void cancelLoadDialog() {
	}

	/** 获取当前处于前台的activity */
	public static DTActivity getForegroundActivity() {
		return mForegroundActivity;
	}
}
