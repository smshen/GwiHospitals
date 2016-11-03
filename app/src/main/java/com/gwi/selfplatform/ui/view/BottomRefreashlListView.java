package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.gwi.phr.hospital.R;

/**
 * 底部刷新expandablelistView
 * @author 彭毅
 *
 */
public class BottomRefreashlListView extends ExpandableListView{
    
    private boolean mIsRefreshing=false;
    private FooterViewProxy mFooterProxy;
    private boolean mNeedRefreshing = true;
    
private OnScrollListener mOnScrollListener = new OnScrollListener() {
        
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            
        }
        
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            //到达底部开始加载
            if(firstVisibleItem+visibleItemCount==totalItemCount) {
                if(!mIsRefreshing&&mNeedRefreshing) {
                    if(mFooterProxy!=null) {
                        if(mOnRefreshListener!=null) {
                            mFooterProxy.startLoading();
                            mOnRefreshListener.onRefresh();
                            mIsRefreshing = true;
                        }
                    }
                }
            }
        }
    };

    public BottomRefreashlListView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BottomRefreashlListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomRefreashlListView(Context context) {
        super(context);
        init();
    }
    
    
    private void init() {
        setOnScrollListener(mOnScrollListener);
        mFooterProxy = new FooterViewProxy();
        addFooterView(mFooterProxy.getFootView());
    }
    
    public void isNeedRefresh(boolean isNeeded) {
        mNeedRefreshing = isNeeded;
    }
    

    
    
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
    
    public FooterViewProxy getFooterProxy() {
        return mFooterProxy;
    }

    private OnRefreshListener mOnRefreshListener;
    
    /**
     * 刷新接口
     * @author 彭毅
     *
     */
    public interface OnRefreshListener {
        /**
         * 底部刷新时调用，刷新完成需要调用{@link BottomRefreashlListView#refreshComplete()}
         */
        void onRefresh();
    }
    
    /**
     * 刷新完成，更新footView视图
     */
    public void refreshComplete() {
        mFooterProxy.showLoadMore();
        mIsRefreshing = false;
    }
    
    /**
     * 底部view代理类
     * @author 彭毅
     *
     */
    public class FooterViewProxy implements OnClickListener{
        private View root;
        private View loadingView;
        private Button loadMore;
//        private TextView loadingContent;
        
        public FooterViewProxy() {
            root = View.inflate(getContext(), R.layout.layout_bottom_refresh_footer, null);
            loadingView = root.findViewById(R.id.bottom_footer_container_loading);
            loadMore = (Button) root.findViewById(R.id.bottom_footer_loading_more);
            loadMore.setOnClickListener(this);
        }
        
        public View getFootView() {
            return root;
        }
        
        public void startLoading() {
            root.setVisibility(View.VISIBLE);
            loadMore.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        }
        
        public void showLoadMore(){
            root.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            loadMore.setVisibility(View.VISIBLE);
            loadMore.setText("点击加载更多");
            loadMore.setEnabled(true);
        }
        
        public void showReTry() {
            root.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            loadMore.setVisibility(View.VISIBLE);
            loadMore.setText("网络未连接，点击重试");
            loadMore.setEnabled(true);
        }
        
        public void hideAllViews() {
            root.setVisibility(View.GONE);
//            loadingView.setVisibility(View.GONE);
//            loadMore.setVisibility(View.GONE);
        }
        
        /**
         * 显示数据加载完毕信息
         */
        public void showEndMsg() {
            loadingView.setVisibility(View.GONE);
            loadMore.setVisibility(View.VISIBLE);
            loadMore.setText("已加载完毕~");
            loadMore.setEnabled(false);
        }

        @Override
        public void onClick(View v) {
            mOnRefreshListener.onRefresh();
            startLoading();
        }
    }

}
