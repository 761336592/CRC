package com.example.CRC;

import android.app.Activity;
import java.io.InputStream;


import java.io.OutputStream;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class mmxg extends Activity{
	 EditText loginText;
	    EditText mmText;
	    Button  loginButton;
	    Button  blackButton;
	    Button  returnButton;
	public  OutputStream os;
	 public InputStream is; 	
	 private LoginOpenHelper sqlHelper;
	 byte zcFlag=0; 
	 String Text_of_output="";
	 String YG="";
	 SYS sys=new SYS();
	 protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.mm);
			/*先读取登录的员工编号*/
			/*Text_of_output=sys.My_ReadFile(is,"YGBH",mmxg.this);
		  	 if(Text_of_output.trim().length()>0){
		  		YG=Text_of_output.trim();
		  	 }*/
		  	YG=YGDL.YG;
		  	sqlHelper=LoginOpenHelper.getInstance(mmxg.this,log.DB_NAME);
			loginText=(EditText)findViewById(R.id.jmm);
			mmText=(EditText)findViewById(R.id.xmm);
			Button	loginButton=(Button)findViewById(R.id.mmqd);
			Button	returnButton=(Button)findViewById(R.id.mmfh);
			loginButton.setOnClickListener(login);
			returnButton.setOnClickListener(blackClickListener);
	 }
	 private OnClickListener login =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				boolean LoginIndex=false;

				//查询，然后比较
			     if(mmText.getText().toString().length()==0||loginText.getText().toString().length()==0){
					Toast.makeText( mmxg.this, "新密码和旧密码不能为空", Toast.LENGTH_SHORT).show();
					return;	
				}
				SQLiteDatabase db = sqlHelper.getReadableDatabase();

				Cursor cursor = db.query("YGZL", new String[]{"YGBH","YGMM"}, "YGBH=?", new String[]{YG}, null, null, null);
				if(cursor.getCount()==0){
					LoginIndex=false;			
				}
				while(cursor.moveToNext()){
					if(cursor.getString(cursor.getColumnIndex("YGMM")).equals(loginText.getText().toString()))
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
					Toast.makeText(mmxg.this, "旧密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{ 
					 SQLiteDatabase dbw =sqlHelper.getWritableDatabase();
					  ContentValues va=new ContentValues();
					  String where = "YGBH" + " = ?";
						String[] whereValue = {YG.trim()};
						va.put("YGMM",mmText.getText().toString().trim());
						db.update("YGZL", va, where, whereValue);
					
					  Toast.makeText(mmxg.this, "密码修改成功", Toast.LENGTH_SHORT).show();
					  mmxg.this.finish();
				}
			}
		};
		private OnClickListener blackClickListener =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 mmxg.this.finish();
			}
		};
		public boolean onKeyDown(int keyCode, KeyEvent event)  {  
			
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
		          mmxg.this.finish();
		    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
		       return false;
		    }
		    return true; 
		} 
}
