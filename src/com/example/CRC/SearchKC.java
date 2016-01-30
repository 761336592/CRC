package com.example.CRC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchKC extends Activity{
	 
	 List<String> data=null;
	 private ListView listkc = null;
	 private ArrayList<HashMap<String, Object>> list =null;
	 private String Text_of_output="";
	 private TextView textView;
	 private	String[] bufx=null;
	 private	String name="";
	 private	String hh="";
	 private	String xh="";
	 private	String gg="";
	 private	String jj="";
	 private	String sj="";
	 private	String dw="";
	 private	String bz1="";
	 private	String bz2="";
	 private	String bz3="";
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.search);
	        String rString = getIntent().getExtras().getString("result");  //获取传递过来的名称
	        Text_of_output="";
	        listkc = (ListView) findViewById(R.id.clist);
	        textView=(TextView) findViewById(R.id.inftext13);
	        File	file=new File(log.pdir,"INF.TXT");
		    if(file.exists()){
		    	Log.d("INF", "INF.TXT");
		    	Text_of_output=WriteReadSD.readline("INF.TXT",log.pdir);
		    }
		    if(Text_of_output.trim().length()>0){
				bufx=SYS.splitString(Text_of_output.trim(),",");				
				name=bufx[1];
				 hh=bufx[2];
				 xh=bufx[3];
				  gg=bufx[4];
				 jj=bufx[5];
				 sj=bufx[6];
				 dw=bufx[7];
				 bz1=bufx[8];
				 bz2=bufx[9];
				 bz3=bufx[10];
			}else{
				name="名称";
				 hh="货号";
				 xh="规格";
				  gg="型号";
				 jj="进价";
				 sj="售价";
				 dw="单位";
				 bz1="备注一";
				 bz2="备注二";
				 bz3="备注三";
			}		
			query(rString);
		  
	  }
	  /*先要通过名称查询条码，然后再查询库存*/
	  private void query(String str){
	    	
	    	SQLiteDatabase db = LoginOpenHelper.getInstance(SearchKC.this,log.DB_NAME).getReadableDatabase();
	     Cursor cursor=null;
	    	 cursor= db.query("JBZL",new String[] { "INF1",
			  			"INF2", "INF3", "INF4", "INF5", "INF6", "INF7",
						"INF8", "INF9", "INF10", "INF11" },"INF2 LIKE ?",
						new String[] { str.trim() },null, null, null);
	    	  data=new  ArrayList<String>(); 
	    	  String string="";
	    	  new  HashMap<String, Object>(); 
	        	 new ArrayList<HashMap<String,Object>>();
	       if(cursor.getCount()!= 0){
	    	   cursor.moveToNext();
	        	string = "条码:"+cursor.getString(cursor.getColumnIndex("INF1")).toString()+"\n"
	        			+name+":"+cursor.getString(cursor.getColumnIndex("INF2")).toString()+"\n"
	        			+hh+":"+cursor.getString(cursor.getColumnIndex("INF3")).toString()+"\n"
	        			+xh+":"+cursor.getString(cursor.getColumnIndex("INF4")).toString()+"\n"
	        			+gg+":"+cursor.getString(cursor.getColumnIndex("INF5")).toString()+"\n"
	        			+jj+":"+cursor.getString(cursor.getColumnIndex("INF6")).toString()+"         "
	        			+sj+":"+cursor.getString(cursor.getColumnIndex("INF7")).toString()+"\n"
	        			+dw+":"+cursor.getString(cursor.getColumnIndex("INF8")).toString()+"\n"
	        			+bz1+":"+cursor.getString(cursor.getColumnIndex("INF9")).toString()+"\n"
	        			+bz2+":"+cursor.getString(cursor.getColumnIndex("INF10")).toString()+"\n"
	        			+bz3+":"+cursor.getString(cursor.getColumnIndex("INF11")).toString();
             textView.append(string);
             string=cursor.getString(cursor.getColumnIndex("INF1")).toString();
	    	/*   map=new  HashMap<String, Object>(); 
	        	 listItem = new ArrayList<HashMap<String,Object>>();
	    	   string=cursor.getString(cursor.getColumnIndex("INF1")).toString();
	        	 map.put("1","条码:"+cursor.getString(cursor.getColumnIndex("INF1")).toString());
	        	 map.put("2",name+":"+cursor.getString(cursor.getColumnIndex("INF2")).toString());
	        	 map.put("3",hh+":"+cursor.getString(cursor.getColumnIndex("INF3")).toString());
	        	 map.put("4",xh+":"+cursor.getString(cursor.getColumnIndex("INF4")).toString());
	        	 map.put("5",gg+":"+cursor.getString(cursor.getColumnIndex("INF5")).toString());
	        	 map.put("6",jj+":"+cursor.getString(cursor.getColumnIndex("INF6")).toString());
	        	 map.put("7",sj+":"+cursor.getString(cursor.getColumnIndex("INF7")).toString());
	        	 map.put("8",dw+":"+cursor.getString(cursor.getColumnIndex("INF8")).toString());
	        	 map.put("9",bz1+":"+cursor.getString(cursor.getColumnIndex("INF9")).toString());
	        	 map.put("10",bz2+":"+cursor.getString(cursor.getColumnIndex("INF10")).toString());
	        	 map.put("11",bz3+":"+cursor.getString(cursor.getColumnIndex("INF11")).toString());
	        	 listItem.add(map);*/
	        }
	       cursor.close();
	       cursor= db.query("KC",new String[] { "BAR","CK", "KCNUM","RKNUM","CKNUM"},"BAR LIKE ?",
					new String[] { string.trim() },null, null, null);
	       list = new ArrayList<HashMap<String,Object>>();
	       while(cursor.moveToNext()){
	    	   HashMap<String, Object>   mape=new  HashMap<String, Object>();
	    	   mape.put("1","仓库:"+cursor.getString(cursor.getColumnIndex("CK")).toString());
	        	 mape.put("2","库存:"+cursor.getString(cursor.getColumnIndex("KCNUM")).toString());
	        	 mape.put("3","入库数量:"+cursor.getString(cursor.getColumnIndex("RKNUM")).toString());
	        	 mape.put("4","出库数量:"+cursor.getString(cursor.getColumnIndex("CKNUM")).toString());
	        	 list.add(mape); 
	       }
	       SimpleAdapter  mSimple=new SimpleAdapter(this, list,R.layout.kcitem,new String[]{"1","2","3","4"}
			  ,new int[]{R.id.inftextkc1,R.id.inftextkc2,R.id.inftextkc3,R.id.inftextkc4});
	       listkc.setAdapter(mSimple);
	        
   }

}
