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
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//������������̵�����
			setContentView(R.layout.zhtj);
			/*�ȶ�ȡ��¼��Ա�����*/
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
            	view.setText("��û��Ȩ������˺�");
            	//Toast.makeText(  ZHTJ.this, "��û��Ȩ������˺�", Toast.LENGTH_SHORT).show();
				//return;	
		  	 }else{
			     loginButton.setOnClickListener(login);
		  	 }
			returnButton.setOnClickListener(blackClickListener);
			
	 }
	 private OnClickListener login =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				boolean LoginIndex=false;

				//��ѯ��Ȼ��Ƚ�
			     if(mmText.getText().toString().length()==0||loginText.getText().toString().length()==0){
					Toast.makeText(  ZHTJ.this, "�˺ź����벻��Ϊ��", Toast.LENGTH_SHORT).show();
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
					Toast.makeText( ZHTJ.this, "�˺��Ѿ�����", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{ 
					 SQLiteDatabase dbw =LoginOpenHelper.getInstance(ZHTJ.this,log.DB_NAME).getReadableDatabase();
					  ContentValues va=new ContentValues();
						va.put("YGMM",mmText.getText().toString().trim());
						va.put("YGBH",loginText.getText().toString().trim());
						dbw.insert("YGZL",null ,va);
					
					  Toast.makeText( ZHTJ.this, "��ӳɹ�", Toast.LENGTH_SHORT).show();
					  ZHTJ.this.finish();
				}
			}
		};
		private OnClickListener blackClickListener =new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
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
