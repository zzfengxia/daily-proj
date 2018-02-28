package com.zz.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Francis.zz on 2017/7/31.
 */
public class GXAirOpenServiceTest  {

    @Test
    public void testQueryCardInfo() throws Exception {
        String requestJson = "{\"accessUser\":\"gxecard_wfc\",\"cardMasterType\":\"11\",\"cardNo\":\"3104840899000018140\",\"cardSubType\":\"1111\",\"cityCode\":\"6310\",\"messageType\":\"2057\",\"outletNo\":\"6310029031000001\",\"proxyFlag\":\"1\",\"terminalDeviceNo\":\"6310413190000001\",\"terminalNo\":\"6310413190000001\"}";
        // 发起请求
        String url = "https://mail.gxecard.com:8440/service";
        CustomApacheHttpClient client = new CustomApacheHttpClient( "E:\\key\\gx-test-key\\Vfuchong_Tester.p12", "123456", "123456",  30 * 1000);
        Map<String, String> reqMap = ImmutableMap.of("parameter", requestJson);
        String respStr = client.doPost(url, reqMap, "utf-8");

        System.out.println(respStr);
    }

}
