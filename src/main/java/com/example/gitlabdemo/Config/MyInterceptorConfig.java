package com.example.gitlabdemo.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gitlabdemo.Model.Result;
import com.example.gitlabdemo.Model.User;
import com.example.gitlabdemo.Util.GitProcess;
import com.example.gitlabdemo.Util.TokenUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MyInterceptorConfig implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse resp, Object handler) throws Exception {
        System.out.println(request.getRequestURI().toString());
        if (request.getRequestURI().toString().equals("/api/login/student")){
            return true;
        }

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

        } catch (Exception e){
            System.out.println(e.toString());
            return true;
        }
        String user_id;

        try{
            user_id = claims.get("user_id").asString();
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println("user_id error");
            return false;
        }

        User user = new User(user_id);
        GitProcess.user = user;
        System.out.println(user);

        return true;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
//
//        return true;

//        System.out.println("getContextPath:" + request.getContextPath());
//        System.out.println("getServletPath:" + request.getServletPath());
//        System.out.println("getRequestURI:" + request.getRequestURI());
//        System.out.println("getRequestURL:" + request.getRequestURL());
//
//        if(request.getMethod().equals("OPTIONS")){
//            response.setStatus(HttpServletResponse.SC_OK);
//            return true;
//        }
//        response.setCharacterEncoding("utf-8");
//
//        String token = request.getHeader("Authorization");
//        if(token != null){
//            boolean result = TokenUtil.verify(token);
//            if(result){
//                System.out.println("通过拦截器");
//                return true;
//            }
//        }
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        PrintWriter out = null;
//        try{
//            Result result = new Result("登录失败");
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonStr = mapper.writeValueAsString(result);
//            response.getWriter().append(jsonStr);
//            System.out.println("认证失败，未通过拦截器");
//        }catch (Exception e){
//            e.printStackTrace();
//            response.sendError(500);
//            return false;
//        }
//        return false;
//    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    /*
     * 处理请求完成后视图渲染之前的处理操作
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }
}