package com.example.gitlabdemo;

import com.example.gitlabdemo.Util.GitProcess;
import org.junit.Test;

public class GitTest {

    @Test
    public void deleteGroup()throws Exception{
        GitProcess gitProcess = new GitProcess();
        for(int i = 2; i < 11; i++){
            gitProcess.getGitLabApi().getGroupApi().deleteGroup("t" + i);
            System.out.println("delete " + i  + " 成功");
        }

    }
}
