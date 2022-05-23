package com.cjs.notebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDB extends SQLiteOpenHelper {
//创建SQLite数据库的步骤
//1.创建一个类继承SQLiteOpenHelper类
//2.在该类中重写onCreate()方法和onUpgrade()方法

	//创建数据库
	public static final String TABLE_NAME_NOTES = "note";
	public static final String COLUMN_NAME_ID = "_id";
	public static final String COLUMN_NAME_NOTE_CONTENT = "content";
	public static final String COLUMN_NAME_NOTE_DATE = "date";

	public NotesDB(Context context) {
		//通过super()调用父类SQLiteOpenHelper的构造方法，并传入4个参数
		//上下文、数据库名称、游标工厂（通常是null）、数据库版本
		super(context, "note", null, 1);
		Log.d("NoteDB","创建数据库成功！！");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME_NOTES + "(" + COLUMN_NAME_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_NAME_NOTE_CONTENT + " TEXT NOT NULL DEFAULT\"\","
				+ COLUMN_NAME_NOTE_DATE + " TEXT NOT NULL DEFAULT\"\"" + ")";
		Log.d("SQL", sql);
		Log.d("NoteDB","已经在数据库中创建表结构");
		db.execSQL(sql);
//		String sql1="insert into "+TABLE_NAME_NOTES+"values("+"1,"+"'д��ҵ',"+"'����Ҫд��ҵ�ĸɻ�'"+")";
//		Log.d("SQL1", sql1);
//		db.execSQL(sql1);
//		ContentValues values=new ContentValues();
//		values.put("id",1);
//		values.put("content","д��ҵ");
//		values.put("date", "2013-1-2");
//		db.insert("note", null, values);

	}

	@Override
	//onUpgrade()方法在数据库版本号增加时调用，如果版本号不增加，则该方法不调用。
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
