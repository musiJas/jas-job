package com.sunbox.job.jobexecutor.model;

import lombok.Data;

import java.util.Date;

/**
 * @program: job-boot
 * @description:  记录实时任务的状态
 * @author: chen_mx
 * @create: 2019-09-04 15:51
 **/
@Data
public class JobExecutorHistory {

    private  Long  id;
    private  String   businessId;  // 业务流水,判断唯一性
    private  String   identification; //执行的指令
    private  Integer   executeStatus; // 当前任务执行的状态 1 已完成  0 执行中 2 执行失败
    private  String   executeMsg; // 执行失败的原因
    private  Date   createTime;
    private  Date  updateTime;

}
