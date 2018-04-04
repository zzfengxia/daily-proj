package com.zz.interview.algorithm;

import org.junit.Test;

import java.util.Arrays;

/**
 * 常见排序算法
 * --------------------------------
 * create by Intellij IDEA.
 * @author Francis.zz
 * @date 2018-03-30 17:11
 * --------------------------------
 */
public class Sorting {

    @Test
    public void testSorting() {
        int[] arry = new int[]{5, 10, 8, 12, 4, 3, 7, 6, 9, 20, 18, 22, 19, 30, 17, 25, 16, 23, 15, 14, 24, 2};
        //bubbleSort(arry, SortingSpec.DESC);      // 231
        //bubbleSort_1(arry, SortingSpec.DESC);  // 210
        bubbleSort_2(arry, SortingSpec.DESC);
        System.out.println("数组长度:" + arry.length);
        for(int a : arry) {
            System.out.print(a + "\t");
        }
    }

    /**
     * 冒泡排序
     *
     * @param arry
     * @param spec 排序规则
     */
    public void bubbleSort(int[] arry, SortingSpec spec) {
        int num = 0;
        int len = arry.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                num++;
                boolean condition = spec == SortingSpec.ASC ? arry[j] > arry[j + 1] : arry[j] < arry[j + 1];
                if(condition) {
                    // 交换
                    int temp = arry[j];
                    arry[j] = arry[j + 1];
                    arry[j + 1] = temp;
                }
            }
        }
        System.out.println("执行次数:" + num);
    }

    /**
     * 冒泡排序优化
     * 增加交换标识，减少排序次数(如果没有再进行交换，则停止排序)
     *
     * @param arry
     * @param spec
     */
    public void bubbleSort_1(int[] arry, SortingSpec spec) {
        int num = 0;
        int len = arry.length;
        int i = len - 1;

        while(i > 0) {
            // 初始位置
            int pos = 0;
            for (int j = 0; j < i; j++) {
                num++;
                boolean condition = spec == SortingSpec.ASC ? arry[j] > arry[j + 1] : arry[j] < arry[j + 1];
                if(condition) {
                    pos = j;
                    // 交换
                    int temp = arry[j];
                    arry[j] = arry[j + 1];
                    arry[j + 1] = temp;
                }
            }
            i = pos;
        }

        System.out.println("执行次数:" + num);
    }

    /**
     * 冒泡排序优化
     * 一次冒泡两个(最小与最大)
     *
     * @param arry
     * @param spec
     */
    public void bubbleSort_2(int[] arry, SortingSpec spec) {
        int num = 0;
        int len = arry.length;

        int low = 0;
        int high = len -1;
        int temp, j;
        while(low < high) {
            // 初始位置
            int pos = 0;
            for (j = low; j < high; j++) {
                num++;
                boolean condition = spec == SortingSpec.ASC ? arry[j] > arry[j + 1] : arry[j] < arry[j + 1];
                if(condition) {
                    // 交换
                    temp = arry[j];
                    arry[j] = arry[j + 1];
                    arry[j + 1] = temp;
                }
            }
            --high;
            for (j = high; j > low; j--) {
                num++;
                boolean condition = spec == SortingSpec.ASC ? arry[j] < arry[j - 1] : arry[j] > arry[j - 1];
                if(!condition) {
                    // 交换
                    temp = arry[j];
                    arry[j] = arry[j - 1];
                    arry[j - 1] = temp;
                }
            }
            ++low;
        }

        System.out.println("执行次数:" + num);
    }

    public enum SortingSpec {
        DESC, ASC
    }
}
