package com.sunbox.job.jobexecutor.dao;

import com.sunbox.job.jobexecutor.model.JobQueue;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-04 17:57
 **/
@Mapper
public interface JobQueueDao {

    void save(JobQueue jobQueue);

    List<JobQueue>   findList();

    void  removeAllById(List<JobQueue> list);
}
