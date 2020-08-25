package com.sunbox.job.jobexecutor.mvc.service;

import com.alibaba.fastjson.JSONObject;
import com.sunbox.job.jobexecutor.common.Constants;
import com.sunbox.job.jobexecutor.common.Result;
import com.sunbox.job.jobexecutor.core.execotor.ExecutorAsynService;
import com.sunbox.job.jobexecutor.core.execotor.JobExecutorService;
import com.sunbox.job.jobexecutor.core.execotor.JobSingleTask;
import com.sunbox.job.jobexecutor.dao.JobExecutorHistoryDao;
import com.sunbox.job.jobexecutor.dao.JobMappingDao;
import com.sunbox.job.jobexecutor.dao.JobQueueDao;
import com.sunbox.job.jobexecutor.model.JobExecutorHistory;
import com.sunbox.job.jobexecutor.model.JobMapping;
import com.sunbox.job.jobexecutor.model.JobQueue;
import com.sunbox.job.jobexecutor.service.actualhandler.CommandJobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @program: job-boot
 * @description:  执行任务历史数据 增加策略
 * @author: chen_mx
 * @create: 2019-09-04 16:39
 **/
@Service
public class JobHistoryServiceImpl  implements   IJobHistoryService{

    private  static  Object obj=new Object();
    //@Resource
    JobExecutorHistoryDao  jobExecutorHistoryDao;
    JobMappingDao  jobMappingDao;
    JobQueueDao   jobQueueDao;
    private  static  JobMappingDao   staticJobMapping;
    private  static  JobQueueDao    staticQueue;
    private  static  JobExecutorHistoryDao  staticExecutorHistoryDao;
    @Resource
    public void setJobQueueDao(JobQueueDao jobQueueDao) {
        this.jobQueueDao = jobQueueDao;
        staticQueue=jobQueueDao;
    }
    @Resource
    public void setJobMappingDao(JobMappingDao jobMappingDao) {
        this.jobMappingDao = jobMappingDao;
        staticJobMapping=jobMappingDao;
    }

    @Resource
    public void setJobExecutorHistoryDao(JobExecutorHistoryDao jobExecutorHistoryDao) {
        this.jobExecutorHistoryDao = jobExecutorHistoryDao;
        staticExecutorHistoryDao=jobExecutorHistoryDao;
    }

    /**
     *   1. 执行前先判断是否存在相同流水和指令的任务,存在这提示重复执行，请等待
     *   2. 当执行多个的时候执行个数是否超过阀值,超过则放入队列中
     *   3. 当队列中存在多个任务是,按照队列中的任务优先级执行
     * **/
    @Override
    public Result executorJob(String businessId, String identification) {
        JobExecutorHistory   job=null;
        synchronized (obj){
            job=jobExecutorHistoryDao.findObjectByCondition(businessId,identification);
            if(job!=null){
                return  Result.isError("当前任务有执行中状态的数据,不允许重复执行！请稍后执行！");
            }
        }
        /** 根据 identification 去查找执行包 **/
        JobMapping  jobMapping=jobMappingDao.load(identification);
        String  packageName=jobMapping.getPackageName();
        System.out.println("开始写入任务!");
        JobExecutorHistory  jh=new JobExecutorHistory();
        jh.setBusinessId(businessId);
        jh.setExecuteStatus(0);
        jh.setIdentification(identification);
        jh.setCreateTime(new Date());
        jh.setUpdateTime(new Date());
        jobExecutorHistoryDao.save(jh);
        /** 判断当前正在执行的任务数量  */
        List<JobExecutorHistory> list=jobExecutorHistoryDao.findExecuteJob();
        if(list.size()> Constants.THRESHOLDCODE){
               /** 将任务放到 阻塞队列中 等待执行 存在数据表中   */
                JobQueue jq=new JobQueue();
                jq.setBusinessId(businessId);
                jq.setPackageName(packageName);
                jq.setPriority(jobMapping.getPriority());
                jq.setCreateTime(new Date());
                jq.setUpdateTime(new Date());
                jobQueueDao.save(jq);
               return   Result.isOk("当前任务执行超过阀值,正在等待执行!");
        } else {
            /** 异步任务队列执行 job */
           // Result  res=JobExecutorService.executorTestJob(packageName);
            Result  res=ExecutorAsynService.nettyAsyncExecute(new JobSingleTask(packageName,businessId,identification));
            return  res;
        }
    }


    public  static    Result   executeBusinessJob(Result res){
        String  errMsg=res.getMsg();
        JSONObject resObj= (JSONObject) res.getObj();
        System.out.println("执行成功,正在执行任务回调:>>>>>>>>>>>>>>>>>>>>"+resObj.toJSONString());
        if(res.getCode().intValue()==Constants.SUCCESS.intValue()){
            /** 修改状态为已处理 */
            staticExecutorHistoryDao.updateSuccessByCondition(resObj.getString("businessId"),resObj.getString("identification"));
        }else {
            staticExecutorHistoryDao.updateFailByCondition(resObj.getString("businessId"),resObj.getString("identification"),errMsg);
        }
        executorUnit();
        return Result.isOk();
    }

    public  static  synchronized  void  executorUnit(){
        /** 判断是否需要执行代办任务表中的数据*/
        List<JobQueue>  jobs=staticQueue.findList();
        if(jobs.size()==0){
            return ;
        }
        int index=jobs.size();
        while(index>=0){
            int end=Constants.THRESHOLDCODE;
            if(jobs.size()<=Constants.THRESHOLDCODE){
                end=jobs.size();
            }
            if(jobs.size()==0){
                break;
            }
            List<JobQueue>  list1= jobs.subList(0,end);
            Result rs=JobExecutorService.executorBatchJob(list1);
            /** 更新批量执行的结果到数据库中 */
            List<Result>  ls= (List<Result>) rs.getObj();
            for(Result  childRes:ls){
                JSONObject  childResObj= (JSONObject) childRes.getObj();
                String  childBusiness= childResObj.getString("businessId");
                String  childName=childResObj.getString("packageName");
                JobMapping  jm=staticJobMapping.loadByPackageName(childName);
                if(childRes.getCode().intValue()==Constants.SUCCESS.intValue()){
                    /** 修改状态为已处理 */
                    staticExecutorHistoryDao.updateSuccessByCondition(childBusiness,jm.getIdentification());
                }else {
                    staticExecutorHistoryDao.updateFailByCondition(childBusiness,jm.getIdentification(),childRes.getMsg());
                }
            }
            /** 删除 已经执行的job * */
            staticQueue.removeAllById(list1);
            System.out.println("删除待处理的任务.");
            System.out.println("批量执行结果:"+rs.getMsg());
            jobs=jobs.subList(end,jobs.size());
            index=jobs.size();
        }
    }

    @Override
    public Result jobStatus(String businessId) {
        JobExecutorHistory  jd=new JobExecutorHistory();
        jd.setBusinessId(businessId);
        List<JobExecutorHistory>  list=jobExecutorHistoryDao.findHistory(jd);
        if(list.size()==0){
            return  Result.failArgs("没有查询到当前执行的任务");
        }
        for(JobExecutorHistory  jh:list){
             if(jh.getExecuteStatus()==0){
                  return Result.isOk(jh);
             }
        }
        JobExecutorHistory  jh=list.get(0);
        return  Result.isOk(jh);
    }




}
