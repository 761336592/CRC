package com.example.CRC;

import java.io.UnsupportedEncodingException;
import com.mining.app.socket.mySocket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

public class SearchOk extends Activity {
	
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		mysocket.stop();
		System.out.println("OKonDestroy()");
	}
	private mySocket mysocket=null;

	 private     String   iPString="";
	 private     String   portString="";
	 private     String   androidID="";
	 private     String   fwq="";
	 private     StringBuffer reusltCode=null;
	 private int pageNo=1;//页码
	 private boolean isconnect=false;
	 private StringBuffer barcodeString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchok);
		
		barcodeString=new StringBuffer();
		barcodeString.append(getIntent().getStringExtra("CXDH").trim());
        TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        androidID=telephonyManager.getDeviceId();
         String   Text_of_output="";
         String   str[]=null;
         getIntent().getStringExtra("CXDH");
		 Text_of_output=WriteReadSD.read("IP",log.fdir);
	  	 if(Text_of_output.length()>0){
	  		str=SYS.splitString(Text_of_output,":");
	  		iPString=str[0].trim();
	  		portString=str[1].trim();
	  	 }
	  	Text_of_output="";
	  	Text_of_output=WriteReadSD.read("FWQM",log.fdir);
	 	 if(Text_of_output.length()>0){
	 		fwq=Text_of_output.trim();
	 	 }else{
	 		fwq="RS";
	 	 }
	 	 pageNo=1;
	 	mysocket=new mySocket(this, mHandler,iPString,Integer.parseInt(portString));
	 	mysocket.connect();
	 	
	}
	/*连接服务器*/

