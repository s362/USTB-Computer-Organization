package com.example.ustbdemo.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class ReadRountine {
    public static Map readRountine(String command){
        try {
            Process pro = null;
            pro = Runtime.getRuntime().exec(command);
            int status = pro.waitFor();
            if (status != 0)
            {
                System.out.println("Failed to call shell's command ");
                System.out.println("error messages");
                StringBuilder error = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(pro.getErrorStream(), "UTF-8"));
                while ((line = br.readLine()) != null) {
                    error.append(line).append('\n');
                }
                System.out.println(error.toString());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuilder strbr = new StringBuilder();
            String line;
            while ((line = br.readLine())!= null)
            {
                strbr.append(line).append("\n");
            }
            System.out.println(strbr);
            ObjectMapper mapper = new ObjectMapper();
            Map readValue = mapper.readValue(strbr.toString(), Map.class);
            return readValue;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }
    }
}
