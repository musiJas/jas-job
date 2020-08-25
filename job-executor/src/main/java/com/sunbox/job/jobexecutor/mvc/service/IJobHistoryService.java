package com.sunbox.job.jobexecutor.mvc.service;


import com.sunbox.job.jobexecutor.common.Result;

public interface IJobHistoryService {

    Result   executorJob(String  businessId, String  identification);

    Result   jobStatus(String  businessId);
}
