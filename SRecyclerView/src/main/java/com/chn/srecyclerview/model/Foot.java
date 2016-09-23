package com.chn.srecyclerview.model;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class Foot {
    boolean loading;
    int viewType;

    public Foot(int viewType) {
        this.viewType = viewType;
    }

    public Foot(boolean loading, int viewType) {
        this.loading = loading;
        this.viewType = viewType;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }
}
