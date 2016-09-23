package com.chn.srecyclerview.model;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class Fill {

    String tips;
    int viewType;
    int iconId;

    public Fill(String tips, int viewType, int iconId) {
        this.tips = tips;
        this.viewType = viewType;
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTips() {
        return tips;
    }

    public int getViewType() {
        return viewType;
    }
}
