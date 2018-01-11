package com.feiwww.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * 基于JDK或BC的AES算法，工作模式采用ECB
 */
public class AESUtil {
    private static final String ENCODING = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";//产生密钥的算法
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//加解密算法 格式：算法/工作模式/填充模式 注意：ECB不使用IV参数
    private static final String ENCRYPT_KEY = "qCyu+oePWjktoTTdaX49a8Kk4TKW1X+m902F8YjkiDs=";//这个是先运行一遍getKey()方法产生的，然后卸载了这里。
    // private static final String ENCRYPT_KEY = "q+OH3IITAj+mrfN0YzuEUg==";//这个是先运行一遍getKey()方法产生的，然后卸载了这里。 初始化密钥长度,128

    /**
     * 产生秘钥（线下产生好，然后配置到项目中）
     * TODO(生成秘钥这个方法，自己写怎么实现，源码中没有注释。。。。。。)
     */
    public static byte[] getKey() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());//在BC中用，JDK下去除
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(256);//初始化密钥长度,128,192,256（选用192和256的时候需要配置无政策限制权限文件--JDK6）
        //keyGenerator.init(128);//初始化密钥长度,128,192,256（选用192和256的时候需要配置无政策限制权限文件--JDK6）
        SecretKey key = keyGenerator.generateKey();//产生秘钥
        return key.getEncoded();
    }

    @Test
    public void Test() throws NoSuchAlgorithmException {
        String key = Base64.encodeBase64String(AESUtil.getKey());
        System.out.println(key);
    }

    /**
     * 还原秘钥：二进制字节数组转换为Java对象
     */
    public static Key toKey(byte[] keyBayte) {
        Security.addProvider(new BouncyCastleProvider());//在BC中用,JDK下去除
        return new SecretKeySpec(keyBayte, KEY_ALGORITHM);
    }

    /**
     * AES加密，并转为16进制字符串或Base64编码字符串
     */
    public static String encrypt(String data, byte[] keyByte) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidKeyException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException {
        Key key = toKey(keyByte);//还原密钥
        //Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);//JDK下用
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM,"BC");//BC下用
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encodedByte = cipher.doFinal(data.getBytes(ENCODING));
        return Base64.encodeBase64String(encodedByte);//借助CC的Base64编码
    }

    /***
     * AES解密
     * @param data 待解密数据为字符串
     * @param keyByte 秘钥
     */
    public static String decrypt(String data, byte[] keyByte) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            UnsupportedEncodingException {
        Key key = toKey(keyByte);//还原密钥
        // Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);//JDK下用
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM,"BC");//BC下用
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decryptByte = cipher.doFinal(Base64.decodeBase64(data));//注意data不可以直接采用data.getByte()方法转化为字节数组，否则会抛异常
        return new String(decryptByte, ENCODING);//这里注意用new String()
    }

    /**
     * 测试
     * 测试通过，由于加密长度的问题。需要修改：java密码扩展无限制权限策略文件
     */
    public static void main(String[] args) throws NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            UnsupportedEncodingException,
            NoSuchProviderException,
            InvalidKeyException {
        String data = "找一个好姑娘做老婆是我的梦 想!";
        /*测试encryptAESex()、decrypt()*/
        System.out.println("原文--》" + data);
        byte[] keyByte3 = Base64.decodeBase64(ENCRYPT_KEY);
        System.out.println("秘钥---》" + Base64.encodeBase64String(keyByte3));//这里将二进制的密钥使用base64加密保存，这也是在实际中使用的方式
        String encodedStr = AESUtil.encrypt(data, keyByte3);
        System.out.println("加密后-->"+encodedStr);
        System.out.println("解密String后-->"+AESUtil.decrypt(encodedStr, keyByte3));
    }

}
