package com.springbootquartz.quartz;

import com.springbootquartz.service.QuartzHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobsListener implements JobListener {

	@Autowired
	private QuartzHistoryService quartzHistoryService;

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
	 * Job Exception 발생 시 3번의 재시도 / 그래도 실패 시 해당 Trigger 중지
	 * @param context
	 * @param jobException
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobWasExecuted :: jobKey : {}", jobKey);
		if (!context.getJobDetail().getJobDataMap().containsKey("reTryCnt"))
		{
			context.getJobDetail().getJobDataMap().put("reTryCnt", 0);
		}

		if(jobException != null){
			log.info("jobWasExecuted :: jobKey : {} :: Exception : {}", jobKey,jobException.getMessage());
			int reTryCnt = context.getJobDetail().getJobDataMap().getIntValue("reTryCnt");
			if(3 >= reTryCnt) {
				context.getJobDetail().getJobDataMap().put("reTryCnt",++reTryCnt);
				jobException.setRefireImmediately(true);
			}else{
				//해당 Trigger 중지
				jobException.setUnscheduleAllTriggers(true);
				//실패 관련 로직(알림,Email)
				log.info("Send Email :: context : {}", context);
			}
		}
	}
}
