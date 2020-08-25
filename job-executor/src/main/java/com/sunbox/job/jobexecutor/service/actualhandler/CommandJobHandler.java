package com.sunbox.job.jobexecutor.service.actualhandler;

import com.sunbox.core.log.XxlJobLogger;
import com.sunbox.job.jobexecutor.common.Constants;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.util.FileUtils;
import com.sunbox.job.jobexecutor.util.process.ProcessHandler;

import java.io.*;


/**
 * @program: job-boot
 * @description:  使用Command 执行统计任务
 * @author: chen_mx
 * @create: 2019-09-04 16:42
 **/
//@Service
@SuppressWarnings("all")
public class CommandJobHandler {

    public static  Result  executorJob(String  businessId,String packageName)  {
        if(!packageName.endsWith(".jar")){
            return  Result.isError("不允许执行此类文件,请检查!");
        }
        String filePath=Constants.FILESUFFIX+packageName;
        String executorCommand = Constants.PREFIX;
        String executorArgs=Constants.SUFFIX;
        StringBuffer command=new StringBuffer();
        command.append(executorCommand).append(filePath).append(" ").append(businessId);
        int exitValue = -1;
        /** 判断当前目录下是否存在该文件 TODO */
        if(!FileUtils.isExist(packageName,Constants.FILESUFFIX)){
            return  Result.isError("服务器上没有发现此类执行文件,请联系管理人员!");
        }
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
            ProcessHandler.readConsole(input,error);
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
            return Result.isOk();
        } else {
            return  Result.isError("command exit value("+exitValue+") is failed");
        }
    }



    public  static  void  test(){
         /*  Process process = Runtime.getRuntime().exec(command.toString());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
            // command log
            String line;
            boolean tag=false;
            XxlJobLogger.log("start reader ...");
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("log@"+line);
                if(line.indexOf(Constants.ERRORCODE)!=-1){
                    tag=true;
                }
                XxlJobLogger.log(line);
            }
            if(tag){
                return Result.isError(line);
            }
            // command exit
            System.out.println("process.waitFor.....");
            process.waitFor();
            exitValue = process.exitValue();*/

    }
}
