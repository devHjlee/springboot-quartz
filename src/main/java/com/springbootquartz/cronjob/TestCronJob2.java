package com.springbootquartz.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 스케줄러 중복 실행 방지
 */
@Slf4j
//@DisallowConcurrentExecution //클러스터링 환경에선 해당 어노테이션 작동하지 않음 테스트 해봐야함
public class TestCronJob2 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("START TEST2==================================================");
            log.info("TestCronJob2");
            Thread.sleep(29000);
            log.info("TestCronJob2");
            log.info("END TEST2================================================================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
