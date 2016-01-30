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
 *   ��Ҫ�Ĳ���:
 *  1��  ��ʼ�����֣�����һ���ײ��Ĳ��֣�����Ϊ���ɼ�  
 *  2�� ���������¼����жϻ���֮�󣬽��� ����������Ҫʹ�ýӿڻص��ķ�ʽ,���ײ��Ĳ�����ʾ����
 *  3�� ���֮�����ز����ļ�
 *  
 * 
 * 
 * */
public class MyListview extends ListView implements OnScrollListener{

	View footer;// �ײ����֣�
	int totalCount;// ��������
	int lastVisibleItem;// ���һ���ɼ���item��
	boolean isLoading;// ���ڼ��أ�
	ILoadListener iLoadListener;
	public MyListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO �Զ����ɵĹ��캯�����
		InitView(context);
	}
	public MyListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO �Զ����ɵĹ��캯�����
		InitView(context);
	}
	public MyListview(Context context){
		super(context);
		InitView(context);
		// TODO �Զ����ɵĹ��캯�����
	}
	private void  InitView(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		footer=inflater.inflate(R.layout.my_loadview,null);
		/*�� ��������Ϊ���ɼ�*/
	    footer.findViewById(R.id.myloadview).setVisibility(View.GONE);
	    addFooterView(footer);
	    this.setOnScrollListener(this);
	}
	/*�������*/
	public void isOver(){ 
		isLoading=false;
		footer.findViewById(R.id.myloadview).setVisibility(GONE); //���ײ�������������
	}
	/*����������,��������м���*/
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO �Զ����ɵķ������
		if(lastVisibleItem==totalCount &&scrollState ==SCROLL_STATE_IDLE){
			if(!isLoading){
				isLoading=true;
				footer.findViewById(R.id.myloadview).setVisibility(VISIBLE); //�ײ����ֿɼ�
				iLoadListener.onLoad();
			}
		}
	}
	/*��ȡItem������ ��ȡ�����һ��Item   �������������һ�����ʱ����ʾ�Ѿ��ǻ�������*/
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO �Զ����ɵķ������
		lastVisibleItem=firstVisibleItem+visibleItemCount;
		totalCount=totalItemCount;
	}
	
	public void setInterface(ILoadListener iLoadListener){
		this.iLoadListener = iLoadListener;
	}
	//���ظ������ݵĻص��ӿ�
	public interface ILoadListener{
		public void onLoad();
	}
}
