package com.example.CRC;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class gsh extends Activity{
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
	// SYS sys=new SYS();
	 protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.gsh);
			YG=YGDL.YG;
			/*先读取登录的员工编号*/
			/*Text_of_output=sys.My_ReadFile(is,"YGBH",gsh.this);
		  	 if(Text_of_output.trim().length()>0){
		  		YG=Text_of_output.trim();
		  	 }*/
		  	sqlHelper=LoginOpenHelper.getInstance(this,log.DB_NAME);
		
			Button	loginButton=(Button)findViewById(R.id.gshqd);
			Button	returnButton=(Button)findViewById(R.id.gshfh);
			 if(YG.equals("01")){
			loginButton.setOnClickListener(login);
			 }else{
				 Toast.makeText( gsh.this, "没有权限", Toast.LENGTH_SHORT).show();
			 }
			returnButton.setOnClickListener(blackClickListener);
			
	 }
	 private OnClickListener login =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 SQLiteDatabase dbw =sqlHelper.getWritableDatabase();
				  ContentValues va=new ContentValues();
					dbw.delete("YGZL",null, null );
					va.put("YGBH", "01");
					va.put("YGXM", "");
					va.put("YGMM", "111111");
					dbw.insert("YGZL",null,va);
				  Toast.makeText(gsh.this, "恢复成功", Toast.LENGTH_SHORT).show();
				  gsh.this.finish();
			}
		};
		private OnClickListener blackClickListener =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 gsh.this.finish();
			}
		};
		public boolean onKeyDown(int keyCode, KeyEvent event)  {  
			
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
		          gsh.this.finish();
		    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
		       return false;
		    }
		    return true; 
		} 
	
	
}
