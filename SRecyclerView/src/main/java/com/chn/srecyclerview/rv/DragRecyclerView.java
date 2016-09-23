package com.chn.srecyclerview.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.chn.srecyclerview.R;
import com.chn.srecyclerview.adapter.RecyclerAdapter;
import com.chn.srecyclerview.model.Fill;
import com.chn.srecyclerview.model.Foot;

/**
 * Created by Youga on 2016/2/17.
 */
public class DragRecyclerView extends RecyclerView {
    private static final String TAG = "DragRecyclerView";
    private int mRequestCount = 10;
    private Context mContext;

    public DragRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public DragRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RecyclerAdapter adapter = (RecyclerAdapter) DragRecyclerView.this.getAdapter();
                if (adapter != null)
                    adapter.setLayoutParams(DragRecyclerView.this.getWidth(), DragRecyclerView.this.getHeight());
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        setAdapter(adapter, false);
    }

    public void setAdapter(Adapter adapter, boolean loadMore) {
        setAdapter(adapter, loadMore, null);
    }

    public void setAdapter(final Adapter adapter, boolean loadMore, LayoutManager layout) {
        super.setAdapter(new RecyclerAdapter(adapter, mContext, loadMore));
        if (layout == null) {
            setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            setLayoutManager(layout);
        }
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layout;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override

                public int getSpanSize(int position) {
                    return position == adapter.getItemCount() ? manager.getSpanCount() : 1;
                }
            });
        }
        RecyclerAdapter recyclerAdapter = (RecyclerAdapter) getAdapter();
        recyclerAdapter.setLayoutManager(layout);
    }

    public void setAdapter(final Adapter adapter, boolean loadMore, LayoutManager layout, int footLayoutId, int fillLayoutId) {

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(adapter, mContext, loadMore);
        recyclerAdapter.setFootLayoutId(footLayoutId);
        recyclerAdapter.setFillLayoutId(fillLayoutId);

        super.setAdapter(recyclerAdapter);
        if (layout == null) {
            setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            setLayoutManager(layout);
        }
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layout;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override

                public int getSpanSize(int position) {
                    return position == adapter.getItemCount() ? manager.getSpanCount() : 1;
                }
            });
        }
//        RecyclerAdapter recyclerAdapter = (RecyclerAdapter) getAdapter();
        recyclerAdapter.setLayoutManager(layout);
    }


    public void setRequestCount(int requestCount) {
        this.mRequestCount = requestCount;
    }

    public void showLoadingView() {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.showView(new Fill(null, RecyclerAdapter.TYPE_LOADING, 0));
    }

    public void showEmptyView(@NonNull String emptyTips) {
        showEmptyView(emptyTips, R.mipmap.empty);
    }

    public void showEmptyView(@NonNull String emptyTips, int resId) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.showView(new Fill(emptyTips, RecyclerAdapter.TYPE_EMPTY, resId));
    }

    public void showErrorView(@NonNull String errorTips) {
        showErrorView(errorTips, R.mipmap.error);
    }

    public void showErrorView(@NonNull String errorTips, int resId) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.showView(new Fill(errorTips, RecyclerAdapter.TYPE_ERROR, resId));
    }

    //method nothing do
    @Deprecated
    public void showItemView() {

    }

    public void onDragState(int resultCount) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        if (resultCount < 0) {
            adapter.showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_FAULT));
        } else if (resultCount >= 0 && resultCount < mRequestCount) {
//            adapter.showFoot(null);
            adapter.showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_OVER));

        } else {
            adapter.showFoot(new Foot(RecyclerAdapter.TYPE_FOOT_LOAD));
        }
    }


    public void setOnStateClickListener(OnStateClickListener onStateClickListener) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.setOnStateClickListener(onStateClickListener);
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.setOnPullListener(onPullListener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        RecyclerAdapter adapter = (RecyclerAdapter) getAdapter();
        adapter.setOnItemClickListener(onItemClickListener);
    }
}
