package myview;

import com.example.CRC.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
/*   
 *   主要的步骤:
 *  1、  初始化布局，加载一个底部的布局，设置为不可见  
 *  2、 监听滑动事件，判断滑动之后，进行 操作，这里要使用接口回调的方式,将底部的布局显示出来
 *  3、 完成之后隐藏布局文件
 *  
 * 
 * 
 * */
public class MyListview extends ListView implements OnScrollListener{

	View footer;// 底部布局；
	int totalCount;// 总数量；
	int lastVisibleItem;// 最后一个可见的item；
	boolean isLoading;// 正在加载；
	ILoadListener iLoadListener;
	public MyListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
		InitView(context);
	}
	public MyListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		InitView(context);
	}
	public MyListview(Context context){
		super(context);
		InitView(context);
		// TODO 自动生成的构造函数存根
	}
	private void  InitView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		footer=inflater.inflate(R.layout.my_loadview,null);
		/*将 布局设置为不可见*/
	    footer.findViewById(R.id.myloadview).setVisibility(View.GONE);
	    addFooterView(footer);
	    this.setOnScrollListener(this);
	}
	/*加载完毕*/
	public void isOver(){ 
		isLoading=false;
		footer.findViewById(R.id.myloadview).setVisibility(GONE); //将底部布局隐藏起来
	}
	/*滑动监听的,在这里进行加载*/
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		if(lastVisibleItem==totalCount &&scrollState ==SCROLL_STATE_IDLE){
			if(!isLoading){
				isLoading=true;
				footer.findViewById(R.id.myloadview).setVisibility(VISIBLE); //底部布局可见
				iLoadListener.onLoad();
			}
		}
	}
	/*获取Item的总数 获取到最后一个Item   当总数量跟最后一个相等时，表示已经是滑动过了*/
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根
		lastVisibleItem=firstVisibleItem+visibleItemCount;
		totalCount=totalItemCount;
	}
	
	public void setInterface(ILoadListener iLoadListener){
		this.iLoadListener = iLoadListener;
	}
	//加载更多数据的回调接口
	public interface ILoadListener{
		public void onLoad();
	}
}
