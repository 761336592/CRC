package com.example.CRC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import android.os.Environment;

public class WriteReadSD
{
	//SYS sys=new SYS();
	public static String read(String FILE_NAME,File dir)
	{
		String string="";
		char [] buf=new char[1024];
		try
		{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				// 获取指定文件的对应输入流
				File file=new File(dir,  FILE_NAME) ;
				if(!file.exists()){
					System.out.println("没有文件");
					return string;
				}
				FileInputStream fis = new FileInputStream(
						file);
			    
				StringBuilder sb=new StringBuilder("");
				int count=0;
				InputStreamReader reader=new InputStreamReader(fis,"GBK");
				//int count=0;
				while((count=reader.read(buf))>0){
					sb.append(SYS.getInfoBuff(buf, count));
					buf=new char[1024];
				}
				string=sb.toString().trim();
				
			}
		} catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			string="";
		}
		return string;
	}
	public static String readline(String FILE_NAME,File dir)
	{
		String string="";
		byte [] buf=new byte[1024];
		try
		{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				// 获取指定文件的对应输入流
				File file=new File(dir,  FILE_NAME) ;
				if(!file.exists()){
					System.out.println("没有文件");
					return string;
				}
				RandomAccessFile raf=new RandomAccessFile(file,"rw");
				//将指针移动到最后
				raf.seek(0);
				raf.read(buf, 0,150);
				string=new String(buf, "GBK").trim();
				System.out.println(string);
			}
		} catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			string="";
		}
		return string;
	}
	public static void write(String content , String FILE_NAME,File dir )
	{
		try
		{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				File targetFile=new File (dir,FILE_NAME);
				RandomAccessFile raf=new RandomAccessFile(targetFile,"rw");
				//将指针移动到最后
				raf.seek(targetFile.length());
				//输入文件内容
				raf.write(content.getBytes());
				raf.close();
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void writex(String content , String FILE_NAME,File dir){
		try
		{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				
				File file=new File(dir,FILE_NAME);
				OutputStream out=new FileOutputStream(file);  
		        out.write(content.getBytes());  
		        out.close();
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
