package com.sunbox.job.jobexecutor.dao;

import com.sunbox.job.jobexecutor.model.JobMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JobMappingDao {

    JobMapping  load(@Param("identification") String  identification);

    JobMapping  loadByPackageName(@Param("packageName") String  packageName);

}
