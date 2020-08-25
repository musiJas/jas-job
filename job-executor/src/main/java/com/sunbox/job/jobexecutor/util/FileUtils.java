package com.sunbox.job.jobexecutor.util;

import java.io.*;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-04 17:39
 **/
public class FileUtils {

    public  static  boolean  isExist(String fileName,String suffix){
        System.out.println("执行的文件地址:"+suffix+fileName);
        File  file=new File(suffix+fileName);
        if(file.exists()){
            return  true;
        }
        return  false;
    }

    public  static  String    toString( InputStream is) throws IOException {
        BufferedReader  br=new BufferedReader(new InputStreamReader(is,"utf8"));
        StringBuffer sb=new StringBuffer();
        String line="";
        while((line=br.readLine())!=null){
            System.out.println("当前行数据:"+line);
            sb.append(line);
        }
        return  sb.toString();
    }

}
