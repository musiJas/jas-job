package com.sunbox.job.jobexecutor.service.jobhandler;
import com.sunbox.core.biz.model.ReturnT;
import com.sunbox.core.handler.IJobHandler;
import com.sunbox.core.handler.annotation.JobHandler;
import com.sunbox.core.log.XxlJobLogger;
import com.sunbox.core.util.DateUtil;
import com.sunbox.job.jobexecutor.common.Constants;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.util.FileUtils;
import com.sunbox.job.jobexecutor.util.process.ProcessHandler;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;

/**
 * 命令行任务
 *
 * @author xuxueli 2018-09-16 03:48:34
 */
@SuppressWarnings("all")
@JobHandler(value="commandJobHandler")
@Component
public class CommandJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        String command = param;
        int exitValue = -1;
        BufferedReader bufferedReader = null;
        InputStream  input=null;
        InputStream  error=null;
        try {
            System.out.println("执行的命令是@:"+command.toString());
            // command process
          /*  System.out.println("执行的命令是:"+command.toString());
            long  timeOut=1000*60*60; *//** 最大超时时间1小时 **//*
            ProcessUtils.ProcessStatus  ps=   ProcessUtils.execute(timeOut,executorCommand,filePath,businessId);
            System.out.println("执行结果:"+ps.toString());*/
            Process process = Runtime.getRuntime().exec(command.toString());
           /* BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));*/
            // command log
            String line;
            boolean tag=false;
            XxlJobLogger.log("start reader ...");
            input=process.getInputStream();
            error=process.getErrorStream();
            readConsole(input,error);
          /*  while ((line = bufferedReader.readLine()) != null) {
                System.out.println("log@"+line);
                if(line.indexOf(Constants.ERRORCODE)!=-1){
                    tag=true;
                }
                XxlJobLogger.log(line);
            }
            if(tag){
                return Result.isError(line);
            }*/
            // command exit
            System.out.println("process.waitFor.....");
            process.waitFor();
            exitValue = process.exitValue();
            process.destroy();
        } catch (Exception e) {
            XxlJobLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(error!=null){
                try {
                    error.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "command exit value("+exitValue+") is failed");
        }
    }





    public  static  void   readConsole(InputStream inputLog, InputStream  errorLog){

        new Thread() {
            public void run() {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(inputLog));
                try {
                    String line1 = null;
                    while ((line1 = br1.readLine()) != null) {
                        if (line1 != null){
                            XxlJobLogger.log(DateUtil.formatDateTime(new Date())+"----Log@"+line1);
                            //System.out.println(DateUtil.formatDateTime(new Date())+"----Log@"+line1);
                            if(line1.indexOf("syntax check result:")!=-1){
                                //builder.append(line1);
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        inputLog.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            public void  run() {
                BufferedReader br2 = new  BufferedReader(new  InputStreamReader(errorLog));
                try {
                    String line2 = null ;
                    while ((line2 = br2.readLine()) !=  null ) {
                        if (line2 != null){
                            XxlJobLogger.log(DateUtil.formatDateTime(new Date())+"----Log@"+line2);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        errorLog.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }



    public static void main(String[] args) {

        String  command="ping www.baidu.com";
        System.out.println(command.indexOf("com")!=-1);


     /*   BufferedReader bufferedReader = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream,"utf-8"));

            // command log
            String line;
            XxlJobLogger.log("start reader ...");
            while ((line = bufferedReader.readLine()) != null) {
                XxlJobLogger.log(line);
                System.out.println("123:"+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
