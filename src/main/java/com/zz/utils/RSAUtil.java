package com.zz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.bean.User;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis.zz on 2016-04-21.
 * 描述：签名验签，RSA加解密 <br/>
 * 使用jar包：commons-codec-1.8.jar;Base64编码支持包 <br/>
 */

public class RSAUtil {
    public static final String KEY_ALGORITHM = "RSA";                   // 密钥生成算法
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String PRIVATE_KEY_HEX = "privateKeyHex";
    public static final String PRIVATE_KEY_BASE64 = "privateKeyBase64";
    public static final String PUBLIC_KEY_HEX = "publicKeyHex";
    public static final String PUBLIC_KEY_BASE64 = "publicKeyBase64";
    public static final String MODULE = "module";
    public static final String EXPONENT = "exponent";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";        // 签名算法
    
    private static final int KEY_SIZE = 1024;                            // 密钥长度(bit)
    private static final int MAX_ENCRYPT_SIZE = KEY_SIZE / 8 - 11;       // 最大加密长度(明文长度byte)
    private static final int MAX_DECRYPT_SIZE = KEY_SIZE / 8;           // 最大解密长度(密文长度byte)
    
    /**
     * 生成密钥对
     *
     * @return
     * @throws Exception
     */
    
    public static KeyPair getKeyPair() throws Exception {
        
        return getKeyPair(KEY_SIZE);
    }
    
    /**
     * 生成密钥对
     *
     * @return
     * @throws Exception
     */
    
    public static KeyPair getKeyPair(int keySize) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        
        generator.initialize(keySize);
        KeyPair keyPair = generator.generateKeyPair();
        
