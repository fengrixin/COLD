package com.rixin.cold.fragment;

import com.rixin.cold.fragment.tabs.AnimalFragment;
import com.rixin.cold.fragment.tabs.AstronomyFragment;
import com.rixin.cold.fragment.tabs.BodyFragment;
import com.rixin.cold.fragment.tabs.CelebrityFragment;
import com.rixin.cold.fragment.tabs.CultureFragment;
import com.rixin.cold.fragment.tabs.EncyclopediaFragment;
import com.rixin.cold.fragment.tabs.GeographyFragment;
import com.rixin.cold.fragment.tabs.HistoryFragment;
import com.rixin.cold.fragment.tabs.LifeFragment;
import com.rixin.cold.fragment.tabs.NatureFragment;
import com.rixin.cold.fragment.tabs.PhysicalFragment;
import com.rixin.cold.fragment.tabs.PlantFragment;
import com.rixin.cold.fragment.tabs.ScienceFragment;

import java.util.HashMap;

/**
 * 生产Fragment的工厂方法
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class FragmentFactory {

    public static final int EVERYDAY_FRAGMENT = 0;
    public static final int IMAGE_TXT_FRAGMENT = 1;
    public static final int VIDEO_FRAGMENT = 2;

    private static HashMap<Integer, BaseFragment> fragmentMap = new HashMap<Integer, BaseFragment>();
    private static HashMap<Integer, BaseFragment> pageFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int position) {
        //先从集合中取，如果没有才创建对象
        BaseFragment fragment = fragmentMap.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new EverydayFragment();
                    break;
                case 1:
                    fragment = new ImageTxtFragment();
                    break;
                case 2:
                    fragment = new VideoFragment();
                    break;
            }
            fragmentMap.put(position, fragment);  //将fragment保存到集合中
        }
        return fragment;
    }

    public static BaseFragment createPageFragment(int position) {
        BaseFragment fragment = pageFragmentMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new EncyclopediaFragment();  // 百科
                    break;
                case 1:
                    fragment = new AnimalFragment();  // 动物
                    break;
                case 2:
                    fragment = new AstronomyFragment();  // 天文
                    break;
                case 3:
                    fragment = new CultureFragment();  // 文化
                    break;
                case 4:
                    fragment = new BodyFragment();  // 人体
                    break;
                case 5:
                    fragment = new HistoryFragment();  // 历史
                    break;
                case 6:
                    fragment = new GeographyFragment();  // 地理
                    break;
                case 7:
                    fragment = new NatureFragment();  // 自然
                    break;
                case 8:
                    fragment = new LifeFragment();  // 生活
                    break;
                case 9:
                    fragment = new ScienceFragment();  // 科学
                    break;
                case 10:
                    fragment = new PhysicalFragment();  // 体育
                    break;
                case 11:
                    fragment = new PlantFragment();  // 植物
                    break;
                case 12:
                    fragment = new CelebrityFragment();  // 名人
                    break;
            }
            pageFragmentMap.put(position, fragment);
        }
        return fragment;
    }

}
