package com.example.gitlabdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class GitlabdemoApplicationTests {

    @Test
    public void contextLoads() {
        String ls = "{'score': '0', 'detail': '{\"config\": {\"hscale\": 1}, \"foot\": {\"tock\": \"1 2 3 4 5 6 7 8 9 10 \"}, \"signal\": [[\"Input\", {\"name\": \"a\", \"wave\": \"==========\", \"data\": [\"0\", \"10\", \"14\", \"10\", \"14\", \"10\", \"14\", \"10\", \"14\", \"10\"]}, {\"name\": \"b\", \"wave\": \"==========\", \"data\": [\"0\", \"5\", \"12\", \"5\", \"12\", \"5\", \"12\", \"5\", \"12\", \"5\"]}], {}, [\"REF\", {\"name\": \"p\", \"wave\": \"==========\", \"data\": [\"0\", \"30\", \"42\", \"30\", \"42\", \"30\", \"42\", \"30\", \"42\", \"30\"]}, {\"name\": \"q\", \"wave\": \"==========\", \"data\": [\"0\", \"30\", \"42\", \"30\", \"42\", \"30\", \"42\", \"30\", \"42\", \"30\"]}], {}, [\"YOURS\", {\"name\": \"p\", \"wave\": \"==========\", \"data\": [\"0\", \"31\", \"43\", \"31\", \"43\", \"31\", \"43\", \"31\", \"43\", \"31\"]}, {\"name\": \"q\", \"wave\": \"==========\", \"data\": [\"0\", \"31\", \"43\", \"31\", \"43\", \"31\", \"43\", \"31\", \"43\", \"31\"]}], {}, {\"name\": \"mismatch\", \"wave\": \"01........\"}, {}]}', 'verdict': 'answer error', 'comment': 'answer error'}\n";
        try {
            //字符串转换JSON数组
            JSONArray jsonarry = new JSONArray(ls);
            //遍历JSON数组
            if(jsonarry.length() > 0){
                for (int i = 0;i < jsonarry.length();i++) {
                    //获得json数据
                    JSONObject jsonObject = jsonarry.getJSONObject(i);
                    //根据key建取值
                    System.err.println(jsonObject.getString("shuxue"));
                }
            }

        } catch (JSONException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
