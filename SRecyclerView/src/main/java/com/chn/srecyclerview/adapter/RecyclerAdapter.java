package com.chn.srecyclerview.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.view.ViewGroup.LayoutParams;

import com.chn.srecyclerview.R;
import com.chn.srecyclerview.rv.DragRecyclerView.OnItemClickListener;
import com.chn.srecyclerview.model.Fill;
import com.chn.srecyclerview.model.Foot;
import com.chn.srecyclerview.rv.OnPullListener;
import com.chn.srecyclerview.rv.OnStateClickListener;


/**
 * Created by Youga on 2015/9/2.
 */
public class RecyclerAdapter extends RecyclerWrapper {

    private static final String TAG = "RecyclerAdapter";
    //是否再加更多
    private final boolean mLoadMore;
    private RecyclerView.LayoutManager mLayoutManager;
    //是否还有更多数据
    private boolean mShouldMore;
    private final Context mContext;
    private DisplayMetrics mMetrics;

    public static final int TYPE_LOADING = 160809001;
    public static final int TYPE_EMPTY = 160809002;
    public static final int TYPE_ERROR = 160809003;
    public static final int TYPE_FOOT_LOAD = 160809004;//正在加载中
    public static final int TYPE_FOOT_FAULT = 160809005;//加载失败，点击会重新触发
    public static final int TYPE_FOOT_OVER = 160809006;//加载完全

    private OnStateClickListener onStateClickListener;
    private OnPullListener mOnPullListener;

    private OnItemClickListener mOnItemClickListener;
    private LayoutParams mLayoutParams;
    private RecyclerView.Adapter mAdapter;
    //填充对象
    private Fill mFill;
    //foot对象
    private Foot mFoot;

    int footLayoutId;
    int fillLayoutId;


    public RecyclerAdapter(@NonNull RecyclerView.Adapter adapter, Context context,
                           boolean loadMore) {
        super(adapter);
        mContext = context;
        mMetrics = context.getResources().getDisplayMetrics();
        mLoadMore = loadMore;
        mAdapter = adapter;
    }

    public void setFootLayoutId(int footLayoutId) {
        this.footLayoutId = footLayoutId;
    }

    public void setFillLayoutId(int fillLayoutId) {
        this.fillLayoutId = fillLayoutId;
    }

    @Override
    public int getItemCount() {


        if (mFill != null) {
            return 1;
        } else {
            return mLoadMore & mShouldMore ? mAdapter.getItemCount() + 1 : mAdapter.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mFill != null) {
            return mFill.getViewType();
        } else if (position == mAdapter.getItemCount()) {
            return mFoot.getViewType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING || viewType == TYPE_EMPTY || viewType == TYPE_ERROR) {
            return new FillViewHolder(View.inflate(parent.getContext(), fillLayoutId == 0 ? R.layout.def_fill_view : fillLayoutId, null));
        } else if (viewType == TYPE_FOOT_LOAD || viewType == TYPE_FOOT_FAULT || viewType == TYPE_FOOT_OVER) {
            return new FootViewHolder(View.inflate(parent.getContext(), footLayoutId == 0 ? R.layout.def_foot_view : footLayoutId, null));
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FillViewHolder) {
            LayoutParams params = getLayoutParams();
            if (params != null) holder.itemView.setLayoutParams(params);
            FillViewHolder fillHolder = (FillViewHolder) holder;
            fillHolder.bindView(mFill);
        } else if (holder instanceof FootViewHolder) {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, mMetrics);
            FootViewHolder footHolder = (FootViewHolder) holder;
            footHolder.bindView(mFoot);
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(getLayoutParams().width, height);
                params.setFullSpan(true);
                holder.itemView.setLayoutParams(params);
            } else {
                LayoutParams params = new LayoutParams(getLayoutParams().width, height);
                holder.itemView.setLayoutParams(params);
            }
        } else {
            if (mOnItemClickListener != null)
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                    }
                });
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    public void showView(Fill fill) {
        mFill = fill;
        mShouldMore = true;
        notifyDataSetChanged();
        getWrappedAdapter().notifyDataSetChanged();
    }

    public void showFoot(Foot foot) {
        mFill = null;
        mFoot = foot;
        if (foot == null) {
            mShouldMore = false;
        } else {
            mShouldMore = true;
        }
        notifyDataSetChanged();
        getWrappedAdapter().notifyDataSetChanged();
    }

    public Foot getmFoot() {
        return mFoot;
    }

    public class FillViewHolder extends RecyclerView.ViewHolder {

//        private final TextView mTipsView;
//        private final View mPbLoading;


        private View mViewEmpty, mViewFail, mViewProgress;


        public FillViewHolder(View itemView) {
            super(itemView);

            mViewEmpty = itemView.findViewById(R.id.srv_fill_empty);
            mViewFail = itemView.findViewById(R.id.srv_fill_fail);
            mViewProgress = itemView.findViewById(R.id.srv_fill_progress);


            mViewFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStateClickListener.onStateClick();
                }
            });
            mViewEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStateClickListener.onStateClick();
                }
            });


