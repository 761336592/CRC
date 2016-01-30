package com.example.CRC;

import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;//
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class zhgl extends Activity{
	EditText loginText;
    EditText mmText;
    Button  loginButton;
    Button  blackButton;
    Button  returnButton;
public  OutputStream os;
 public InputStream is; 	
  private Context mContext;
 byte zcFlag=0; 
    String Text_of_output="";
    SYS sys=new SYS();
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhgl);
		new LoginOpenHelper(mContext, "AndroidPosJxc.db", null, 1);
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 RelativeLayout fh=(RelativeLayout)findViewById(R.id.fhsz);
		 fh.setOnClickListener(fhClickListener);
		 
		 RelativeLayout zhqh=(RelativeLayout)findViewById(R.id.zhqh);
		 zhqh.setOnClickListener(qhClickListener);
		 
		 RelativeLayout mm=(RelativeLayout)findViewById(R.id.mmxg);
		 mm.setOnClickListener( mmClickListener);
		 
		 RelativeLayout tj=(RelativeLayout)findViewById(R.id.tjyh);
		 tj.setOnClickListener( tjClickListener);
		 RelativeLayout sc=(RelativeLayout)findViewById(R.id.yhsc);
		 sc.setOnClickListener( scClickListener);
		 
		 RelativeLayout gsh=(RelativeLayout)findViewById(R.id.scyh);
		 gsh.setOnClickListener( gshClickListener);
		 
	}	
private  OnClickListener fhClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		 zhgl.this.finish();
	}
}; 
private  OnClickListener qhClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(); 
		intent.setClass(zhgl.this,zhqh.class);
		startActivity(intent); 
	}
}; 
private  OnClickListener gshClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(); 
		intent.setClass(zhgl.this,gsh.class);
		startActivity(intent); 
	}
}; 
private  OnClickListener tjClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(); 
		intent.setClass(zhgl.this,ZHTJ.class);
		startActivity(intent); 
	}
}; 
private  OnClickListener scClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(); 
		intent.setClass(zhgl.this,zhsc.class);
		startActivity(intent); 
	}
}; 
private  OnClickListener mmClickListener=new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent(); 
		intent.setClass(zhgl.this,mmxg.class);
		startActivity(intent); 
	}
}; 
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
		
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
        	 zhgl.this.finish();

        }else if(keyCode == KeyEvent.KEYCODE_MENU) {
           return false;
        }
        return true; 
    } 
	
}
