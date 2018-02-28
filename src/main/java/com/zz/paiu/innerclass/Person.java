package com.zz.paiu.innerclass;
/**
 * @author zz
 * @date 2016-8-3
 * 内部类实例化
 */
public class Person {
	private String name;
	
	public class Women {
		private String feature;

		public String getFeature() {
			return feature;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}
		
	}
	
	public Women getWomen() {
		return new Women();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
