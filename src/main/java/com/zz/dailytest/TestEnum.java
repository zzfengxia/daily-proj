package com.zz.dailytest;

/**
 * Created by Francis.zz on 2016-04-13.
 * 描述：枚举类的常见用法 <br/>
 */
public class TestEnum {
    /**
     * 普通枚举
     */
    public enum Status {
        YES, NO
    }
    /**
     * 枚举中添加属性和方法
     */
    public enum SeasonEnum {
        /** 枚举要写在最前面，否则报错 */
        SPRING, SUMMER , AUTUMN, WINTER;
        private static String value = "test";
        public static SeasonEnum getValue() {
            if("test".equals(value)) {
                return SPRING;
            }else {
                return SUMMER;
            }
        }
    }

    /**
     * 带有构造方法的枚举；构造方法必须对所有属性赋值；构造方法默认且只能是私有的
     * 枚举也必须对所有属性赋值，不赋值则不允许有构造方法存在；
     */
    public enum ColorEnum {
        RED("this is red", 0), BLUE("this is blue", 1), GREEN("this is green", 2), YELLOW("this is yellow", 3);

        private final String value;
        private final int index;
        ColorEnum(String value, int index) {
            this.value = value;
            this.index = index;
        }
        public static String getValue(int index) {
            for(ColorEnum c : ColorEnum.values()) {
                if(c.getIndex() == index) {
                    return c.getValue();
                }
            }

            return "";
        }
        public String getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * 带有抽象方法的枚举
     */
    public enum OrderEnum {
        CANCEL{public String getValue() {return "取消";}},
        WAITCONFIRM{public String getValue() {return "待审核"; }},
        /** 等待付款 */
        WAITPAYMENT {public String getValue(){return "等待付款";}},
        /** 正在配货 */
        ADMEASUREPRODUCT {public String getValue(){return "正在配货";}},
        /** 等待发货 */
        WAITDELIVER {public String getValue(){return "等待发货";}},
        /** 已发货 */
        DELIVERED {public String getValue(){return "已发货";}},
        /** 已收货 */
        RECEIVED {public String getValue(){return "已收货";}};

        // 抽象方法
        public abstract String getValue();
    }


    /**
     * 测试不同类型枚举的用法
     * @param args
     */
    public static void main(String[] args) {
        /** 遍历枚举 */
        System.out.println("-----------遍历枚举①-----------");
        for(Status sta : Status.values()) {
            System.out.println(sta + "\t");
        }
        System.out.println("-----------遍历枚举②-----------");
        // 通过反射得到Status的类对象
        Class<Status> status = Status.class;
        for(Status sta : status.getEnumConstants()) {
            System.out.println(sta + "\t");
        }

        /** 使用枚举的方法 */
        System.out.println("-----------获取枚举值-----------");
        System.out.println(SeasonEnum.getValue() + "\t季节：" + SeasonEnum.SPRING);

        /** 枚举构造方法使用 */
        System.out.println("-----------枚举构造方法使用-----------");
        System.out.println("颜色：" + ColorEnum.RED.getValue() + "\t索引：" + ColorEnum.RED.index);
        System.out.println("索引2颜色：" + ColorEnum.getValue(2));
        /** 枚举抽象方法使用 */
        System.out.println("-----------枚举抽象方法使用-----------");
        System.out.println("订单状态：" + OrderEnum.CANCEL.getValue());
    }
}
