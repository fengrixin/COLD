package com.rixin.cold.bean;

/**
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdInfo {

    public String title;   // 标题
    public String picUrl;  // 图片URL
    public String content;  // 部分详情
    public String readCount;  // 阅读数
    public String starCount;  // 赞数
    public String contentUrl;  //详情URL

    @Override
    public String toString() {
        return "ColdInfo{" +
                "title='" + title + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", content='" + content + '\'' +
                ", readCount='" + readCount + '\'' +
                ", starCount='" + starCount + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }

}
