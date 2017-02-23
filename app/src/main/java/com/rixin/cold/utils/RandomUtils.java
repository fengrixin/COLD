package com.rixin.cold.utils;

import java.util.Random;

/**
 * 随机数
 * Created by 飘渺云轩 on 2017/2/23.
 */

public class RandomUtils {

    /**
     * 生成阅读数
     * @return
     */
    public static int getReadRandom(){
        Random random = new Random();
        int count = random.nextInt(2000) + 7500;
        return count;
    }

    /**
     * 生成赞数
     * @return
     */
    public static int getStarRandom(){
        Random random = new Random();
        int count = random.nextInt(2000) + 1500;
        return count;
    }

}
