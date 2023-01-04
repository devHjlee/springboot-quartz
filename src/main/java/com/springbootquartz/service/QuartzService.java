package com.springbootquartz.service;

import com.springbootquartz.dto.JobRequest;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

public interface QuartzService {

    void addScheduleJob(JobRequest jobReques);

    boolean updateScheduleJob(JobRequest jobRequest);

    boolean deleteScheduleJob(JobRequest jobRequest);

    boolean pauseScheduleJob(JobKey jobKey);

    boolean resumeScheduleJob(JobKey jobKey);

    boolean immediatelyJob(JobRequest jobRequest);

    boolean isJobRunning(JobRequest jobRequest);

    boolean isJobExists(JobRequest jobRequest);
}
