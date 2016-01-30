package com.example.CRC;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DHTatil extends Activity{
    private	String nameString="";
	private TextView textView;
	private ListView listview = null;
	private HashMap<String, Object>  map=null;
	private ArrayList<HashMap<String, Object>> listItem=null;
	private SimpleAdapter  mSimple;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.dhtatil);
	        textView=(TextView) findViewById(R.id.dhtail);
	        listItem=new ArrayList<HashMap<String,Object>>();
	        nameString=getIntent().getExtras().getString("result");
	        String bufx[]=null;
	        String bufString[]=null;
	        bufx=SYS.splitString(nameString, "#");
	        String string1=bufx.length-1+"";
	        textView.setText("单号:"+getIntent().getExtras().getString("DH")+"  共"+string1+"条未完成 ");
	        for(int i=0;i<bufx.length-1;i++){
	        	map=new  HashMap<String, Object>();
	        	bufString=SYS.splitString(bufx[i].trim(), "@");
	        	map.put("1",bufString[0].trim());
		    	map.put("2","名称:"+bufString[1].trim());
		    	map.put("3","应出数量:"+bufString[2].trim());
		    	map.put("4","已出数量:"+bufString[3].trim()); 
		    	listItem.add(map); 
	        }
	        showListView(listItem);
	  }

	 private void showListView(ArrayList<HashMap<String, Object>> apk_list){
			if (mSimple == null){
				listview = (ListView) findViewById(R.id.dhlist);
				mSimple=new SimpleAdapter(this,apk_list,R.layout.detailsitem
						,new String[]{"1","2","3","4"}
		                ,new int[]{R.id.detail1,R.id.detail2,R.id.detail3,R.id.detail4});
				listview.setAdapter(mSimple);
			}else{
				mSimple.notifyDataSetChanged();
			}
			//listview.setSelection(0);
		}
}
