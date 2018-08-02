package com.rixin.cold.domain;

/**
 *  列表数据地JavaBean
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdInfo {

    private String id;  // id
    private String title;   // 标题
    private String picUrl;  // 图片URL
    private String contentUrl;  // 详情URL
    private int readCount;  // 阅读数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    @Override
    public String toString() {
        return id + "-IOU-" + title + "-IOU-" + picUrl + "-IOU-" + contentUrl + "-IOU-" + readCount;
    }

}
