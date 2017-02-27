package com.rixin.cold.domain;

/**
 *  列表数据地JavaBean
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdInfo {

    public String title;   // 标题
    public String picUrl;  // 图片URL
    public String contentUrl;  // 详情URL
    public int readCount;  // 阅读数
    public int starCount;  // 赞数

    @Override
    public String toString() {
        return title + "-IOU-" + picUrl + "-IOU-" + contentUrl + "-IOU-" + readCount + "-IOU-" + starCount;
    }

}
