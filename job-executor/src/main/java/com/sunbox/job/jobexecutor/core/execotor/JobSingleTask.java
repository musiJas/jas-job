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
public class JobSingleTask implements Callable<Result>{
    private  String fileName;
    private  String  businessId;
    private  String  identification;

    public   JobSingleTask(String packageName,String  businessId,String  identification){
          this.fileName=packageName;
          this.businessId=businessId;
          this.identification=identification;
    }

    @Override
    public Result call() throws Exception {
        System.out.println("任务JobSingleTask......");
        Result res=CommandJobHandler.executorJob(businessId,fileName);
        /** 执行完毕后 判断阻塞队列中是否还有需要执行的任务 */
        JSONObject  obj=new JSONObject();
        obj.put("businessId",businessId);
        obj.put("identification",identification);
        res.setObj(obj);
        return res;
    }
}
