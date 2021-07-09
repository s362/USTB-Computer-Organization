package com.example.ustbdemo.Controller;

import com.example.ustbdemo.Model.UtilModel.Result;
import com.example.ustbdemo.Util.ResultUtil;
import com.example.ustbdemo.Util.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.ustbdemo.Util.Base64Convert.baseConvertStr;
import static com.example.ustbdemo.Util.Base64Convert.strConvertBase;

/**
 *
 * Description:用户相关接口
 * @author huangweicheng
 * @date 2019/10/22
 */
@RestController
@RequestMapping("/api/verifyCode")
public class VerifyCodeController
{
    private String code;


    @PostMapping("/")
    public void verifyCode(HttpServletResponse response) throws IOException
    {
        /*禁止缓存*/
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        /*获取验证码*/
        code = VerifyCodeUtils.generateVerifyCode(4);
        ServletOutputStream outputStream = response.getOutputStream();
        //ImageIO.write(bufferedImage,"jpg",outputStream);
        VerifyCodeUtils.outputImage(110,40,outputStream,code);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/getcode")
    public ResponseEntity<Result> getCode()
    {
        Result result = new Result(strConvertBase(code));
        result.setSuccess(true);
        return ResultUtil.getResult(result, HttpStatus.OK);
    }
}
