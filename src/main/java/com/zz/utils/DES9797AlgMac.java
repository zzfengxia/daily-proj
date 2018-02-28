package com.zz.utils;

import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.macs.ISO9797Alg3Mac;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

/**
 * Created by Francis.zz on 2017/7/18.
 * 3des-ecb mac
 */
public class DES9797AlgMac {

    /**
     * 使用双长度DES生成4或8字节的mac信息
     *
     * @param key           密钥
     * @param initVector    初始向量
     * @param message       加密数据
     * @param output        mac输出
     * @param offset        mac输出起始偏移量
     * @param length        mac输出长度
     */
    public static void getMac(String key, String initVector, String message, byte[] output, int offset, int length) {
        getMac(Hex.decode(key), Hex.decode(initVector), Hex.decode(message), output, offset, length);
    }

    /**
     * 使用双长度DES生成4或8字节的mac信息
     *
     * @param key           密钥
     * @param initVector    初始向量
     * @param message       加密数据
     * @param output        mac输出
     * @param offset        mac输出起始偏移量
     * @param length        mac输出长度
     */
    public static void getMac(byte[] key, byte[] initVector, byte[] message, byte[] output, int offset, int length) {
        if(length != 4 && length != 8) {
            throw new IllegalArgumentException("the length of mac must be 4 or 8.");
        }
        // ALG_DES_MAC8_ISO9797_1_M2_ALG3 算法，第二个参数为生成的MAC位数(除以8为字节)，80补位
        ISO9797Alg3Mac isoMac = new ISO9797Alg3Mac(new DESEngine(), 64, new ISO7816d4Padding());
        KeyParameter keyParameter = new KeyParameter(key);
        ParametersWithIV ivParameter = new ParametersWithIV(keyParameter, initVector);
        isoMac.init(ivParameter);
        isoMac.update(message, 0, message.length);

        // 生成Mac结果
        byte[] mac = new byte[8];
        isoMac.doFinal(mac, 0);

        System.arraycopy(mac, 0, output, offset, length);
    }
}
