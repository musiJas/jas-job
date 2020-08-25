package com.sunbox.job.jobexecutor.model;

import lombok.Data;

import java.util.Date;

/**
 * @program: job-boot
 * @description: 任务和指令的映射
 * @author: chen_mx
 * @create: 2019-09-04 16:20
 **/
@Data
public class JobMapping {

    private  Long   id;
    private  String   identification; //执行的指令
    private  String   packageName; //包名 执行的jar包
    private  Integer   priority; //优先级
    private  Date  createTime;
    private  Date  updateTime;

}