//            mTipsView = (TextView) itemView.findViewById(R.id.tips_view);
//            mPbLoading = itemView.findViewById(R.id.pb_loading);
//            mTipsView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onStateClickListener.onStateClick();
//                }
//            });
        }

        public void bindView(Fill fill) {


            setViewVisible(fill);


//            if (fill.getViewType() == TYPE_LOADING) {
//                mPbLoading.setVisibility(View.VISIBLE);
//                mTipsView.setVisibility(View.INVISIBLE);
//            } else {
//                mPbLoading.setVisibility(View.INVISIBLE);
//                mTipsView.setVisibility(View.VISIBLE);
//                Drawable top = mContext.getResources().getDrawable(fill.getIconId());
//                if (top != null)
//                    top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
//                mTipsView.setCompoundDrawables(null, top, null, null);
//                mTipsView.setText(fill.getTips());
//            }
        }


        private void setViewVisible(Fill fill) {
            mViewEmpty.setVisibility(fill.getViewType() == TYPE_EMPTY ? View.VISIBLE : View.INVISIBLE);
            mViewFail.setVisibility(fill.getViewType() == TYPE_ERROR ? View.VISIBLE : View.INVISIBLE);
            mViewProgress.setVisibility(fill.getViewType() == TYPE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }


    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mViewLoading, mViewFault, mViewOver;

        public FootViewHolder(View itemView) {
            super(itemView);
            mViewLoading = (LinearLayout) itemView.findViewById(R.id.srv_foot_loading);
            mViewFault = (LinearLayout) itemView.findViewById(R.id.srv_foot_fail);
            mViewOver = (LinearLayout) itemView.findViewById(R.id.srv_foot_over);
        }

        public void bindView(final Foot foot) {
            setViewVisible(foot);
            if (foot.getViewType() == TYPE_FOOT_LOAD && !foot.isLoading()) {
                foot.setLoading(true);
                mOnPullListener.onLoadMore();
            }
            mViewFault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!foot.isLoading()) {//当外部的下拉刷新进行的时候，需要设置为true
                        foot.setLoading(true);
                        foot.setViewType(TYPE_FOOT_LOAD);//更改foot的状态
                        setViewVisible(foot);
                        mOnPullListener.onLoadMore();
                    }
                }
            });
        }

        private void setViewVisible(Foot foot) {
            mViewOver.setVisibility(foot.getViewType() == TYPE_FOOT_OVER ? View.VISIBLE : View.INVISIBLE);
            mViewFault.setVisibility(foot.getViewType() == TYPE_FOOT_FAULT ? View.VISIBLE : View.INVISIBLE);
            mViewLoading.setVisibility(foot.getViewType() == TYPE_FOOT_LOAD ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setOnStateClickListener(OnStateClickListener onStateClickListener) {
        this.onStateClickListener = onStateClickListener;
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        this.mOnPullListener = onPullListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public void setLayoutParams(int width, int height) {
        if (mLayoutParams == null) {
            mLayoutParams = new LayoutParams(width, height);
            notifyDataSetChanged();
            getWrappedAdapter().notifyDataSetChanged();
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }
}
