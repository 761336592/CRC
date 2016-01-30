package com.example.CRC;

import java.util.ArrayList;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Kcdatatil extends Activity{
	String tabString="";
	String nameString="";
	private TextView text_pageNo;   //��ʾҳ���textview
	 private ImageButton button_next, button_last;
	 private ListView listview = null;
	 private HashMap<String, Object>  map=null;
	 private ArrayList<HashMap<String, Object>> listItem=null;
	 private int limit=10;//��ʾ10������
	 private int pageNo=1;//ҳ��
	 private int pageCount=0;//ҳ������
	 private boolean isfirst=false;
	 private String name="";
	 private TextView textView;
	 private EditText editText;
	 private TextView ts;
	 private String sql=new String();
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.kcdetails);
	        textView=(TextView) findViewById(R.id.kcdattail);
	        ts=(TextView) findViewById(R.id.kcts);
	        nameString=getIntent().getExtras().getString("KCH");   // ��һ���ֶε�
	        
			sql=String.format("SELECT KC.*,JBZL.INF2 from KC LEFT JOIN JBZL ON KC.BAR = JBZL.INF1 Where KC.CK =? ");
	        textView.setText(R.string.CK1);
	        textView.append(nameString);
	        isfirst=true;
	        findView();
	        query(1,nameString,name);
	  }
	 private void findView(){   
	     button_last = (ImageButton) findViewById(R.id.kcdatbutton_last);
			 button_last.setOnClickListener(new MyButtonListener());
			  button_next = (ImageButton) findViewById(R.id.kcdatbutton_next);
			  button_next.setOnClickListener(new MyButtonListener());
			  listview = (ListView) findViewById(R.id.kcdatlist);
			  listview.setOnItemClickListener(onItemClickListener);
			  text_pageNo=(TextView)findViewById(R.id.kcdattext_page);
			  editText=(EditText) findViewById(R.id.kcdatedit);
			  editText.addTextChangedListener(teWatcher); //����ǲ��Ƿ����ı�
			  
	  }
	  class MyButtonListener implements android.view.View.OnClickListener{

		  @Override
		  public void onClick(View v) {
		   // TODO Auto-generated method stub
		   switch(v.getId()){
		  
		           case R.id.kcdatbutton_next:
		            pageNo++;
		            if(pageNo>pageCount){
		             pageNo=1;
		            }
		            query(pageNo,nameString,name);
		            break;
		           case R.id.kcdatbutton_last:
		            pageNo--;
		            if(pageNo<1){
		             pageNo=pageCount;
		            }
		            query(pageNo,nameString,name);
		            break;
		     }
	     }
		
	  }		  //������ʾ��ҳ��
		    private void setpageNo(int pageNo){
		       String text=null;
		       text=pageNo+"/"+pageCount;
		       text_pageNo.setText(text);
		      
		    }
		    //��ѯ����
		    private void query(int pageNo,String str,String tablename){
		    
		    
		  
		    }
		    /*��������֮����뵽��һ������*/
		    private OnItemClickListener onItemClickListener=new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3){
					// TODO �Զ����ɵķ������	
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
				
					bundle.putString("BAR",listItem.get(arg2).get("1").toString());
					bundle.putString("KC",nameString);
					resultIntent.putExtras(bundle);
					resultIntent.setClass(Kcdatatil.this,SearchTatil.class);
					startActivity(resultIntent);
				}
			};
			 private TextWatcher teWatcher=new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO �Զ����ɵķ������
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO �Զ����ɵķ������
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO �Զ����ɵķ������
						
					    name=editText.getText().toString().trim();
						pageNo=1;
						query(pageNo,nameString,name);
					   	 
					}
				};
}
