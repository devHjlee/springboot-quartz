package com.springbootquartz.service;


import com.springbootquartz.domain.QuartBatchLog;
import com.springbootquartz.domain.QuartSchedulerHistory;
import com.springbootquartz.repository.QuartzBatchLogRepository;
import com.springbootquartz.repository.QuartzHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

@Slf4j
@Service("quartzBatchLogService")
public class QuartzBatchLogService {
    @Resource
    QuartzBatchLogRepository quartzBatchLogRepository;

    public void save(QuartBatchLog qt) {
        qt.setJobName("BBB");
        qt.setJobGroup("BBB");
        quartzBatchLogRepository.save(qt);
        try {
            throw new SQLException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
