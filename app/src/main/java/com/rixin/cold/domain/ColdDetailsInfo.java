package com.rixin.cold.domain;

/**
 * 详情页的JavaBean
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdDetailsInfo {

    public String title;  // 标题
    public String picUrl;  // 图片URL
    public String pContent; // 内容
    public String prevUrl;  // 上一篇URL
    public String nextUrl;  // 下一篇URL
    public int read; // 阅读数
    public int star; // 赞数

    @Override
    public String toString() {
        return title + "-IOU-" + picUrl + "-IOU-" + pContent + "-IOU-" + prevUrl + "-IOU-" + nextUrl + "-IOU-" + read + "-IOU-" + star;
    }
}
