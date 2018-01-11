package com.feiwww.model.log;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志
 * TODO(这个日志什么时候用的，在哪里用的额？？？？？？)
 * lombok.Data 在common中引入
 */
@Data
public class Log implements Serializable{
    private static final long serialVersionUID = -8280602625152351898L; //这个字段不是随意定义的，请看源码

    private String operation;   // 执行的操作
    private String currentTime; // 当前时间

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

}
