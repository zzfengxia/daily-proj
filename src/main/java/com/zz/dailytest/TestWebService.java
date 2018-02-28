package com.zz.dailytest;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Francis.zz on 2016-05-05.
 * 描述：简单的webservice方法，发布webservice <br/>
 */
@WebService
public class TestWebService {

    public String testService(String arg) {
        return "Webservice test," + arg + "success!";
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9001/TestWebService/test", new TestWebService());
        System.out.println("publish success!");
    }
}
