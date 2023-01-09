package com.springbootquartz.service;


import com.springbootquartz.domain.QuartSchedulerHistory;
import com.springbootquartz.repository.QuartzHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Slf4j
@Service("quartzHistoryService")
public class QuartzHistoryService {
    @Autowired
    QuartzHistoryRepository quartzHistoryRepository;

}
