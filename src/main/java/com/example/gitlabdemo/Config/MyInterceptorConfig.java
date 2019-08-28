package com.example.gitlabdemo.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gitlabdemo.Model.User;
import com.example.gitlabdemo.Util.GitProcess;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Configuration
public class MyInterceptorConfig implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse resp, Object handler) throws Exception {
        Map<String, Claim> claims;
        try{
            String token = request.getHeader("Authorization").split(" ")[1];
            System.out.println(token);
            Algorithm algorithm = Algorithm.HMAC256("jwt");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String subject = jwt.getSubject();
            List<String> audience = jwt.getAudience();
            System.out.println(audience);
            claims = jwt.getClaims();

//            System.out.println(claims.get("user_id").asString());
//            System.out.println(claims);
//            System.out.println(request.getQueryString().split("=")[1]);
        } catch (Exception e){
            System.out.println(e.toString());
            return false;
        }
        String user_id;
        String task_id;
        try{
            task_id = request.getQueryString().split("=")[1];
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("task_id error");
            return false;
        }
        try{
            user_id = claims.get("user_id").asString();
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("user_id error");
            return false;
        }

        User user = new User(task_id, user_id);
        GitProcess.user = user;
        System.out.println(user);

        return true;
    }
}