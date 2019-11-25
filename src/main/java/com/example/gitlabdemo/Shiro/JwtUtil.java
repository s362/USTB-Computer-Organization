package com.example.gitlabdemo.Shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRE_TIME = 5 * 60 * 1000000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token.split(" ")[1]);
            System.out.println("jwtUtil验证成功");
            return true;
        } catch (Exception exception) {
            System.out.println("jwtUtil验证失败");
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        System.out.println(token);
        try {
            DecodedJWT jwt = JWT.decode(token.split(" ")[1]);
//            System.out.println(jwt);
//            System.out.println(jwt.getClaim("username").asString());
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            System.out.println(e.toString());
            return null;
        } catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}