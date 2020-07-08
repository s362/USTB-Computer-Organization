package com.example.ustbdemo.Shiro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class TestJWT {
    public void test() {
		try
		{
			String data= "";
			// =================示例：解密xjwt (token也是一个xjwt);
//			String xjwt = "AAABbUy0XxsCAAAAAAABiDA%3D.xPwVH6y5s7tALHu1W3z4zX9Moo5j3qHhHylUxL2lVFzRKDBzQpK1YmrohX2gKKVE.zxDXPoreJXv8N1BAtMUcceupBM8nf0UcWQx5j0u6Ao0%3D";
			String xjwt = "AAABbUy0XxsCAAAAAAABiDA%3D.xPwVH6y5s7tALHu1W3z4zX9Moo5j3qHhHylUxL2lVFzRKDBzQpK1YmrohX2gKKVE.zxDXPoreJXv8N1BAtMUcceupBM8nf0UcWQx5j0u6Ao0%3D";
			data = dencrty(xjwt);
			System.out.println(data);

			Map map = new HashMap();
			map.put("username","test");
			map.put("issuerId", KEY.issueId.toString());
			ObjectMapper mapper = new ObjectMapper();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(map));

			String json=jsonStr.toString();
			data = encrty(json);
			System.out.println(data);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
    }

    public static String dencrty(String xjwt) throws Exception {
    	//获取当前时间
//    	long now = System.currentTimeMillis() ;
    	long now = 0 ;
    	//创建JWT实例
        JWT jwt = new JWT(KEY.secret, KEY.aeskey,now,KEY.issueId);
        //对数据进行url
        xjwt=URLDecoder.decode(xjwt,"UTF-8");
        //解密数据
        String json = jwt.verifyAndDecrypt(xjwt, now);
        return json;
    }
    public static String encrty(String json) throws Exception {
	//获取当前时间
		long now=System.currentTimeMillis();
		//创建JWT实例
		JWT jwt=new JWT(KEY.secret,KEY.aeskey,now,KEY.issueId);
		//创建payload
		ByteBuffer payload = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
		payload.put(json.getBytes("UTF-8")).flip();
	//创建out
		ByteBuffer out = ByteBuffer.allocate(1024);
		//加密数据
		jwt.encryptAndSign(JWT.Type.SYS,payload,out,now+10*60*100000); //设置过期时间，例:10分钟
		String xjwt = new String(out.array(),out.arrayOffset(),out.remaining());
		//对数据进行url 编码
		return URLEncoder.encode(xjwt,"UTF-8");
	}
}
