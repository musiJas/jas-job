package com.sunbox.job.jobexecutor.mvc.controller;

import com.sunbox.job.jobexecutor.common.Constants;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.mvc.service.IJobHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: xxl-job-master
 * @description:  http接口支持远程调用
 * @author: chen_mx
 * @create: 2019-09-04 09:33
 **/

@RestController
public class ExecutorController {

    @Autowired
    IJobHistoryService  iJobHistoryService;

    @RequestMapping("/api/executorJob")
    public Result   executorJob(String businessId,String  identification){
        if(StringUtils.isEmpty(businessId)){
            return  Result.isError("业务流水不能为空.");
        }
        if(StringUtils.isEmpty(identification)){
            return  Result.isError("执行指令不允许为空.");
        }
        Result result=iJobHistoryService.executorJob(businessId,identification);
        if(result.getCode().intValue()!= Constants.SUCCESS.intValue()){
            System.out.println("执行异常@"+result.getMsg());
        }
        return  result;
    }

    @RequestMapping("/api/queryJob")
    public Result   queryJob(String businessId){
        if(StringUtils.isEmpty(businessId)){
            return  Result.isError("业务流水不能为空.");
        }
        Result  result=iJobHistoryService.jobStatus(businessId);
        return  result;
    }



    @RequestMapping("/api/isOk")
    public   Result  isOk(){
        return  Result.isOk();
    }

}
