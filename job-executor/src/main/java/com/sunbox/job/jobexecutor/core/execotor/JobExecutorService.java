package com.sunbox.job.jobexecutor.core.execotor;

import com.alibaba.fastjson.JSONObject;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.model.JobQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-04 18:09
 **/
public class JobExecutorService  {



    public  static Result   executorTestJob(String packageName){
        ExecutorService es=Executors.newCachedThreadPool();
        es.submit(new JobRunableTask());
        es.shutdown();
        return  Result.isOk();
    }


    public  static Result   executorJob(String packageName){
        ExecutorService es=Executors.newCachedThreadPool();
        Future<Result> future=es.submit(new JobSingleTask(packageName,"",""));
        try {
            Result res=future.get();
            es.shutdown();
            return  res;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  Result.isOk();
    }


    public  static Result   executorBatchJob(List<JobQueue>  queue){
        ExecutorService es=Executors.newCachedThreadPool();
        List<JobTask>  list=new ArrayList<>();
        List<Result> res=new ArrayList();
        for(JobQueue job:queue){
            list.add(new JobTask(job));
        }
        try {
            List<Future<Result>>  futures=es.invokeAll(list);
            for(Future<Result> fu: futures){
                Result  result= fu.get();
                res.add(result);
                System.out.println("invokeALL>>>>>"+fu.get().getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Result.isOk(res);
    }

}
