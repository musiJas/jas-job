package com.sunbox.job.jobexecutor.util.process;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-11 20:55
 **/
public class TimeoutException extends  RuntimeException {

    public TimeoutException() {
        super();
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }

    protected TimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
