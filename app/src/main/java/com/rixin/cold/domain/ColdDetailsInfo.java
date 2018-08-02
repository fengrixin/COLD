package com.rixin.cold.domain;

/**
 * 详情页的JavaBean
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdDetailsInfo {

    private String title;  // 标题
    private String picUrl;  // 图片URL
    private String pContent; // 内容
    private String prevUrl;  // 上一篇URL
    private String nextUrl;  // 下一篇URL
    private int read; // 阅读数

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getpContent() {
        return pContent;
    }

    public void setpContent(String pContent) {
        this.pContent = pContent;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(String prevUrl) {
        this.prevUrl = prevUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return title + "-IOU-" + picUrl + "-IOU-" + prevUrl + "-IOU-" + prevUrl + "-IOU-" + nextUrl + "-IOU-" + read;
    }
}
