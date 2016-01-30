package com.example.CRC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import myview.MyListview;
import myview.MyListview.ILoadListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class SearchDH extends Activity implements ILoadListener{
	MyListview listview;
	ArrayAdapter<String> adapter;
	ArrayList<String> data=null;
	private String tableName="";  //获取是出库还是入库
	private int limit=10;//显示10行数据
	 private int pageNo=1;//页码
	 private int pageCount=0;//页面总数
	 private boolean isfirst=false;
	 EditText editText;
	 String numString="";
	 String name="";
	 
		private ProgressBar progressbar;
		private BufferedReader mBufferedReaderClient = null;
		private PrintWriter mPrintWriterClient = null;
		private	Socket mSocketServer = null;
		
		String androidID="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()        
        .detectDiskReads()        
        .detectDiskWrites()        
        .detectNetwork()   // or .detectAll() for all detectable problems       
        .penaltyLog()        
        .build());        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()        
        .detectLeakedSqlLiteObjects()     
        .penaltyLog()        
        .penaltyDeath()        
        .build());
		setContentView(R.layout.djll);
	
	
		editText=(EditText)findViewById(R.id.dh_edit);
		
		isfirst=true;
		data=new ArrayList<String>();
	//	query(1,"",tableName) ;
		showListView(data);
		listview.setOnItemClickListener(itemClickListener);
	}
	Handler mHandler = new Handler(){
		 public void handleMessage(Message msg){
			
				super.handleMessage(msg);
				  switch (msg.what) {
				  case 0:
					  progressbar.setVisibility(View.VISIBLE);
					  break;
				  case 1:
					  progressbar.setVisibility(View.GONE);	
					  new AlertDialog.Builder(SearchDH.this).setTitle(R.string.SYS)
					    .setPositiveButton("获取失败!",new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();//在按键响应事件中显示此对话框  
						 
					break;
				  case 2:
					  progressbar.setVisibility(View.GONE);
					  new AlertDialog.Builder(SearchDH.this).setTitle(R.string.SYS)
					    .setPositiveButton("获取失败!",new DialogInterface.OnClickListener() {//添加确定按钮  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
			             return ;   
			         }  
			     }).show();
					  break;
				  case 3:
					  progressbar.setVisibility(View.GONE);
					  pageNo++; 
					  //showListView(list);  //显示
					  
					  break;
				}
		 }
	};	
	/*显示  */
	private void showListView(ArrayList<String> apk_list) {
		if (adapter == null) {
			listview = (MyListview) findViewById(R.id.my_list_view);
			listview.setInterface(this);
			adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,apk_list);
			listview.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}
	
	private boolean connection(){
		boolean con=false;
		String iPString="";
		String portString="0";
		 String Text_of_output=WriteReadSD.read("IP",log.fdir);
	      String str[]=null;
		  	 if(Text_of_output.length()>0){
		  		str=SYS.splitString(Text_of_output,":");
		  		iPString=str[0].trim();
		  		portString=str[1].trim();
		    }
		  	try {
				mSocketServer = new Socket();  
				mSocketServer.connect(new InetSocketAddress(iPString,Integer.parseInt(portString)),2000);

				mBufferedReaderClient = new BufferedReader(
						new InputStreamReader(mSocketServer.getInputStream(),"GBK"));

				mPrintWriterClient = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(mSocketServer.getOutputStream(),
								"GBK")));
			    con=true;
			}catch(UnknownHostException e){
				// TODO 自动生成的 catch 块
			
				mSocketServer=null;
				con=false;
			}catch (IOException e) {
				// TODO 自动生成的 catch 块
				mSocketServer=null;
				con=false;
			}
		return con;
	}
	private  boolean recData(){
		boolean rec=false;
		int  len =0;
		int count=0;
		String [] string=null;
   	    String [] buf=null;
   	    String [] bufx=null;
   	    String recvMessageserver="";

   	    String sqlString=String.format("talk□%s□",androidID);
		char [] buffer=new char[1024];
		
		try{
			mSocketServer.setSoTimeout(5000);
			if ((count = mBufferedReaderClient.read(buffer)) > 0)// 读出接收到的数据
			{
				 recvMessageserver=SYS.getInfoBuff(buffer, count) ;// 消息换行
				 System.out.println("接收到的数据:"+recvMessageserver);
				 if(SYS.indexofString(recvMessageserver, sqlString)){
					 buf=SYS.splitString(recvMessageserver.trim(),"■");
					 try{
							len=SYS.crc_r(buf[0].getBytes("GBK"),buf[0].getBytes("GBK").length);
							len=len % 99;
							if(len==Integer.parseInt(buf[1].trim())){
								rec=true;
								string=SYS.splitString(buf[0].trim().substring(sqlString.trim().length(),buf[0].length()),",");
								for(int i=0;i<string.length-1;i++){
									//list.add(string[i].trim());
							    }
                              /*拆分数据*/
							}else{
							   rec=false;
							}
						}catch (UnsupportedEncodingException e){
							// TODO 自动生成的 catch 块
							e.printStackTrace();
							rec=false;
						}
				 }
			}
		}catch (Exception e) {
			rec=false;
		}
		return rec;
	}
	@Override
	public void onLoad() {
		// TODO 自动生成的方法存根   
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//	getLoadData();
				if(pageCount>pageNo) {
					pageNo++;
				
				}
				showListView(data);  //显示
				listview.isOver();  //加载结束
			}
		}, 1000);
	}
	private OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO 自动生成的方法存根
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("DH",data.get(position));  //单号
			bundle.putString("dh",tableName);  //出库还是入库
			resultIntent.putExtras(bundle);
		//	resultIntent.setClass(SearchDH.this,Details.class);
			startActivity(resultIntent);
		}
	};
}
