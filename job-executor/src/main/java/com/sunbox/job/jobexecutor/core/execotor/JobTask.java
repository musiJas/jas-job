package com.sunbox.job.jobexecutor.core.execotor;

import com.alibaba.fastjson.JSONObject;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.model.JobQueue;
import com.sunbox.job.jobexecutor.service.actualhandler.CommandJobHandler;

import java.util.concurrent.Callable;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-04 18:11
 **/
public class JobTask implements Callable<Result>{
 /*   private  String fileName;*/
    private JobQueue  correntQueue;


    public  JobTask  (JobQueue JobQueue){
        this.correntQueue=JobQueue;
    }

    @Override
    public Result call() throws Exception {
        Result res=CommandJobHandler.executorJob(correntQueue.getBusinessId(),correntQueue.getPackageName());
        /** 执行完毕后 判断阻塞队列中是否还有需要执行的任务 */
        JSONObject obj=new JSONObject();
        obj.put("businessId",correntQueue.getBusinessId());
        obj.put("packageName",correntQueue.getPackageName());
        res.setObj(obj);
        System.out.println("队列条目执行成功!"+res.getObj());
        return res;
    }
}
