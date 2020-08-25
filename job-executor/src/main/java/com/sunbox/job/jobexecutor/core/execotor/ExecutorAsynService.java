package com.sunbox.job.jobexecutor.core.execotor;

import com.google.common.util.concurrent.*;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.mvc.service.JobHistoryServiceImpl;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-05 15:31
 **/
public class ExecutorAsynService {

     public  static <T extends Callable> Result    nettyAsyncExecute(T   callable ){
       /* EventExecutorGroup group = new DefaultEventExecutorGroup(10);
         Future<Result> f = group.submit(callable);
         f.addListener(new FutureListener<Result>() {
             @Override
             public void operationComplete(Future<Result> objectFuture) throws Exception {
                 System.out.println("计算结果:：" + objectFuture.get());
                 System.out.println("执行结束更新数据:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                 JobHistoryServiceImpl.executeBusinessJob(objectFuture.get());
             }
         });*/
         ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
         ListenableFuture<Result> future = service.submit(callable);//<1>
         Futures.addCallback(future, new FutureCallback<Result>() {
             public void onSuccess(Result result) {
                 System.out.println("计算结果:：" + result);
                 System.out.println("执行结束更新数据:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                 JobHistoryServiceImpl.executeBusinessJob(result);
             }
             public void onFailure(Throwable throwable) {
                 System.out.println("异步处理失败,e=" + throwable);
             }
         });
         return  Result.isOk("正在执行中,请等待...");
     }





}
