package com.rixin.cold.bean;

/**
 * Created by 飘渺云轩 on 2017/2/13.
 */

public class ColdDetailsInfo {

    public String title;  // 标题
    public String picUrl;  // 图片URL
    public StringBuffer pContent;
//    public ArrayList<String> pContent;  // 详情P标签内的内容

    @Override
    public String toString() {
        return "ColdDetailsInfo{" +
                "title='" + title + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", pContent=" + pContent +
                '}';
    }
}
