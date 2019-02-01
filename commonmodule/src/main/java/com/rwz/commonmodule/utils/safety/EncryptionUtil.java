package com.rwz.commonmodule.utils.safety;


import android.text.TextUtils;

import com.rwz.commonmodule.utils.app.CalendarUtils;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 加密工具
 * Created by lyy on 14-5-14.
 */
public class EncryptionUtil {

    private static String initRandomCode() {
        return CalendarUtils.formatStringWithDate(new Date(), "yyyyMMDDHHmmss");
    }

    /**
     * 随机数：yyyyMMDDHHmmss格式的日期
     */
    public static String getRandomCode() {
        return initRandomCode();
    }

    public static String getSingCode(Object... params) {
        StringBuilder sb = new StringBuilder();
        if (params == null) {
            return "";
        }
        for (Object param : params) {
            sb.append(param);
        }
        return encodeSHA1ToString(sb.toString());
    }

    /** h5_url加密 **/
    public static String getSingCodeToUrl(Object... params) {
        StringBuilder sb = new StringBuilder();
        if (params == null) {
            return "";
        }
        for (Object param : params) {
            sb.append(param);
        }
        return encodeSHA1ToString(sb.toString());
    }

    public static byte[] encodeSha1(String str) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            return sha1.digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeMD5(String strSrc) {
        byte[] returnByte = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            returnByte = md5.digest(strSrc.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnByte;
    }

    /**
     * 把中文字符串转换为十六进制Unicode编码字符串
     */
    public static String stringToUnicode(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255)
                str.append("\\u").append(Integer.toHexString(ch));
            else
                str.append("\\").append(Integer.toHexString(ch));
        }
        return str.toString();
    }

    public static String encodeMD5ToString(String src) {
        if(TextUtils.isEmpty(src))
            return src;
        String s = bytesToHexString(encodeMD5(src));
        LogUtil.d("encodeMD5ToString", "src = " + src, "\n result = " + s);
        return s;
    }

    public static String encodeSHA1ToString(String src) {
        if (TextUtils.isEmpty(src)) {
            return src;
        }
        return bytesToHexString(encodeSha1(src));
    }

    private static String bytesToHexString(byte[] bytes) {
        if(bytes == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * AES加密
     * @param key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public static String encodeAES(String encrypted, String key, String ivs) {
        try {
            return AESOperator.encrypt(encrypted, key, ivs);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * AES解密
     * @param encrypted 解密字符串
     * @param key 解密key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     * @param iv 向量
     * @return
     */
    public static String decodeAES(String encrypted, String key, String iv) {
        try {
            return AESOperator.decrypt(encrypted, key, iv);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private final static String DES = "DES";
    private final static String ENCODE = "UTF-8";
    /**
     * Description 根据键值进行加密
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encodeDES(String express, String key) throws Exception {
        if (express == null) return null;
        byte[] encryptDES = encryptDES(express.getBytes(ENCODE), key.getBytes(ENCODE));
        return new sun.misc.BASE64Encoder().encode(encryptDES);//返回密文
    }

    public static String encodeBase64( byte[] bytes) {
        return new sun.misc.BASE64Encoder().encode(bytes);
    }

    public static byte[] decodeBase64(String str) {
        try {
            return new sun.misc.BASE64Decoder().decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description 根据键值进行解密
     * @param key 加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decryptDES(String cipher, String key) throws IOException, Exception {
        if (cipher == null) return null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(cipher);
        byte[] bExpress = decryptDES(buf, key.getBytes(ENCODE));
        return new String(bExpress, ENCODE);
    }

    /**
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encryptDES(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decryptDES(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
        return cipher.doFinal(data);
    }

}
