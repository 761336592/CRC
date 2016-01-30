package com.example.CRC;
import android.app.Activity;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
public class zhqh  extends Activity{
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
    private ImageButton inp;
	 private PopupWindow popView;
		private ArrayAdapter<String> mAdapter;  
		private ListView mListView; 
		  String[] 	arr ; 
			 List<String> data=null;
		  private boolean mShowing;
		  private boolean mInitPopup; 
   private String androidID="";
protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	mContext=this;
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//������������̵�����
	SysApplication.getInstance().addActivity(this);
	 setContentView(R.layout.dl);
	 TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
     androidID=telephonyManager.getDeviceId();
	 loginText=(EditText)findViewById(R.id.login_edit);
	 mmText=(EditText)findViewById(R.id.login_passwd);
    
	 initData();
		inp=(ImageButton) findViewById(R.id.ygImageButton02);
		inp.setOnClickListener(in);
		Button	loginButton=(Button)findViewById(R.id.login_btn);
		Button	returnButton=(Button)findViewById(R.id.login_return);
		
		loginButton.setOnClickListener(login);
		returnButton.setOnClickListener(blackClickListener);
		
}  
private OnClickListener login =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		boolean LoginIndex=false;

		//��ѯ��Ȼ��Ƚ�
	     if(mmText.getText().toString().length()==0||loginText.getText().toString().length()==0)
		{
			Toast.makeText( zhqh.this, "�û��������벻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;	
		}
		SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,androidID).getReadableDatabase();
		
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
			Toast.makeText(zhqh.this, "�û��������벻ƥ�䣬����������", Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			zhqh.this.finish();
		    YGDL.YG=loginText.getText().toString().trim();
		  // sys.My_WriteFile("YGBH", loginText.getText().toString().trim(),zhqh.this,os);
		    Toast.makeText(zhqh.this, "�л��ɹ�", Toast.LENGTH_SHORT).show();
		}
	}
};
private int initData(){
   	SQLiteDatabase db =LoginOpenHelper.getInstance(mContext,androidID).getReadableDatabase();
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
/*�����еĵ�¼�����ŵ�ListView*/
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
		// TODO �Զ����ɵķ������
		mShowing = false;
	}
};

private OnItemClickListener onItem=new OnItemClickListener(){
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO �Զ����ɵķ������
		// Log.e(TAG, "- �������������� -");
		loginText.setText(arr[arg2]);  
		 popView.dismiss(); 
	}
};
/*�����ǵ���Ǹ�����*/	
private OnClickListener in =new OnClickListener() {

@Override
public void onClick(View v){
	// TODO �Զ����ɵķ������
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
private OnClickListener blackClickListener =new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		 zhqh.this.finish();
	}
};
public boolean onKeyDown(int keyCode, KeyEvent event)  {  
	
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   
          zhqh.this.finish();
    }else if(keyCode == KeyEvent.KEYCODE_MENU) {
       return false;
    }
    return true; 
} 
}
