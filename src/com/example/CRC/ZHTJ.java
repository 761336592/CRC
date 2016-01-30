package com.example.CRC;
import android.app.Activity;
import java.io.InputStream;


import java.io.OutputStream;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ZHTJ extends Activity{
	 EditText loginText;
	    EditText mmText;
	    Button  loginButton;
	    Button  blackButton;
	    Button  returnButton;
	    TextView view;
	public  OutputStream os;
	 public InputStream is; 	
	 byte zcFlag=0; 
	 String Text_of_output="";
	 String YG="";
	 SYS sys=new SYS();
	 protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决了软件软键盘的问题
			setContentView(R.layout.zhtj);
			/*先读取登录的员工编号*/
			/*Text_of_output=sys.My_ReadFile(is,"YGBH", ZHTJ.this);
		  	 if(Text_of_output.trim().length()>0){
		  		YG=Text_of_output.trim();
		  	 }*/
		  	YG=YGDL.YG;
		  	//sqlHelper=new LoginOpenHelper( ZHTJ.this, "AndroidPosJxc.db", null, 1);
			loginText=(EditText)findViewById(R.id.tjbh);
			mmText=(EditText)findViewById(R.id.tjmm);
			view=(TextView)findViewById(R.id.t);
			Button	loginButton=(Button)findViewById(R.id.zhqd);
			Button	returnButton=(Button)findViewById(R.id.tjfh);
            if(!YG.equals("01")){
            	loginText.setEnabled(false);
            	mmText.setEnabled(false);
            	view.setText("你没有权限添加账号");
            	//Toast.makeText(  ZHTJ.this, "你没有权限添加账号", Toast.LENGTH_SHORT).show();
				//return;	
		  	 }else{
			     loginButton.setOnClickListener(login);
		  	 }
			returnButton.setOnClickListener(blackClickListener);
			
	 }
	 private OnClickListener login =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				boolean LoginIndex=false;

				//查询，然后比较
			     if(mmText.getText().toString().length()==0||loginText.getText().toString().length()==0){
					Toast.makeText(  ZHTJ.this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
					return;	
				}
				SQLiteDatabase db = LoginOpenHelper.getInstance(ZHTJ.this,log.DB_NAME).getReadableDatabase();
				Cursor cursor = db.query("YGZL", new String[]{"YGBH","YGMM"}, "YGBH=?", new String[]{loginText.getText().toString().trim()}, null, null, null);
				if(cursor.getCount()==0)
				{
					LoginIndex=false;			
				}
				else
				{
					LoginIndex=true;
				}
				
				if(LoginIndex==true)
				{
					Toast.makeText( ZHTJ.this, "账号已经存在", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{ 
					 SQLiteDatabase dbw =LoginOpenHelper.getInstance(ZHTJ.this,log.DB_NAME).getReadableDatabase();
					  ContentValues va=new ContentValues();
						va.put("YGMM",mmText.getText().toString().trim());
						va.put("YGBH",loginText.getText().toString().trim());
						dbw.insert("YGZL",null ,va);
					
					  Toast.makeText( ZHTJ.this, "添加成功", Toast.LENGTH_SHORT).show();
					  ZHTJ.this.finish();
				}
			}
		};
		private OnClickListener blackClickListener =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 ZHTJ.this.finish();
			}
		};
		public boolean onKeyDown(int keyCode, KeyEvent event)  {  
			
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
		          ZHTJ.this.finish();
		    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
		       return false;
		    }
		    return true; 
		} 
}
