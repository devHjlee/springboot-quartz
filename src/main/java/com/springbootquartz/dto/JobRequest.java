package com.springbootquartz.dto;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class JobRequest {
    private String jobGroup = "DEV";
    @NotBlank(message = "JobName 필수 값입니다.")
    private String jobName;
    @NotBlank(message = "JobClass 필수 값입니다.")
    private String jobClass;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateAt;
    private long repeatIntervalInSeconds;
    private int repeatCount;

    private String cronExpression;
    private JobDataMap jobDataMap;

    private String desc;

}
