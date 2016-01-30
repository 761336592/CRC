package com.example.CRC;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginOpenHelper extends SQLiteOpenHelper
{
	//public Context mContext;

	public static final String createTableLogin = "create table YGZL (" +
			"YGBH text primary key, " +
			"YGXM text, " +
			"YGMM text)";
	public static final String createTableJBZL = "create table JBZL (" +
			"INF1 text primary key, " +
			"INF2 text, " +
			"INF3 text, " +
			"INF4 text, " +
			"INF5 text, " +
			"INF6 text, " +
			"INF7 text, " +
			"INF8 text, " +
			"INF9 text, " +
			"INF10 text, "+
			"INF11 text)";

	public static final String createTablePD = "create table YD (" +
			"BH integer primary key autoincrement, " +
			"BAR text, " +    //条码
			"NUM1 text, " +   //应扫 
			"TIME text, " +   //扫描时间 
			"NUM2 text, " +   //已扫 
			"DH text, " +     //单号
			"BZ1 text, " +    //资料的
			"BZ2 text, " +
			"BZ3 text, " +
			"BZ4 text, " +
			"BZ5 text, " +
			"BZ6 text, " +
			"BZ7 text, " +
			"BZ8 text, " +
			"BZ9 text, " +
			"BZ10 text, " +
			"DQ text, " +    //地区
			"YGBH text)";    //员工
	
	
	
	public static final String createTableSent = "create table SENT (" +
			"BH integer primary key autoincrement, " +
			"BAR text, " +
			"ISsent text)";
	/*存放订单状态的 */
	public static final String createTableDHCX = "create table DHCX (" +
			"BH integer primary key autoincrement, " +
			"BZ1 text, " +
			"BZ2 text, " +
			"BZ3 text, " +
			"BZ4 text, " +
			"DH text)";
	public static final String createTableZC = "create table ZC (" +
			"BH integer primary key autoincrement, " +
			"zcm text, " +     //机器号
			"mm text, "+       //密码
			"flag text, "+     //试用或者注册
			"d text)";         //第一次安装时间

	public LoginOpenHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
		// TODO 自动生成的构造函数存根
		//mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		// TODO 自动生成的方法存根
	//	arg0=SQLiteDatabase.openOrCreateDatabase(f, null);
		arg0.execSQL(createTableLogin);
	
		arg0.execSQL(createTablePD);
		
		arg0.execSQL(createTableSent);
		arg0.execSQL(createTableDHCX);
		arg0.execSQL(createTableZC);
		
		System.out.println("----成功");
		//Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO 自动生成的方法存根
		switch (arg2){
	   case 2:
				/*String sql1="ALTER TABLE DHCX ADD BZ1 TEXT";
				String sql2="ALTER TABLE DHCX ADD BZ2 TEXT";
				String sql3="ALTER TABLE DHCX ADD BZ3 TEXT";
				String sql4="ALTER TABLE DHCX ADD BZ4 TEXT";
				arg0.execSQL(sql1);
				arg0.execSQL(sql2);
				arg0.execSQL(sql3);
				arg0.execSQL(sql4);*/
				break;
		}	
	}

	  private static  LoginOpenHelper helper = null;

	public synchronized static LoginOpenHelper getInstance(Context context,String name) { 
	if (helper == null) { 
		helper = new LoginOpenHelper(context, name, null,1);
	} 
	return helper; 
	};
	/*判断数据表是否存在*/
	  public boolean tabIsExist(String tabName){
          boolean result = false;
          if(tabName == null){
               return false;
          }
          SQLiteDatabase db = null;
          Cursor cursor = null;
          try {
                  db = helper.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
                  String sql =" select count(*) as c from sqlite_master where type ='table' and name ='+tabName.trim()+' ";
                  cursor = db.rawQuery(sql, null);
                  if(cursor.moveToNext()){
                          int count = cursor.getInt(0);
                          if(count>0){
                                  result = true;
                          }
                  }
                   
          } catch (Exception e) {
                  // TODO: handle exception
        	  System.out.println("-*---");
          }                
          return result;
  }
	
}
