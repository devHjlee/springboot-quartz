package com.springbootquartz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "QUARTZ_SCHEDULER_HISTORY")
public class QuartSchedulerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String triggerState;
    private String triggerType;
    private String jobClassName;
    private String cronExpression;
    private String description;
    private String resaon;

}
