package com.needto.common.utils;

import com.needto.common.exception.LogicException;
import org.springframework.util.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Administrator
 */
public class CryptoUtil {

    public static class KeyPairData{
        public String publicKey;
        public String privateKey;

        public KeyPairData(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    /**
     * rsa 公钥和私钥生成函数
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static KeyPairData genKeyPair() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return new KeyPairData(new String(publicKey.getEncoded(), "utf-8"), new String(privateKey.getEncoded(), "utf-8"));
    }

    /**
     * sha加密
     * @param text
     * @param mode
     * @return
     */
    public static String shaEncrypto(String text, String mode){
        if(text == null || mode == null){
            return text;
        }
        try {
            MessageDigest sha = null;
            sha = MessageDigest.getInstance(mode);

            byte[] byteArray = text.getBytes("UTF-8");
            byte[] md5Bytes = sha.digest(byteArray);
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LogicException("SHA1_ENCRYPTO_FAILTURE", "");
        }
    }

    /**
     * @author Administrator
     */
    public enum Crypto {

        /**
         * md5
         */
        MD5("1"){
            @Override
            public String encry(String text, String key) {
                if(text == null){
                    return null;
                }
                String temp = Utils.nullToString(text) + Utils.nullToString(key);
                return DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public String decode(String text, String key) {
                throw new LogicException("NO_SUPPORT", "");
            }
        },
        /**
         * rsa
         */
        RSA("2"){
            @Override
            public String encry(String text, String key) {
                try {
                    RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key.getBytes("utf-8")));
                    //RSA加密
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                    return new String(cipher.doFinal(text.getBytes("UTF-8")), "utf-8");
                } catch (InvalidKeySpecException | NoSuchAlgorithmException | UnsupportedEncodingException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException | NoSuchPaddingException e) {
                    e.printStackTrace();
                    throw new LogicException("RSA_ENCRYPTO_FAILTURE", "");
                }
            }

            @Override
            public String decode(String text, String key) {
                try {
                    RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key.getBytes("utf-8")));
                    //RSA解密
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, priKey);
                    return new String(cipher.doFinal(text.getBytes("utf-8")), "utf-8");
                } catch (InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                    throw new LogicException("RSA_DECRYPTO_FAILTURE", "");
                }
            }
        },
        /**
         * base64
         */
        BASE64("3"){
            @Override
            public String encry(String text, String key) {
                if(text == null){
                    return null;
                }
                return new String(Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            }

            @Override
            public String decode(String text, String key) {
                if(text == null){
                    return null;
                }
                return new String(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            }
        },
        AES_128("4"){
            @Override
            public String encry(String text, String key) {
                if(text == null || key == null){
                    return null;
                }
                try {
                    // 创建AES的Key生产者
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
                    // 利用用户密码作为随机数初始化出
                    kgen.init(128, new SecureRandom(key.getBytes()));
                    // 128位的key生产者
                    //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
                    // 根据用户密码，生成一个密钥
                    SecretKey secretKey = kgen.generateKey();
                    // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
                    byte[] enCodeFormat = secretKey.getEncoded();
                    // 转换为AES专用密钥
                    SecretKeySpec aeskey = new SecretKeySpec(enCodeFormat, "AES");
                    // 创建密码器
                    Cipher cipher = Cipher.getInstance("AES");
                    byte[] byteContent = text.getBytes("utf-8");
                    // 初始化为加密模式的密码器
                    cipher.init(Cipher.ENCRYPT_MODE, aeskey);
                    // 加密
                    byte[] bytes = cipher.doFinal(byteContent);
                    return new String(bytes, Charset.forName("utf-8"));
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                    throw new LogicException("AES_ENCRYPTO_FAILTURE", "");
                }
            }

            @Override
            public String decode(String text, String key){
                if(text == null || key == null){
                    return null;
                }
                try {
                    // 创建AES的Key生产者
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
                    kgen.init(128, new SecureRandom(key.getBytes()));
                    // 根据用户密码，生成一个密钥
                    SecretKey secretKey = kgen.generateKey();
                    // 返回基本编码格式的密钥
                    byte[] enCodeFormat = secretKey.getEncoded();
                    // 转换为AES专用密钥
                    SecretKeySpec aeskey = new SecretKeySpec(enCodeFormat, "AES");
                    // 创建密码器
                    Cipher cipher = Cipher.getInstance("AES");
                    // 初始化为解密模式的密码器
                    cipher.init(Cipher.DECRYPT_MODE, aeskey);
                    byte[] bytes = cipher.doFinal(text.getBytes("utf-8"));
                    return new String(bytes, Charset.forName("utf-8"));
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new LogicException("AES_DECRYPTO_FAILTURE", "");
                }
            }
        },
        DES("5"){
            @Override
            public String encry(String text, String key) {
                if(text == null || key == null){
                    return null;
                }
                try{
                    SecureRandom random = new SecureRandom();
                    DESKeySpec desKey = new DESKeySpec(key.getBytes());
                    //创建一个密匙工厂，然后用它把DESKeySpec转换成
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey securekey = keyFactory.generateSecret(desKey);
                    //Cipher对象实际完成加密操作
                    Cipher cipher = Cipher.getInstance("DES");
                    //用密匙初始化Cipher对象
                    cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
                    //现在，获取数据并加密
                    //正式执行加密操作
                    return new String(cipher.doFinal(text.getBytes("utf-8")), "utf-8");
                }catch(Throwable e){
                    e.printStackTrace();
                    throw new LogicException("DES_ENCRYPTO_FAILTURE", "");
                }
            }

            @Override
            public String decode(String text, String key) {
                if(text == null || key == null){
                    return null;
                }
                try {
                    // DES算法要求有一个可信任的随机数源
                    SecureRandom random = new SecureRandom();
                    // 创建一个DESKeySpec对象
                    DESKeySpec desKey = new DESKeySpec(key.getBytes());
                    // 创建一个密匙工厂
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    // 将DESKeySpec对象转换成SecretKey对象
                    SecretKey securekey = keyFactory.generateSecret(desKey);
                    // Cipher对象实际完成解密操作
                    Cipher cipher = Cipher.getInstance("DES");
                    // 用密匙初始化Cipher对象
                    cipher.init(Cipher.DECRYPT_MODE, securekey, random);
                    // 真正开始解密操作
                    return new String(cipher.doFinal(text.getBytes("utf-8")), "utf-8");
                } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    throw new LogicException("DES_DECRYPTO_FAILTURE", "");
                }

            }
        },
        SHA1("6"){
            @Override
            public String encry(String text, String key) {
                return shaEncrypto(text, "SHA");
            }

            @Override
            public String decode(String text, String key) {
                throw new LogicException("NO_SUPPORT", "");
            }
        },
        SHA256("7"){
            @Override
            public String encry(String text, String key) {
                return shaEncrypto(text, "SHA-256");
            }

            @Override
            public String decode(String text, String key) {
                throw new LogicException("NO_SUPPORT", "");
            }
        },
        SHA512("8"){
            @Override
            public String encry(String text, String key) {
                return shaEncrypto(text, "SHA-512");
            }

            @Override
            public String decode(String text, String key) {
                throw new LogicException("NO_SUPPORT", "");
            }
        };
        public String key;
        Crypto(String key){
            this.key = key;
        }
        public static boolean contain(String key){
            for(Crypto encry : Crypto.values()){
                if(encry.key.equals(key)){
                    return true;
                }
            }
            return false;
        }

        /**
         * 根据key获取加密对象
         * @param key
         * @return
         */
        public static Crypto getCrypto(String key){
            for(Crypto encry : Crypto.values()){
                if(encry.key.equals(key)){
                    return encry;
                }
            }
            return null;
        }

        /**
         * 全部使用utf-8，若不相同需要更改编码后再进行编码
         * @param text
         * @param key
         * @return
         */
        abstract public String encry(String text, String key);
        /**
         * 全部使用utf-8，若不相同需要更改编码后再进行解码
         * @param text
         * @param key
         * @return
         */
        abstract public String decode(String text, String key);
    }
}
