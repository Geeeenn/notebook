package com.cjs.notebook;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements OnScrollListener,
        OnItemClickListener, OnItemLongClickListener {

    private Context mContext;//这个MainActivity的上下文
    private ListView listview;//首页显示备忘录条目
    private SimpleAdapter simp_adapter;//适配器
    private List<Map<String, Object>> dataList;
    private ImageButton addNote;//首页添加按钮
    private NotesDB DB;//数据库
    private SQLiteDatabase dbread;

    //android6.0之后要动态获取权限
    //检测是否有写的权限的方法，如果没有就授权
    private void checkPermission() {
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("MainActivity","已经动态授权写的权限！");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//他的意思是需要软件全屏显示、自定义标题（使用按钮等控件）和其他的需求//FEATURE_NO_TITLE：没有标题
        setContentView(R.layout.activity_main);
        //先查看是否授权
        checkPermission();
        listview = findViewById(R.id.listview);
        dataList = new ArrayList<>();

        addNote = findViewById(R.id.btn_editnote);
        mContext = this;//当前 MainActivity 的上下文
        addNote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //开启四大组件之一的 Activity 通过 Intent 激活
                NoteEdit.ENTER_STATE = 0;
                Intent intent = new Intent(mContext, NoteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                Log.d("MainActivity","点击首页添加按钮，激活进入编辑活动页面。");
            }
        });
        Button btn_todo = findViewById(R.id.todo);
        btn_todo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ToDo.class);
                Bundle bundle = new Bundle();
                bundle.putString("todoinfo","");
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                Log.d("MainActivity","点击首页代办按钮，激活进入代办活动页面。");
            }
        });
        DB = new NotesDB(this);
        dbread = DB.getReadableDatabase();
        // 清空数据库表中内容
        //dbread.execSQL("delete from note");
        RefreshNotesList();

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        listview.setOnScrollListener(this);
    }

    //清空数据库表中内容的方法
    public void RefreshNotesList() {

        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simp_adapter.notifyDataSetChanged();
            listview.setAdapter(simp_adapter);
        }
        simp_adapter = new SimpleAdapter(this, getData(), R.layout.item,
                new String[] { "tv_content", "tv_date" }, new int[] {
                R.id.tv_content, R.id.tv_date });
        listview.setAdapter(simp_adapter);
        Log.d("MainActivity","RefreshNotesList,自动显示listview。。");
    }

    private List<Map<String, Object>> getData() {

        Cursor cursor = dbread.query("note", null, "content!=\"\"", null, null,
                null, null);


        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            map.put("tv_id", id);
            dataList.add(map);
        }
        cursor.close();
        return dataList;

    }

    //OnScrollListener 中的方法
    @Override//重写
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    //OnScrollListener 中的方法
    // 滑动listview监听事件
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case SCROLL_STATE_FLING:
                Log.i("main", "");
                Log.d("MainActivity","滑动监听事件1");
            case SCROLL_STATE_IDLE:
                Log.i("main", "");
                Log.d("MainActivity","滑动监听事件2");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "");
                Log.d("MainActivity","滑动监听事件3");
        }
    }

    //OnItemClickListener 中的方法
    // 点击某一个item
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        NoteEdit.ENTER_STATE = 1;
         Log.d("arg2", arg2 + "");
        Log.d("MainActivity", "点击第"+ arg2 +"条备忘");
        // TextView
        // content=(TextView)listview.getChildAt(arg2).findViewById(R.id.tv_content);
        // String content1=content.toString();
        Map<String,String> itemMap = (Map<String, String>) listview.getItemAtPosition(arg2);
        Log.d("content", itemMap.get("tv_content"));//tv_content 是 edit.xml 输入的区域
        String id = itemMap.get("tv_id");
        Log.d("ID", id);
        Cursor c = dbread.query("note", null,
                "_id=" + "'" + id + "'", null, null, null, null);
        while (c.moveToNext()) {
            String No = c.getString(c.getColumnIndex("_id"));
            String noteContent = c.getString(c.getColumnIndex("content"));
            Log.d("TEXT", No);
            // Intent intent = new Intent(mContext, NoteEdit.class);
            // intent.putExtra("data", text);
            // setResult(4, intent);
            // // intent.putExtra("data",text);
            // startActivityForResult(intent, 3);
            Intent myIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("info", noteContent);
            NoteEdit.id = Integer.parseInt(No);
            myIntent.putExtras(bundle);
            myIntent.setClass(MainActivity.this, NoteEdit.class);
            startActivityForResult(myIntent, 1);
            Log.d("MainActivity","激活 NoteEdit 服务");
        }

    }

    @Override
    // 接受上一个页面返回的数据
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshNotesList();
        }
        Log.d("MainActivity", "onActivityResult: 接受上一个页面返回的数据");
    }

    //OnItemLongClickListener 中的方法
    // 长按item的点击事件,现OnItemLongClickListener接口的方式设置点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        final int n=arg2;
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志吗？");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Map<String,String> itemMap = (Map<String, String>) listview.getItemAtPosition(n);
                Log.d("content", itemMap.get("tv_content"));
                String id = itemMap.get("tv_id");
                Log.d("ID", id);
                Cursor c = dbread.query("note", null,
                        "_id=" + "'" + id + "'", null, null, null, null);
                while (c.moveToNext()) {
                    String sql_del = "update note set content='' where _id="
                            + id;
                    dbread.execSQL(sql_del);
                    RefreshNotesList();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }

    //ToDo的任务
}
