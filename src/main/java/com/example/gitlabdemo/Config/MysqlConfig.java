/**
 * mysql config配置，修改编码格式为utf-8
 */

package com.example.gitlabdemo.Config;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public class MysqlConfig extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}