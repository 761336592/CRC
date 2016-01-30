package com.example.CRC;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.io.InputStream;

import java.io.OutputStream;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;

public class log extends Activity{
	
	 EditText loginText;
	    EditText mmText;
	    Button  loginButton;
	    Button  blackButton;
	    Button  returnButton;
	public  OutputStream os;
	 public InputStream is; 	
	  byte zcFlag=0; 
	    String Text_of_output="";
	//    SYS sys=new SYS();
	    private String androidID="";   //��ȡ�ֻ������кţ�����ע������������ϴ� 
	    private String isNUM="";      //�Ƿ���Ҫ����
		public static  boolean isRes=false;
	public static File pdir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CRC");
	public static File fdir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CRC/.System");
	public  static final String DB_NAME = "/sdcard/CRC/.ApLication/.Jxc.db";
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		if(!pdir.exists()){
		  if(!pdir.mkdir()){
			  new AlertDialog.Builder(log.this).setTitle(R.string.SYS)//���öԻ������  
			     .setPositiveButton(R.string.CJSB,new DialogInterface.OnClickListener() {//���ȷ����ť  
			         @Override  
			           public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
			             return ;   
			         }  
			     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
			  this.finish();
			  return ;
		  }
		} 
		if(!fdir.exists()){
			fdir.mkdir();
		}
		Text_of_output="";
		Text_of_output=WriteReadSD.read("isReScan",fdir);
		 System.out.println("�ظ�:"+Text_of_output.trim());
		if(Text_of_output.trim().length()<=0){
	     	WriteReadSD.writex("0","isReScan", fdir);
		}
		
		 isNUM="";
		 isNUM=WriteReadSD.read("isNUM", fdir);
		 System.out.println("����:"+isNUM.trim());
		 if(isNUM.trim().length()<=0){
			 WriteReadSD.writex("0","isNUM", fdir);
		 }
		 
	    /*��ȡ�ֻ���ID��*/
	     TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	     androidID=telephonyManager.getDeviceId();
	     SQLiteDatabase db =LoginOpenHelper.getInstance(log.this,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		cursor = db.query("ZC", null,null, null, null, null,null);
		if(cursor.getCount()==0){	
			SimpleDateFormat sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd ");
			String date = sDateFormat.format(new   java.util.Date());
			SQLiteDatabase dbw =LoginOpenHelper.getInstance(log.this,log.DB_NAME).getWritableDatabase();
			ContentValues va=new ContentValues();
			va.put("d",date);
			va.put("mm","@@@@@@@@");
			va.put("zcm",androidID); 
			va.put("flag","0"); 
			dbw.insert("ZC", null, va);
			
			Intent intent = new Intent(); 
 		    intent.setClass(log.this,Welcome.class); 
 		    startActivity(intent); 
		}else{
			cursor.moveToNext();
			int len=0;
			String idString =cursor.getString(cursor.getColumnIndex("zcm")).toString().trim();
			try {
				 len = SYS.crc_r(idString.getBytes(MainActivity.bm),idString.getBytes(MainActivity.bm).length);
				 len=len%99;
			} catch (UnsupportedEncodingException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			String s=String.format("%02d", len); 
			String pasString=idString.substring(1, 2)+s+idString.substring(9, 10)+idString.substring(5,6)+idString.substring(6, 7);
			if(pasString.equals(cursor.getString(cursor.getColumnIndex("mm")).toString().trim())){
				Intent intent = new Intent(); 
		 		intent.setClass(log.this,YGDL.class);
		 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		 		startActivity(intent); 
		 		isRes=true;
			}else{
				 Intent intent = new Intent(); 
		 		 intent.setClass(log.this,Welcome.class); 
		 		 startActivity(intent); 
		 		 isRes=false;
			}
			
		}

	  }
	 	
	}  