package com.zz.utils;

import com.zz.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<TLVData> parseTLVString(String hexData) {
        List<TLVData> result = new ArrayList<>();
        int position = 0;
        while(position < hexData.length()) {
            String tag = fetchTag(hexData, position);
            position += tag.length();

            Length len = fetchLength(hexData, position);
            position += len.getLengthOfL();

            String val = hexData.substring(position, position + len.getLengthOfV());
            position += len.getLengthOfV();

            TLVData tlvData = new TLVData(tag, len, val);
            result.add(tlvData);
        }

        return result;
    }

    /**
     * 使用提供的tag和数据原文组装完整TLV格式的数据
     * 数据原文长度超过0x80,L则为0x81LC
     *
     * @param oriV
     * @param tag
     * @return
     */
    public static String assembleTLVDataWithTag(String oriV, String tag) {
        if(StringUtils.isEmpty(tag)) {
            return oriV;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(tag);

        int len = oriV.length() / 2;
        String lenStr = HexUtil.toHexString(len);
        if(len > 0x80) {
            // 长度大于0x80时，bit8为1,bit7-bit1标识实际长度的字节数
            String lt = HexUtil.toHexString((0x7F & (lenStr.length() / 2)) | 0x80);
            sb.append(lt).append(lenStr);
        } else {
            sb.append(lenStr);
        }

        sb.append(oriV);

        return sb.toString();
    }

    /**
     * 使用给定数据替换指定tag的原始数据
     *
     * @param oriTLV 原始TLV数据
     * @param tag    指定tag
     * @param data   替换数据
     * @return
     */
    public static String replaceDataOfTag(String oriTLV, String tag, String data) {
        if(StringUtils.isEmpty(oriTLV)) {
            return assembleTLVDataWithTag(data, tag);
        }

        StringBuilder result = new StringBuilder();
        List<TLVData> tlvDatas = parseTLVString(oriTLV);
        for(TLVData tlvData : tlvDatas) {
            String tlvTag = tlvData.getTag();
            String tlvValue = tlvData.getValue();

            if(tag.equals(tlvTag)) {
                if(tlvData.getLength().getLengthOfV() != data.length()) {
                    throw new BizException("TLV格式错误：替换数据[%s]长度不够 %s", data, tlvData.getLength().getLengthOfV());
                }
                tlvValue = data;
            }

            result.append(assembleTLVDataWithTag(tlvValue, tlvTag));
        }

        return result.toString();
    }

    public static class Length {
        private String originData;  // L原始数据
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

    public static class TLVData {
        private String tag;
        private Length length;
        private String value;

        public TLVData(String tag, Length length, String value) {
            this.tag = tag;
            this.length = length;
            this.value = value;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Length getLength() {
            return length;
        }

        public void setLength(Length length) {
            this.length = length;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
