package com.example.CRC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SearchTatil extends Activity{
	List<String> data=null;
	 private String Text_of_output=null;
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
	 private String barcodeString=new String();
	 private String CK=new String();
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.searchtatil);
	        barcodeString = getIntent().getExtras().getString("BAR");  //��ȡ���ݹ���������
	        CK=getIntent().getExtras().getString("KC");  //��ȡ���ݹ���������
	      
	        textView=(TextView) findViewById(R.id.tainftext13);
	        
	        File file=new File(log.pdir,"��Ʒ����.TXT");
		    if(file.exists()){
		    	Log.d("INF", "INF.TXT");
		    	String[] buf=null;
		    	Text_of_output=WriteReadSD.readline("��Ʒ����.TXT",log.pdir);
		    	
				if(Text_of_output.trim().length()>0){
					buf=SYS.splitString(Text_of_output,"\n");
					bufx=SYS.splitString(buf[0],"\t");
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
					name="����";
					hh="����";
					xh="���";
					gg="�ͺ�";
					jj="����";
					sj="�ۼ�";
					dw="��λ";
					bz1="��עһ";
					bz2="��ע��";
					bz3="��ע��";
				}		
		    }else{
		    	name="����";
				 hh="����";
				 xh="���";
				  gg="�ͺ�";
				 jj="����";
				 sj="�ۼ�";
				 dw="��λ";
				 bz1="��עһ";
				 bz2="��ע��";
				 bz3="��ע��";
		    }		
			query(CK,barcodeString);
	        /**/	        
	  }
	  /*��Ҫͨ�����Ʋ�ѯ���룬Ȼ���ٲ�ѯ���*/
	  private void query(String str,String bar){
	    	
	    SQLiteDatabase db = LoginOpenHelper.getInstance(SearchTatil.this,log.DB_NAME).getReadableDatabase();
	     Cursor cursor=null;
	     String sql=String.format("SELECT KC.*,JBZL.INF2,JBZL.INF3,JBZL.INF4,JBZL.INF5,JBZL.INF6,JBZL.INF7" +
	     		"  ,JBZL.INF8,JBZL.INF9,JBZL.INF10,JBZL.INF11 from KC LEFT JOIN JBZL ON KC.BAR = JBZL.INF1 Where KC.CK =? AND KC.BAR=?");
	    	 cursor=db.rawQuery(sql,new String[] {str.trim(),bar.trim()});
	    	  data=new  ArrayList<String>(); 
	    	  String string="";
	    	  new  HashMap<String, Object>(); 
	        new ArrayList<HashMap<String,Object>>();
	       if(cursor.getCount()!= 0){
	    	   cursor.moveToNext();
	    	   if(cursor.getString(cursor.getColumnIndex("INF2"))!=null){
	    		   string = "����:"+cursor.getString(cursor.getColumnIndex("BAR")).toString()+"\n"
		        			+name+":"+cursor.getString(cursor.getColumnIndex("INF2")).toString()+"\n"
		        			+hh+":"+cursor.getString(cursor.getColumnIndex("INF3")).toString()+"\n"
		        			+xh+":"+cursor.getString(cursor.getColumnIndex("INF4")).toString()+"\n"
		        			+gg+":"+cursor.getString(cursor.getColumnIndex("INF5")).toString()+"\n"
		        			+jj+":"+cursor.getString(cursor.getColumnIndex("INF6")).toString()+"         "
		        			+sj+":"+cursor.getString(cursor.getColumnIndex("INF7")).toString()+"\n"
		        			+dw+":"+cursor.getString(cursor.getColumnIndex("INF8")).toString()+"\n"
		        			+bz1+":"+cursor.getString(cursor.getColumnIndex("INF9")).toString()+"\n"
		        			+bz2+":"+cursor.getString(cursor.getColumnIndex("INF10")).toString()+"\n"
		        			+bz3+":"+cursor.getString(cursor.getColumnIndex("INF11")).toString()+"\n"
		        			+"�ֿ�:"+str+"\n"+"���:"+cursor.getString(cursor.getColumnIndex("KCNUM")).toString()+"\n"
		        			+"�������:"+cursor.getString(cursor.getColumnIndex("RKNUM")).toString()+"\n"+"��������:"+cursor.getString(cursor.getColumnIndex("CKNUM")).toString()+"\n";

	    	   }else{
	    		   string = "����:"+cursor.getString(cursor.getColumnIndex("BAR")).toString()+"\n"
		        			+"�޸���������"+"\n"
		        			+"�ֿ�:"+str+"\n"+"���:"+cursor.getString(cursor.getColumnIndex("KCNUM")).toString()+"\n"
		        			+"�������:"+cursor.getString(cursor.getColumnIndex("RKNUM")).toString()+"\n"+"��������:"+cursor.getString(cursor.getColumnIndex("CKNUM")).toString()+"\n";
	    	   }	
            textView.append(string);
	      }else{
	    	  textView.append("�Ҳ�������");
	    	  
	      }
	      
	        
  }
	

}
