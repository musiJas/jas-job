package com.sunbox.job.jobexecutor.core.execotor;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-05 15:01
 **/
public class JobRunableTask implements Runnable {
    @Override
    public void run() {
        for(int i=0;i<1000;i++){
            try {
                Thread.sleep(1000);
                System.out.println("当前时间:"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
