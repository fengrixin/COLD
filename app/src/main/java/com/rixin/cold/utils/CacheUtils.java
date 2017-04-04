package com.rixin.cold.utils;

import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.global.GlobalConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 缓存工具
 * Created by 飘渺云轩 on 2017/2/23.
 */

public class CacheUtils {

    /**
     * 写缓存，以EverydayCache为文件名，ColdDetailsInfo.toString()为内容，保存到本应用的缓存目录中
     *
     * @param str
     */
    public static void setCache(String key, String str) {
        File cacheDir = UIUtils.getContext().getCacheDir();  // 获取缓存目录
        File cacheFile = new File(cacheDir, key);  //生成缓存文件
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            writer.write(" " + str);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    /**
     * 获取每日一冷的缓存，并对其内容进行解析，返回ColdDetailsInfo对象
     *
     * @return
     */
    public static ColdDetailsInfo getEverydayCache() {
        ColdDetailsInfo detailsInfo = null;
        File cacheDir = UIUtils.getContext().getCacheDir();  // 获取缓存目录
        File cacheFile = new File(cacheDir, GlobalConstants.EVERYDAY_CACHE_KEY);  //获得缓存文件
        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String str = sb.toString();
                if (str == null || str == "" || str.isEmpty()) {
                    return null;
                }
                detailsInfo = new ColdDetailsInfo();
                String[] strs = str.split("-IOU-");
                if (strs.length == 7) {
                    detailsInfo.title = strs[0];
                    detailsInfo.picUrl = strs[1];
                    detailsInfo.pContent = strs[2];
                    detailsInfo.prevUrl = strs[3];
                    detailsInfo.nextUrl = strs[4];
                    detailsInfo.read = Integer.parseInt(strs[5]);
                    detailsInfo.star = Integer.parseInt(strs[6]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return detailsInfo;
    }

    /**
     * 获取缓存
     *
     * @param cacheFileName
     * @return
     */
    public static ArrayList<ColdInfo> getCache(String cacheFileName) {
        ArrayList<ColdInfo> data = null;
        File cacheDir = UIUtils.getContext().getCacheDir(); // 获取缓存目录
        // 获得缓存文件
        File cacheFile = new File(cacheDir, cacheFileName);
        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                data = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    ColdInfo info = new ColdInfo();
                    String[] strs = line.split("-IOU-");
                    for (int i = 0; i < strs.length; i++) {
                        info.id = strs[0];
                        info.title = strs[1];
                        info.picUrl = strs[2];
                        info.contentUrl = strs[3];
                        info.readCount = Integer.parseInt(strs[4]);
                        info.starCount = Integer.parseInt(strs[5]);
                    }
                    data.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return data;
    }

}
