package com.sunbox.job.jobexecutor.dao;

import com.sunbox.job.jobexecutor.model.JobExecutorHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobExecutorHistoryDao {

    List<JobExecutorHistory>  findAll();

    JobExecutorHistory  load(Long id);


    List<JobExecutorHistory>  findHistory(JobExecutorHistory job);

    JobExecutorHistory findObjectByCondition(@Param("businessId") String  businessId,@Param("identification") String identification);

    void  save(JobExecutorHistory  executorHistory);

    List<JobExecutorHistory>  findExecuteJob();

    void  updateSuccessByCondition(@Param("businessId") String  businessId,@Param("identification") String identification);

    void  updateFailByCondition(@Param("businessId") String  businessId,@Param("identification") String identification,@Param("executeMsg") String  executeMsg);

    void  updateBatchByIds(List  id);
}
