/*
package com.zz.dailytest;

import com.zz.utils.HexUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by Francis.zz on 2016-05-06.
 * 描述：<br/>
 *//*

public class TestKeyDerivation {
    private Map<String, Object> params;

    @Before
    public void setUp() {
        // UICC params
        params = new HashMap<String, Object>();
        params.put(MockKeyDerivation.SEID, "1300000A032000001891");
        params.put(MockKeyDerivation.SD_AID, "D1560001010001600000000100000000");
        params.put(MockKeyDerivation.QA_KEY, "746B6C63776C6B6A797867737A677964");
        params.put(MockKeyDerivation.PADDING, (byte) 0x00);
        params.put(MockKeyDerivation.KEY_VER, 32);
    }

    */
/** 测试UICC密钥分散 *//*

    @Test
    public void testUICC() throws Exception {

        byte[] key1 = MockKeyDerivation.generateUICCKey((byte) 0x0E, params);
        byte[] key2 = MockKeyDerivation.generateUICCKey((byte) 0x0C, params);
        byte[] key3 = MockKeyDerivation.generateUICCKey((byte) 0x0D, params);

        System.out.println("-- UICC DK1: " + HexUtil.byteArrayToHexString(key1));
        System.out.println("-- UICC DK2: " + HexUtil.byteArrayToHexString(key2));
        System.out.println("-- UICC DK3: " + HexUtil.byteArrayToHexString(key3));
    }

    */
/** 测试深圳通密钥分散 *//*

    @Test
    public void testSZT() throws Exception {
        // SZT params
        params.put(MockKeyDerivation.SEID, "52484781002400460003");
        params.put(MockKeyDerivation.QA_KEY, "B638A3856F90546873E0882D69C94E48");

        byte[] key1 = MockKeyDerivation.generateSZTKey((byte) 0x01, params);
        byte[] key2 = MockKeyDerivation.generateSZTKey((byte) 0x02, params);
        byte[] key3 = MockKeyDerivation.generateSZTKey((byte) 0x03, params);
        byte[] key4 = MockKeyDerivation.generateSZTKey((byte) 0x73, params);

        System.out.println("-- UICC DK1: " + HexUtil.byteArrayToHexString(key1));
        System.out.println("-- UICC DK2: " + HexUtil.byteArrayToHexString(key2));
        System.out.println("-- UICC DK3: " + HexUtil.byteArrayToHexString(key3));
        System.out.println("-- UICC DK4: " + HexUtil.byteArrayToHexString(key4));
    }
}
*/
