package com.sunbox.job.jobexecutor.model;

import lombok.Data;

import java.util.Date;

/**
 * @program: job-boot
 * @description:  任务等待执行队列
 * @author: chen_mx
 * @create: 2019-09-04 17:56
 **/
@Data
public class JobQueue {
    private  Long   id;
    private  String  businessId;
    private  String  packageName;
    private  Integer   priority;
    private  Date   createTime;
    private  Date   updateTime;

}