/*发送下载指令*/
private void sentData(int pag){
	String bufString="ASKDH□"+getIntent().getStringExtra("CXDH")+"□"+pageNo;
	if(isconnect)
	   mysocket.writeData(SYS.SenData(bufString,fwq,androidID));
	
		
}
@Override
		protected void onResume() {
			// TODO 自动生成的方法存根
			super.onResume();

			System.out.println("OK:onResume");
			
			
		}	
	/*消息处理*/
	Handler mHandler = new Handler(){
		 public void handleMessage(Message msg){
				super.handleMessage(msg);
				int returnCode=0;
				String[] buf=null;
				String[] bufx=null;
				int len=0;
				switch(msg.what){
			     	case 1:
			     		Intent intent=new Intent();
		     			 intent.putExtra("result","fail");
		     			  setResult(RESULT_OK, intent);
		    	         	finish();
		    	         break;	
   
			     	case 17:
			     		String string=String.format("talk%s%s%s%d%s%s,","□",androidID,"□",pageNo,"□",barcodeString.toString().trim());
			    		reusltCode=new StringBuffer();
			     		reusltCode.append(msg.getData().getString("REC"));
			     		System.err.println("数据是:"+reusltCode.toString());
			     		 if(SYS.indexofString(reusltCode.toString().trim(),string)){
		    				   buf=SYS.splitString(reusltCode.toString().trim(),"■");
		    				   if(buf.length>=2){	    					
		 					  try {
								len=SYS.crc_r(buf[0].trim().getBytes("GBK"),buf[0].trim().getBytes("GBK").length);
								len=len%99;
								System.out.println("校验值是:"+len);
			 					  if(len!=Integer.parseInt(buf[1].trim())){
			 						
			 					  }else{
			 						  bufx=SYS.splitString(buf[0].trim(),",");
			 						  if(bufx[1].trim().equals("OVER")){
			 							  Intent intent1=new Intent();
							     	      intent1.putExtra("result","OVER");
							     		  setResult(RESULT_OK, intent1);
							     	
							     		  
					    				  finish();
			 						  }else if(bufx[1].equals("无--")){
			 							 Intent intent1=new Intent();
							     	      intent1.putExtra("result","无--");
							     		  setResult(RESULT_OK, intent1);
					    				  finish();
					    				
			 						  }else if(bufx[1].equals("DOING")){
			 							 Intent intent1=new Intent();
							     	      intent1.putExtra("result","DOING");
							     		  setResult(RESULT_OK, intent1);
							     		 
					    				  finish();
			 						  }else if(bufx[1].equals("DownOver")){
			 								SQLiteDatabase dbwW =LoginOpenHelper.getInstance(SearchOk.this,log.DB_NAME).getWritableDatabase();
			 								Cursor cursor=dbwW.rawQuery("select * from DHCX WHERE DH=? ", new String []{getIntent().getStringExtra("CXDH").trim()});
							          		if(cursor.getCount()==0){
							          		  String sqlString="insert into DHCX(DH,BZ1) values (?,?)";
							          		   dbwW.execSQL(sqlString,new String[]{getIntent().getStringExtra("CXDH").trim(),"0"});
							          		}
							          		cursor.close();
							          		dbwW.close();
							          		
			 							 Intent intent1=new Intent();
							     	     intent1.putExtra("result","DownOver");
							     		 setResult(RESULT_OK, intent1);
					    				 finish();
			 						  }
			 						  else{ 
			 							 /*将订单数据写入到数据库里面*/
											String bufString[]=null;
											SQLiteDatabase dbwW =LoginOpenHelper.getInstance(SearchOk.this,log.DB_NAME).getWritableDatabase();
									    	String	sqlString="insert into YD(DH,BAR,BZ1,BZ2,BZ3,BZ4,BZ5,BZ6,BZ7,DQ,NUM1,NUM2) values (?,?,?,?,?,?,?,?,?,?,?,?)";
												 bufString=SYS.splitString(bufx[1].trim(), "@");
										        	String where = "DH= ? AND BAR =?";
						          					String[] whereValue = {barcodeString.toString().trim(),bufString[0].trim()};
						          					dbwW.delete("YD", where,whereValue);

						          					dbwW.execSQL(sqlString, 
					          							new String[]{ barcodeString.toString().trim()
					          							,bufString[0].trim()
					          							,bufString[1].trim()
					          							,bufString[2].trim()
					          							,bufString[3].trim()
					          							,bufString[4].trim()
					          							,bufString[5].trim()
					          							,bufString[6].trim()
					          							,bufString[7].trim()
					          							,bufString[8].trim()
					          							,bufString[9].trim()
					          							,bufString[10].trim()});				
										        	
							          		/*同时更新状态*/ 
							          	    dbwW.close();
			 							  pageNo++;
			 							 sentData(pageNo) ;
			 						  }
			 					  }
							} catch (UnsupportedEncodingException e) {
								// TODO 自动生成的 catch 块
								System.err.println("x错误了");
								e.printStackTrace();
								 sentData(pageNo) ;
							  }
		    			   }else{
		    				   System.err.println(buf.length);
		    				   sentData(pageNo) ;
		    			   }    	    
		    			 }else if(returnCode==2) {
		    				 System.err.println("错误了");
		    				 sentData(pageNo) ;
		    			 } 
			     		  break;
			     	 case 16:
				    	 switch (msg.arg1) {
						case 0:
							 Intent intent1=new Intent();
				     	      intent1.putExtra("result","fail");
				     		  setResult(RESULT_OK, intent1);
		    				  finish();
							break;
						case 1:
							 Intent intent11=new Intent();
				     	      intent11.putExtra("result","fail");
				     		  setResult(RESULT_OK, intent11);
		    				  finish();
							break;
						case 2:
						
							break;	
						case 3:
							isconnect=true;
							sentData(pageNo);
							break;	
						case 7:
							 Intent intent111=new Intent();
				     	      intent111.putExtra("result","fail");
				     		  setResult(RESULT_OK, intent111);
		    				  finish();
							break;
						case 6:  //接收数据超时
							System.err.println("超时了");
							 sentData(pageNo) ;
							break;
						  default:
							break;
						}
				}
		}
				
	};			
}
