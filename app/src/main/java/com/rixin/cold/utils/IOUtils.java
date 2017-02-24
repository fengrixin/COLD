package com.rixin.cold.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭文件IO流
 * Created by 飘渺云轩 on 2017/2/23.
 */

public class IOUtils {

    /** 关闭流 */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
