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
                        info.setId(strs[0]);
                        info.setTitle(strs[1]);
                        info.setPicUrl(strs[2]);
                        info.setContentUrl(strs[3]);
                        info.setReadCount(Integer.parseInt(strs[4]));
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
