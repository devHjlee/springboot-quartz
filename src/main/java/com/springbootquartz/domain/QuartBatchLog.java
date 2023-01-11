package com.springbootquartz.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "QUARTZ_BATCH_LOG")
public class QuartBatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String result;
    private String exceptionMessage;
    private Long nextTime;
    private Long preTime;
    @Temporal(TemporalType.TIMESTAMP )
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP )
    private Date endTime;

}
