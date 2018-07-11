package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;

@Component
public class Base {
    /**
     * 定义加密方式
     */
    private final static String KEY_SHA = "SHA";
    private final static String KEY_SHA1 = "SHA-1";

    private final static String MAIL_USERNAME = "1585185302@qq.com";
    private final static String MAIL_PASSWORD = "amwouolbmipejahf";

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
    public String encryptSHA(String data) {
        // 验证传入的字符串
        if (data == null || data.equals("")) {
            return "";
        }
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(KEY_SHA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
     * @param
     * @return Boolean
     */
    public int checkToken(HttpServletRequest request) {
        int user_id = -1;
        if (request.getCookies() != null) {
            HashMap hashMap = parseCookies(request.getCookies());
            String token = (String) hashMap.get("dscj");
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);
                Claim claim = jwt.getClaim("user_id");
                user_id = claim.asInt();
            } catch (UnsupportedEncodingException exception) {
                //UTF-8 encoding not supported
            } catch (JWTVerificationException exception) {
                //Invalid signature/claims
                user_id = -1;
            }
        } else {
            user_id = -1;
        }
        return user_id;
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
                    .withClaim("user_id", user.getUser_id())
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

    /**
     * 发送邮件
     */
    public void sendEmail() {
        Properties props = new Properties();
//        props.setProperty("mail.smtp.localhost", "mail.digu.com");
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        // qq邮箱需要通过ssl通道
        props.setProperty("mail.smtp.ssl.enable", "true");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        try {
            msg.setSubject("账户信息");
            // 设置邮件内容
            msg.setText("宝宝已经注册成功！");

            // 设置发件人
            msg.setFrom(new InternetAddress("1585185302@qq.com"));

            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect(MAIL_USERNAME, MAIL_PASSWORD);
            // 发送邮件
            transport.sendMessage(msg, new Address[] {new InternetAddress("keifc02@outlook.com")});
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
