package com.rtm.frm.dianxin;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.location.BDLocation;
import com.rtm.frm.dianxin.base.BaseActivity;
import com.rtm.frm.dianxin.manager.RtMapLocManager;
import com.rtm.frm.dianxin.pages.RtmFragment;


public class RtmActivity extends BaseActivity {


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        RtmFragment rtmFragment = new RtmFragment();
        rtmFragment.setArguments(bundle);
        pushFragment(rtmFragment, R.id.main_content);
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected void processLogic() {
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void getDataAgain() {

    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RtMapLocManager.instance().setFollowMode(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
