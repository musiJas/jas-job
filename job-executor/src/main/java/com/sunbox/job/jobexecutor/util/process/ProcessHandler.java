package com.sunbox.job.jobexecutor.util.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @program: job-boot
 * @description:  清理进程控制台的日志信息
 * @author: chen_mx
 * @create: 2019-09-12 10:23
 **/

public class ProcessHandler {

    public  static  void   readConsole(InputStream inputLog, InputStream  errorLog){

        new Thread() {
            public void run() {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(inputLog));
                try {
                    String line1 = null;
                    while ((line1 = br1.readLine()) != null) {
                        if (line1 != null){
                            System.out.println("inputLog@"+line1);
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
                            System.out.println("errorLog@"+line2);
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

}
