package com.exmaple.myapplication;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMemorialActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtTitle;
    EditText mEtContent;
    TextView mBtAdd;
    private TextView tvType;
    List<Object> options1Items;
    private String img = "";
    private ImageView ivImg;
    private TextView tvTime;
    private Memorial note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        note = (Memorial) getIntent().getSerializableExtra("Note");
        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
        onSetTitle();


    }

    public void onSetTitle() {
        Toolbar toolbar =  findViewById(R.id.at_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back_arr);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void initView() {
        mEtTitle = findViewById(R.id.et_title);
        mEtContent = findViewById(R.id.et_content);
        tvType = findViewById(R.id.tv_period);
        mBtAdd = findViewById(R.id.bt_add);
        tvTime = findViewById(R.id.tv_time);
        View llType = findViewById(R.id.ll_type);
        View rl_img = findViewById(R.id.rl_img);
        View rl_time = findViewById(R.id.rl_time);
        ivImg = findViewById(R.id.iv_img);
        llType.setOnClickListener(this);
        rl_img.setOnClickListener(this);
        rl_time.setOnClickListener(this);
        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mEtTitle.getText().toString();
                String content = mEtContent.getText().toString();
                String time = tvTime.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    onToast("请输入标题");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    onToast("请输入内容");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    onToast("请选择时间");
                    return;
                }
                if (TextUtils.isEmpty(img)) {
                    onToast("请选择图片");
                    return;
                }
                if (note != null) {
                    note.setTitle(title);
                    note.setContent(content);
                    note.setTime(time);
                    note.setImg(img);
                    MemorialDao.getInstance(AddMemorialActivity.this).updateNote(note);

                } else{
                    MemorialDao.getInstance(AddMemorialActivity.this).insertNote(new Memorial(title, content, time, "", img));
              }onToast("操作成功");
                setResult(RESULT_OK);
                finish();
            }
        });
        if (note!=null){
            mEtTitle.setText(note.getTitle());
            mEtContent.setText(note.getContent());
            tvTime.setText(note.getTime());
            img = note.getImg();
            Bitmap photo = BitmapFactory.decodeFile(img);
            ivImg.setImageBitmap(photo);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_type:
                break;
            case R.id.rl_time:
                showTime();
                break;
            case R.id.rl_img:
                new AlertDialog.Builder(AddMemorialActivity.this)
                        .setTitle("请选择图片")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getPhotosFromCamera();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
                break;
        }
    }
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private void showTime() {//显示选日期对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (AddMemorialActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                tvTime.setText(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        tvTime.setText(format.format(calendar.getTime()));

    }

    private static final int FLAG_PHOTO = 0;
    private void getPhotosFromCamera() {//从相册中获取图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, FLAG_PHOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FLAG_PHOTO:
                if (data != null) {
                    Uri uri_ = data.getData();
                    if (uri_ != null) {
                        try {
                            img = getPath(AddMemorialActivity.this,uri_);
                            Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri_));
                            ivImg.setImageBitmap(photo);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            default:
                break;
        }

    }

    public void onToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
