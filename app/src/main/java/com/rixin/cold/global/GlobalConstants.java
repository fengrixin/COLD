package com.rixin.cold.global;

/**
 * 访问数据的接口
 * Created by 飘渺云轩 on 2017/2/21.
 */

public class GlobalConstants {

    public final static String SERVICE_URL = "http://www.lengzs100.com/";  // 首页
    public final static String BEFORE_TIME_KEY = "oldDate";  // 最近打开应用的日期key
    public final static String INVITE_BEFORE_TIME_KEY = "inviteDate";  // 上一次邀请好评的日期key
    public final static String DETAILS_URL_KEY = "detailsUrl";  // 跳转到详情页的key
    public final static String STARCOUNT_KEY = "starCount";  // 赞的key
    public final static String READCOUNT_KEY = "readCount";  // 阅读的key

    /**
     *  搜索
     */
    public final static String SOCKET = "?s=";
    public final static String SEARCH_SERVICE_URL = "http://www.lengzs100.com/";
    public final static String SEARCH_SERVICE_NEXT_URL = "http://www.lengzs100.com/page/";

    /**
     * 每日一冷
     */
    public final static String EVERYDAY_CACHE_KEY = "EverydayCache";  // 缓存key
    public final static String EVERYDAY_NEXT_URL_KEY = "everydayUrl";   // 下一篇key
    public final static String EVERYDAY_SERVICE_URL = "http://www.lengzs100.com/21.html";

    /**
     * 图文精选
     */
    public final static String TABS_ENCYCLOPEDIA_CACHE_KEY = "baikeCache";  // 缓存key
    public final static String TABS_ENCYCLOPEDIA_URL = SERVICE_URL + "baike";  // 百科
    public final static String TABS_ENCYCLOPEDIA_NEXT_URL = TABS_ENCYCLOPEDIA_URL + "/page/";  // 百科下一页

    public final static String TABS_ANIMAL_CACHE_KAY = "dongwuCache";
    public final static String TABS_ANIMAL_URL = SERVICE_URL + "dongwu";  // 动物
    public final static String TABS_ANIMAL_NEXT_URL = TABS_ANIMAL_URL + "/page/";  // 动物下一页

    public final static String TABS_ASTRONOMY_CACHE_KAY = "tianwenCache";
    public final static String TABS_ASTRONOMY_URL = SERVICE_URL + "tianwen";  // 天文
    public final static String TABS_ASTRONOMY_NEXT_URL = TABS_ASTRONOMY_URL + "/page/";  // 天文下一页

    public final static String TABS_CULTURE_CACHE_KAY = "wenhuaCache";
    public final static String TABS_CULTURE_URL = SERVICE_URL + "wenhua";  // 文化
    public final static String TABS_CULTURE_NEXT_URL = TABS_CULTURE_URL + "/page/";  // 文化下一页

    public final static String TABS_BODY_CACHE_KAY = "rentiCache";
    public final static String TABS_BODY_URL = SERVICE_URL + "renti";  // 人体
    public final static String TABS_BODY_NEXT_URL = TABS_BODY_URL + "/page/";  // 人体下一页

    public final static String TABS_HISTORY_CACHE_KAY = "lishiCache";
    public final static String TABS_HISTORY_URL = SERVICE_URL + "lishi";  // 历史
    public final static String TABS_HISTORY_NEXT_URL = TABS_HISTORY_URL + "/page/";  // 历史下一页

    public final static String TABS_GEOGRAPHY_CACHE_KAY = "diliCache";
    public final static String TABS_GEOGRAPHY_URL = SERVICE_URL + "dili";  // 地理
    public final static String TABS_GEOGRAPHY_NEXT_URL = TABS_GEOGRAPHY_URL + "/page/";  // 地理下一页

    public final static String TABS_NATURE_CACHE_KAY = "ziranCache";
    public final static String TABS_NATURE_URL = SERVICE_URL + "ziran";  // 自然
    public final static String TABS_NATURE_NEXT_URL = TABS_NATURE_URL + "/page/";  // 自然下一页

    public final static String TABS_LIFE_CACHE_KAY = "shenghuoCache";
    public final static String TABS_LIFE_URL = SERVICE_URL + "shenghuo";  // 生活
    public final static String TABS_LIFE_NEXT_URL = TABS_LIFE_URL + "/page/";  // 生活下一页

    public final static String TABS_SCIENCE_CACHE_KAY = "kexueCache";
    public final static String TABS_SCIENCE_URL = SERVICE_URL + "kexue";  // 科学
    public final static String TABS_SCIENCE_NEXT_URL = TABS_SCIENCE_URL + "/page/";  // 科学下一页

    public final static String TABS_PHYSICAL_CACHE_KAY = "tiyuCache";
    public final static String TABS_PHYSICAL_URL = SERVICE_URL + "tiyu";  // 体育
    public final static String TABS_PHYSICAL_NEXT_URL = TABS_PHYSICAL_URL + "/page/";  // 体育下一页

    public final static String TABS_PLANT_CACHE_KAY = "zhiwuCache";
    public final static String TABS_PLANT_URL = SERVICE_URL + "zhiwu";  // 植物
    public final static String TABS_PLANT_NEXT_URL = TABS_PLANT_URL + "/page/";  // 植物下一页

    public final static String TABS_CELEBRITY_CACHE_KAY = "mingrenCache";
    public final static String TABS_CELEBRITY_URL = SERVICE_URL + "mingren";  // 名人
    public final static String TABS_CELEBRITY_NEXT_URL = TABS_CELEBRITY_URL + "/page/";  // 名人下一页

}
