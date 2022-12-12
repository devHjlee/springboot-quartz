package com.springbootquartz.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class TestCronJob2 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("============================================================================");
        log.info("TestCronJob2");
        log.info("TestCronJob2");
        log.info("============================================================================");
    }
}
