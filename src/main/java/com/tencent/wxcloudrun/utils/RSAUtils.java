package com.tencent.wxcloudrun.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zero
 * @Data: 2022/12/14 16:20
 * @Motto: Nothing is impossible
 */
public class RSAUtils {

    // 字符编码格式
    public static final String CHARSET = "UTF-8";

    // 算法加密方式
    public static final String RSA_ALGORITHM = "RSA";

    // 公钥 （进行加密）
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9CB6yRir7apVzg4HYtPaDHgbKJzXet8qNzmTNmscpqnAtnV3blHY+p9CgJv6FokLgTrn3IaOE7puC3bE7Zuj65VI+ZIbugxV4pzroOUjZ48oisCKgOY4HdUFiIhp/kfuPwocQ4qSsxRClWL3NiZj/AR9ZnvpZTriIA5xggG0xEQIDAQAB";

    // 私钥 （进行解密）
    private static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL0IHrJGKvtqlXODgdi09oMeBsonNd63yo3OZM2axymqcC2dXduUdj6n0KAm/oWiQuBOufcho4Tum4LdsTtm6PrlUj5khu6DFXinOug5SNnjyiKwIqA5jgd1QWIiGn+R+4/ChxDipKzFEKVYvc2JmP8BH1me+llOuIgDnGCAbTERAgMBAAECgYEAmnopQ7zt4Tov2g9tLGBuV6ZRHCDA+DowMISrgg5rldQwCkbg7dG8c+WGK2rScviwOEh91sTJsZegxGhRLt8hooYSjEaz/GPYSrPpFpr3vgwccQA0OPcLxeWRv5SRGBisv/a9nwGq0pilD9MQB6rk/1nXB79FnOHJnc8szqmT+7ECQQDz2vFvZbjy5QsgJFgrjg7AcI4oQj9YJeIPr0UkPz8oKExpzp6ZpZ4rZi/kNibDRbpWtr/e9dYuK4AYs0xoFELVAkEAxnIy4H0WzN4P1H7m4F765mwbVPp8WX/oUle67LF1EgmPb7HnZIPslXh/Gb15m/n6PhfP0I8wAHhiKgWjtjY7TQJBANM4K7QvyfkqB275zB092O07EQcyCqgReDPZvuvclJn1/eoP/DRdzhkNVCI5/W1askJ13NAE5FXte/DpLR9hnVkCQFViMW3RpIvFR/5gE6qac03NWf04QWSo2icDxFOvDvYR0vt2Cl9/O74cOGO02Kw55GfvCuVrlnW9Vk9KWeQ+dUkCQQC8ToO9bblgYShWYlDT2B9oAOixRF7xa4Z/BtkxDElOgIrAP4/4jTB1y5Au+1pTUP8dIQ99uQeQ2zGH7QVWR/uu";


    /**
     * 创建密钥
     * @param keySize
     * @return
     */
    public static Map<String, String> createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        // map装载公钥和私钥
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        // 返回map
        return keyPairMap;
    }

    /**
     * 获取公钥
     * @param publicKey  密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 获取私钥
     * @param privateKey  密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            //每个Cipher初始化方法使用一个模式参数opmod，并用此模式初始化Cipher对象。此外还有其他参数，包括密钥key、包含密钥的证书certificate、算法参数params和随机源random。
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    //rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;  //最大块
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    /**
     * 根据加密后的密文，使用私钥解密
     * @param encodedData
     * @return
     */
    public static String getDecodedData(String encodedData) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(PRIVATE_KEY));
    }


    public static void main(String[] args) throws Exception {
        // 请求体
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("snCode", "SN_CODE_20221214001");
        requestBody.put("faceUrl", "http://gbb.wubaobao.com");

        // 公钥加密
        String publicJson = RSAUtils.publicEncrypt(JSONUtil.toJsonStr(requestBody), RSAUtils.getPublicKey(PUBLIC_KEY));
        System.out.println("加密后的值：" + publicJson);

        // 私钥解密
        String privateJson = getDecodedData(publicJson);
        System.out.println("解密后的值：" + privateJson);

        cn.hutool.json.JSONObject result = JSONUtil.parseObj(privateJson);
        String snCode = result.getStr("snCode");
        String faceUrl = result.getStr("faceUrl");
        System.out.println("snCode：" + snCode + " / faceUrl：" + faceUrl);
    }
}
