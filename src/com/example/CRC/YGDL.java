package com.example.CRC;

import java.io.OutputStream;
import java.util.List;



import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class YGDL extends Activity{
	private EditText loginText;
	private   EditText mmText;
	private  OutputStream os;
	private Context mContext;
	private ImageButton inp;
	 private PopupWindow popView;
		private ArrayAdapter<String> mAdapter;  
		private ListView mListView; 
		  String[] 	arr ; 
			 List<String> data=null;
		  private boolean mShowing;
		  private boolean mInitPopup; 
	  String Text_of_output="";
	    SYS sys=new SYS();
	    public static String YG="";
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mContext=this;
		SysApplication.getInstance().addActivity(this);
		 setContentView(R.layout.dl);
			 loginText=(EditText)findViewById(R.id.login_edit);
			 mmText=(EditText)findViewById(R.id.login_passwd);
				SQLiteDatabase db =LoginOpenHelper.getInstance(this,log.DB_NAME).getReadableDatabase();
				
				Cursor cursor = db.query("YGZL", null, null, null, null, null, null);	
				if(cursor.getCount()==0){
					SQLiteDatabase dbw =LoginOpenHelper.getInstance(this,log.DB_NAME).getReadableDatabase();
					ContentValues values = new ContentValues();
					values.put("YGBH", "01");
					values.put("YGXM", "");
					values.put("YGMM", "111111");
					
					dbw.insert("YGZL", null, values);
				}
				if (cursor!=null) {
					cursor.close();
				}
				initData();
				inp=(ImageButton) findViewById(R.id.ygImageButton02);
				inp.setOnClickListener(in);
			Button	loginButton=(Button)findViewById(R.id.login_btn);
			Button	returnButton=(Button)findViewById(R.id.login_return);
			
			loginButton.setOnClickListener(login);
			returnButton.setOnClickListener(blackClickListener);
			
	}  
	
	private int initData(){
	   	SQLiteDatabase db =LoginOpenHelper.getInstance(this,log.DB_NAME).getReadableDatabase();
		Cursor cursor=null;
		try{
			cursor=db.query("YGZL", new String[]{"YGBH"}, null,null,null, null, null);
			if(cursor.getCount()==0){
				
				cursor.close();
				return 0;
			}else{
				int i=0;
			  	arr = new String[cursor.getCount()];
				while (cursor.moveToNext())
				{
					arr[i++]=cursor.getString(cursor
							.getColumnIndex("YGBH"));
				}
				String name=arr[arr.length - 1];
				 loginText.setText(name);
				 loginText.setSelection(arr.length);
				return 1;
			} 
		}catch(Exception e){
			cursor.close();
		}finally{
			cursor.close();
			
		}
		return 0;
	}
	/*将所有的登录名都放到ListView*/
	private void initPopup() {  
		
		initData();
			mAdapter = new ArrayAdapter<String>(this,  
			android.R.layout.simple_dropdown_item_1line, arr);  
			mListView =new ListView(mContext); 
			mListView.setAdapter(mAdapter);  
			
			int height = ViewGroup.LayoutParams.WRAP_CONTENT;  
			int width =  loginText.getWidth();  
			//System.out.println(width);  
			popView = new PopupWindow(mListView, width, height, true);  
			popView.setOutsideTouchable(true);  
			popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
			popView.setOnDismissListener(oClickListener); 
			mListView.setOnItemClickListener(onItem); 
			
		}  
	private OnDismissListener oClickListener=new OnDismissListener() {
		@Override
		public void onDismiss() {
			// TODO 自动生成的方法存根
			mShowing = false;
		}
	};
	
	private OnItemClickListener onItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			// Log.e(TAG, "- 啊啊啊啊啊啊啊 -");
			loginText.setText(arr[arg2]);  
			 popView.dismiss(); 
		}
	};
/*这里是点击那个向下*/	
   private OnClickListener in =new OnClickListener() {
	
	@Override
	public void onClick(View v){
		// TODO 自动生成的方法存根
		if(arr!=null && arr.length>0 && !mInitPopup){  
			mInitPopup = true;  
			initPopup();  
			}  
			if (popView!= null){  
			if (!mShowing) {  
				popView.showAsDropDown(loginText,0,-5);  
			mShowing = true;  
			} else {  
				popView.dismiss();  
			}  
		 }  
	}
};	
	
private OnClickListener login =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			boolean LoginIndex=false;

			//查询，然后比较
		     if(mmText.getText().toString().length()==0||loginText.getText().toString().length()==0)
			{
				Toast.makeText( YGDL.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
				return;	
			}
		 	SQLiteDatabase db =LoginOpenHelper.getInstance(YGDL.this,log.DB_NAME).getReadableDatabase();
			
			Cursor cursor = db.query("YGZL", new String[]{"YGBH","YGMM"}, "YGBH=?", new String[]{loginText.getText().toString()}, null, null, null);
			if(cursor.getCount()==0)
			{
				LoginIndex=false;			
			}
			
			while(cursor.moveToNext())
			{
				
				if(cursor.getString(cursor.getColumnIndex("YGMM")).equals(mmText.getText().toString()))
				{
					LoginIndex=true;
				}
				else
				{
					LoginIndex=false;
				}
			}
			
			if(LoginIndex==false)
			{
				Toast.makeText(YGDL.this, "用户名和密码不匹配，请重新输入", Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				
			   Intent intent=new Intent();
			    sys.My_WriteFile("YGBH", loginText.getText().toString().trim(),YGDL.this,os);
			   YG=loginText.getText().toString().trim();   
			 intent.setClass(YGDL.this, MainActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		     startActivity(intent);	
		     mmText.clearFocus();
		     YGDL.this.finish();
		    // System.exit(1);
			}
		}
	};
	private OnClickListener blackClickListener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			SysApplication.getInstance().exit();
		}
	};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
              SysApplication.getInstance().exit();
        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
}