package com.gwi.selfplatform.ui.beans;

import com.gwi.selfplatform.common.interfaces.IMethodCallback;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class MainModuleBean {
    private int resDescription;
    private int resDespColor;
    private int resBgColor;
    private int resIcon;
    private int resTips;
    private IMethodCallback<Void> callback;

    /**
     * @param resBgColor     背景颜色
     * @param resDescription 文字描述
     * @param resDespColor   文字描述的颜色
     * @param resIcon        背景左边ICON图标
     * @param resTips        背景右边提示图标
     * @param callback       事件回调
     */
    public MainModuleBean(int resBgColor, int resDescription, int resDespColor, int resIcon, int resTips, IMethodCallback<Void> callback) {
        this.callback = callback;
        this.resBgColor = resBgColor;
        this.resDescription = resDescription;
        this.resDespColor = resDespColor;
        this.resIcon = resIcon;
        this.resTips = resTips;
    }

    public int getResBgColor() {
        return resBgColor;
    }

    public void setResBgColor(int resBgColor) {
        this.resBgColor = resBgColor;
    }

    public int getResDescription() {
        return resDescription;
    }

    public void setResDescription(int resDescription) {
        this.resDescription = resDescription;
    }

    public int getResDespColor() {
        return resDespColor;
    }

    public void setResDespColor(int resDespColor) {
        this.resDespColor = resDespColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getResTips() {
        return resTips;
    }

    public void setResTips(int resTips) {
        this.resTips = resTips;
    }

    public IMethodCallback<Void> getCallback() {
        return callback;
    }

    public void setCallback(IMethodCallback<Void> callback) {
        this.callback = callback;
    }
}
