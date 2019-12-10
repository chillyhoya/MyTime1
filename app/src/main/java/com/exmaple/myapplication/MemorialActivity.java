package com.exmaple.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemorialActivity extends AppCompatActivity{
    TextView mEtTitle;
    TextView tvTime;
    TextView mEtContent;
    Memorial note;
    private TextView tvDay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         note = (Memorial) getIntent().getSerializableExtra("Note");
        onSetTitle();
        initView();
        init();
    }
    public void onSetTitle() {

        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Toolbar toolbar =  findViewById(R.id.at_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.nav_leftbai);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        mEtTitle = findViewById(R.id.tv_title);
        mEtContent = findViewById(R.id.tv_content);
        tvDay = findViewById(R.id.tv_day);
        tvTime = findViewById(R.id.tv_time);
        ImageView iv_img = findViewById(R.id.iv_img);

        if (!TextUtils.isEmpty(note.getImg())){
            iv_img.setImageBitmap(getLoacalBitmap(note.getImg()));
        }
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void init() {
        mEtTitle.setText(note.getTitle());
        mEtContent.setText(note.getContent());
        tvTime.setText(note.getTime());
        tvDay.setText("还剩"+get(note.getTime())+"天");//显示倒计时
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
               showDel();

                return true;
            case R.id.menu_change:
                Intent intent = new Intent();
                intent.setClass(MemorialActivity.this, AddMemorialActivity.class);
                intent.putExtra("Note",note);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDel() {
        new AlertDialog.Builder(MemorialActivity.this)
                .setTitle("提示")
                .setMessage("请选择操作")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        del();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }


        }).create().show();
    }

    private void del() {
        MemorialDao.getInstance(this).del(note.getId()+"");
       setResult(RESULT_OK);
       finish();
    }

    private long get(String time){//实现倒计时功能
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(time);
            Date d2 = new Date();
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        }
        catch (Exception e) {
        }
        return 0;
    }
}
