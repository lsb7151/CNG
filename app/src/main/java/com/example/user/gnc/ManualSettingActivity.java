package com.example.user.gnc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.user.gnc.R;


public class ManualSettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    CheckBox check_view;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        setContentView(R.layout.manual_setting_layout);
        check_view = (CheckBox) findViewById(R.id.setting_check_view);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //bt_return.setOnClickListener(this);
        check_view.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.setting_check_view) {
            if (compoundButton.isChecked()) {
                String sql = "update manual_flags set setting=1";
                defaultAct.db.execSQL(sql);
                finish();
            }
        }
    }

    protected void onDestroy() {
        Log.d(TAG, "내가 꺼졌따~");
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
        super.onDestroy();
    }
}
