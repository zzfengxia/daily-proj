/*
package com.zz.dailytest;

import com.zz.utils.HexUtil;
import com.zz.utils.StringUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.DESUtil;

import java.util.Map;

*/
/**
 * Created by Francis.zz on 2016-05-19.
 * 描述：模拟UICC、SZT密钥分散(使用3DES算法) <br/>
 *//*

public class MockKeyDerivation {
    public static final String QA_KEY  = "key";
    public static final String SD_AID  = "sdAid";
    public static final String SEID    = "seId";
    public static final String PADDING = "padding";
    public static final String KEY_VER = "keyVersion";

    */
/**
     * UICC密钥分散
     * @param keyType
     * @param param
     * @return
     *//*

    public static byte[] generateUICCKey(byte keyType, Map param) throws Exception {
        if(StringUtil.isEmpty(param)) {
            throw new Exception("Params for Generated key is none.");
        }
        String keyStr = (String) param.get(QA_KEY);
        String sdAid = (String) param.get(SD_AID);
        String seId = (String) param.get(SEID);
        byte padding = (Byte) param.get(PADDING);
        int ver = (Integer) param.get(KEY_VER);
        byte version = (byte) ver;

        byte[] finalKey = new byte[16];
        byte[] temp = new byte[8];
        // 一级分散，分别加密sdAID的左8个字节和右8个字节，得到16个字节的密钥
        finalKey = uiccKeyDerivation(HexUtil.hexToByteArray(sdAid), HexUtil.hexToByteArray(keyStr));

        // 获取二级分散因子
        byte[] factor = getDerivationFactor(seId, padding, version, keyType);
        // 二级分散，得到最后的分散密钥
        finalKey = uiccKeyDerivation(factor, finalKey);

        return finalKey;
    }

    */
/**
     * SZT密钥分散
     * @param keyType
     * @param param
     * @return
     * @throws Exception
     *//*

    public static byte[] generateSZTKey(byte keyType, Map param) throws Exception {
        if(StringUtil.isEmpty(param)) {
            throw new Exception("Params for Generated key is none.");
        }
        byte[] finalKey = null;
        String keyStr = (String) param.get(QA_KEY);
        String seId = (String) param.get(SEID);
        // 根据keyType获取获取一级分散因子
        byte[] factor = getKsFactorByKeyType(keyType);
        // 使用3DES加密一级分散因子
        byte[] secondKey = DESUtil.des3EncryptECB(HexUtil.hexToByteArray(keyStr), factor);
        // 根据SEID获取二级分散因子
        byte[] factor2 = getKsFactorBySEID(seId);
        // 使用3DES加密二级分散因子
        finalKey = DESUtil.des3EncryptECB(secondKey, factor2);
        System.out.println("分散后的密钥：" + HexUtil.byteArrayToHexString(finalKey));

        return finalKey;
    }

    */
/**
     * 使用3des算法加密分散因子
     * @param factor
     * @param key
     * @return
     * @throws Exception
     *//*

    private static byte[] uiccKeyDerivation(byte[] factor, byte[] key) throws Exception {
        byte[] finalKey = new byte[16];
        byte[] temp = new byte[8];
        System.arraycopy(factor, 0, temp, 0, 8);
        // 加密左8个字节
        temp = DESUtil.des3EncryptECB(key, temp);
        System.arraycopy(temp, 0, finalKey, 0, 8);
        // 加密右8个字节
        System.arraycopy(factor, factor.length - 8, temp, 0, 8);
        temp = DESUtil.des3EncryptECB(key, temp);
        System.arraycopy(temp, 0, finalKey, 8, 8);

        System.out.println("-- 分散后的密钥：" + HexUtil.byteArrayToHexString(finalKey));
        return finalKey;
    }

    */
/**
     * 使用10字节的SEID与8个字节填充+1byte KeyVersion + 1byte keyType组成的10字节数据做异或操作
     * @param seidStr SEID
     * @param padding 填充字节
     * @param keyVer
     * @param keyType
     * @return
     * @throws Exception
     *//*

    private static byte[] getDerivationFactor(String seidStr, byte padding, byte keyVer, byte keyType) throws Exception {
        System.out.println("-- Begin to generate second derivation factor.");
        if(StringUtil.isEmpty(seidStr)) {
            throw new Exception("SE ID is none.");
        }
        byte[] result = new byte[10];
        byte[] temp = new byte[10];
        for(int i = 0; i < 8; i++) {
            temp[i] = padding;
        }
        temp[8] = keyVer;
        temp[9] = keyType;

        byte[] seId = HexUtil.hexToByteArray(seidStr);
        System.out.println("开始异或操作...");
        for(int i = 0; i < 10; i++) {
            result[i] = (byte) (seId[i] ^ temp[i]);
        }
        System.out.println("Generating factor is [" + HexUtil.byteArrayToHexString(result) + "].");

        return result;
    }

    */
/**
     * 根据KeyType获取SZT一级分散因子
     * @param keyType 加密类型
     * @return
     * @throws Exception
     *//*

    public static byte[] getKsFactorByKeyType(byte keyType) throws Exception {
        byte[] factor = null;

        if(keyType == 0x01){
            //factor = ByteUtil.hexToByteArray("535A544430323831");
            factor = new byte[]{0x05,0x03,0x05,0x0A,0x05,0x04,0x04,0x04,0x03,0x00,0x03,0x02,0x03,0x08,0x03,0x01};
        }else if(keyType == 0x02){
            //factor = ByteUtil.hexToByteArray("535A544430323832");
            factor = new byte[]{0x05,0x03,0x05,0x0A,0x05,0x04,0x04,0x04,0x03,0x00,0x03,0x02,0x03,0x08,0x03,0x02};
        }else if(keyType == 0x03){
            //factor = ByteUtil.hexToByteArray("535A544430323833");
            factor = new byte[]{0x05,0x03,0x05,0x0A,0x05,0x04,0x04,0x04,0x03,0x00,0x03,0x02,0x03,0x08,0x03,0x03};
        }else if(keyType == 0x73){
            //factor = ByteUtil.hexToByteArray("535A544430323834");
            factor = new byte[]{0x05,0x03,0x05,0x0A,0x05,0x04,0x04,0x04,0x03,0x00,0x03,0x02,0x03,0x08,0x03,0x04};
        }else{
            throw new Exception("-- Invalid KeyType["+keyType+"] to retrieve factor.");
        }

        System.out.println("-- KeyType["+keyType+"] factor retrieved: " + HexUtil.byteArrayToHexString(factor));
        return factor;
    }

    */
/**
     * 根据seid获取SZT二级分散因子
     * @param seId
     * @return
     *//*

    public static byte[] getKsFactorBySEID(String seId) throws Exception {
        int len = seId == null ? 0 : seId.length();
        if(len >= 16) {
            seId = seId.substring(len - 16);
        }else if(len == 12) {
            seId += "535A";
        }else {
            throw new Exception("SEID 的长度不符合规定。");
        }
        byte[] factor = new byte[seId.length()];
        for(int i = 0; i < seId.length(); i++) {
            factor[i] = HexUtil.hexToByte("0" + seId.charAt(i));
        }
        System.out.println("二级分散因子：" + HexUtil.byteArrayToHexString(factor));
        return factor;
    }
}
*/
