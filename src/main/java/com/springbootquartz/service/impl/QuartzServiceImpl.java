package com.springbootquartz.service.impl;


import com.springbootquartz.dto.JobRequest;
import com.springbootquartz.quartz.QuartzUtils;
import com.springbootquartz.service.QuartzHistoryService;
import com.springbootquartz.service.QuartzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzServiceImpl implements QuartzService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final QuartzHistoryService quartzHistoryService;

    private final ApplicationContext context;

    @Override
    public boolean addJob(JobRequest jobRequest, Class<? extends Job> jobClass) {
        JobKey jobKey;
        JobDetail jobDetail;
        Trigger trigger;

        jobKey = JobKey.jobKey(jobRequest.getJobName(),jobRequest.getJobGroup());
        jobDetail = QuartzUtils.createJob(jobRequest,jobClass,context);
        trigger = QuartzUtils.createTrigger(jobRequest);

        try {
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail,trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateJob(JobRequest jobRequest) {
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
        try {
            if(isJobExists(jobKey)){
                schedulerFactoryBean.getScheduler().triggerJob(jobKey);
                return true;
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean isJobExists(JobKey jobKey) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);
        }
        return false;
    }

}
