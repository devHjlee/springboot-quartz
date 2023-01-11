package com.springbootquartz.repository;

import com.springbootquartz.domain.QuartBatchLog;
import com.springbootquartz.domain.QuartSchedulerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartzBatchLogRepository extends JpaRepository<QuartBatchLog, Long> {
}
