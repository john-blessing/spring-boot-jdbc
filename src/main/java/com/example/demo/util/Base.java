package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;

@Component
public class Base {
    /**
     * 定义加密方式
     */
    private final static String KEY_SHA = "SHA";
    private final static String KEY_SHA1 = "SHA-1";

    /**
     * 全局数组
     */
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * SHA 加密
     * @param data 需要加密的字符串
     * @return 加密之后的字符串
     * @throws Exception
     */
    public String encryptSHA(String data) throws Exception {
        // 验证传入的字符串
        if (data == null || data.equals("")) {
            return "";
        }
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
     */
    public String byteToHexString(byte b) {
        int ret = b;
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * 验证token
     * @param cookies
     * @return Boolean
     */
    public Boolean checkToken(Cookie[] cookies) {
        HashMap hashMap = parseCookies(cookies);
        String token = (String) hashMap.get("dscj");
        boolean flag = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)   //1 sec for nbf and iat
                    .acceptExpiresAt(5)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            flag = true;
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            flag = false;
        }
        return flag;
    }

    /**
     * 创建token
     * @param user
     * @return String
     */
    public String createToken(User user) {
        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("user_id", 123)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        }

        return token;
    }

    /**
     * 解析cookie
     * @param cookies
     * @return HashMap
     */
    public HashMap parseCookies(Cookie[] cookies) {
        HashMap hashMap = new HashMap();
        for (Cookie cookie : cookies) {
            hashMap.put(cookie.getName(), cookie.getValue());
        }

        return hashMap;
    }
}
