package com.zz.dailytest;

import com.zz.bean.Constants;
import com.zz.utils.HttpClientUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis.zz on 2016-05-20.
 * 描述：使用httpClient向服务器发起请求；测试session的传递。<br/>
 * 使用jar包：httpcore-4.4.4.jar，httpclient-4.5.2.jar <br/>
 */
public class TestHttpClient {
    //String serverURL = "http://localhost:8091/tsm";
    //String serverURL = "http://localhost:8082/myTest/myPro";
    //String serverURL = "http://localhost:8080/otaService2";
    String serverURL = "http://localhost:8080/iwms/api/interfacePlatform/open?method=deliveryorder.create&customerId=HQ&format=xml";

    Map<String, Object> req;
    CloseableHttpClient client;
    @Before
    public void setUp() {
        req = new HashMap<String, Object>();
        req.put(Constants.COMMAND_ID, Constants.OP_TYPE_BUILD_SESSION);
        req.put("name", "闪电");
        req.put("id", "1234");
        //client = HttpClientUtil.createClient(1000 * 60 * 5, "utf-8");
        client = HttpClientUtil.createClient(1000 * 60, "utf-8");
    }

    @Test
    public void testCreateOrder() {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<request>\n" +
                "    <deliveryOrder>\n" +
                "        <deliveryOrderCode>HQCK0032</deliveryOrderCode>\n" +
                "        <warehouseCode>YB</warehouseCode>\n" +
                "        <orderType>JYCK</orderType>\n" +
                "        <orderFlag></orderFlag>\n" +
                "        <logisticCatagory>FX1</logisticCatagory>\n" +
                "        <logisticCode>10*10</logisticCode>\n" +
                "        <remark>备注，string（500）</remark>\n" +
                "    </deliveryOrder>\n" +
                "    <orderLines>\n" +
                "        <orderLine>\n" +
                "            <customerCode>HQ</customerCode>\n" +
                "            <itemCode>178966404</itemCode>\n" +
                "            <ownerCode>HQVMI</ownerCode>\n" +
                "            <inventoryType>ZP</inventoryType>\n" +
                "            <itemName></itemName>\n" +
                "            <planQty>2</planQty>\n" +
                "        </orderLine>\n" +
                "        <orderLine>\n" +
                "            <customerCode>HQ</customerCode>\n" +
                "            <itemCode>174228101</itemCode>\n" +
                "            <ownerCode>HQYG</ownerCode>\n" +
                "            <inventoryType>ZP</inventoryType>\n" +
                "            <itemName></itemName>\n" +
                "            <planQty>2</planQty>\n" +
                "        </orderLine>\n" +
                "    </orderLines>\n" +
                "</request>";
        try {
            HttpClientUtil.excuteHttpPost(serverURL, client, xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequest() {
        // request to build session
        try {
            String path = TestHttpClient.class.getClassLoader().getResource("").getPath();
            System.out.println("Path:" + path);
            String reqStr = TestJson.mapToJsonStr(req);
            System.out.println("req json str:" + reqStr);

            String respStr = HttpClientUtil.excuteHttpPost(serverURL, client, reqStr);

           /* String reqStr = "{\"apduResp\":{\"dataLength\":5,\"exception\":false,\"originalAPDU\":[],\"realResult\":[106,-126],\"seqNO\":0},\"crcCode\":[0,0],\"exceptionFlowStepID\":-1,\"flowStepID\":0,\"params\":null,\"protocolVersion\":\"3.00\",\"respCode\":0,\"sessionCount\":0,\"version\":\"3.00\"}";
            System.out.println("Request str:" + reqStr);
            CloseableHttpClient client = HttpClientUtil.createClient(1000 * 60 * 5);
            String respStr = HttpClientUtil.excuteHttpPost(serverURL, client, reqStr);*/

            // request to Demo
            req.put(Constants.COMMAND_ID, Constants.COMMAND_ID_DEMO);
            reqStr = TestJson.mapToJsonStr(req);
            HttpClientUtil.excuteHttpPost(serverURL, client, reqStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPath() {
        String path = TestHttpClient.class.getClassLoader().getResource("").getPath();
        System.out.println("Path:" + path);
    }
    @Test
    public void testMapRequest() {
        try {
            String reqStr = TestJson.mapToJsonStr(req);
            System.out.println("req json str:" + reqStr);
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", "你好");
            params.put("params", reqStr);
            String respStr = HttpClientUtil.excuteHttpPost(serverURL, client, params);

            // request to Demo
            req = new HashMap<String, Object>();
            req.put(Constants.COMMAND_ID, Constants.COMMAND_ID_DEMO);
            reqStr = TestJson.mapToJsonStr(req);
            params.put("params", reqStr);
            HttpClientUtil.excuteHttpPost(serverURL, client, params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
