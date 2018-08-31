package com.kuliao.oa.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
@Component
public class Coder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Coder.class);

    private static String appName;

    @Value("${spring.application.name}")
    public void setAppName(String appName) {
        Coder.appName = appName;
    }

    public static String getAppName() {
        return appName;
    }

    /**
     * DES加密
     * @param datasource
     * @return
     */
    public static String encode(String datasource,String key){
        try {
            SecureRandom random = new SecureRandom();
            key = getKey(key);
            DESKeySpec desKey = new DESKeySpec(key.getBytes("UTF-8"));
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            byte[] temp = Base64.encodeBase64(cipher.doFinal(datasource.getBytes()));
            return new String(temp);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * DES解密
     * @return
     */
    public static String decode(String src,String key) throws Exception {
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
        return new String(cipher.doFinal(Base64.decodeBase64(src)),"UTF-8");
        //return IOUtils.toString(cipher.doFinal(Base64.decodeBase64(src)),"UTF-8");
    }

    /**
     * 加密秘钥必须为8位以上，如果不足八位则进行扩容。
     * @param key
     * @return
     */
    public static String getKey(String key){
        if(key.length()>=8){
            return key;
        }else {
            return getKey(key+Coder.getAppName());

        }
    }



}
