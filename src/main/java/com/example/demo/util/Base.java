package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Service
public class Base {
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

    public String createToken(User user) {
        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
        }

        return token;
    }

    public HashMap parseCookies(Cookie[] cookies) {
        HashMap hashMap = new HashMap();
        for (Cookie cookie : cookies) {
            hashMap.put(cookie.getName(), cookie.getValue());
        }

        return hashMap;
    }
}
