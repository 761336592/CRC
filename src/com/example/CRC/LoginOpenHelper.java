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
			"BAR text, " +    //����
			"NUM1 text, " +   //Ӧɨ 
			"TIME text, " +   //ɨ��ʱ�� 
			"NUM2 text, " +   //��ɨ 
			"DH text, " +     //����
			"BZ1 text, " +    //���ϵ�
			"BZ2 text, " +
			"BZ3 text, " +
			"BZ4 text, " +
			"BZ5 text, " +
			"BZ6 text, " +
			"BZ7 text, " +
			"BZ8 text, " +
			"BZ9 text, " +
			"BZ10 text, " +
			"DQ text, " +    //����
			"YGBH text)";    //Ա��
	
	
	
	public static final String createTableSent = "create table SENT (" +
			"BH integer primary key autoincrement, " +
			"BAR text, " +
			"ISsent text)";
	/*��Ŷ���״̬�� */
	public static final String createTableDHCX = "create table DHCX (" +
			"BH integer primary key autoincrement, " +
			"BZ1 text, " +
			"BZ2 text, " +
			"BZ3 text, " +
			"BZ4 text, " +
			"DH text)";
	public static final String createTableZC = "create table ZC (" +
			"BH integer primary key autoincrement, " +
			"zcm text, " +     //������
			"mm text, "+       //����
			"flag text, "+     //���û���ע��
			"d text)";         //��һ�ΰ�װʱ��

	public LoginOpenHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
		// TODO �Զ����ɵĹ��캯�����
		//mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		// TODO �Զ����ɵķ������
	//	arg0=SQLiteDatabase.openOrCreateDatabase(f, null);
		arg0.execSQL(createTableLogin);
	
		arg0.execSQL(createTablePD);
		
		arg0.execSQL(createTableSent);
		arg0.execSQL(createTableDHCX);
		arg0.execSQL(createTableZC);
		
		System.out.println("----�ɹ�");
		//Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();	
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO �Զ����ɵķ������
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
	/*�ж����ݱ��Ƿ����*/
	  public boolean tabIsExist(String tabName){
          boolean result = false;
          if(tabName == null){
               return false;
          }
          SQLiteDatabase db = null;
          Cursor cursor = null;
          try {
                  db = helper.getReadableDatabase();//��this�Ǽ̳�SQLiteOpenHelper��õ���
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
