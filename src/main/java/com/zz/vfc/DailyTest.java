package com.zz.vfc;

import com.zz.vfc.pojo.Animals;
import com.zz.vfc.pojo.Cat;
import com.zz.vfc.pojo.XiaoMing;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * Created by Francis.zz on 2017/8/1.
 */
public class DailyTest {
    @Test
    public void testSerialize() {
        XiaoMing s = new XiaoMing();
        Animals a = new Cat();
        s.setPet(a);

        String jsonStr = JSONObject.fromObject(s).toString();
        System.out.println(jsonStr);


        JSONObject oa = JSONObject.fromObject(jsonStr);
        XiaoMing fa = (XiaoMing) JSONObject.toBean(oa, XiaoMing.class);
    }

    @Test
    public void testInteger() {
        Integer a = new Integer(1);
        Integer b = new Integer(1);

        System.out.println(a == b);
    }
}
