package com.example.ustbdemo.Aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ControllerRequestAdvice {
    // 用于存放当前线程的请求时间，方便在日志中查看
    private static final ThreadLocal<Long> timeThreadLocal = new ThreadLocal<Long>();

    public static final Logger logger = LoggerFactory.getLogger(ControllerRequestAdvice.class);

    /**
     * 定义切点
     * execution表达式可以百度下，这里代表是controller下的所有public方法
     */
    @Pointcut("execution(public * com.example.gitlabdemo.Controller.*.*(..))")
    public void log() {

    }

    /**
     * 处理请求前处理
     *
     * @param joinPoint 连接点
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String client = request.getRemoteAddr();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String token = request.getHeader("token");
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        if (args.length > 0) {
            params = getParamMap(args);
        }
        long currentTimeMillis = System.currentTimeMillis();
        timeThreadLocal.set(currentTimeMillis);
        StringBuffer sb = new StringBuffer();
        sb.append("Request_").append(currentTimeMillis).append(" ");
        sb.append("<").append(client).append(">");
        sb.append(" ").append(method).append(" ");
        sb.append("\"").append(requestURI).append("\"");
        sb.append(" token:").append(token).append(" ");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append("params").append("=>").append(objectMapper.writeValueAsString(params));
        } catch (Exception e){

        }

        logger.info(sb.toString());
    }

    /**
     * 利用反射获取参数信息
     *
     * @return
     */
    private Map<String, Object> getParamMap(Object[] params) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Object param : params) {
            if (param == null) {
                continue;
            }
            // 一些特殊的类不需要进行打印，他们的字段太多太多
            if (param instanceof HttpSession) {
                continue;
            }
            // 单文件
            if (param instanceof MultipartFile) {
                map.put("file", getFileParam((MultipartFile) param));
                continue;
            }
            // 多文件
            if (param instanceof MultipartFile[]) {
                MultipartFile[] files = (MultipartFile[]) param;
                Map<String, Object> tmp = new HashMap<>();
                for (MultipartFile file : files) {
                    Map<String, Object> fileParam = getFileParam(file);
                    tmp.put("file", fileParam);
                }
                map.put("files", tmp);
                continue;
            }
            // 处理没有用vm封装的参数
            if (param instanceof String || param instanceof Integer || param instanceof Boolean ||
                    param instanceof Byte || param instanceof Short || param instanceof Long ||
                    param instanceof Character || param instanceof Float || param instanceof Double) {
                map.put(param.getClass().getSimpleName(), param.toString());
                continue;
            }
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置访问private修饰的字段的值
                Object val = new Object();
                try {
                    val = field.get(param);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                map.put(field.getName(), val);
            }
        }
        return map;
    }

    /**
     * 处理请求后返回
     *
     * @param obj 返回值
     */
    @AfterReturning(pointcut = "log()", returning = "obj")
    public void afterReturning(Object obj) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            logger.info("Response_" + timeThreadLocal.get() + " => " + objectMapper.writeValueAsString(obj));
        } catch (Exception e){
            logger.info("Response_" + timeThreadLocal.get());
        }

    }

    /**
     * 处理文件参数
     *
     * @param file 文件
     */
    private Map<String, Object> getFileParam(MultipartFile file) {
        Map<String, Object> fileMap = new HashMap<>();
        fileMap.put("文件名", file.getOriginalFilename());
        fileMap.put("文件类型", file.getContentType());
        return fileMap;
    }

}