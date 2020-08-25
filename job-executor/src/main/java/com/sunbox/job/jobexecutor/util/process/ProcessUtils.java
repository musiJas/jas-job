package com.sunbox.job.jobexecutor.util.process;

import com.sunbox.core.log.XxlJobLogger;
import com.sunbox.core.util.FileUtil;
import com.sunbox.job.jobexecutor.common.Constants;
import com.sunbox.job.jobexecutor.util.process.TimeoutException;
import javafx.concurrent.Worker;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: job-boot
 * @description:  进程调用工具
 * @author: chen_mx
 * @create: 2019-09-11 20:51
 **/
@SuppressWarnings("all")
public class ProcessUtils {

    /**
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     *
     */
    public static ProcessStatus execute(final long timeout, final String... command)
            throws IOException, InterruptedException, TimeoutException {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
       // pb.directory(new File("D:\\devspace\\job-boot\\job-executor\\src\\main\\java\\com\\sunbox\\job\\jobexecutor\\util"));
        Process process = pb.start();
        Worker worker = new Worker(process);
        worker.start();


        ProcessStatus ps = worker.getProcessStatus();
        try {
            worker.join(timeout);
            if (ps.exitCode == ProcessStatus.CODE_STARTED) {
                // not finished
                worker.interrupt();
                throw new TimeoutException();
            } else {
                return ps;
            }
        } catch (InterruptedException e) {
            // canceled by other thread.
            worker.interrupt();
            throw e;
        } finally {
            process.destroy();
        }
    }


    /**
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     *
     */
    @SuppressWarnings("all")
    public static ProcessStatus execute(final long timeout, final String  command)
            throws IOException, InterruptedException, TimeoutException {
     /*   ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        // pb.directory(new File("D:\\devspace\\job-boot\\job-executor\\src\\main\\java\\com\\sunbox\\job\\jobexecutor\\util"));
        Process process = pb.start();*/



            // command process
        Process process = Runtime.getRuntime().exec(command);
        Worker worker = new Worker(process);
        worker.start();
        ProcessStatus ps = worker.getProcessStatus();
        try {
            worker.join(timeout);
            if (ps.exitCode == ProcessStatus.CODE_STARTED) {
                // not finished
                worker.interrupt();
                throw new TimeoutException();
            } else {
                return ps;
            }
        } catch (InterruptedException e) {
            // canceled by other thread.
            worker.interrupt();
            throw e;
        } finally {
            process.destroy();
        }
    }


    private static class Worker extends Thread {
        private final Process process;
        private ProcessStatus ps;

        private Worker(Process process) {
            this.process = process;
            this.ps = new ProcessStatus();
        }

        public void run() {
            try {
                InputStream is = process.getInputStream();
                try {
                    ps.output = org.apache.commons.io.IOUtils.toString(is); //FileUtils.toString(is);
                } catch (IOException ignore) { }
                ps.exitCode = process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public ProcessStatus getProcessStatus() {
            return this.ps;
        }
    }

    public static class ProcessStatus {
        public static final int CODE_STARTED = -257;
        public volatile int exitCode;
        public volatile String output;
    }


    public static void main(String[] args) {
        List<String>   arr=new ArrayList<>();
        String  key1="cmd";
        String  key2="java ";
        String  key6=" --";
        String  key3="-jar ";
        String  key4=" TestExecutorJob.v1.jar ";
        String  key5=" 1000";
        String  packageName="TestExecutorJob.v1.jar";
        arr.add(key2);
        arr.add(key6);
        arr.add(key3);
        arr.add(key4);
        String  command="java -jar  TestExecutorJob.v1.jar  1000";
        try {
            ProcessStatus  ps= execute(1000L,command);
            System.out.println("1"+ps.output);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      /*  ProcessBuilder pb = new ProcessBuilder(arr);
        pb.redirectErrorStream(true);
        // pb.directory(new File("D:\\devspace\\job-boot\\job-executor\\src\\main\\java\\com\\sunbox\\job\\jobexecutor\\util"));
        try {
            Process process = pb.start();
            System.out.println(org.apache.commons.io.IOUtils.toString(process.getInputStream(),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
