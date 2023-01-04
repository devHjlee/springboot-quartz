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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzServiceImpl implements QuartzService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final QuartzHistoryService quartzHistoryService;

    private final ApplicationContext context;

    @Override
    public void addScheduleJob(JobRequest jobRequest) {
        JobDetail jobDetail;
        Trigger trigger;
        Class<Job> jobClass = null;

        try {
            jobClass = (Class<Job>) Class.forName("com.springbootquartz.cronjob." +jobRequest.getJobClass());
            jobDetail = QuartzUtils.createJob(jobRequest,jobClass,context);
            trigger = QuartzUtils.createTrigger(jobRequest);

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobRequest.getJobName(), e);
        } catch (ClassNotFoundException e){
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobRequest.getJobName(), e);
            // TODO: 2023-01-04 Business Exception 추가해서 처리 필요
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateScheduleJob(JobRequest jobRequest) {
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
    public boolean deleteScheduleJob(JobRequest jobRequest) {
        JobKey jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        try {
            throw new SchedulerException();
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while deleting job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean pauseScheduleJob(JobKey jobKey) {
        return false;
    }

    @Override
    public boolean resumeScheduleJob(JobKey jobKey) {
        return false;
    }

    @Override
    public boolean immediatelyJob(JobRequest jobRequest){
        return false;
    }

    @Override
    public boolean isJobRunning(JobRequest jobRequest) {
        try {
            List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    if (jobRequest.getJobName().equals(jobCtx.getJobDetail().getKey().getName())) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobRequest.getJobName(), e);

        }
        return false;
    }

    @Override
    public boolean isJobExists(JobRequest jobRequest) {
        JobKey jobKey = null;
        try {
            jobKey = new JobKey(jobRequest.getJobName(),jobRequest.getJobGroup());
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }

        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);

        }
        return false;
    }

//    @Override
//    public String getJobState(JobKey jobKey) {
//        try {
//            Scheduler scheduler = schedulerFactoryBean.getScheduler();
//            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//
//            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
//
//            if (triggers != null && triggers.size() > 0) {
//                for (Trigger trigger : triggers) {
//                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//                    if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
//                        return "SCHEDULED";
//                    }
//                    return triggerState.name().toUpperCase();
//                }
//            }
//        } catch (SchedulerException e) {
//            log.error("[schedulerdebug] Error occurred while getting job state with jobKey : {}", jobKey, e);
//        }
//        return null;
//    }
}
