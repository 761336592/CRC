package com.example.CRC;


import android.R.raw;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

//	public static String[] img_text = {"入库扫描","出库扫描","盘点扫描","资料更新","库存查询","扫描设置","账号设置","网络设置","导入资料","清空数据","关于我们","退出" };
	public int[] imgs = {
		   R.drawable.app_fund,
		   
		   R.drawable.app_phonecharge,
		   R.drawable.app_creditcard,
		   R.drawable.app_assign,
		  
		   R.drawable.app_essential,
		   R.drawable.app_creditcard,
		   R.drawable.app_exchange,
		   R.drawable.app_freewifi,
	};
	public static int [] img_text = {R.string.CKtital,R.string.SMSZ,R.string.ZHGL,R.string.WLSZ,R.string.qksj,R.string.smtt,R.string.GYJXC,R.string.TCQ };
	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
  
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}
	

}
