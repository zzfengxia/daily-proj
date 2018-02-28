package com.zz.guava;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.*;

/**
 * Created by Francis.zz on 2016/10/11.
 * 描述：
 */
public class TestGuawa {

    @Test
    public void testInitCollection() {
        Map<String, String> res = ImmutableMap.of("name", "Tom", "color", "black");
        for(String key : res.keySet()) {
            System.out.println(key + ":" + res.get(key));
        }

        List<String> resList = Lists.newArrayList("Tom", "Jerry", "Merry");
        for(String val : resList) {
            System.out.println("List:" + val);
        }
    }

    @Test
    public void testImmutableCollection() {
        /************** 普通不可修改集合 ************/
        List<String> ptList = new ArrayList<String>();
        ptList.addAll(Arrays.asList("Tom", "Jerry", "Merry"));
        List<String> ptUnmodifiable = Collections.unmodifiableList(ptList);
        System.out.println("--------unmodifiable List before-----------");
        for(String val : ptUnmodifiable) {
            System.out.print(val + "\t");
        }
        // ptUnmodifiable.add("Jack");  // java.lang.UnsupportedOperationException 不可修改
        ptList.add("Jack");      // 新增元素，但是可以通过对原集合的修改来改变ptUnmodifiable的内容
        System.out.println("\n--------unmodifiable List after-----------");
        for(String val : ptUnmodifiable) {
            System.out.print(val + "\t");
        }
        /************** 普通不可修改集合，实际可以改变集合内容 ************/

        /************** 使用Guawa提供的不可变集合 ***************/
        List<String> unChangeableList = ImmutableList.of("Tom", "Jerry", "Merry");      // 方式1
        //unChangeableList.add("Jack");   // java.lang.UnsupportedOperationException 不可修改
        List<String> unChangeableList2 = ImmutableList.copyOf(ptList);      // 方式2
        System.out.println("\n--------immutable List before-----------");
        for(String val : unChangeableList2) {
            System.out.print(val + "\t");
        }
        ptList.add("Rose");     // 通过改变原集合内容，使用Guawa创建的不可变集合内容并没有变，是真的不可变
        System.out.println("\n--------unmodifiable List after-----------");
        for(String val : ptUnmodifiable) {
            System.out.print(val + "\t");
        }
        System.out.println("\n--------immutable List after-----------");
        for(String val : unChangeableList2) {
            System.out.print(val + "\t");
        }

        List<String> unChangeableList3 = ImmutableList.<String>builder().addAll(ptList).add("Frank").build();   // 方式3
        // unChangeableList3.add("Mike");      // java.lang.UnsupportedOperationException
        System.out.println("\n--------immutable List3 -----------");
        for(String val : unChangeableList3) {
            System.out.print(val + "\t");
        }
    }

    @Test
    public void testNewCollection() {
        /*
         * Multimap     允许key值重复的Map
         * ListMultimap 通过key获取关联的List
         * SetMultimap  通过key获取关联的Set
         */
        ListMultimap<String, String> newListMultimap = ArrayListMultimap.create();
        newListMultimap.put("Person", "Mike");
        newListMultimap.put("Person", "Jack");
        newListMultimap.put("Person", "Mike");

        newListMultimap.put("Animal", "Tom");
        newListMultimap.put("Animal", "Jerry");
        Set<String> keys = newListMultimap.keySet();
        for(String s : keys) {
            System.out.println("key:" + s);
        }
        List<String> personList = newListMultimap.get("Person");
        for(String val : personList) {
            System.out.println("Person:" + val);
        }
        
        // SetMultimap 根据key获取的结果set会自动去重
        SetMultimap<String, String> newSetMultimap = HashMultimap.create();
        newSetMultimap.put("Person", "Mike");
        newSetMultimap.put("Person", "Mike");

        newSetMultimap.put("Animal", "Tom");
        newSetMultimap.put("Animal", "Jerry");
        newSetMultimap.put("Animal", "Tom");

        Set<String> personSet = newSetMultimap.get("Animal");
        System.out.println(personSet.size());
        for(String val : personSet) {
            System.out.println("Person:" + val);
        }
    }
}
