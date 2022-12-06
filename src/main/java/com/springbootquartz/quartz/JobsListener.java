package com.springbootquartz.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobsListener implements JobListener {

	@Override
	public String getName() {
		return "globalJob";
	}

	/**
	 * Job 수행 전
	 * @param context
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobToBeExecuted :: jobKey : {}", jobKey);
	}

	/**
	 * Job 중단된 상태
	 * @param context
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobExecutionVetoed :: jobKey : {}", jobKey);
	}

	/**
	 * Job 수행 완료 후
	 * @param context
	 * @param jobException
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobWasExecuted :: jobKey : {}", jobKey);
	}
}
