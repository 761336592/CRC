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
	private String tableName="";  //��ȡ�ǳ��⻹�����
	private int limit=10;//��ʾ10������
	 private int pageNo=1;//ҳ��
	 private int pageCount=0;//ҳ������
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
		// TODO �Զ����ɵķ������
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
					    .setPositiveButton("��ȡʧ��!",new DialogInterface.OnClickListener() {//���ȷ����ť  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
			             return ;   
			         }  
			     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
						 
					break;
				  case 2:
					  progressbar.setVisibility(View.GONE);
					  new AlertDialog.Builder(SearchDH.this).setTitle(R.string.SYS)
					    .setPositiveButton("��ȡʧ��!",new DialogInterface.OnClickListener() {//���ȷ����ť  
			         @Override  
			         public void onClick(DialogInterface dialog, int which) {//ȷ����ť����Ӧ�¼�  
			             return ;   
			         }  
			     }).show();
					  break;
				  case 3:
					  progressbar.setVisibility(View.GONE);
					  pageNo++; 
					  //showListView(list);  //��ʾ
					  
					  break;
				}
		 }
	};	
	/*��ʾ  */
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
				// TODO �Զ����ɵ� catch ��
			
				mSocketServer=null;
				con=false;
			}catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
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

   	    String sqlString=String.format("talk��%s��",androidID);
		char [] buffer=new char[1024];
		
		try{
			mSocketServer.setSoTimeout(5000);
			if ((count = mBufferedReaderClient.read(buffer)) > 0)// �������յ�������
			{
				 recvMessageserver=SYS.getInfoBuff(buffer, count) ;// ��Ϣ����
				 System.out.println("���յ�������:"+recvMessageserver);
				 if(SYS.indexofString(recvMessageserver, sqlString)){
					 buf=SYS.splitString(recvMessageserver.trim(),"��");
					 try{
							len=SYS.crc_r(buf[0].getBytes("GBK"),buf[0].getBytes("GBK").length);
							len=len % 99;
							if(len==Integer.parseInt(buf[1].trim())){
								rec=true;
								string=SYS.splitString(buf[0].trim().substring(sqlString.trim().length(),buf[0].length()),",");
								for(int i=0;i<string.length-1;i++){
									//list.add(string[i].trim());
							    }
                              /*�������*/
							}else{
							   rec=false;
							}
						}catch (UnsupportedEncodingException e){
							// TODO �Զ����ɵ� catch ��
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
		// TODO �Զ����ɵķ������   
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			//	getLoadData();
				if(pageCount>pageNo) {
					pageNo++;
				
				}
				showListView(data);  //��ʾ
				listview.isOver();  //���ؽ���
			}
		}, 1000);
	}
	private OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO �Զ����ɵķ������
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("DH",data.get(position));  //����
			bundle.putString("dh",tableName);  //���⻹�����
			resultIntent.putExtras(bundle);
		//	resultIntent.setClass(SearchDH.this,Details.class);
			startActivity(resultIntent);
		}
	};
}
