package com.zz.utils;

import java.util.*;

/**
 * Created by Francis.zz on 2018/1/13.
 * TLV：TLV格式数据是指由Tag，Length，Value组成的数据。具体说明如下：
 * tag标签的属性为bit，由16进制表示，占1～2个字节长度。
 * 若tag标签的第一个字节的低四个bit为“1111”，则说明该tag占两个字节，例如“9F33”；否则占一个字节，例“95”。
 *
 * 子域长度（即L本身）的属性也为bit，占1～3个字节长度。具体编码规则如下：
 * a)  当 L 字段最左边字节的最高位（bit8）为 0，表示该 L 字段占一个字节，它的后续7个bit位（即 bit7～bit1）表示子域取值的长度。
 * 例如，某个域取值占 3 个字节，那么其子域取值长度表示为“00000011”。所以，若子域取值的长度在 1～127 字节之间，那么该 L 字段本身仅占一个字节。
 * b)  当 L 字段最左边字节的最左 bit 位（即 bit8）为 1，表示该 L 字段不止占一个字节，那么它到底占几个字节由该最左字节的后续 7 个 bit 位（即 bit7～bit1）的十进制取值表示。
 * 例如，若最左字节为 10000010，表示 L 字段除该字节外，后面还有两个字节。其后续字节的十进制
 * 取值表示子域取值的长度。例如，若 L 字段为“1000 0001 1111 1111”，表示该子域取值占255 个字节。所以，若子域取值的长度在 127～255 字节之间，那么该 L 字段本身需占两个字节。
 */
public class TLVParseUtils {
    /**
     * 获取16进制TVL字符串的TAG
     * 若tag标签的第一个字节的低四个bit为“1111”，则说明该tag占两个字节，否则占一个字节
     *
     * @param hexData
     * @param position 当前解析位置
     * @return 解析出的tag
     */
    public static String fetchTag(String hexData, int position) {
        String tag;
        String firstByte = hexData.substring(position, position + 2);
        int firstInt = Integer.parseInt(firstByte, 16);
        if((firstInt & 0x0f) == 0x0f) {
            // tag两个字节
            tag = hexData.substring(position, position + 4);
        }else {
            // tag一个字节
            tag = firstByte;
        }

        return tag;
    }

    /**
     * 获取16进制TVL字符串的L
     * 当L字段最左边字节的最高位（bit8）为 0，表示该L字段占一个字节，bit7～bit1表示V的长度。
     * 当 L 字段最左边字节的最左 bit 位（即 bit8）为 1，bit7~bit1标识L长度的后续字节数
     *
     * @param hexData
     * @param position 当前解析位置
     * @return arg[0] 表示V的长度(十进制)，arg[1]表示L的长度(总长，十进制)
     */
    public static int[] parseLength(String hexData, int position) {
        Length lObj = fetchLength(hexData, position);

        return new int[]{lObj.getLengthOfV(), lObj.getLengthOfL()};
    }

    /**
     * 获取16进制TVL字符串的L
     * 当L字段最左边字节的最高位（bit8）为 0，表示该L字段占一个字节，bit7～bit1表示V的长度。
     * 当 L 字段最左边字节的最左 bit 位（即 bit8）为 1，bit7~bit1标识L长度的后续字节数
     *
     * @param hexData
     * @param position 当前解析位置
     * @return
     */
    public static Length fetchLength(String hexData, int position) {
        Length result = new Length();
        String firstByte = hexData.substring(position, position + 2);
        int firstInt = Integer.parseInt(firstByte, 16);
        if((firstInt & 0xf0) >>> 7 == 1) {
            int len = firstInt & 127;
            // V的长度字节
            String lenStr = hexData.substring(position + 2, position + 2 + len * 2);
            int vl = Integer.parseInt(lenStr, 16);
            result.setLengthOfV(vl * 2);
            result.setOriginData(firstByte + lenStr);
        }else {
            // 单字节表示长度
            result.setOriginData(firstByte);
            result.setLengthOfV(firstInt * 2);
        }

        return result;
    }

    /**
     * 解析TVL数据,数据不存在重复TAG时可使用
     *
     * @param hexData
     * @return key:T，value:V
     */
    public static Map<String, String> parseTLVStringToMap(String hexData) {
        Map<String, String> result = new HashMap<>();
        int position = 0;
        while(position < hexData.length()) {
            String tag = fetchTag(hexData, position);
            position += tag.length();

            Length len = fetchLength(hexData, position);
            position += len.getLengthOfL();

            String val = hexData.substring(position, position + len.getLengthOfV());
            position += len.getLengthOfV();

            result.put(tag, val);
        }

        return result;
    }

    /**
     * 解析TVL数据
     *
     * @param hexData
     * @return V
     */
    public static List<String> parseTLVStringToList(String hexData) {
        List<String> result = new ArrayList<>();
        int position = 0;
        while(position < hexData.length()) {
            String tag = fetchTag(hexData, position);
            position += tag.length();

            Length len = fetchLength(hexData, position);
            position += len.getLengthOfL();

            String val = hexData.substring(position, position + len.getLengthOfV());
            position += len.getLengthOfV();

            result.add(val);
        }

        return result;
    }

    public static class Length {
        private String originData;  // L原始数据
        private int lengthOfL;      // L长度
        private int lengthOfV;      // V长度

        public String getOriginData() {
            return originData;
        }

        public void setOriginData(String originData) {
            this.originData = originData;
        }

        public int getLengthOfL() {
            return originData.length();
        }

        public int getLengthOfV() {
            return lengthOfV;
        }

        public void setLengthOfV(int lengthOfV) {
            this.lengthOfV = lengthOfV;
        }
    }

    public static void main(String[] args) {
        List<String> result = TLVParseUtils.parseTLVStringToList("70369F4701039F48009F49039F37049F4A01825F300202209F080200309F631001691210FFFFFFFF10FFFFFFFFFFFFFF9F1401009F230100");
        for(String s : result) {
            System.out.println(s);
        }

        Map<String, String> result2 = TLVParseUtils.parseTLVStringToMap("9F4701039F48009F49039F37049F4A01825F300202209F080200309F631001691210FFFFFFFF10FFFFFFFFFFFFFF9F1401009F230100");
        Set<String> keyset = result2.keySet();
        for(String s : keyset) {
            System.out.println(s + " " + result2.get(s));
        }
    }
}
