package com.example.ustbdemo.Shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ustbdemo.Model.DataModel.User;
import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Util.ResultUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final long EXPIRE_TIME = 5 * 60 * 1000000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @return 是否正确
     */
//    public static boolean verify(String token, String username, String secret) {
//        try {
//            //根据密码生成JWT效验器
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withClaim("username", username)
//                    .build();
//            //效验TOKEN
//            DecodedJWT jwt = verifier.verify(token.split(" ")[1]);
//            System.out.println("jwtUtil验证成功");
//            return true;
//        } catch (Exception exception) {
//            System.out.println("jwtUtil验证失败");
//            return false;
//        }
//    }

    public static boolean verify(String token) {
        try {
            System.out.println(token);
            String json = TestJWT.dencrty(token);
            System.out.println(json);
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
//    public static String getUsername(String token) {
//        System.out.println(token);
//        try {
//            DecodedJWT jwt = JWT.decode(token.split(" ")[1]);
//            return jwt.getClaim("username").asString();
//        } catch (JWTDecodeException e) {
//            System.out.println(e.toString());
//            return null;
//        } catch (Exception e){
//            System.out.println(e.toString());
//            return null;
//        }
//    }

    public static String getUsername(String token) {
        System.out.println(token);
        try {
            if(token.contains("Bearer")){
                token = token.split(" ")[1];
            }
            String json = TestJWT.dencrty(token);
            System.out.println(json);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            String username = root.path("username").asText();
            return username;
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
     * @return 加密的token
     */
//    public static String sign(String username, String secret) {
//        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
//        Algorithm algorithm = Algorithm.HMAC256(secret);
//        // 附带username信息
//        return JWT.create()
//                .withClaim("username", username)
//                .withExpiresAt(date)
//                .sign(algorithm);
//    }
    public static String sign(String username) {
        Map map = new HashMap();
        map.put("username",username);
        map.put("issuerId", KEY.issueId);
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        String token;
        try {
            JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(map));
            String json=jsonStr.toString();
            token = TestJWT.encrty(json);
        } catch (Exception e){
            token = null;
        }
        return token;
    }
}