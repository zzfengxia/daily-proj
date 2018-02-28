package com.zz.utils;

import java.security.MessageDigest;

/**
 * Created by Francis.zz on 2017/11/13.
 */
public class MessageUtils {
    /***
     * MD5加密 生成32位md5码
     *
     * @return
     */
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);

            return HexUtil.encodeHexStr(md5Bytes);
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * SHA-1 HASH
     *
     * @return
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] shaBytes = sha.digest(byteArray);

            return HexUtil.encodeHexStr(shaBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String message = MessageUtils.shaEncode("010501150117100010011002110011011102110311041105110611071108110911101111111211131114111511161117");
        System.out.println(message);
    }
}
