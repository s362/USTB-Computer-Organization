//package com.example.ustbdemo;
//
//import com.example.ustbdemo.Shiro.KEY;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.example.ustbdemo.Shiro.TestJWT.dencrty;
//import static com.example.ustbdemo.Shiro.TestJWT.encrty;
//
//public class JwtTest {
//    final String ilabloginUrl = "http://www.ilab-x.com/open/api/v2/token?";//??
//    @Test
//    public void test() throws Exception {
//        try
//        {
//            String data= "";
//            // =================示例：解密xjwt (token也是一个xjwt);
////			String xjwt = "AAABbUy0XxsCAAAAAAABiDA%3D.xPwVH6y5s7tALHu1W3z4zX9Moo5j3qHhHylUxL2lVFzRKDBzQpK1YmrohX2gKKVE.zxDXPoreJXv8N1BAtMUcceupBM8nf0UcWQx5j0u6Ao0%3D";
////            String xjwt = "AAABei5W3j8BAAAAAAABiDA%3D.MHZf65Boy%2F7jOZoBdyOLhrldVYzVyL15k0DDuHHb7pEwYtmTNZSKMQnF%2BNFsEHdVpA2zkBwWJHef0PgjKI%2FL0Q%3D%3D.Tjds7CxOrwlYygt9kKxByNxf0BloRMlTrxxNQ3K2oGA%3D&ticket=udhK4eTKk67bmlCRBqgaUnJSKZFtXBDMnFQU5aODPRoMENA7PfBzQya0UbizTsjxE9002nnzyKb9YHcuTQICS1O5UqMbGk%2BWRL3gudRnIlw%3D";
//            String xjwt = "AAABejIg2joCAAAAAAABiDA%3D.AnWukueTnwzLbOrQ0SrvXBS%2BpchnVqc1GmC4gRlzvKPYMTH3IuShxM6aWa9g7vDn.UfylLc955aTaQAHiWqdlgHBWQj2TAALpHTx2jXAmtNw%3D";
//
//            if(xjwt.indexOf("&ticket") != -1){
//                xjwt = xjwt.substring(0,xjwt.indexOf("&ticket"));
//                System.out.println(xjwt);
//            }
//            data = dencrty(xjwt);
//            System.out.println(data);
//
//            Map map = new HashMap();
//            map.put("username","test");
//            map.put("issuerId", KEY.issueId.toString());
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonStr = mapper.readTree(objectMapper.writeValueAsString(map));
//
//            String json=jsonStr.toString();
//            data = encrty(json);
//            System.out.println(data);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//    @Test
//    public void ticketTest() throws Exception {
//        try
//        {
//            String ticket = "udhK4eTKk67bmlCRBqgaUjHZ74gJp3qaP4Ms2kNNyfoYcUynUJtcZQYOqVYc7HGY2lzIPYsxrkJ1zN6LdeBmr1O5UqMbGk%2BWRL3gudRnIlw%3D";
////            String param = String.format("ticket=%s&appid=%s&signature=%s", ticket, KEY.issueId, KEY.SECRET);
//            String param = "ticket=" + ticket + "appid" + KEY.issueId + "signature" + KEY.SECRET;
//            String command = String.format(ilabloginUrl + param);
//            System.out.println(command);
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//}
