package com.example.gitlabdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonParseException;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class TempTest {
    @Test
    public void contextLoads() {
//        String ls = "{\"detail\": {\"config\": {\"hscale\": 1}, \"signal\": [[\"Input\", {\"wave\": \"==========.=.=================\", \"name\": \"a\", \"data\": [\"0\", \"4\", \"9\", \"13\", \"5\", \"1\", \"6\", \"13\", \"9\", \"5\", \"2\", \"8\", \"12\", \"13\", \"3\", \"0\", \"10\", \"6\", \"13\", \"11\", \"2\", \"13\", \"3\", \"10\", \"2\", \"1\", \"8\", \"11\"]}, {\"wave\": \"======.=============.=========\", \"name\": \"b\", \"data\": [\"0\", \"1\", \"3\", \"13\", \"2\", \"13\", \"12\", \"6\", \"10\", \"7\", \"15\", \"14\", \"5\", \"13\", \"5\", \"10\", \"0\", \"13\", \"3\", \"5\", \"14\", \"15\", \"10\", \"12\", \"10\", \"8\", \"9\", \"6\"]}], {}, [\"p\", {\"wave\": \"==============================\", \"name\": \"REF\", \"data\": [\"0\", \"4\", \"27\", \"91\", \"5\", \"7\", \"42\", \"39\", \"27\", \"15\", \"35\", \"30\", \"14\", \"24\", \"84\", \"39\", \"9\", \"0\", \"70\", \"18\", \"39\", \"33\", \"14\", \"195\", \"9\", \"30\", \"6\", \"1\", \"24\", \"33\"]}, {\"wave\": \"==============================\", \"name\": \"YOURS\", \"data\": [\"0\", \"4\", \"28\", \"95\", \"5\", \"11\", \"46\", \"40\", \"28\", \"16\", \"39\", \"41\", \"18\", \"25\", \"88\", \"40\", \"10\", \"0\", \"74\", \"19\", \"40\", \"34\", \"18\", \"206\", \"10\", \"31\", \"7\", \"1\", \"25\", \"34\"]}], {\"wave\": \"0.1.01...........01........01.\", \"name\": \"mismatch\"}, {}, [\"q\", {\"wave\": \"==============================\", \"name\": \"REF\", \"data\": [\"0\", \"4\", \"27\", \"91\", \"5\", \"7\", \"42\", \"39\", \"27\", \"15\", \"35\", \"30\", \"14\", \"24\", \"84\", \"39\", \"9\", \"0\", \"70\", \"18\", \"39\", \"33\", \"14\", \"195\", \"9\", \"30\", \"6\", \"1\", \"24\", \"33\"]}, {\"wave\": \"==============================\", \"name\": \"YOURS\", \"data\": [\"0\", \"4\", \"28\", \"95\", \"5\", \"11\", \"46\", \"40\", \"28\", \"16\", \"39\", \"41\", \"18\", \"25\", \"88\", \"40\", \"10\", \"0\", \"74\", \"19\", \"40\", \"34\", \"18\", \"206\", \"10\", \"31\", \"7\", \"1\", \"25\", \"34\"]}], {\"wave\": \"0.1.01...........01........01.\", \"name\": \"mismatch\"}, {}], \"foot\": {\"tock\": \"1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 \"}}, \"verdict\": \"answer error\", \"score\": 18, \"comment\": \"answer error\"}";
        String ls = "{\"comment\": \"mult4.v:16: syntax error\\nmult4.v:17: Syntax in assignment statement l-value.\\nmult4.v:20: syntax error\\nmult4.v:21: error: invalid module item.\\nmult4.v:22: syntax error\\n/home/ojfiles/test_tb.v:3: error: invalid module item.\\n/home/ojfiles/test_tb.v:6: p wire definition conflicts with reg definition at mult4.v:6.\\n/home/ojfiles/test_tb.v:6: error: Net ``p`` has already been declared.\", \"detail\": \"\", \"score\": \"0\", \"verdict\": \"syntax error\"}";
        ls = ls.replace("'", "\"");
        System.out.println(ls);
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(ls);

//            root.root.path("detail");

            System.out.println(root.path("detail"));
            System.out.println(root.path("detail").isNull());
            System.out.println(root.path("detail").toString().equals("\"\""));
            System.out.println(root.path("detail").toString().equals("\""));
            System.out.println(root.path("detail").toString());

            Map m1 = new HashMap();
            try{
                if (root.findValue("detail").isNull());
                m1.put("detail", "");
            } catch (Exception e){
                m1.put("detail", root.findValue("detail").toString());
            }
            m1.put("verdict", root.findValue("verdict"));
            m1.put("comment", root.findValue("comment"));
            m1.put("score", root.findValue("comment"));

            System.out.println(m1);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode result = mapper.readTree(objectMapper.writeValueAsString(m1));
            System.out.println(result);

        }catch (Exception e){
            System.out.println("false");
            e.printStackTrace();
        }
    }
}
