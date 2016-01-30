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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class zhsc extends Activity{
	 EditText loginText;
	    EditText mmText;
	    Button  loginButton;
	    Button  blackButton;
	    Button  returnButton;
	    TextView view;
	public  OutputStream os;
	 public InputStream is; 	
	 private LoginOpenHelper sqlHelper;
	 byte zcFlag=0; 
	 String Text_of_output="";
	 String YG="";
	 SYS sys=new SYS();
	 protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决了软件软键盘的问题
			setContentView(R.layout.zhsc);
	loginText=(EditText)findViewById(R.id.scbh);
	YG=YGDL.YG;
	view=(TextView)findViewById(R.id.sct);
	Button	loginButton=(Button)findViewById(R.id.scqd);
	Button	returnButton=(Button)findViewById(R.id.scfh);
    if(!YG.equals("01")){
    	loginText.setEnabled(false);
    	view.setText("你没有权限删除账号");
    	
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
	     if(loginText.getText().toString().length()==0)
		{
			Toast.makeText(  zhsc.this, "账号不能为空", Toast.LENGTH_SHORT).show();
			return;	
		}
		SQLiteDatabase db = LoginOpenHelper.getInstance(zhsc.this,log.DB_NAME).getReadableDatabase();
		
		Cursor cursor = db.query("YGZL", new String[]{"YGBH","YGMM"}, "YGBH=?", new String[]{loginText.getText().toString().trim()}, null, null, null);
		if(cursor.getCount()==0)
		{
			LoginIndex=false;			
		}
		else
		{
			LoginIndex=true;
		}
		
		if(LoginIndex==false)
		{
			Toast.makeText( zhsc.this, "账号不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{ 
			 SQLiteDatabase dbw =sqlHelper.getWritableDatabase();
			  String where = "YGBH" + " = ?";
				String[] whereValue = { loginText.getText().toString().trim() };
				dbw.delete("YGZL",where, whereValue );
			  Toast.makeText(zhsc.this, "删除成功", Toast.LENGTH_SHORT).show();
			  zhsc.this.finish();
		}
	}
};
private OnClickListener blackClickListener =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		zhsc.this.finish();
	}
};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
    	zhsc.this.finish();
    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
       return false;
    }
    return true; 
} 

	 
}
