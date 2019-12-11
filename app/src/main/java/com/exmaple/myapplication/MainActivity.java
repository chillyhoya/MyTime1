package com.exmaple.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;//抽屉布局
    private RecyclerView mRecyclerView;//列表项
    private List<Memorial> list = new ArrayList<>();
    private NoteListAdapter mAdapter;//适配器
    private LinearLayout llBg;//更改颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.RecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//fab按钮点击事件
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddMemorialActivity.class);
                startActivityForResult(intent,100);
            }
        });
         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        llBg = headerView.findViewById(R.id.ll_bg);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //初始化状态
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home://主页
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_gallery:

                        break;
                    case R.id.nav_slideshow://修改颜色
                        showColor();
                        break;
                    case R.id.nav_tools://修改颜色
                        showColor();
                        break;
                    case R.id.nav_send:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteListAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NoteListAdapter.ItemClickListener() {//列表项点击事件
            @Override
            public void setOnItemClickListener(int position) {//点击列表项，跳转到详细界面
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MemorialActivity.class);
                intent.putExtra("Note",list.get(position));
                startActivityForResult(intent,100);
            }

            @Override
            public void setOnItemLongClickListener(int position) {
                showDel(position);
            }
            //长按列表中的项，出现删除询问框
        });
    }

   void  showColor(){//更改主题颜色
        new ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton("确定",
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                llBg.setBackgroundColor(envelope.getColor());
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    }
    @Override
    public void onBackPressed() {//把抽屉菜单滑进去
        if (drawer == null) return;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void init() {//初始化
        List<Memorial> data = MemorialDao.getInstance(this).loadNote();//从数据库加载数据
        list.clear();
        list.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    private void showDel(final int position) {//显示删除对话框
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("请选择操作")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        del(position);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }


        }).create().show();
    }

    private void del(int position) {//实现删除功能
        MemorialDao.getInstance(this).del(list.get(position).getId()+"");
        list.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {//退出时保存数据
        super.onResume();
        init();
    }
}
