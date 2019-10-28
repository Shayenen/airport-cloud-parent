package com.platform.airportcloudapply.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACSHA1 {

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

 /*
    * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
     * @return
    * @throws Exception
   *
     public static String initMacKey() throws Exception {
      //得到一个 指定算法密钥的密钥生成器
   KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME);
      //生成一个密钥
      SecretKey secretKey =keyGenerator.generateKey();
      return null;
      }
    */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data=encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), MAC_NAME);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(MAC_NAME);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }
    }
    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    public static void main(String[] args){
        try {
            String s1 = byte2hex(HmacSHA1Encrypt("d5e7ee839781a3a9883a4bb54cb7e0abAdministrator75de96af4aa0a3700a8766db393032ba8e76f0a7", "4dPHcqxpvhmVQ9T"));
            String s2 = genHMAC("bf31d5dfa3b76af369a1ed01fb44ac080b1b392e","4dPHcqxpvhmVQ9T");
            System.out.println(s1);
            System.out.println(s2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}