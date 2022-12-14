package com.springbootquartz.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobRequest {
    private String jobGroup = "DEV";
    @NotNull
    private String jobName;
    @NotNull
    private String jobClass;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateAt;
    private long repeatIntervalInSeconds;
    private int repeatCount;

    private String cronExpression;
    private JobDataMap jobDataMap;

    private String desc;

}
