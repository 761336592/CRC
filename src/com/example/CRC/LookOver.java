package com.example.CRC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LookOver extends Activity{
	private TextView text_pageNo;   //显示页码的textview
	 private ImageButton button_next, button_last;
	 private ListView listview = null;
	 private ListAdapter adapter;
	 private EditText editText;
	 private TextView ts;
	 private HashMap<String, Object>  map=null;
	 private ArrayList<HashMap<String, Object>> listItem=null;
	 private int limit=10;//显示10行数据
	 private int pageNo=1;//页码
	 private int pageCount=0;//页面总数
	 private boolean isfirst=false;
	 private String name="";
	 List<String> data=null;
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.zlll);
	        findView();// 获得控件对象
	        isfirst=true;
	        query(1,name);
	  }
	  private void findView(){   
	     button_last = (ImageButton) findViewById(R.id.button_last);
			 button_last.setOnClickListener(new MyButtonListener());
			  button_next = (ImageButton) findViewById(R.id.button_next);
			  button_next.setOnClickListener(new MyButtonListener());
			  listview = (ListView) findViewById(R.id.list);
			  listview.setOnItemClickListener(onItemClickListener);
			  text_pageNo=(TextView)findViewById(R.id.text_page);
			  editText=(EditText)findViewById(R.id.flag_edit);
			  editText.addTextChangedListener(teWatcher); //检测是不是发生改变
			  ts=(TextView) findViewById(R.id.ckts);
	  }
	  
	  private TextWatcher teWatcher=new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				
			    name=editText.getText().toString().trim();
				pageNo=1;
				query(pageNo,name);
			   	 
			}
		};
	  class MyButtonListener implements android.view.View.OnClickListener{

		  @Override
		  public void onClick(View v) {
		   // TODO Auto-generated method stub
		   switch(v.getId()){
		           case R.id.button_next:
		            pageNo++;
		            if(pageNo>pageCount){
		             pageNo=1;
		            }
		            query(pageNo,name);
		            break;
		           case R.id.button_last:
		            pageNo--;
		            if(pageNo<1){
		             pageNo=pageCount;
		            }
		            query(pageNo,name);
		            break;
		     }
	     }
		
	  }		  //设置显示的页码
		    private void setpageNo(int pageNo){
		     String text=null;
		     text=pageNo+"/"+pageCount;
		     text_pageNo.setText(text);
		    }
		    //查询数据
		    private void query(int pageNo,String str){
		    	SQLiteDatabase db =LoginOpenHelper.getInstance(LookOver.this,log.DB_NAME).getReadableDatabase();
		     Cursor cursor=null;
		     System.out.println(str);
		     if(isfirst){
		    	 if(str.trim().length()!=0){
		    		cursor= db.query("JBZL", new String[] { "INF1",
		 		  			"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
		 					"INF8", "INF9", "INF10", "INF11" }, "INF2 LIKE ?",
		 					new String[] {"%" + str.trim() + "%" },
		 					null, null, null);  
		    	 }else{
		              cursor= db.query("JBZL", null, null,
		                 null,null, null, null, null);
		    	 }
		    	  ts.setText(R.string.TMZL);
			      ts.append(cursor.getCount()+"");
		         pageCount=cursor.getCount()/limit+1;
		        isfirst=false;
		     }
		     if(str.trim().length()!=0){
		    	 cursor= db.query("JBZL",new String[] { "INF1",
				  			"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
							"INF8", "INF9", "INF10", "INF11" },"INF2 LIKE ?",
							new String[] {"%" + str.trim() + "%" },null, null, null, limit*(pageNo-1)+","+limit);
		    	  pageCount=cursor.getCount()/limit+1;
		     }else{
		      cursor= db.query("JBZL", null, null,null,null, null, null, limit*(pageNo-1)+","+limit);
		      // System.out.println(limit*(pageNo-1)+"--=--"+limit*pageNo);
		    } 
		     setpageNo(pageNo);
		     int i=0;
		     data=new  ArrayList<String>(); 
		     // listItem=new ArrayList<HashMap<String,Object>>();
		    
		      while(cursor.moveToNext()){
		    	 /*  map=new  HashMap<String, Object>();
		    	  map.put("1",cursor.getString(cursor.getColumnIndex("INF2")).toString());
		    	  listItem.add(map);*/
		    	  data.add(cursor.getString(cursor.getColumnIndex("INF2")).toString());
		    	 
		      }
		      
		      adapter = new ArrayAdapter<String>(this,  
	    				android.R.layout.simple_dropdown_item_1line, data);  
	    				listview.setAdapter(adapter);  
		    /*  SimpleAdapter  mSimple=new SimpleAdapter(this,listItem,R.layout.item,new String[]{"1"
				}
		  ,new int[]{R.id.inftext1,});
		      listview.setAdapter(mSimple);*/
		  
		    }
		    /*按键按下之后进入到下一个界面*/
		    private OnItemClickListener onItemClickListener=new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3){
				
					// TODO 自动生成的方法存根	
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
				//	listItem.get(arg2);
					bundle.putString("result",data.get(arg2));
					resultIntent.putExtras(bundle);
					resultIntent.setClass(LookOver.this,SearchKC.class);
					startActivity(resultIntent);
				}
			};
		
}
