package com.zz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.bean.User;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
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
    public static final String PUBLIC_KEY_HEX = "publicKeyHex";
    public static final String MODULE = "module";
    public static final String EXPONENT = "exponent";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";		// 签名算法

    private static final int KEY_SIZE = 512;                            // 密钥长度(bit)
    private static final int MAX_ENCRYPT_SIZE = KEY_SIZE / 8 -11;       // 最大加密长度(明文长度byte)
    private static final int MAX_DECRYPT_SIZE = KEY_SIZE / 8;           // 最大解密长度(密文长度byte)
    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);

        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }

    /**
     * 从密钥对中获取公钥和私钥的16进制字符串
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
        // 保存私钥数据
        priKeyInfo.put(PRIVATE_KEY, priKey);
        if(priKey instanceof RSAPrivateKey) {
            // 保存RSAPrivateKey的模数和指数
            RSAPrivateKey rsaPriKey = (RSAPrivateKey) priKey;
            String moduleStr = rsaPriKey.getModulus().toString(16);
            priKeyInfo.put(MODULE, moduleStr);

            String exponentStr = rsaPriKey.getPrivateExponent().toString(16);
            priKeyInfo.put(EXPONENT, exponentStr);
        }

        // 保存公钥信息
        Map<String, Object> pubKeyInfo = new HashMap<String, Object>();
        PublicKey pubKey = keyPair.getPublic();
        // 保存原始公钥
        pubKeyInfo.put(PRIVATE_KEY, pubKey);
        byte[] pubByte = pubKey.getEncoded();
        pubKeyInfo.put(PUBLIC_KEY_HEX, HexUtil.byteArrayToHexString(pubByte));
        if(pubKey instanceof RSAPublicKey) {
            RSAPublicKey rsaPubKey = (RSAPublicKey) pubKey;
            // 保存RSAPublicKey的模数和指数
            String moduleStr = rsaPubKey.getModulus().toString(16);
            pubKeyInfo.put(MODULE, moduleStr);
            String exponentStr = rsaPubKey.getPublicExponent().toString(16);
            pubKeyInfo.put(EXPONENT, exponentStr);
        }

        keyResult.put(PUBLIC_KEY, pubKeyInfo);
        keyResult.put(PRIVATE_KEY, priKeyInfo);

        return keyResult;
    }

    /**
     * 使用模数和指数生成公钥或私钥
     * @param module 模数(16进制)
     * @param exponent 指数(16进制)
     * @param type 密钥类型
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
        if(PRIVATE_KEY.equals(type)) {
            spec = new RSAPrivateKeySpec(mo, po);
            reKey = factory.generatePrivate(spec);
        }else if(PUBLIC_KEY.equals(type)) {
            // 创建公钥规则
            spec = new RSAPublicKeySpec(mo, po);
            reKey = factory.generatePublic(spec);
        }else {
            throw new Exception("不正确的密钥类型!");
        }
        return reKey;
    }

    /**
     * 使用私钥对数据进行签名，返回签名后的16进制数据
     * @param data 需要签名的数据
     * @param key 私钥
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

    /**
     * 验签
     * @param signCode 签名后的数据
     * @param data 原始数据
     * @param key 公钥
     * @param model 签名算法
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
     * @param key
     * @param keyType
     * @return
     * @throws Exception
     */
    public static Key getKey(byte[] key, String keyType) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key reKey = null;
        if(PRIVATE_KEY.equals(keyType)) {
            PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(key);
            reKey = factory.generatePrivate(priKeySpec);
        }else if(PUBLIC_KEY.equals(keyType)) {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
            reKey = factory.generatePublic(pubKeySpec);
        }else {
            throw new Exception("未知的密钥类型!");
        }

        return reKey;
    }

    /**
     * 将经过base64编码后的密钥转换为对应的密钥对象
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
     * @param data 需要加密的数据
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
        while(dataLen - offset > 0) {
            if(dataLen - offset > MAX_ENCRYPT_SIZE) {
                temp = cipher.doFinal(data, offset, MAX_ENCRYPT_SIZE);
            }else {
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
     * @param data 需要解密的数据
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
        while(dataLen - offset > 0) {
            if(dataLen - offset > MAX_DECRYPT_SIZE) {
                temp = cipher.doFinal(data, offset, MAX_DECRYPT_SIZE);
            }else {
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

    /** KeyStore对象测试 */
    @Test
    public void testKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

    }

    /**
     * 测试从.pem,.crt文件中获取公私钥进行签名验签
     * 使用jar包：
     */
    /*@Test11
    public void testGetKeyFromPEM(){
        String data = "479010800036299553550E98B1FF7AFCC7C932747A63417DF811";
        String pivateKeyPath = "C:\\Users\\Administrator.SC-201603281046\\Desktop\\日常\\key\\test_1.pem";
        String publicKeyPath = "C:\\Users\\Administrator.SC-201603281046\\Desktop\\日常\\key\\test_public.pem";
        String publicKeyCRT = "C:\\Users\\Administrator.SC-201603281046\\Desktop\\日常\\key\\test_pub.crt";

        InputStreamReader fileReader;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            // 从.pem文件中读取私钥和公钥(无密码)
            fileReader = new InputStreamReader(new FileInputStream(new File(pivateKeyPath)));
            PEMParser parser = new PEMParser(fileReader);
            Object o;
            PrivateKeyInfo keyInfo = null;
            byte[] priKeyByte = null;
            String base64PriKey = null;
            while ((o = parser.readObject()) != null)
            {
                if (o instanceof PrivateKeyInfo)
                {
                    keyInfo = (PrivateKeyInfo) o;
                    priKeyByte = keyInfo.getEncoded();
                    base64PriKey = Base64.encodeBase64String(priKeyByte);
                    System.out.println("--------------私钥信息--------------");
                    System.out.println(Base64.encodeBase64String(priKeyByte));
                    System.out.println("--------------16进制私钥信息--------------");
                    System.out.println(HexUtil.encodeHexStr(priKeyByte));
                }
            }
            fileReader.close();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(priKeyByte);
            KeyFactory factory = KeyFactory.getInstance("RSA");

            PrivateKey realPriKey = factory.generatePrivate(spec);
            System.out.println("-- 签名数据字节长度：" + Base64.decodeBase64(data).length);
            System.out.println("--------------签名前的信息(byte)--------------");
            for(byte b : Base64.decodeBase64(data)) {
                System.out.print(b + ",");
            }
            System.out.println();
            byte[] signByte = RSAUtil.getSignHex(data, base64PriKey, RSAUtil.SIGNATURE_ALGORITHM);
            String signStr = RSACoder.encrypt2Base64String(signByte);
            System.out.println("-- 签名后数据字节长度：" + signByte.length);
            System.out.println("--------------签名后的信息(byte)--------------");
            for(byte b : signByte) {
                System.out.print(b + ",");
            }
            System.out.println("\n--------------签名后的信息--------------");
            System.out.println(signStr);
            // ---------验签
            fileReader = new InputStreamReader(new FileInputStream(new File(publicKeyPath)));

            parser = new PEMParser(fileReader);
            Object pubO;
            SubjectPublicKeyInfo publicKeyInfo = null;
            byte[] pubKeyByte = null;
            String base64PubKey = null;
            while ((pubO = parser.readObject()) != null)
            {
                if (pubO instanceof SubjectPublicKeyInfo)
                {
                    publicKeyInfo = (SubjectPublicKeyInfo) pubO;
                    pubKeyByte = publicKeyInfo.getEncoded();
                    base64PubKey = RSACoder.encrypt2Base64String(pubKeyByte);
                    System.out.println("--------------公钥信息--------------");
                    System.out.println(RSACoder.encrypt2Base64String(pubKeyByte));
                    System.out.println(ByteUtil.byteArrayToHex(pubKeyByte));
                }
            }
            fileReader.close();
            Assert.assertTrue(RSACoder.verify(RSACoder.decryptBase64(data), base64PubKey, signStr));
            System.out.println("验签结果：" + RSACoder.verify(RSACoder.decryptBase64(data), base64PubKey, signStr));

            // 读取.crt文件
            FileInputStream fileIn = new FileInputStream(new File(publicKeyCRT));
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate c = certFactory.generateCertificate(fileIn);

            PublicKey testKey = c.getPublicKey();
            String crtPubKeyStr = RSACoder.encrypt2Base64String(testKey.getEncoded());
            System.out.println("--------------crt文件公钥信息--------------");
            System.out.println(RSACoder.encrypt2Base64String(testKey.getEncoded()));
            System.out.println(ByteUtil.byteArrayToHex(testKey.getEncoded()));
            Assert.assertTrue(ByteUtil.byteArrayToHex(pubKeyByte).equals(ByteUtil.byteArrayToHex(testKey.getEncoded())));
            System.out.println("crt文件与pem文件是否有相同的公钥：" + ByteUtil.byteArrayToHex(pubKeyByte).equals(ByteUtil.byteArrayToHex(testKey.getEncoded())));
            fileIn.close();
            Assert.assertTrue(RSACoder.verify(RSACoder.decryptBase64(data), crtPubKeyStr, signStr));
            System.out.println("验签结果：" + RSACoder.verify(RSACoder.decryptBase64(data), crtPubKeyStr, signStr));

            String testPub = "30819F300D06092A864886F70D010101050003818D0030818902818100AE2E985568FC708C70BF28474C8B6DA8C2D86A25B97997EAADDA22DD7B0642AF1E0BD81CA89D808F41FF07E5EF686879A9040AE68263CCE01B2A2E89030C9EBBE60E096C180CDA4631A65D349064921BF372450A9A3B0C8FD78A79461E6212ABDB7CC2608F66C099D71245DAE50F3D171E16A114069B06E68A61C340AD1FD5750203010001";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
