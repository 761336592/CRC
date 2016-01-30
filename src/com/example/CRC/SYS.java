/*主要是用来存放一些常用的公共函数*/
package com.example.CRC;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.ScrollView;
import android.widget.TextView;


/*将文件保存在*/
public class SYS extends Activity{
	//public OutputStream os;
	public static final String bm = "GBK";
	String Text_of_output="";
public static	int crc_r(byte[] bs,int length) {
		
		int num1 = 0;
		int i = 0;
		num1 = 0;
		for (i = 0; i <length; i++) {
				num1 = num1^bs[i];
		}
		 return  num1;
}
public static	int crc_t( byte[] bs,int length) {
	
	int num1 = 0;
	int i = 0;
	num1 = 0;
    int num2=0;
	for (i = 0; i <length; i++) {
		   if(bs[i]<0){
			   num2=bs[i]+256;
		   }else{
			   num2=bs[i];
		   }
		num1 = num1+num2;
	}
	 return  num1%50+0x30;
}	

	/* 字符串分割函数 待分割的字符串 分割符，这个比较占内存 */
public static String[] splitString(String str, String sdelimiter) {
	String arry[]=null;
   try{	
	   arry= str.split(sdelimiter,-1);
   }catch (Exception e){
	   arry=null;
   }
	if(arry.length>0)
	   return arry;
	else{
		return null;
	}
}
/*新的字符串分割函数*/	
public static List<String> NewsplitString(String str, String sdelimiter) {
	StringTokenizer token = new StringTokenizer(str,sdelimiter);
	if(token.countTokens()==0){
		 System.out.println("***-");
		return null;
	}else{
		List<String> arry=new ArrayList<String>(); 
		int i=0;
		while( token.hasMoreElements()){
			     arry.add(token.nextToken());
	    }
		return 	arry;
	}	
}
public static List<String> NewsplitString1(String str, String sdelimiter) {
	List<String> arryList=new ArrayList<String>();
	String temp=str;//定义一个 字符串变量，把str赋给他，保持str字符串不变
	char [] string=new char[512];
	System.out.println(str);
	int k=0;
	int j=0;
	for(int i=0;i<temp.length();i++){
		if(temp.toCharArray()[i]!=0x09){
			string[k++]=temp.toCharArray()[i];
		}else{
			j++;
			String string2="";
			string2=getInfoBuff(string,k);
			if(string2.length()==0){
				string2="     ";  
			}
			arryList.add(string2);
			//System.out.println("读到的数据是:"+string2);
			k=0;
			string=new char[512];
		}
		
	}
	return arryList;
}
public void ReadSD(File f){
		char [] buf=new char[1024];
		Text_of_output="";
		
		 try {
			FileInputStream inputStream=new FileInputStream(f);
			InputStreamReader reader=new InputStreamReader(inputStream);
			int count=0;
			while((count=reader.read(buf))>0){
				Text_of_output=Text_of_output+getInfoBuff(buf, count);
				buf=new char[1024];
			}
			//sys.disPlay(sView, dis, Text_of_output);
		} catch (FileNotFoundException e){
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e){
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void My_WriteFile(String name,String string,Context c,OutputStream os){
		try{
			   os=c.openFileOutput(name,MODE_PRIVATE);//
			   os.write(string.getBytes());
		}catch(FileNotFoundException e){	
		}
		catch (Exception e1) {
			// TODO: handle exception
		}finally{
			try {
				  os.close();
			} catch (Exception e) {
				
			}
		}
	}

	/*读取文件*/
	
	public  String  My_ReadFile(InputStream i,String name,Context c) {
		String s="";
		try {
			
			  i=c.openFileInput(name);
			 byte[] b=new byte[2048];
			  int length=i.read(b);
			  s=new String(b);
			  return s;
		}catch(FileNotFoundException e){
			//return 1;
		} 
		catch (Exception e2){
			//return 1;
			// TODO: handle exception
		}finally{
			 try {
				    i.close();
				 //   return 0;
			} catch (Exception e) {
			
				//return 1;
				// TODO: handle exception
			}
		}
		return s;
	}
 public  static String SenData(String barcode,String name,String androidID){
		  String string="";
		  string= "talkandroid-"+name+"□"+androidID+ "□" + barcode+"□";
		  int len;
		try {
			len = crc_r(string.getBytes(bm),string.getBytes(bm).length);
			len = len % 99;
			String s ="";
			s=String.format("%02d",len);
			string = string + "■" + s + "■";
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
		return string;
	  }
	/*发送函数*/
	  public  static String SenData(String barcode,String name,String androidID,int running){
		  String string="";
		  String buf="";
		  buf="M"+String.format("%02d",running)+"RT,"+barcode;
		  string= "talkandroid-"+ name+"□" +androidID  + "□" + buf+ "\r"+"□";
		  int len;
		try {
			len = crc_r(string.getBytes(bm),string.getBytes(bm).length);
			len = len % 99;
			String s ="";
			s=String.format("%02d",len);
			string = string + "■" + s + "■" + "\n";
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
		return string;
	  }
	  public static  String getInfoBuff(char[] buff, int count) {
			
			char[] temp = new char[count];
			for(int i=0; i<count; i++)
			{
				temp[i] = buff[i];
			}
			return new String(temp);
	}
	
	  /*显示函数*/
	  public static void disPlay(ScrollView s, TextView t,String str){
		  t.append(str);
		  s.scrollTo(0, t.getMeasuredHeight());
		  
	  }
	 /*判断接收到的蓝牙数据是否是正确的*/
	  public static String  BTistrue(String buf) throws UnsupportedEncodingException{
		   
		  String [] str=null;
		  String st="";
		  String bufx[] =null;
		    str=splitString(buf,"■");
		    if(str.length==0){
		    	return st;
		    }else{
		    	int len=crc_r(str[0].getBytes(bm), str[0].getBytes(bm).length);
		    	len = len % 99;
				String s=String.format("%02d",len);
				if(s.equals(str[1].trim())){
					 bufx=splitString(str[0].trim(), "□");
					 if(bufx[1].equals("BT")){
							st=bufx[2].trim();
					}
				}
		    }
		    return st;
		   
	  }
	  public static String  BTisPOWER(String buf) throws UnsupportedEncodingException{
		  String [] str=null;
		  String st="";
		  String bufx[] =null;
		    str=splitString(buf,"■");
		    if(str.length==0){
		    	return st;
		    }else{
		    	int len=crc_r(str[0].getBytes(bm), str[0].getBytes(bm).length);
		    	len = len % 99;
				String s=String.format("%02d",len);
				System.out.println("输出是:"+str[1].trim());
				if(s.equals(str[1].trim())){
					 bufx=splitString(str[0].trim(),"□");
					 if(bufx[1].equals("POWER")){
							st=bufx[2].trim();
					}
				}else{
					st="";
				}
		    }
		    return st;
		   
	  }
	  /*将字符串转换成16进制*/
	 public byte[] getHexBytes(String message) {
	        int len = message.length() / 2;
	        char[] chars = message.toCharArray();
	        String[] hexStr = new String[len];
	        byte[] bytes = new byte[len];
	        for (int i = 0, j = 0; j < len; i += 2, j++) {
	            hexStr[j] = "" + chars[i] + chars[i + 1];
	            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
	        }
	        return bytes;
	    }
	 File initData() throws IOException{
		File sdCard=Environment.getExternalStorageDirectory();
		File dir=new File(sdCard.getAbsolutePath()+"/JXC");
		if(!dir.exists()){
			dir.mkdir();
		}
		return dir;

	}
   /*检测子字符串*/
	 public static  boolean indexofString(String str,String dest){
		 boolean flag = false;
		try{
		if(str.indexOf(dest)>=0)
			  flag=true;
		}catch (Exception e){
			flag = false;
		}
		return flag;

	 }
}