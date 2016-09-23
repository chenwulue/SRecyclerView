package com.chn.srecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class RecyclerWrapper extends RecyclerView.Adapter {

    private final RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    public RecyclerWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onChanged() {
                RecyclerWrapper.this.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                RecyclerWrapper.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                RecyclerWrapper.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                RecyclerWrapper.this.notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                RecyclerWrapper.this.notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapter.bindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getWrappedAdapter() {
        return mAdapter;
    }
}
