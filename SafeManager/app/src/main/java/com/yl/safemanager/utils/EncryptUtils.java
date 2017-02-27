package com.yl.safemanager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by YL on 2017/2/27.
 */

public class EncryptUtils {

    // MD5加密
    public static String md5Encrypt(String password) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance("md5");// algorithm
            byte[] bytes = digest.digest(password.getBytes()); // 参数是，明文字节数组，返回的就是加密后的结果，字节数组
            for (byte b : bytes) { // 数byte 类型转换为无符号的整数
                int n = b & 0XFF; // 将整数转换为16进制
                String s = Integer.toHexString(n); // 如果16进制字符串是一位，那么前面补0
                if (s.length() == 1) {
                    sb.append("0" + s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
}
