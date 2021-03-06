package com.sai.user.gnc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class defaultAct extends Activity {
    private static final int WINDOW_ALERT_REQUEST = 1;
    private static final int REQUEST_ACCESS_CALL = 2;
    String TAG;
    public static com.sai.user.gnc.defaultAct defaultAct;
  /*  MyDB myDB;
    public static SQLiteDatabase db;*/

    Handler handler, handler2;
    Runnable task, task2;
    Thread thread;
    int dy;
    public static WindowManager.LayoutParams params, params3, params4, main_layout_params;
    LinearLayout.LayoutParams layoutParams;
    RelativeLayout layout;
    RelativeLayout title;
    RelativeLayout copyright;
    RelativeLayout main_layout;
    DisplayMetrics dm;
    float alpha = 1.0f;
    int cnt = 0;
    boolean moveDown = false;
    public static WindowManager windowManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultAct = this;


        layout = new RelativeLayout(this);
        copyright = new RelativeLayout(this);
        title = new RelativeLayout(this);
        main_layout=new RelativeLayout(this);
        defaultAct = this;
        dm = Resources.getSystem().getDisplayMetrics();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        main_layout_params=new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params3 = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params4 = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String str = bundle.getString("thread");
                int dy = Integer.parseInt(str);
                params.y = dy;
                windowManager.updateViewLayout(layout, params);
            }
        };


        handler2 = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String str = bundle.getString("threadAlpha");
                float alpha = Float.parseFloat(str);
                params.alpha = alpha;
                params4.alpha=alpha;
                main_layout_params.alpha=alpha;
                windowManager.updateViewLayout(layout, params);
                windowManager.updateViewLayout(title, params);
                windowManager.updateViewLayout(copyright, params4);
            }
        };



        task2 = new Runnable() {
            public void run() {
                while (alpha >= 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }

                    alpha -= 0.08;

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("threadAlpha", Float.toString(alpha));
                    message.setData(bundle);
                    handler2.sendMessage(message);
                }

                windowManager.removeView(layout);
                windowManager.removeView(title);
                windowManager.removeView(copyright);

                startService(new Intent(defaultAct, StartActivity.class));
                finish();
            }
        };

        task = new Runnable() {
            public void run() {
                while (cnt < 8) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                    }

                    if (!moveDown) {
                        dy -= 5;
                        if (dy <= 50) {
                            moveDown = true;
                            cnt++;
                        }
                    } else {
                        dy += (int) 5 + Math.pow(2, 2);
                        if (dy >= 150) {
                            moveDown = false;
                            cnt++;
                        }
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("thread", Integer.toString(dy));
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                Thread threadAlpha = new Thread(task2);
                threadAlpha.start();

            }

        };


        layout.setBackgroundColor(Color.argb(100, 255, 0, 0));
        layoutParams.setMargins(0, 20, 0, 0);
        layout.setLayoutParams(layoutParams);
        copyright.setLayoutParams(layoutParams);
        title.setLayoutParams(layoutParams);


        layout.setBackgroundResource(R.drawable.m_logo);
        copyright.setBackgroundResource(R.drawable.m_copy);
        title.setBackgroundResource(R.drawable.m_title);
        main_layout.setBackgroundColor(Color.WHITE);

        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;


        params3.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params4.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.y = 150;
        params3.y = 150;
        params4.y = dm.heightPixels-200;


        windowManager.addView(title,params3);
        windowManager.addView(copyright, params4);
        windowManager.addView(layout, params);


        dy = params.y;

        thread = new Thread(task);
        thread.start();
        // init();


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        TAG = this.getClass().getName();

    }



    @Override
    public void onBackPressed() {
    }

    protected void onDestroy() {

        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }
}
