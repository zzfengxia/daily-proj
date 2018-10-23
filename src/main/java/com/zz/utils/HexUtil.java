package com.zz.utils;

import com.zz.exception.BizException;

/**
 * Created by zengzheng on 2016-04-25.
 * 描述：16进制与字节数组的转换 <br/>
 */
public class HexUtil {

    public static final int CONVERT_MODEL_UPPER = 0;

    /**
     * 将字节数组转换为16进制字符数据
     *
     * @param data
     * @return
     */
    public static char[] byteArrayToCharArray(byte[] data) {
        if(data == null || data.length == 0) {
            throw new BizException("不存在需要转换的数据!");
        }
        int dataLen = data.length;
        char[] enChar = new char[dataLen << 1];
        /**
         * 将十进制数字与0xf0作与运算并右移4位的结果作为16进制的高位并转为16进制字符
         * 与0x0f作与运算后的结果作为16进制的低位并转为16进制字符
         */
        for(int i = 0, j = 0; i < dataLen; i++) {
            enChar[j++] = Character.forDigit((0xf0 & data[i]) >>> 4, 16);
            enChar[j++] = Character.forDigit(0x0f & data[i], 16);
        }

        return enChar;
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param data
     * @param type 大写或小写
     * @return
     */
    public static String byteArrayToHexString(byte[] data, int type) {
        String result;
        if(type == CONVERT_MODEL_UPPER) {
            result = new String(byteArrayToCharArray(data)).toUpperCase();
        }else {
            result = new String(byteArrayToCharArray(data)).toLowerCase();
        }

        return result;
    }

    /**
     * 将字节数组转换为16进制字符串
     * @param data
     * @return
     */
    public static String byteArrayToHexString(byte[] data) {
        return byteArrayToHexString(data, CONVERT_MODEL_UPPER);
    }

    /**
     * 16进制字符串解码为字节数组
     *
     * @param data
     * @return
     */
    public static byte[] hexToByteArray(char[] data) {
        if(data == null || data.length == 0) {
            throw new IllegalArgumentException("不存在需要解析的数据!");
        }
        int dataLen = data.length;
        if((dataLen & 0x01) != 0) {
            throw new IllegalArgumentException("需要解析的字符长度应为偶数!");
        }
        byte[] deByte = new byte[dataLen >> 1];
        for(int i = 0, j = 0; j < dataLen; i++) {
            int hexHigh = Character.digit(data[j++], 16) << 4;
            int hexLow = Character.digit(data[j++], 16);
            deByte[i] = (byte) ((hexHigh | hexLow) & 0xff);
        }
        return deByte;
    }

    /**
     * 16进制字符串解码为字节数组
     *
     * @param data
     * @return
     */
    public static byte[] hexToByteArray(String data) {
        return hexToByteArray(data.toCharArray());
    }

    /**
     * 16进制字符串转换为字节
     *
     * @param data
     * @return
     */
    public static byte hexToByte(String data) {
        if(data == null || data.length() != 2) {
            throw new IllegalArgumentException("Hex String is invalid.");
        }
        return hexToByteArray(data)[0];
    }

    /**
     * 大端转为10进制
     * 最多4个字节，因为int只有4个字节，最大值7FFFFFFF
     */
    public static int bigEndianToInt(byte[] bigByte) {
        if(bigByte.length > 4)
            throw new IllegalArgumentException("more than 4 bytes");
        if(bigByte.length == 4 && (bigByte[0] & 0xFF) > 0x7F )
            throw new NumberFormatException("more than maximum limit");

        int resInt = 0;

        int i = bigByte.length - 1;
        for (byte b : bigByte) {
            resInt |= (b & 0xFF) << (8 * i--);
        }

        return resInt;
    }

    /**
     * 大端转为10进制long
     * 最大值7FFFFFFFFFFFFFFF
     *
     * @param bigByte
     * @return
     */
    public static long bigEndianToLong(byte[] bigByte) {
        if(bigByte.length > 8)
            throw new IllegalArgumentException("more than 8 bytes");
        if(bigByte.length == 8 && (bigByte[0] & 0xFF) > 0x7F )
            throw new NumberFormatException("more than maximum limit");

        long resLong = 0;
        int i = bigByte.length - 1;
        for(byte b : bigByte) {
            resLong |= (long) (b & 0xFF) << (8 * i--);
        }

        return resLong;
    }

    /**
     * int转为16进制字符串(自动补位)
     */
    public static String toHexString(int data) {
        String temp = Integer.toHexString(data);
        if(temp.length() % 2 != 0) {
            temp = "0" + temp;
        }
        return temp.toUpperCase();
    }

    public static String toHexString(int data, int length) {
        String temp = Integer.toHexString(data);
        if(temp.length() < length) {
            StringBuilder sb = new StringBuilder();
            int l = length - temp.length();
            for(int i = 0; i < l; i++) {
                sb.append("0");
            }
            return sb.append(temp).toString().toUpperCase();
        }
        return temp.toUpperCase();
    }
}
