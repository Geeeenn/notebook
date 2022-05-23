package com.cjs.notebook;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEdit extends Activity {
	private TextView tv_date;//显示时间的 TextView
	private EditText et_content;//编辑区域
	private Button btn_ok;//同意保存按钮
	private Button btn_cancel;//取消保存按钮
	private NotesDB DB;//数据库
	private SQLiteDatabase dbread;
	public static int ENTER_STATE = 0;//开始输入状态设为：0
	public static String last_content;//接收Intent传过来的内容
	public static int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set no title feature
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit);//显示布局为 edit.xml

		tv_date = findViewById(R.id.tv_date);//编辑页面的编辑时间
		//获得系统当前时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//显示时间的模板
		String dateString = sdf.format(date);
		tv_date.setText(dateString);//把时间显示在要存放的位置

		et_content = findViewById(R.id.et_content);//编辑区域
		// 设置软键盘自动弹出
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				Log.d("NoteEdit","点击editor区域，软键盘自动弹出");

		DB = new NotesDB(this);//创建数据库对象
		dbread = DB.getReadableDatabase();//读取数据库

		Bundle myBundle = this.getIntent().getExtras();//获取意图
		last_content = myBundle.getString("info");
		Log.d("LAST_CONTENT", last_content);
		et_content.setText(last_content);
		Log.d("NoteEdit","NoteEdit服务被激活");

		// 确认按钮点击事件
		btn_ok = findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// 获取内容
				String content = et_content.getText().toString();
				Log.d("保存的内容是", content);
				// 获取当前事件
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String dateNum = sdf.format(date);
				String sql;
				String sql_count = "SELECT COUNT(*) FROM note";
				SQLiteStatement statement = dbread.compileStatement(sql_count);
				long count = statement.simpleQueryForLong();
				Log.d("COUNT", "这是保存的第"+ count + "条备忘录");
				Log.d("ENTER_STATE", ENTER_STATE + "");
				Log.d("ENTER_STATE", ENTER_STATE + "：输入的状态，表示输入完毕");
				// 添加一个新的日志
				if (ENTER_STATE == 0) {
					if (!content.equals("")) {
						sql = "insert into " + NotesDB.TABLE_NAME_NOTES
								+ " values(" + count + "," + "'" + content
								+ "'" + "," + "'" + dateNum + "')";
						Log.d("LOG", sql);
						dbread.execSQL(sql);
					}
				}
				// 保存和修改一个已有日志
				else {
						String updatesql = "update note set content='"
								+ content + "' where _id=" + id;
						dbread.execSQL(updatesql);
						// et_content.setText(last_content);
				}
				Intent data = new Intent();
				setResult(2, data);
				finish();
				Log.d("NoteEdit","已保存编辑内容，NoteEdit 活动关闭！！");
			}
		});
		btn_cancel = findViewById(R.id.btn_cancle);
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
				Log.d("NoteEdit","取消保存编辑内容，NoteEdit 活动关闭！！");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == 3 && resultCode == 4) {
		// last_content=data.getStringExtra("data");
		// Log.d("LAST_STRAING", last_content+"gvg");
		// }
	}
}
