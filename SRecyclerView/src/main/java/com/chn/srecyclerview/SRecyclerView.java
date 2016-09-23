package com.chn.srecyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import com.chn.srecyclerview.adapter.RecyclerAdapter;
import com.chn.srecyclerview.model.Foot;
import com.chn.srecyclerview.rv.DragRecyclerView;
import com.chn.srecyclerview.rv.OnPullListener;
import com.chn.srecyclerview.rv.OnStateClickListener;

/**
 * Created by WulueChen on 2016/9/22.
 */

public class SRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener, OnPullListener, OnStateClickListener {


    SwipeRefreshLayout swipeRefreshLayout;
    DragRecyclerView dragRecyclerView;
    RecyclerAdapter recyclerAdapter;
    RecyclerView.Adapter innerAdapter;
    OnRequestListener onRequestListener;

    final int REQUEST_STATE = 1, REQUEST_REFRESH = 2, REQUEST_LOADMORE = 3;
    final int STATE_ERROR = 1, STATE_EMPTY = 2, STATE_PROGRESS = 3;

    int footLayoutId;
    int fillLayoutId;
    boolean showOver;
    int spanCount;
    int orientation;
    RecyclerView.LayoutManager layoutManager;

    final int LAYOUT_LINEAR = 1, LAYOUT_GRID = 2, LAYOUT_STAGGEREDGRID = 3;
    final int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;


    public SRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public SRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_swipe_recycler, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_sr_swipe);
        setColorSchemeResources(R.color.holo_green_light, R.color.holo_red_light, R.color.holo_blue_light, R.color.holo_orange_light);
        dragRecyclerView = (DragRecyclerView) findViewById(R.id.layout_sr_rv);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SRecyclerView);
            footLayoutId = typedArray.getResourceId(R.styleable.SRecyclerView_srv_footLayoutId, R.layout.def_foot_view);
            fillLayoutId = typedArray.getResourceId(R.styleable.SRecyclerView_srv_fillLayoutId, R.layout.def_fill_view);
            showOver = typedArray.getBoolean(R.styleable.SRecyclerView_srv_showOver, false);
            spanCount = typedArray.getInteger(R.styleable.SRecyclerView_srv_spanCount, 1);
            int orientationValue = typedArray.getInteger(R.styleable.SRecyclerView_srv_orientation, ORIENTATION_VERTICAL);//默认垂直的
            if (orientationValue == ORIENTATION_HORIZONTAL) {
                orientation = OrientationHelper.HORIZONTAL;
            } else {
                orientation = OrientationHelper.VERTICAL;
            }
            int layoutManagerValue = typedArray.getInteger(R.styleable.SRecyclerView_srv_layoutManager, LAYOUT_LINEAR);
            if (layoutManagerValue == LAYOUT_LINEAR) {
                layoutManager = new LinearLayoutManager(context, orientation, false);
            } else if (layoutManagerValue == LAYOUT_GRID) {
                layoutManager = new GridLayoutManager(context, spanCount, orientation, false);
            } else if (layoutManagerValue == LAYOUT_STAGGEREDGRID) {
                layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
            }
            typedArray.recycle();
        }
    }

    public void setColorSchemeResources(@ColorRes int... colors) {
        swipeRefreshLayout.setColorSchemeResources(colors);
    }


    /**
     * 三个设置适配器最终都调用了DragRecyclerView的setAdapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, true);
    }

    public void setAdapter(RecyclerView.Adapter adapter, boolean loadMore) {
        setAdapter(adapter, loadMore, layoutManager);
    }

    public void setAdapter(final RecyclerView.Adapter adapter, boolean loadMore, RecyclerView.LayoutManager layout) {
        innerAdapter = adapter;
        dragRecyclerView.setAdapter(innerAdapter, loadMore, layout, footLayoutId, fillLayoutId);
        if (innerAdapter.getItemCount() == 0) {
            dragRecyclerView.showLoadingView();
            swipeRefreshLayout.setEnabled(false);
        }
    }


    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
        swipeRefreshLayout.setOnRefreshListener(this);
        dragRecyclerView.setOnPullListener(this);
        dragRecyclerView.setOnStateClickListener(this);
    }

    public RecyclerAdapter getRecyclerAdapter() {
        if (recyclerAdapter == null) {
            return (RecyclerAdapter) dragRecyclerView.getAdapter();
        }
        return recyclerAdapter;
    }


    public void setHadNextPage(boolean hadNextPage) {

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        if (hadNextPage) {
            getRecyclerAdapter().showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_LOAD));
        } else {

            if (innerAdapter.getItemCount() == 0) {//本身adapter的数目为0，那么显示emptyView
                showView(STATE_EMPTY);
            } else {
                if (showOver) {
                    getRecyclerAdapter().showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_OVER));
                } else {
                    getRecyclerAdapter().showFoot(null);
                }
            }
        }
    }

    public void loadMoreFail() {//加载更多的错误
        swipeRefreshLayout.setEnabled(true);
        getRecyclerAdapter().showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_FAULT));
    }

    public void stateFail() {//刷新错误，不是下拉刷新错误
        showView(STATE_ERROR);
    }


    public void showView(int state) {
        swipeRefreshLayout.setEnabled(false);
        switch (state) {
            case STATE_ERROR:
                dragRecyclerView.showErrorView("网络连接错误");
                break;
            case STATE_EMPTY:
                dragRecyclerView.showEmptyView("神马都没有");
                break;
            case STATE_PROGRESS:
                dragRecyclerView.showLoadingView();
                break;
        }

    }


    public DragRecyclerView getRecyclerView() {
        return dragRecyclerView;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        dragRecyclerView.addItemDecoration(decor);
    }

    public void refreshFail() {//下拉刷新的错误，需要恢复原状态
        swipeRefreshLayout.setRefreshing(false);
        getRecyclerAdapter().showFoot(oldFoot);
    }


    public void requestFail() {
        switch (requestType) {
            case REQUEST_REFRESH:
                refreshFail();
                break;
            case REQUEST_LOADMORE:
                loadMoreFail();
                break;
            case REQUEST_STATE:
                stateFail();
                break;
        }
    }

    Foot oldFoot;
    int requestType = REQUEST_STATE;

    @Override
    public void onRefresh() {
        oldFoot = getRecyclerAdapter().getmFoot();//记录下拉刷新前的状态

        if (oldFoot != null /*&& oldFoot.getViewType() == RecyclerAdapter.TYPE_FOOT_LOAD*/) {//如果是还有更多的，关闭

            if (oldFoot.getViewType() == RecyclerAdapter.TYPE_FOOT_LOAD) {
                getRecyclerAdapter().showFoot(null);
            } else if (oldFoot.getViewType() == RecyclerAdapter.TYPE_FOOT_FAULT) {//设置成加载中
                Log.e("onRefresh", "setTrue");
//                getRecyclerAdapter().getmFoot().setLoading(true);
                getRecyclerAdapter().showFoot(new Foot(true, RecyclerAdapter.TYPE_FOOT_FAULT));
            }

        }
        requestType = REQUEST_REFRESH;
        onRequestListener.onRefresh();
    }

    @Override
    public void onLoadMore() {
        swipeRefreshLayout.setEnabled(false);
        requestType = REQUEST_LOADMORE;
        onRequestListener.onLoadMore();
    }

    @Override
    public void onStateClick() {
        showView(STATE_PROGRESS);
        onRequestListener.onStateClick();
    }


    public interface OnRequestListener {
        void onRefresh();

        void onLoadMore();

        void onStateClick();
    }
}
