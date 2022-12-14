package com.springbootquartz.service;

import com.springbootquartz.dto.JobRequest;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

public interface QuartzService {

    boolean addJob(JobRequest jobReques, Class<? extends Job> jobClass);

    boolean updateJob(JobRequest jobRequest);

    boolean deleteJob(JobKey jobKey);

    boolean pauseJob(JobKey jobKey);

    boolean resumeJob(JobKey jobKey);

    boolean immediatelyJob(JobKey jobKey);

    boolean isJobExists(JobKey jobKey) throws SchedulerException;

}
