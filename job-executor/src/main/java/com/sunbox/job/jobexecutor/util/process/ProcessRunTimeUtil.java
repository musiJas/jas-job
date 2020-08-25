package com.sunbox.job.jobexecutor.util.process;

import com.sunbox.job.jobexecutor.common.Constants;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-12 09:57
 **/
public class ProcessRunTimeUtil {
    /**
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     * @param command
     * @param timeout
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static int executeCommand(final String command, final long timeout) throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command);
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null){
                return worker.exit;
            } else{
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }


    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        public void run() {
            try {

                System.out.println("1>>>>"+IOUtils.toString(process.getInputStream(),"utf-8"));

                exit = process.waitFor();
            } catch (InterruptedException ignore) {
                return;
            }catch (Exception  ex){
                return;
            }
        }
    }


    public static void main(String[] args) {
        StringBuffer  sb=new StringBuffer();
        sb.append(Constants.WINDOWPREFIX);
        sb.append(Constants.WINDOWFILESUFFIX);
        sb.append("TestExecutorJob.v1.jar");
        sb.append(" 1000");
        System.out.println(sb.toString());
        try {
            int  exitValue=ProcessRunTimeUtil.executeCommand(sb.toString(),100000L);
            System.out.println("exitValue:"+exitValue);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
