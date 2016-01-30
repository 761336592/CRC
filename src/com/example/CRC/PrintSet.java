package com.example.CRC;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class PrintSet extends Activity{
	
	private EditText EditText1;     //条码
	private EditText EditText2;     //条码
	private EditText EditText3;     //条码
	private EditText EditText4;     //条码
	private EditText EditText5;     //条码
	private EditText EditText6;     //条码
	
	private EditText EditText7;     //条码
	private EditText EditText8;     //条码
	private EditText EditText9;     //条码
	private EditText EditText10;     //条码
	private EditText EditText11;     //条码
	private EditText EditText13;     //条码
	private EditText EditText14;     //条码
	private EditText EditText15;     //条码
	private EditText EditText16;     //条码
	private EditText EditText17;     //条码
	private EditText EditText18;     //条
	
	private EditText EditText19;     //条码
	private EditText EditText20;     //条码
	private EditText EditText21;     //条码
	private EditText EditText22;     //条码
	private EditText EditText23;     //条码
	private EditText EditText24;     //条
	private EditText EditText26;     //条
	private EditText ttEditText;     //条
	
	private ImageButton ImageButton1;
	private ImageButton ImageButton2;
	private ImageButton ImageButton3;
	private ImageButton ImageButton4;
	private ImageButton ImageButton5;
	private ImageButton ImageButton6;
	private ImageButton ImageButton7;
	private ImageButton ImageButton8;
	private ImageButton ImageButton9;
	private ImageButton ImageButton10;
	private ImageButton ImageButton11;
	private ImageButton ImageButton13;
	private ImageButton ImageButton14;
	private ImageButton ImageButton15;
	private ImageButton ImageButton16;
	private ImageButton ImageButton17;
	private ImageButton ImageButton18;
	private ImageButton ImageButton19;
	private ImageButton ImageButton20;
	private ImageButton ImageButton21;
	private ImageButton ImageButton22;
	private ImageButton ImageButton23;
	private ImageButton ImageButton24;
	private ImageButton ImageButton26;
	
	
	private EditText printbar1;
	private EditText printbar2;
	
	private String sx1;
	private String sx2;
	
	private ImageButton printbarImage1;
	private ImageButton printbarImage2;
	
	private Button save;
	private PopupWindow popView;
	private ArrayAdapter<String> mAdapter;  
	private ListView mListView; 
	private	ArrayList<String> tatialdata=null;
	private	ArrayList<String> bardata=null;
	private boolean mShowing;
	private Context mContext ;
	
	private String barString=""; //用来记录下标的
	private String numString=""; 
	private String nameString=""; //用来记录下标的
	private String hhString=""; 
	private String ggString=""; //用来记录下标的
	private String xhString=""; 
	private String jjString=""; //用来记录下标的
	private String sjString=""; 
	private String dwString=""; //用来记录下标的
	private String bz1String=""; 
	private String bz2tring=""; //用来记录下标的
	private String bz3tring=""; 

	private String barString1=""; //用来记录下标的
	private String numString1=""; 
	private String nameString1=""; //用来记录下标的
	private String hhString1=""; 
	private String ggString1=""; //用来记录下标的
	private String xhString1=""; 
	private String jjString1=""; //用来记录下标的
	private String sjString1=""; 
	private String dwString1=""; //用来记录下标的
	private String bz1String1=""; 
	private String bz2tring1=""; //用来记录下标的
	private String bz3tring1=""; 
	private String isPrint="";
	private ArrayList<String> printList;
	 protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.print);
		mContext=this;
		initTatial();
		initData();
	
		ImageButton1=(ImageButton) findViewById(R.id.printImageButton01);
		ImageButton1.setOnClickListener(new mybutton());
		
		ImageButton2=(ImageButton)findViewById(R.id.printImageButton02);
		ImageButton2.setOnClickListener(new mybutton());
		
		ImageButton3=(ImageButton) findViewById(R.id.printImageButton03);
		ImageButton3.setOnClickListener(new mybutton());
		
		ImageButton4=(ImageButton) findViewById(R.id.printImageButton04);
		ImageButton4.setOnClickListener(new mybutton());
		
		ImageButton5=(ImageButton) findViewById(R.id.printImageButton05);
		ImageButton5.setOnClickListener(new mybutton());
		
		ImageButton6=(ImageButton) findViewById(R.id.printImageButton06);
		ImageButton6.setOnClickListener(new mybutton());
		
		ImageButton7=(ImageButton) findViewById(R.id.printImageButton07);
		ImageButton7.setOnClickListener(new mybutton());
		
		ImageButton8=(ImageButton) findViewById(R.id.printImageButton08);
		ImageButton8.setOnClickListener(new mybutton());
		
		ImageButton9=(ImageButton) findViewById(R.id.printImageButton09);
		ImageButton9.setOnClickListener(new mybutton());
		
		ImageButton10=(ImageButton) findViewById(R.id.printImageButton10);
		ImageButton10.setOnClickListener(new mybutton());
		
		ImageButton11=(ImageButton) findViewById(R.id.printImageButton11);
		ImageButton11.setOnClickListener(new mybutton());
				
		ImageButton13=(ImageButton) findViewById(R.id.printImageButton13);
		ImageButton13.setOnClickListener(new mybutton());
		
		ImageButton14=(ImageButton)findViewById(R.id.printImageButton14);
		ImageButton14.setOnClickListener(new mybutton());
		
		ImageButton15=(ImageButton) findViewById(R.id.printImageButton15);
		ImageButton15.setOnClickListener(new mybutton());
		
		ImageButton16=(ImageButton) findViewById(R.id.printImageButton16);
		ImageButton16.setOnClickListener(new mybutton());
		
		ImageButton17=(ImageButton) findViewById(R.id.printImageButton17);
		ImageButton17.setOnClickListener(new mybutton());
		
		ImageButton18=(ImageButton) findViewById(R.id.printImageButton18);
		ImageButton18.setOnClickListener(new mybutton());
		
		ImageButton19=(ImageButton) findViewById(R.id.printImageButton19);
		ImageButton19.setOnClickListener(new mybutton());
		
		ImageButton20=(ImageButton) findViewById(R.id.printImageButton20);
		ImageButton20.setOnClickListener(new mybutton());
		
		ImageButton21=(ImageButton) findViewById(R.id.printImageButton21);
		ImageButton21.setOnClickListener(new mybutton());
		
		ImageButton22=(ImageButton) findViewById(R.id.printImageButton22);
		ImageButton22.setOnClickListener(new mybutton());
		
		ImageButton23=(ImageButton) findViewById(R.id.printImageButton23);
		ImageButton23.setOnClickListener(new mybutton());
		
		ImageButton24=(ImageButton) findViewById(R.id.printImageButton24);
		ImageButton24.setOnClickListener(new mybutton());
		
		
		ImageButton26=(ImageButton) findViewById(R.id.printImageButton26);
		ImageButton26.setOnClickListener(new mybutton());
		
		printbarImage1=(ImageButton) findViewById(R.id.printbarImageButton1);
		printbarImage2=(ImageButton) findViewById(R.id.printbarImageButton2);
		printbarImage2.setOnClickListener(new mybutton());
		printbarImage1.setOnClickListener(new mybutton());
        save=(Button) findViewById(R.id.printSave);
        save.setOnClickListener(saveClickListener);
	}
	 private void initTatial(){
		 String[] buf=null;
			String[] bufx=null;
			isPrint=WriteReadSD.read("isPrint",log.fdir);
			printList=new ArrayList<String>();
			bardata=new ArrayList<String>();
			printList.add("一维码");
			printList.add("二维码");
	   File	file=new File(log.pdir,"商品资料.TXT");
	    if(file.exists()){
	    	tatialdata=new ArrayList<String>();
	        String Text_of_output=WriteReadSD.readline("商品资料.TXT",log.pdir);
	       
			if(Text_of_output.trim().length()>0){
				
				bufx=SYS.splitString(Text_of_output,"\n");
				buf=SYS.splitString(bufx[0].trim(),"\t");
				int i=0;
				for(i=0;i<11;i++){
					tatialdata.add(buf[i]);
					if(i>=1){
						bardata.add(buf[i]);
					}
				}
			}else{
		    	tatialdata=new ArrayList<String>();
		    	tatialdata.add("条码");
				tatialdata.add("名称");
				tatialdata.add("货号");
				tatialdata.add("规格");
				tatialdata.add("型号");
				tatialdata.add("进价");
				tatialdata.add("售价");
				tatialdata.add("单位");
				tatialdata.add("备注一");
				tatialdata.add("备注二");
				tatialdata.add("备注三");
				
				
				bardata.add("名称");
				bardata.add("货号");
				bardata.add("规格");
				bardata.add("型号");
				bardata.add("进价");
				bardata.add("售价");
				bardata.add("单位");
				bardata.add("备注一");
				bardata.add("备注二");
				bardata.add("备注三");
			}	
	    }else{
	    	tatialdata=new ArrayList<String>();
	    	tatialdata.add("条码");
			tatialdata.add("名称");
			tatialdata.add("货号");
			tatialdata.add("规格");
			tatialdata.add("型号");
			tatialdata.add("进价");
			tatialdata.add("售价");
			tatialdata.add("单位");
			tatialdata.add("备注一");
			tatialdata.add("备注二");
			tatialdata.add("备注三");
			
			bardata.add("名称");
			bardata.add("货号");
			bardata.add("规格");
			bardata.add("型号");
			bardata.add("进价");
			bardata.add("售价");
			bardata.add("单位");
			bardata.add("备注一");
			bardata.add("备注二");
			bardata.add("备注三");
		}	
	 }
	 
	private void initData(){
		
		
		EditText1=(EditText)findViewById(R.id.printEditText01);
		EditText2=(EditText)findViewById(R.id.printEditText02);
		EditText3=(EditText)findViewById(R.id.printEditText03);
		EditText4=(EditText)findViewById(R.id.printEditText04);
		EditText5=(EditText)findViewById(R.id.printEditText05);
		EditText6=(EditText)findViewById(R.id.printEditText06);
		EditText7=(EditText)findViewById(R.id.printEditText07);
		EditText8=(EditText)findViewById(R.id.printEditText08);
		EditText9=(EditText)findViewById(R.id.printEditText09);
		EditText10=(EditText)findViewById(R.id.printEditText10);
		EditText11=(EditText)findViewById(R.id.printEditText11);
		
		
		EditText13=(EditText)findViewById(R.id.printEditText13);
		EditText14=(EditText)findViewById(R.id.printEditText14);
		EditText15=(EditText)findViewById(R.id.printEditText15);
		EditText16=(EditText)findViewById(R.id.printEditText16);
		EditText17=(EditText)findViewById(R.id.printEditText17);
		EditText18=(EditText)findViewById(R.id.printEditText18);
		EditText19=(EditText)findViewById(R.id.printEditText19);
		EditText20=(EditText)findViewById(R.id.printEditText20);
		EditText21=(EditText)findViewById(R.id.printEditText21);
		EditText22=(EditText)findViewById(R.id.printEditText22);
		EditText23=(EditText)findViewById(R.id.printEditText23);
		EditText24=(EditText)findViewById(R.id.printEditText24);
		
		EditText26=(EditText)findViewById(R.id.printEditText26);
		
		printbar1=(EditText)findViewById(R.id.printbar1);
		printbar2=(EditText)findViewById(R.id.printbar2);
		if(isPrint.trim().equals("1"))
		  EditText26.setText("一维码");
		else{
			EditText26.setText("二维码");
		}
		sx1=WriteReadSD.read("Print1",log.fdir);
		if(!sx1.trim().equals("-1")){
			printbar1.setText(bardata.get(Integer.parseInt(sx1.trim())));
		}else{
			printbar1.setText("");
		}
		
		sx2=WriteReadSD.read("Print2",log.fdir);
		if(!sx2.trim().equals("-1")){
			printbar2.setText(bardata.get(Integer.parseInt(sx2.trim())));
		}else{
			printbar2.setText("");
		}
		
		
		ttEditText=(EditText)findViewById(R.id.ttEditText12);
		/*先读取入库单打印信息*/
		SQLiteDatabase db =LoginOpenHelper.getInstance(PrintSet.this,log.DB_NAME).getReadableDatabase();
		 Cursor cu=null;
		 String sqlString="Select * from PRINT WHERE type=?";
		 cu=db.rawQuery(sqlString, new String[]{"RK"});
		 if(cu.getCount()==0){
				SQLiteDatabase dw =LoginOpenHelper.getInstance(PrintSet.this,log.DB_NAME).getWritableDatabase();
				ContentValues va=new ContentValues();
				va.put("type","RK");
				va.put("sx","0,1,5,-1,-1,-1,-1,-1,-1,-1,-1");
				va.put("gl","5"); 
				va.put("tt","xxxxxx");
				dw.insert("PRINT", null, va);
				
				
				EditText1.setText("条码");
				EditText2.setText("名称");
				EditText3.setText("进价");
				EditText4.setText("");
				EditText5.setText("");
				EditText6.setText("");
				EditText7.setText("");
				EditText8.setText("");
				EditText9.setText("");
				EditText10.setText("");
				EditText11.setText("");
				
		 }else{
			  cu.moveToFirst();
			  String string=cu.getString(cu.getColumnIndex("tt")).toString().trim();
			 ttEditText.setText(string);
			  string=cu.getString(cu.getColumnIndex("gl")).toString().trim(); 
			  EditText13.setText(tatialdata.get(Integer.parseInt(string)));
			  bz3tring=string;
			  string=cu.getString(cu.getColumnIndex("sx")).toString().trim();
			  System.out.println("读出来的:"+string);
			  String[] buf=null;
			  buf=SYS.splitString(string, ",");
			  if(buf[0].trim().equals("-1")){
				  EditText1.setText(""); 
			  }else{
				 EditText1.setText(tatialdata.get(Integer.parseInt(buf[0].trim()))); 
			  }
			  barString=buf[0].trim();
			  if(buf[1].trim().equals("-1")){
				  EditText2.setText(""); 
			  }else{
				  EditText2.setText(tatialdata.get(Integer.parseInt(buf[1].trim()))); 
			  }
			  numString=buf[1].trim();
			  if(buf[2].trim().equals("-1")){
				  EditText3.setText(""); 
			  }else{
				  EditText3.setText(tatialdata.get(Integer.parseInt(buf[2].trim()))); 
			  }
			  nameString=buf[2].trim();
			  if(buf[3].trim().equals("-1")){
				  EditText4.setText("");
			  }else{
				  EditText4.setText(tatialdata.get(Integer.parseInt(buf[3].trim())));
			  }
			  hhString=buf[3].trim();
			  if(buf[4].trim().equals("-1")){
				  EditText5.setText("");
			  }else{
				  EditText5.setText(tatialdata.get(Integer.parseInt(buf[4].trim())));
			  }
			  ggString=buf[4].trim();
			  if(buf[5].trim().equals("-1")){
				  EditText6.setText("");
			  }else{
				  EditText6.setText(tatialdata.get(Integer.parseInt(buf[5].trim())));
			  }
			  xhString=buf[5].trim();
			  if(buf[6].trim().equals("-1")){
				  EditText7.setText("");
			  }else{
				  EditText7.setText(tatialdata.get(Integer.parseInt(buf[6].trim())));
			  }
			  jjString=buf[6].trim();
			  if(buf[7].trim().equals("-1")){
				  EditText8.setText("");
			  }else{
				  EditText8.setText(tatialdata.get(Integer.parseInt(buf[7].trim())));
			  }
			  sjString=buf[7].trim();
			  if(buf[8].trim().equals("-1")){
				  EditText9.setText("");
			  }else{
				  EditText9.setText(tatialdata.get(Integer.parseInt(buf[8].trim())));
			  }
			  dwString=buf[8].trim();
			  if(buf[9].trim().equals("-1")){
				  EditText10.setText("");
			  }else{
				  EditText10.setText(tatialdata.get(Integer.parseInt(buf[9].trim())));
			  }
			  bz1String=buf[9].trim();
			  if(buf[10].trim().equals("-1")){
				  EditText11.setText("");
			  }else{
				  EditText11.setText(tatialdata.get(Integer.parseInt(buf[10].trim())));
			  }
			  bz2tring=buf[10].trim();
			  
		 }
		 cu.close();
		 cu=null;
		 sqlString="Select * from PRINT WHERE type=?";
		 cu=db.rawQuery(sqlString, new String[]{"CK"});
		 if(cu.getCount()==0){
				SQLiteDatabase dw =LoginOpenHelper.getInstance(PrintSet.this,log.DB_NAME).getWritableDatabase();
				ContentValues va=new ContentValues();
				va.put("type","CK");
				va.put("sx","0,1,6,-1,-1,-1,-1,-1,-1,-1,-1");
				va.put("gl","6"); 
				va.put("tt","xxxxxx");
				dw.insert("PRINT", null, va);
				
				EditText14.setText("条码");
				EditText15.setText("名称");
				EditText16.setText("售价");
				EditText17.setText("");
				EditText18.setText("");
				EditText19.setText("");
				EditText20.setText("");
				EditText21.setText("");
				EditText22.setText("");
				EditText23.setText("");
				EditText24.setText("");
				
		 }else{
			  cu.moveToNext();  
			 /* String string=cu.getString(cu.getColumnIndex("gl")).toString().trim();
			  EditText26.setText(tatialdata.get(Integer.parseInt(string)));
			  bz3tring1=string;*/
			  String  string=cu.getString(cu.getColumnIndex("sx")).toString().trim();
			  System.out.println("读出来的:"+string);
			  String[] buf=null;
			  buf=SYS.splitString(string, ",");
			  if(buf[0].trim().equals("-1")){
				  EditText14.setText(""); 
			  }else{
				  EditText14.setText(tatialdata.get(Integer.parseInt(buf[0].trim()))); 
			  }
			  barString1=buf[0].trim();
			  if(buf[1].trim().equals("-1")){
				  EditText15.setText(""); 
			  }else{
				  EditText15.setText(tatialdata.get(Integer.parseInt(buf[1].trim()))); 
			  }
			  numString1=buf[1].trim();
			  if(buf[2].trim().equals("-1")){
				  EditText16.setText(""); 
			  }else{
				  EditText16.setText(tatialdata.get(Integer.parseInt(buf[2].trim()))); 
			  }
			  nameString1=buf[2].trim();
			  if(buf[3].trim().equals("-1")){
				  EditText17.setText("");
			  }else{
				  EditText17.setText(tatialdata.get(Integer.parseInt(buf[3].trim())));
			  }
			  hhString1=buf[3].trim();
			  if(buf[4].trim().equals("-1")){
				  EditText18.setText("");
			  }else{
				  EditText18.setText(tatialdata.get(Integer.parseInt(buf[4].trim())));
			  }
			  ggString1=buf[4].trim();
			  if(buf[5].trim().equals("-1")){
				  EditText19.setText("");
			  }else{
				  EditText19.setText(tatialdata.get(Integer.parseInt(buf[5].trim())));
			  }
			  xhString1=buf[5].trim();
			  if(buf[6].trim().equals("-1")){
				  EditText20.setText("");
			  }else{
				  EditText20.setText(tatialdata.get(Integer.parseInt(buf[6].trim())));
			  }
			  jjString1=buf[6].trim();
			  if(buf[7].trim().equals("-1")){
				  EditText21.setText("");
			  }else{
				  EditText21.setText(tatialdata.get(Integer.parseInt(buf[7].trim())));
			  }
			  sjString1=buf[7].trim();
			  if(buf[8].trim().equals("-1")){
				  EditText22.setText("");
			  }else{
				  EditText22.setText(tatialdata.get(Integer.parseInt(buf[8].trim())));
			  }
			  dwString1=buf[8].trim();
			  if(buf[9].trim().equals("-1")){
				  EditText23.setText("");
			  }else{
				  EditText23.setText(tatialdata.get(Integer.parseInt(buf[9].trim())));
			  }
			  bz1String1=buf[9].trim();
			  if(buf[10].trim().equals("-1")){
				  EditText24.setText("");
			  }else{
				  EditText24.setText(tatialdata.get(Integer.parseInt(buf[10].trim())));
			  }
			  bz2tring1=buf[10].trim();
			 
		 }
	}
	
	
	/*初始化popView*/
	private void initbar(ArrayList<String > arrayList){
		mAdapter = new ArrayAdapter<String>(this,  
		android.R.layout.simple_dropdown_item_1line,arrayList);  
		mListView =new ListView(mContext); 
		mListView.setAdapter(mAdapter);  
				
		int height = ViewGroup.LayoutParams.WRAP_CONTENT/2;  
		int width = EditText1.getWidth();  
				//System.out.println(width);  
		popView = new PopupWindow(mListView, width, height, true);  
		popView.setOutsideTouchable(true);  
		popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
		popView.setOnDismissListener(oClickListener); 
		
	}
	
	private OnDismissListener oClickListener=new OnDismissListener() {
		@Override
		public void onDismiss() {
			// TODO 自动生成的方法存根
			mShowing = false;
		}
	};
	
	class mybutton implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			switch (v.getId()) {
			case R.id.printImageButton01:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(barItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText1,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				break;
			case R.id.printImageButton02:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(numItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText2,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;
			case R.id.printImageButton03:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(nameItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText3,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;
			case R.id.printImageButton04:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(hhItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText4,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton05:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(ggItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText5,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton06:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(xhItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText6,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;
			case R.id.printImageButton07:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(jjItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText7,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton08:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(sjItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText8,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton09:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(dwItem);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText9,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton10:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(bz1Item);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText10,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			case R.id.printImageButton11:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(bz2Item);
					if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText11,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }  
				 break;	 
			
			case R.id.printImageButton13:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(sepItem);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText13,0,-5);  
					mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }	
					break;
			case R.id.printImageButton14:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(typeItem);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText14,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			case R.id.printImageButton15:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit15Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText15,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 	
			case R.id.printImageButton16:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit16Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText16,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	
			case R.id.printImageButton17:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit17Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText17,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break; 
			case R.id.printImageButton18:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit18Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText18,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			case R.id.printImageButton19:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit19Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText19,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	
			case R.id.printImageButton20:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit20Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText20,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	
			case R.id.printImageButton21:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit21Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText21,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	
			case R.id.printImageButton22:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit22Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText22,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;		
			case R.id.printImageButton23:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit23Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText23,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			case R.id.printImageButton24:
				initbar(tatialdata);  
				mListView.setOnItemClickListener(edit24Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText24,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			
			case R.id.printImageButton26:
				initbar(printList);  
				mListView.setOnItemClickListener(edit26Item);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(EditText26,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			case R.id.printbarImageButton1:
				initbar(bardata);  
				mListView.setOnItemClickListener(barprintItem1);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(printbar1,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	 
			case R.id.printbarImageButton2:
				initbar(bardata);  
				mListView.setOnItemClickListener(barprintItem2);
				if (popView!= null){  
					if (!mShowing){  
						popView.showAsDropDown(printbar2,0,-5);  
					    mShowing = true;  
					}else{  
						popView.dismiss();  
					}  
				 }		
				 break;	
			default:
				break;
			}
		}
		
	}
	private OnItemClickListener barItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText1.setText(tatialdata.get(arg2));  
			barString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener numItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText2.setText(tatialdata.get(arg2));  
			numString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener nameItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText3.setText(tatialdata.get(arg2));
			nameString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener hhItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText4.setText(tatialdata.get(arg2));
			hhString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener ggItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText5.setText(tatialdata.get(arg2));  
			ggString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener xhItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText6.setText(tatialdata.get(arg2));
			xhString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener jjItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText7.setText(tatialdata.get(arg2));  
			jjString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener sjItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText8.setText(tatialdata.get(arg2));
			sjString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener dwItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText9.setText(tatialdata.get(arg2));  
			dwString=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener bz1Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText10.setText(tatialdata.get(arg2));  
			bz1String=arg2+"";
			 popView.dismiss(); 
		}
	};
	private OnItemClickListener bz2Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText11.setText(tatialdata.get(arg2));  
			bz2tring=arg2+"";
			 popView.dismiss(); 
		}
	};
/*	private OnItemClickListener bz3Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText12.setText(tatialdata.get(arg2)); 
			bz3tring=arg2+"";
			 popView.dismiss(); 
		}
	};*/
	private OnItemClickListener sepItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			
			EditText13.setText(tatialdata.get(arg2));
			bz3tring=arg2+"";
			 popView.dismiss();
		}
	};
	private OnItemClickListener typeItem=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText14.setText(tatialdata.get(arg2));
			barString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit15Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText15.setText(tatialdata.get(arg2));
			numString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit16Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText16.setText(tatialdata.get(arg2));
			nameString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit17Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText17.setText(tatialdata.get(arg2));
			hhString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit18Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText18.setText(tatialdata.get(arg2));
			ggString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit19Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText19.setText(tatialdata.get(arg2));
			xhString1=arg2+"";
			popView.dismiss(); 
		}
	};
	
	private OnItemClickListener edit20Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText20.setText(tatialdata.get(arg2));
			jjString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit21Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText21.setText(tatialdata.get(arg2));
			sjString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit22Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText22.setText(tatialdata.get(arg2));
			dwString1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit23Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText23.setText(tatialdata.get(arg2));
			bz1String1=arg2+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener edit24Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText24.setText(tatialdata.get(arg2));
			bz2tring1=arg2+"";
			popView.dismiss(); 
		}
	};
	/*private OnItemClickListener edit25Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText25.setText(tatialdata.get(arg2));
			bz3tring1=arg2+"";
			popView.dismiss(); 
		}
	};*/
	private OnItemClickListener edit26Item=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			EditText26.setText(printList.get(arg2));
			isPrint=arg2+1+"";
			popView.dismiss(); 
		}
	};
	private OnItemClickListener barprintItem1=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
				printbar1.setText(bardata.get(arg2));
				sx1=arg2+"";
			  popView.dismiss();
		}
	};
	private OnItemClickListener barprintItem2=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
				printbar2.setText(bardata.get(arg2));
				sx2=arg2+"";
			    popView.dismiss();
		}
	};
	private OnClickListener saveClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if(EditText1.getText().toString().trim().length()==0)
				  barString="-1";		 
		    if(EditText2.getText().toString().trim().length()==0) 
			    numString="-1";
		    if(EditText3.getText().toString().trim().length()==0) 
			  nameString="-1";
		    if(EditText4.getText().toString().trim().length()==0) 
			  hhString="-1";
		    if(EditText5.getText().toString().trim().length()==0) 
			  ggString="-1";
		    if(EditText6.getText().toString().trim().length()==0) 
			  xhString="-1";
		    if(EditText7.getText().toString().trim().length()==0) 
			  jjString="-1";
		    if(EditText8.getText().toString().trim().length()==0) 
			  sjString="-1";
		    if(EditText9.getText().toString().trim().length()==0)  
			  dwString="-1";
		    if(EditText10.getText().toString().trim().length()==0)   
			  bz1String="-1";
		    if(EditText11.getText().toString().trim().length()==0) 
			  bz2tring="-1";
		    if(EditText13.getText().toString().trim().length()==0)
		    	bz3tring="";
		    
			SQLiteDatabase dw =LoginOpenHelper.getInstance(mContext,log.DB_NAME).getWritableDatabase();
			 ContentValues values=new ContentValues(); 
			 values.put("sx",barString+","+numString+","+nameString+","+hhString+","+ggString+","+xhString+","
					      +jjString+","+sjString+","+dwString+","+bz1String+","+bz2tring);
			 values.put("gl", "5");
			 values.put("tt", ttEditText.getText().toString().trim());
			 String where = "type = ? ";
			 String[] whereValue = {"RK"};
		     dw.update("PRINT", values,where, whereValue);
				
		     if(EditText14.getText().toString().trim().length()==0)
				  barString1="-1";		 
		    if(EditText15.getText().toString().trim().length()==0) 
			    numString1="-1";
		    if(EditText16.getText().toString().trim().length()==0) 
			  nameString1="-1";
		    if(EditText17.getText().toString().trim().length()==0) 
			  hhString1="-1";
		    if(EditText18.getText().toString().trim().length()==0) 
			  ggString1="-1";
		    if(EditText19.getText().toString().trim().length()==0) 
			  xhString1="-1";
		    if(EditText20.getText().toString().trim().length()==0) 
			  jjString1="-1";
		    if(EditText21.getText().toString().trim().length()==0) 
			  sjString1="-1";
		    if(EditText22.getText().toString().trim().length()==0)  
			  dwString1="-1";
		    if(EditText23.getText().toString().trim().length()==0)   
			  bz1String1="-1";
		    if(EditText24.getText().toString().trim().length()==0) 
			  bz2tring1="-1";
		    if(EditText26.getText().toString().trim().length()==0) 
				  bz3tring1=""; 
	
			 values.put("sx",barString1+","+numString1+","+nameString1+","+hhString1+","+ggString1+","+xhString1+","
					      +jjString1+","+sjString1+","+dwString1+","+bz1String1+","+bz2tring1);
			 values.put("gl", "6");
			 values.put("tt", ttEditText.getText().toString().trim());
			 where = "type = ? ";
			String [] whereVa= {"CK"};
		     dw.update("PRINT", values,where,whereVa);
		     if(isPrint.trim().equals("1")){
		    	 WriteReadSD.writex("1","isPrint", log.fdir);  //1表示打印一维码
		     }else{
		    	 WriteReadSD.writex("2","isPrint", log.fdir);  //1表示打印一维码
		     }
		     
		     if(printbar1.getText().toString().length()==0) sx1="-1";
		     WriteReadSD.writex(sx1,"Print1", log.fdir);     //1表示打印一维码
		     if(printbar2.getText().toString().length()==0) sx2="-1";
		     WriteReadSD.writex(sx2,"Print2", log.fdir);    //1表示打印一维码
		     
				System.out.println("保存成功");
				new AlertDialog.Builder(PrintSet.this).setTitle(R.string.SYS).setPositiveButton(R.string.BCZL,new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();//在按键响应事件中显示此对话框  
		}
	};
	

}