        return keyPair;
    }
    
    /**
     * 从密钥对中获取公钥和私钥的16进制字符串
     *
     * @param keyPair 密钥对
     * @return
     */
    
    public static Map<String, Map<String, Object>> retrieveKeySet(KeyPair keyPair) {
        Map<String, Map<String, Object>> keyResult = new HashMap<String, Map<String, Object>>();
        
        PrivateKey priKey = keyPair.getPrivate();
        byte[] keyByte = priKey.getEncoded();
        Map<String, Object> priKeyInfo = new HashMap<String, Object>();
        // 将私钥转换为16进制字符串
        priKeyInfo.put(PRIVATE_KEY_HEX, HexUtil.byteArrayToHexString(keyByte));
        priKeyInfo.put(PRIVATE_KEY_BASE64, Base64.encodeBase64String(keyByte));
        // 保存私钥数据
        priKeyInfo.put(PRIVATE_KEY, priKey);
        if (priKey instanceof RSAPrivateKey) {
            // 保存RSAPrivateKey的模数和指数
            RSAPrivateKey rsaPriKey = (RSAPrivateKey) priKey;
            String moduleStr = rsaPriKey.getModulus().toString(16).toUpperCase();
            priKeyInfo.put(MODULE, moduleStr);
            
            String exponentStr = rsaPriKey.getPrivateExponent().toString(16).toUpperCase();
            priKeyInfo.put(EXPONENT, exponentStr);
        }
        
        // 保存公钥信息
        Map<String, Object> pubKeyInfo = new HashMap<String, Object>();
        PublicKey pubKey = keyPair.getPublic();
        // 保存原始公钥
        pubKeyInfo.put(PRIVATE_KEY, pubKey);
        byte[] pubByte = pubKey.getEncoded();
        pubKeyInfo.put(PUBLIC_KEY_HEX, HexUtil.byteArrayToHexString(pubByte));
        pubKeyInfo.put(PUBLIC_KEY_BASE64, Base64.encodeBase64String(pubByte));
        if (pubKey instanceof RSAPublicKey) {
            RSAPublicKey rsaPubKey = (RSAPublicKey) pubKey;
            // 保存RSAPublicKey的模数和指数
            String moduleStr = rsaPubKey.getModulus().toString(16).toUpperCase();
            pubKeyInfo.put(MODULE, moduleStr);
            String exponentStr = rsaPubKey.getPublicExponent().toString(16).toUpperCase();
            pubKeyInfo.put(EXPONENT, exponentStr);
        }
        
        keyResult.put(PUBLIC_KEY, pubKeyInfo);
        keyResult.put(PRIVATE_KEY, priKeyInfo);
        
        return keyResult;
    }
    
    /**
     * 使用模数和指数生成公钥或私钥
     *
     * @param module   模数(16进制)
     * @param exponent 指数(16进制)
     * @param type     密钥类型
     * @return
     * @throws Exception
     */
    
    public static Key getKey(String module, String exponent, String type) throws Exception {
        BigInteger mo = new BigInteger(module, 16);
        BigInteger po = new BigInteger(exponent, 16);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        KeySpec spec;
        Key reKey;
        // 创建私钥规则
        if (PRIVATE_KEY.equals(type)) {
            spec = new RSAPrivateKeySpec(mo, po);
            reKey = factory.generatePrivate(spec);
        } else if (PUBLIC_KEY.equals(type)) {
            // 创建公钥规则
            spec = new RSAPublicKeySpec(mo, po);
            reKey = factory.generatePublic(spec);
        } else {
            throw new Exception("不正确的密钥类型!");
        }
        return reKey;
    }
    
    /**
     * 使用私钥对数据进行签名，返回签名后的16进制数据
     *
     * @param data  需要签名的数据
     * @param key   私钥
     * @param model 签名算法
     * @return
     */
    
    public static String getSignHex(String data, PrivateKey key, String model) throws Exception {
        Signature signature = Signature.getInstance(model);
        // 初始化签名的私钥
        signature.initSign(key);
        // 更新要签名的字节
        signature.update(data.getBytes("UTF-8"));
        // 签名信息
        byte[] singInfo = signature.sign();
        
        return HexUtil.byteArrayToHexString(singInfo);
    }
    
    public static String getSignBase64(String data, PrivateKey key, String model) throws Exception {
        Signature signature = Signature.getInstance(model);
        // 初始化签名的私钥
        signature.initSign(key);
        // 更新要签名的字节
        signature.update(data.getBytes("UTF-8"));
        // 签名信息
        byte[] singInfo = signature.sign();
        
        return java.util.Base64.getEncoder().encodeToString(singInfo);
    }
    
    /**
     * 验签
     *
     * @param signCode 签名后的数据
     * @param data     原始数据
     * @param key      公钥
     * @param model    签名算法
     * @return
     * @throws Exception
     */
    
    public static boolean verifySign(byte[] signCode, String data, PublicKey key, String model) throws Exception {
        Signature signature = Signature.getInstance(model);
        // 初始化验签的公钥
        signature.initVerify(key);
        // 更新需要验签的字节
        signature.update(data.getBytes("UTF-8"));
        // 验签
        return signature.verify(signCode);
    }
    
    /**
     * 将字节数组类型的密钥转换为对应的密钥对象
     *
     * @param key
     * @param keyType
     * @return
     * @throws Exception
     */
    
    public static Key getKey(byte[] key, String keyType) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key reKey = null;
        if (PRIVATE_KEY.equals(keyType)) {
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(key);
            reKey = factory.generatePrivate(priKeySpec);
        } else if (PUBLIC_KEY.equals(keyType)) {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
            reKey = factory.generatePublic(pubKeySpec);
        } else {
            throw new Exception("未知的密钥类型!");
        }
        
        return reKey;
    }
    
    /**
     * 将经过base64编码后的密钥转换为对应的密钥对象
     *
     * @param key
     * @param keyType
     * @return
     * @throws Exception
     */
    
    public static Key getKey(String key, String keyType) throws Exception {
        byte[] keyByte = Base64.decodeBase64(key);
        return getKey(keyByte, keyType);
    }
    
    /**
     * 公钥加密操作（Cipher不是线程安全的，使用static不适应多线程的情况）
     *
     * @param data      需要加密的数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 初始化(加密/解密，密钥)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        // 判断需要加密的字节数，做分片处理
        int dataLen = data.length;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] temp;
        int i = 0;
        int offset = 0;
        while (dataLen - offset > 0) {
            if (dataLen - offset > MAX_ENCRYPT_SIZE) {
                temp = cipher.doFinal(data, offset, MAX_ENCRYPT_SIZE);
            } else {
                temp = cipher.doFinal(data, offset, dataLen - offset);
            }
            outStream.write(temp, 0, temp.length);
            i++;
            offset = i * MAX_ENCRYPT_SIZE;
        }
        
        byte[] encryptInfo = outStream.toByteArray();
        outStream.close();
        return encryptInfo;
    }
    
    /**
     * 使用私钥解密数据（Cipher不是线程安全的，使用static不适应多线程的情况）
     *
     * @param data       需要解密的数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        // 判断密文是否超过最大长度，做分片处理
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp;
        int dataLen = data.length;
        int offset = 0;
        int i = 0;
        while (dataLen - offset > 0) {
            if (dataLen - offset > MAX_DECRYPT_SIZE) {
                temp = cipher.doFinal(data, offset, MAX_DECRYPT_SIZE);
            } else {
                temp = cipher.doFinal(data, offset, dataLen - offset);
            }
            outputStream.write(temp, 0, temp.length);
            i++;
            offset = MAX_DECRYPT_SIZE * i;
        }
        
        byte[] decryptInfo = outputStream.toByteArray();
        outputStream.close();
        return decryptInfo;
    }
    
    @Test
    public void testSign() {
        User user = new User();
        user.setUsername("zzxia");
        user.setNickName("zzfengxia");
        user.setContactPhone("13873987652");
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(user);
            
            KeyPair keyPair = RSAUtil.getKeyPair();
            Map<String, Map<String, Object>> keyInfo = RSAUtil.retrieveKeySet(keyPair);
            // 私钥签名
            Map<String, Object> priKeyInfo = keyInfo.get(PRIVATE_KEY);
            String priKeyHex = (String) priKeyInfo.get(PRIVATE_KEY_HEX);
            PrivateKey privateKey = (PrivateKey) priKeyInfo.get(PRIVATE_KEY);
            String priMo = (String) priKeyInfo.get(MODULE);
            String priPo = (String) priKeyInfo.get(EXPONENT);
            
            System.out.println("---------------私钥(16进制)---------------");
            System.out.println(priKeyHex);
            System.out.println("---------------私钥模数(16进制)---------------");
            System.out.println(priMo);
            System.out.println("---------------私钥指数(16进制)---------------");
            System.out.println(priPo);
            
            // 使用原始的私钥签名
            String signStr = RSAUtil.getSignHex(data, privateKey, SIGNATURE_ALGORITHM);
            System.out.println("---------------签名后的数据(16进制)---------------");
            System.out.println(signStr);
            
            // 使用模和指数生成私钥签名
            RSAPrivateKey priKeyBM = (RSAPrivateKey) RSAUtil.getKey(priMo, priPo, PRIVATE_KEY);
            System.out.println("---------------使用模和指生成的私钥(16进制)---------------");
            System.out.println(HexUtil.byteArrayToHexString(priKeyBM.getEncoded()));
            String signHex = RSAUtil.getSignHex(data, priKeyBM, SIGNATURE_ALGORITHM);
            System.out.println("---------------签名后的数据(16进制)---------------");
            System.out.println(signHex);
            
            // 公钥验签
            Map<String, Object> pubKeyInfo = keyInfo.get(PUBLIC_KEY);
            String pubKeyHex = (String) pubKeyInfo.get(PUBLIC_KEY_HEX);
            String pubMo = (String) pubKeyInfo.get(MODULE);
            String pubPo = (String) pubKeyInfo.get(EXPONENT);
            PublicKey publicKey = (PublicKey) pubKeyInfo.get(PRIVATE_KEY);
            
            System.out.println("---------------公钥(16进制)---------------");
            System.out.println(pubKeyHex);
            System.out.println("---------------公钥模数(16进制)---------------");
            System.out.println(pubMo);
            System.out.println("---------------公钥指数(16进制)---------------");
            System.out.println(pubPo);
            
            // 使用原始的公钥验签
            boolean isTrue = RSAUtil.verifySign(HexUtil.hexToByteArray(signStr.toCharArray()), data, publicKey, SIGNATURE_ALGORITHM);
            System.out.println("---------------验签结果---------------");
            System.out.println(isTrue);
            Assert.assertTrue(isTrue);
            
            // 使用模数和指数生成公钥验签
            RSAPublicKey pubKeyMP = (RSAPublicKey) RSAUtil.getKey(pubMo, pubPo, PUBLIC_KEY);
            System.out.println("---------------使用模和指生成的公钥(16进制)---------------");
            System.out.println(HexUtil.byteArrayToHexString(pubKeyMP.getEncoded()));
            isTrue = RSAUtil.verifySign(HexUtil.hexToByteArray(signHex.toCharArray()), data, pubKeyMP, SIGNATURE_ALGORITHM);
            System.out.println("---------------验签结果---------------");
            System.out.println(isTrue);
            Assert.assertTrue(isTrue);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDecrypt() {
        String moHex = "c8c909c5fc432bc0e2de72510539c7282f4f011f97fdcad52623c6d01b007e7d17d9d2965fac08e603f5dcb44697691f51da14a834f3cfd4af4570d6582a75cf";
        String poHex = "8300e8a78d18268c0b7a280c44e495d4da4929ea7b38d4068e83f94bfed22bb4393d44e9231a32fca344b7744739b6e075ea207d45224da517a51a491d1d5101";
        
        String data = "hello, this is encrypt test! 你好，这是一个加解密的测试！this test is success!本次测试很成功！";
        String encryptInfo = "wfm1eQL+aEScl6zDPkGcjxhg1CucSO2sackgP0/6rup8ssu0YFrJvrqCkG6GOpoeau/lmA9n3eXoHHotgjYvsRioWLZeDzLEj785GEwgKmBh4uETxk3pqJkJHYaTubo80jVWjkDAAKLmJT9FSV3TjQUJwbsvZ7kSSmP+1j6qIvqcSJNBvEAMKqbu5DOM/c+TthB4qdCWNlbhkT8iEPdrYTTiRwZZjIvmkAf02jJKZ4ERgB/vOI+DCrQeS4lRXIym";
        // 获取私钥
        try {
            PrivateKey privateKey = (PrivateKey) RSAUtil.getKey(moHex, poHex, PRIVATE_KEY);
            byte[] decryptInfo = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(encryptInfo), privateKey);
            String textStr = new String(decryptInfo);
            Assert.assertTrue(textStr.equals(data));
            System.out.println("解密后的信息：" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testEncrypt() {
        String moHex = "c8c909c5fc432bc0e2de72510539c7282f4f011f97fdcad52623c6d01b007e7d17d9d2965fac08e603f5dcb44697691f51da14a834f3cfd4af4570d6582a75cf";
        String poHex = "10001";
        
        String data = "hello, this is encrypt test! 你好，这是一个加解密的测试！this test is success!本次测试很成功！";
        // 获取公钥
        try {
            PublicKey pubKey = (PublicKey) RSAUtil.getKey(moHex, poHex, PUBLIC_KEY);
            byte[] encryptInfo = RSAUtil.encryptByPublicKey(data.getBytes(), pubKey);
            System.out.println("原数据：" + data);
            System.out.println("明文字节数：" + data.getBytes().length);
            System.out.println("---------------密文(base64)---------------");
            System.out.println(Base64.encodeBase64String(encryptInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 生产RSA公私钥，模指
     */
    
    @Test
    public void testGetKey() throws Exception {
        KeyPair keyPair = RSAUtil.getKeyPair(2048);
        Map<String, Map<String, Object>> keyInfo = RSAUtil.retrieveKeySet(keyPair);
        // 私钥签名
        Map<String, Object> priKeyInfo = keyInfo.get(PRIVATE_KEY);
        String priKeyHex = (String) priKeyInfo.get(PRIVATE_KEY_HEX);
        
        String priMo = (String) priKeyInfo.get(MODULE);
        String priPo = (String) priKeyInfo.get(EXPONENT);
        System.out.println("---------------私钥(16进制)---------------");
        System.out.println(priKeyHex);
        System.out.println("---------------私钥(Base64)---------------");
        System.out.println((String) priKeyInfo.get(PRIVATE_KEY_BASE64));
        System.out.println("---------------私钥模数(16进制)---------------");
        System.out.println(priMo);
        System.out.println("---------------私钥指数(16进制)---------------");
        System.out.println(priPo);
        
        // 公钥验签
        Map<String, Object> pubKeyInfo = keyInfo.get(PUBLIC_KEY);
        String pubKeyHex = (String) pubKeyInfo.get(PUBLIC_KEY_HEX);
        String pubMo = (String) pubKeyInfo.get(MODULE);
        String pubPo = (String) pubKeyInfo.get(EXPONENT);
    
        System.out.println("---------------公钥(16进制)---------------");
        System.out.println(pubKeyHex);
        System.out.println("---------------公钥(Base64)---------------");
        System.out.println((String) pubKeyInfo.get(PUBLIC_KEY_BASE64));
        System.out.println("---------------公钥模数(16进制)---------------");
        System.out.println(pubMo);
        System.out.println("---------------公钥素数(16进制)---------------");
        System.out.println(pubPo);
    }
    
    @Test
    public void testSign2() throws Exception {
        String moHex = "8B6D8CAE9C9494FC1AE3C90E5869111447FA19F66F62D904787C973862D08B0056CA891ECA0CA7CF5D38407BA7AF8FC9A83624CDC46B9A47B0FEBAD0FC730D80C7C1CA1088731D758C6D26A3A2DF7BD7634EEA107B6D752609A16C9C671758A853135214425C58B0DD25779DFB070B08817F0828C72BEF11A5D7F998D0F81D49";
        String poHex = "10001";
        
        String data = "{\"txncode\":\"charge\",\"cardno\":\"2253123456781234\"}";
        String signValue = "8501C57883A26FFDA679403816E31BC3B455C5E04BDE47711BAF50A926151BEB0BE895E2563C1BE205D5E2054A1321FFF5A09D39677A4EE7A856AB1FD74F0A38641A868C5C7A781F89D2496D978DDF6B63E44110C52D58E5D1106271A75083A4E948D19B7BB9BE0040D85536FDAC1649015C2049586590CF878DFE110507E87A";
        PublicKey publicKey = (PublicKey) getKey(moHex, poHex, PUBLIC_KEY);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes("UTF-8"));
        
        System.out.println(signature.verify(HexUtil.hexToByteArray(signValue)));
    }
    
    /**
     * 验证签名与验签
     */
    @Test
    public void testSignAndVerifySign() throws Exception {
        // 私钥模
        String priModule = "A20BAEB059D76DA58D85769193B21172AA795BD63A53238BBD2013E0AF914E7B8AA4E58D475F9BCF771F48B7B169CD36F3023C6CD9006E6A93038ED444D914709343F4EA77868E349F9E98F3EF812F5D89D762CE26790EC0D82ADDA4991B946CF04F68D432A1F76DA5A4D646590861BBBDC43E5A356EDF1870B58B8C92D84137";
        // 私钥指数
        String priExponent = "360192BCEF945ED4B82FECEE63BD0B179E3D482043DC0592C2FB77E4805EFB3B2D5FFBA9744A2711A3EFE73A6184938266C72E0050EA55E26B10DA7F605AC0F6A353924A65D554540D12BD2DB58486D564875B12A926BEBCE3A4699AEF033F728236F614FFC24932D0BFB8D9C5F2FE919A75C409786B094AA77EEFE996F97B81";
        // 公钥密钥
        String pubKey = "30819F300D06092A864886F70D010101050003818D0030818902818100A20BAEB059D76DA58D85769193B21172AA795BD63A53238BBD2013E0AF914E7B8AA4E58D475F9BCF771F48B7B169CD36F3023C6CD9006E6A93038ED444D914709343F4EA77868E349F9E98F3EF812F5D89D762CE26790EC0D82ADDA4991B946CF04F68D432A1F76DA5A4D646590861BBBDC43E5A356EDF1870B58B8C92D841370203010001";
        
        String data = "{key:helloworld}";
        PrivateKey priKey = (PrivateKey) getKey(priModule, priExponent, PRIVATE_KEY);
        // 使用原始的私钥签名
        String signStr = RSAUtil.getSignHex(data, priKey, "SHA256WithRSA");
        System.out.println("---------------签名后的数据(16进制)---------------");
        System.out.println(signStr);
        
        // 验签
        boolean result = RSAUtil.verifySign(HexUtil.hexToByteArray(signStr.toCharArray()), data,
                (PublicKey) getKey(HexUtil.hexToByteArray(pubKey), PUBLIC_KEY), "SHA256WithRSA");
        System.out.println("---------------验签结果---------------");
        System.out.println(result);
    }
    
    @Test
    public void testSignAndVerifySignBase64() throws Exception {
        // 私钥
        String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYEugBikCpa+0zyGz7OenqTHaQ8kk/XLdE9aP+lOuW+it15Q07WQFWios8Wanvj82HJWyIoeGKxJLXoF88D+9L4BuiEWYFL4ntisuox71hFrQ5IWTwTt46SDow6wBKGUIJ1hMOXj2x4f5509vZw7nHyo49E8Y9Jo8wsmCymlNsY1oi1JAwHPq+N8tzp0On9TSl+4BSor5HB3W9xHqo2Z7jSoZR5We/lA7/ISA+Wo/zu8AnqV30dO/+afWXXRwC+ZtMioE/Kb9QmYIxFo6XAAdqu22ZADda2zcTG5DWlcWTwSYTVwxUZ/VlLx3ZaXWwl79j1z+E9U2mLavQIN/yiLCnAgMBAAECggEAFyYJmvJolWGQYemIbVWLfdbV1YqMC9EZNK4PoJkao4jBRNjxAq+xvHKqby1W6NiC+KeBtequYmk50QjgdkwWqP4cNrdfSDrMTBRsm+yLr1O9a1TiAJKA0W5dLUUYbi8aFlU34WAHFCTKj9DoDmX0yazTqghwjK5p7Ye5oHZNbJoWk9v9otaz0AmOsXJqLLDQ4Qm3/5iVmsRjS2Nb7dIEiUJPP57OiM+soJI/KXPlSlUh8vFGK+tdk3G22qtnYvLg8DWVnUy6acRED0J81eacktq2bEQgq0mc0BBh0zXTKN5cFr19LYRGd5N9CjetcR3cTof91jA+DajaSHW0uwUwQQKBgQDcuDED0JiP95Yq881efA1gpHRspPrX93z6UN8hgN/5794f5/x5vIKUv9/z/9u30myJa6f29Fe+D7v22x1/QmSdXNWWrclhTeACLUWuIydmP/A1/9s5RR3ocQDI0NFmfpWdKyi/CRihj5XgdjcozNzDxZdKUNNC+zWi+fSEL8xanQKBgQCwYb7QWuO1H8u+mbCJ1tZwzVEO7EFTPDE6w5Bf5oocJFAOf7iHK46RBz0S5e+VC3x5P09ijleqQ7BmS7CtAz52xkaGT59mQs9RHiFzoy4sUWg7uvPNCPPplhB33b51ssu9vdme7G1nPQQULrnNYeaSMLfxJP2YlDWfsW0ZBZyjEwKBgFTjDg7Oie5MpVdtnp1pDmgNt60VIlgvGcTXl4k2HG3un7obqZFUO9BjnwkjbOhcqFuNqPKkdJ5nRL4i9Uw9R57x1j92JQ4kOjP8H2FiqcN8cbEC7tguydrcoxWYogBOTNI+iBQ4tl1kcJr8PCiX6YvtkUsGnMmU/cLpaO3xyCFNAoGBAJ6On9vpeL1/4cZrnpmquYcZ6v5BP9A1UgQDfavYMNtvgVAnXJiWfUi+exacAS2476KHWTO9xezXgKHU9PPy8JoRT6C5vw9Mi9XeAjBTxtQsMklPnI76t1mKfy9h9S8VFbZyDkmhPBAMd7g08O5CTeWpShHLMPWSdkoiU3Kk6tRlAoGAeEjS48sIFw+hfyoTp/YlkIfvdCUYXpyhdpUpQCdY1HUWrf9GlMeDt2jNii8pQ4HBEwXWpkievcQv4res5uFpzb7Py6LC9jANz6gBbyvfMt08aB9bp08Z+HowN9atsqXWl3DaFi+ois3Qr1DJj4Qbohr8Jv7tdDRjTiOB7zqNsJ4=";
        // 公钥
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmBLoAYpAqWvtM8hs+znp6kx2kPJJP1y3RPWj/pTrlvordeUNO1kBVoqLPFmp74/NhyVsiKHhisSS16BfPA/vS+AbohFmBS+J7YrLqMe9YRa0OSFk8E7eOkg6MOsAShlCCdYTDl49seH+edPb2cO5x8qOPRPGPSaPMLJgsppTbGNaItSQMBz6vjfLc6dDp/U0pfuAUqK+Rwd1vcR6qNme40qGUeVnv5QO/yEgPlqP87vAJ6ld9HTv/mn1l10cAvmbTIqBPym/UJmCMRaOlwAHarttmQA3Wts3ExuQ1pXFk8EmE1cMVGf1ZS8d2Wl1sJe/Y9c/hPVNpi2r0CDf8oiwpwIDAQAB";
        
        String data = "{\"postTime\":\"2021-04-02 18:05:44\",\"amount\":\"1\",\"phoneNumber\":\"13299111233\",\"sourceType\":\"unicom\",\"deviceModel\":\"HUAWEI\",\"cardNo\":\"7100310192992614\",\"couponCode\":\"4XPZVERTXA\",\"expDate\":\"2021-04-30\",\"serialNo\":\"C800B44B02814F0C8E13E8C71BAB6FF6\"}";
        PrivateKey privateKey = (PrivateKey) getKey(java.util.Base64.getDecoder().decode(priKey), PRIVATE_KEY);
        // 使用原始的私钥签名
        String signStr = RSAUtil.getSignBase64(data, privateKey, "SHA256WithRSA");
        System.out.println("---------------签名后的数据(Base64)---------------");
        System.out.println(signStr);
        
        // 验签
        boolean result = RSAUtil.verifySign(java.util.Base64.getDecoder().decode(signStr), data,
                (PublicKey) getKey(java.util.Base64.getDecoder().decode(pubKey), PUBLIC_KEY), "SHA256WithRSA");
        System.out.println("---------------验签结果---------------");
        System.out.println(result);
    }
}
