package com.zz.paiu.innerclass;

import org.junit.Test;

import com.zz.paiu.innerclass.Person.Women;

/**
 * @author zz
 * @date 2016-8-3
 */
public class InnerTest {
	@Test
	public void testPerson() {
		// 实例化内部类
		Person person = new Person();
		Women women = person.new Women();
		women.setFeature("开发建设");
		
		// 使用外部类的方法实例化内部类
		Women w = person.getWomen();
		w.setFeature("shopping");
		System.out.println(women.getFeature());
		System.out.println(w.getFeature());
	}
}
