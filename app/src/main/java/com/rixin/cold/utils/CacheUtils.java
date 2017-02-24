package com.rixin.cold.utils;

import com.rixin.cold.domain.ColdDetailsInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 缓存工具
 * Created by 飘渺云轩 on 2017/2/23.
 */

public class CacheUtils {

    /**
     * 写每日一冷的缓存
     *
     * @param str
     */
    public static void setEverydayCache(String str) {
        File cacheDir = UIUtils.getContext().getCacheDir();  // 获取缓存目录
        File cacheFile = new File(cacheDir, "EverydayCache");  //生成缓存文件
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
     * 获取每日一冷的缓存
     *
     * @return
     */
    public static ColdDetailsInfo getEverydayCache() {
        File cacheDir = UIUtils.getContext().getCacheDir();  // 获取缓存目录
        File cacheFile = new File(cacheDir, "EverydayCache");  //获得缓存文件
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
                if(str == null || str == ""){
                    return null;
                }
                String[] strs = str.split(",,");
                ColdDetailsInfo detailsInfo = new ColdDetailsInfo();
                if (strs.length == 7) {
                    detailsInfo.title = strs[0];
                    detailsInfo.picUrl = strs[1];
                    detailsInfo.pContent = strs[2];
                    detailsInfo.prevUrl = strs[3];
                    detailsInfo.nextUrl = strs[4];
                    detailsInfo.read = Integer.parseInt(strs[5]);
                    detailsInfo.star = Integer.parseInt(strs[6]);
                }
                return detailsInfo;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    /**
     * 写缓存，以url为文件名，以json为文件内容，保存在本应用的缓存目录中
     *
     * @param index
     * @param json
     */
    public void setCache(int index, String json) {
        File cacheDir = UIUtils.getContext().getCacheDir(); // 获取缓存目录
        // 生成缓存文件
        File cacheFile = new File(cacheDir, "");
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            // 缓存失效的截止日期
            long deadLine = System.currentTimeMillis() + 30 * 60 * 1000;
            writer.write(deadLine + "\n"); // 在第一行写入缓存时间，并换行
            writer.write(json); // 写入json
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
     * @param index
     * @return
     */
    public String getCache(int index) {
        File cacheDir = UIUtils.getContext().getCacheDir(); // 获取缓存目录
        // 获得缓存文件
        File cacheFile = new File(cacheDir, "");
        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String readLine = reader.readLine();  //读取第一行的deadLine
                long deadLine = Long.parseLong(readLine);

                if (System.currentTimeMillis() < deadLine) { // 缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

}
