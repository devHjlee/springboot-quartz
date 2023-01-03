package com.springbootquartz.service.impl;


import com.springbootquartz.dto.JobRequest;
import com.springbootquartz.quartz.QuartzUtils;
import com.springbootquartz.service.QuartzHistoryService;
import com.springbootquartz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzServiceImpl implements QuartzService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final QuartzHistoryService quartzHistoryService;

    private final ApplicationContext context;

    @Override
    public void addJob(JobRequest jobRequest) throws Exception {
        JobDetail jobDetail;
        Trigger trigger;
        Class<Job> jobClass = null;

        try {
            jobClass = (Class<Job>) Class.forName("com.springbootquartz." +jobRequest.getJobClass());
            jobDetail = QuartzUtils.createJob(jobRequest,jobClass,context);
            trigger = QuartzUtils.createTrigger(jobRequest);

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail,trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            log.error("[add Job] :"+e.toString());
            throw new Exception(e);
        }
    }

    @Override
    public boolean updateJob(JobRequest jobRequest) {
        JobKey jobKey = null;
        Trigger newTrigger;

        try {
            newTrigger = QuartzUtils.createTrigger(jobRequest);
            jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());

            Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobRequest.getJobName().concat("Trigger"),jobRequest.getJobGroup()), newTrigger);
            log.debug("Job with jobKey : {} rescheduled successfully at date : {}", jobKey, dt);
            return true;
        } catch (SchedulerException e) {
            log.error("error occurred while scheduling with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean deleteJob(JobKey jobKey) {
        return false;
    }

    @Override
    public boolean pauseJob(JobKey jobKey) {
        return false;
    }

    @Override
    public boolean resumeJob(JobKey jobKey) {
        return false;
    }

    @Override
    public boolean immediatelyJob(JobKey jobKey) {
//        try {
//            if(isJobExists(jobKey)){
//                schedulerFactoryBean.getScheduler().triggerJob(jobKey);
//                return true;
//            }
//        } catch (SchedulerException e) {
//            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);
//        }
        return false;
    }

    @Override
    public boolean isJobExists(JobRequest jobRequest) throws SchedulerException {
        JobKey jobKey = null;
        try {
            jobKey = new JobKey(jobRequest.getJobName(),jobRequest.getJobGroup());
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);
            throw new SchedulerException(e);
        }
        return false;
    }

}
