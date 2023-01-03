package com.springbootquartz.service;

import com.springbootquartz.dto.JobRequest;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

public interface QuartzService {

    void addJob(JobRequest jobReques) throws Exception;

    boolean updateJob(JobRequest jobRequest);

    boolean deleteJob(JobKey jobKey);

    boolean pauseJob(JobKey jobKey);

    boolean resumeJob(JobKey jobKey);

    boolean immediatelyJob(JobKey jobKey);

    boolean isJobExists(JobRequest jobRequest) throws SchedulerException;
}
