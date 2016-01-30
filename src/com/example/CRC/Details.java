package com.example.CRC;

import java.util.ArrayList;
import java.util.HashMap;

import myview.MyListview;
import myview.MyListview.ILoadListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Details extends Activity implements ILoadListener{
	String tabString="";
	String nameString="";
	private MyListview listview = null;
	 private HashMap<String, Object>  map=null;
	 private ArrayList<HashMap<String, Object>> listItem=null;
	 private int limit=10;//��ʾ10������
	 private int pageNo=1;//ҳ��
	 private int pageCount=0;//ҳ������
	 private String name="";
	 private int pagNum=0;
	
	 private TextView textView;
	 SimpleAdapter  mSimple;
	 AlertDialog.Builder dialog;
	 private String sql="";
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.details);
	        textView=(TextView) findViewById(R.id.detail);
	      
	        tabString = getIntent().getExtras().getString("dh");  //��ѯ��һ�����     

	        nameString=getIntent().getExtras().getString("DH");   // ��һ���ֶε�
	        if(tabString.equals("RK")){
	        	sql=String.format("SELECT RK.*,JBZL.INF2 from RK LEFT JOIN JBZL ON RK.BAR = JBZL.INF1 Where RK.DH =? ");
	        	textView.setText("��ⵥ:");
	        }else if(tabString.equals("CK")){
	        	sql=String.format("SELECT CK.*,JBZL.INF2 from CK LEFT JOIN JBZL ON CK.BAR = JBZL.INF1 Where CK.DH =? ");
	        	textView.setText("���ⵥ:");
	        }else if(tabString.equals("PD")){
	        	sql=String.format("SELECT PD.*,JBZL.INF2 from PD LEFT JOIN JBZL ON PD.BAR = JBZL.INF1 Where PD.DH =? ");
	        	textView.setText("�̵㵥:");
	        }else if(tabString.equals("KC")){
	        	sql=String.format("SELECT KC.*,JBZL.INF2 from KC LEFT JOIN JBZL ON KC.BAR = JBZL.INF1 Where KC.CK =? ");
	        	textView.setText("�ֿ�:");
	        }
	        System.out.println(sql);
	        textView.append(nameString);
	        findView();
	        query(1,nameString);
	        showListView(listItem);
	        
	  }
	 /*��ȡ������*/
		private String getNum(String table){
			String num="0";
			Cursor cursor=null;
			String sqlString="";
			SQLiteDatabase db=LoginOpenHelper.getInstance(Details.this,log.DB_NAME).getWritableDatabase();
			sqlString=String.format("select count(*) as ����  from %s ",table);
			System.out.println(sqlString);
			cursor =db.rawQuery(sqlString,null);
			
			if(cursor!=null){
				cursor.moveToNext();
				num=cursor.getString(cursor.getColumnIndex("����"));
			}
			pageCount=Integer.parseInt(num)/limit+1;
			pagNum=Integer.parseInt(num);
			cursor.close();
			db.close();
			return num;
		}
	 private void findView(){   	 
		  listview = (MyListview) findViewById(R.id.delist);
		  listItem=new ArrayList<HashMap<String,Object>>();
		  dialog=new AlertDialog.Builder(Details.this);
	  }
	 
	 private void showListView(ArrayList<HashMap<String, Object>> apk_list) {
			if (mSimple == null) {
				listview = (MyListview) findViewById(R.id.delist);
				listview.setInterface(this);
				mSimple=new SimpleAdapter(this,apk_list,R.layout.detailsitem,new String[]{"1","2","3","4"}
		                ,new int[]{R.id.detail1,R.id.detail2,R.id.detail3,R.id.detail4});
				listview.setAdapter(mSimple);
			} else{
				mSimple.notifyDataSetChanged();
			}
		}
		    //��ѯ����
	    private void query(int pageNo,String str){
		    
		     SQLiteDatabase db =LoginOpenHelper.getInstance(Details.this,log.DB_NAME).getReadableDatabase();
		     Cursor cursor=null;
		     
		     String string= sql+" limit ?,?";

		     cursor=db.rawQuery(string,new String[] {str.trim(),String.valueOf(limit*(pageNo-1)),String.valueOf(limit)});
		     int i=0;
		     while(cursor.moveToNext()){
		    	  map=new  HashMap<String, Object>();
		    	    map.put("1",cursor.getString(cursor.getColumnIndex("BAR")).toString());
		    	    if(cursor.getString(cursor.getColumnIndex("INF2")) != null){
		    	    	 map.put("2","����:"+cursor.getString(cursor.getColumnIndex("INF2")).toString());
			    	  }else{
			    		  map.put("2","----");
			    	  }
		    	    if(!tabString.equals("KC")){
		    	      map.put("3","Ӧ������:"+cursor.getString(cursor.getColumnIndex("SJ")).toString());
		    	      map.put("4","�ѳ�����:"+cursor.getString(cursor.getColumnIndex("NUM")).toString()); 
		    	    }else{
		    	    	 map.put("3","���:"+cursor.getString(cursor.getColumnIndex("KCNUM")).toString());
			    	     map.put("4","�������:"+cursor.getString(cursor.getColumnIndex("RKNUM")).toString());
			    	     map.put("5","��������:"+cursor.getString(cursor.getColumnIndex("CKNUM")).toString());
		    	    } 
		    	    map.put("6",cursor.getString(cursor.getColumnIndex("BH")).toString());
		    	    listItem.add(map);  
		      } 
		      cursor.close();
		      db.close();
		    }
	  
			@Override
			public void onLoad() {
				// TODO �Զ����ɵķ������
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
					//	getLoadData();
						if(pageCount>pageNo) {
							pageNo++;
						   query(pageNo,nameString);
						}
						showListView(listItem);  //��ʾ
						listview.isOver();  //���ؽ���
					}
				}, 1000);
			}
}

