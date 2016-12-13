package com.example.user.gnc.com.example.user.gnc.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.gnc.R;
import com.example.user.gnc.defaultAct;

/**
 * Created by Jusung on 2016. 11. 30..
 */

public class KeySettingActivity extends Activity {
    String number;
    String TAG;
    String name, phoneNumber;

    int DOUBLE_CLICK = 1;
    int TOP_CLICK = 2;
    int BOTTOM_CLICK = 3;
    int LEFT_CLICK = 4;
    int RIGHT_CLICK = 5;

    int confirmNum = -1;
    private static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    private static final int START_PHONE_CALL = 1;
    private static final int START_APP_CALL = 2;
    private static final int START_WEB_CALL = 3;

    TextView txt_doubleClick, txt_right, txt_left, txt_bottom, txt_top;
    ImageView img_doubleClick, img_top, img_bottom, img_right, img_left;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();

        setContentView(R.layout.key_setting_activity);
        txt_doubleClick = (TextView) findViewById(R.id.txt_doubleClick);
        txt_right = (TextView) findViewById(R.id.txt_right);
        txt_left = (TextView) findViewById(R.id.txt_left);
        txt_top = (TextView) findViewById(R.id.txt_top);
        txt_bottom = (TextView) findViewById(R.id.txt_bottom);

        txt_doubleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(DOUBLE_CLICK);
            }
        });
        txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(RIGHT_CLICK);
            }
        });
        txt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(LEFT_CLICK);
            }
        });
        txt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(TOP_CLICK);
            }
        });
        txt_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedDialog(BOTTOM_CLICK);

            }
        });

        img_doubleClick = (ImageView) findViewById(R.id.img_doubleClick);
        img_top= (ImageView) findViewById(R.id.img_top);
        img_bottom= (ImageView) findViewById(R.id.img_bottom);
        img_right= (ImageView) findViewById(R.id.img_right);
        img_left= (ImageView) findViewById(R.id.img_left);
    }

    public void showSelectedDialog(final int short_id) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                KeySettingActivity.this);
        alertBuilder.setIcon(R.drawable.logo);
        alertBuilder.setTitle("항목중에 하나를 선택하세요.");

        // List Adapter 생성
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                KeySettingActivity.this,
                android.R.layout.select_dialog_singlechoice);
        adapter.add("전화 걸기");
        adapter.add("앱 실행");
        adapter.add("웹 실행");


        // 버튼 생성
        alertBuilder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });

        // Adapter 셋팅
        alertBuilder.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        /*// AlertDialog 안에 있는 AlertDialog
                        String strName = adapter.getItem(id);
                        AlertDialog.Builder innBuilder = new AlertDialog.Builder(
                                KeySettingActivity.this);
                        innBuilder.setMessage(strName);
                        innBuilder.setTitle("당신이 선택한 것은 ");
                        innBuilder
                                .setPositiveButton(
                                        "확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                            }
                                        });
                        innBuilder.show();*/

                        String strName = adapter.getItem(id);
                        if (strName.equals("전화 걸기")) {
                            selectContact(short_id);
                        } else if (strName.equals("앱 실행")) {
                            selectApp(short_id);
                        } else if (strName.equals("웹 실행")) {

                        }

                        /*Log.d(TAG,"번호"+number);
                        txt_doubleClick.setText(number);*/
                    }
                });
        alertBuilder.show();
    }

    /*전화번호부 가져오기*/
    public void selectContact(int short_id) {
        // Start an activity for the user to pick a phone number from contacts
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
            confirmNum = short_id;
        }
    }

    public void selectApp(int short_cut) {
        Intent intent = new Intent(this, AppListActivity.class);
        intent.putExtra("short_cut", Integer.toString(short_cut));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 1; i <= 5; i++) {
            String sql = "select * from shortcut where short_cut=" + i;
            Cursor rs = defaultAct.db.rawQuery(sql, null);
            rs.moveToNext();
            String name = rs.getString(rs.getColumnIndex("name"));
            String icon_img = rs.getString(rs.getColumnIndex("path"));
            String call_img=rs.getString(rs.getColumnIndex("call_img"));

            Log.d(TAG,"이미지는??"+call_img);

            if (name != null && i == 1) {
                txt_doubleClick.setText(name);
                try {
                    img_doubleClick.setImageDrawable(getPackageManager().getApplicationIcon(icon_img));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (name != null && i == 2) {
                txt_top.setText(name);
                try {
                    img_top.setImageDrawable(getPackageManager().getApplicationIcon(icon_img));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (name != null && i == 3) {
                txt_bottom.setText(name);
                try {
                    img_bottom.setImageDrawable(getPackageManager().getApplicationIcon(icon_img));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (name != null && i == 4) {
                txt_left.setText(name);
                try {
                    img_left.setImageDrawable(getPackageManager().getApplicationIcon(icon_img));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (name != null && i == 5) {
                txt_right.setText(name);
                try {
                    img_right.setImageDrawable(getPackageManager().getApplicationIcon(icon_img));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                number = cursor.getString(numberIndex);
                name=cursor.getString(nameIndex);

                // Do something with the phone number
                Log.d(TAG, "number는?" + number);
                if (confirmNum == 1) {
                    txt_doubleClick.setText(number);
                    img_doubleClick.setImageResource(R.drawable.phone);
                } else if (confirmNum == 2) {
                    txt_top.setText(number);
                    img_top.setImageResource(R.drawable.phone);
                } else if (confirmNum == 3) {
                    txt_bottom.setText(number);
                    img_bottom.setImageResource(R.drawable.phone);
                } else if (confirmNum == 4) {
                    txt_left.setText(number);
                    img_left.setImageResource(R.drawable.phone);
                } else if (confirmNum == 5) {
                    txt_right.setText(number);
                    img_right.setImageResource(R.drawable.phone);
                }

                String sql = "update shortcut set name=?, path=?, method=? where short_cut=?";

                defaultAct.db.execSQL(sql, new String[]{
                        name+"에게 전화걸기", number, Integer.toString(START_PHONE_CALL), Integer.toString(confirmNum)
                });
                confirmNum = -1;
            }
        }
    }
}
