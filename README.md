# 개요
* 스프링스케줄러+shedLock 을 사용하여 스케줄링 이중화를 구현하였으나 
스케줄에 대한 컨트롤이 불가능하였고 어드민 UI 에서 스케줄에 대한 기능추가를 위해 Quartz로 변경
* 구현 목록
  * 이중화 : Cluster 기능은 DB Cluster 사용
  * Job : 생성,즉시수행
  * CronTrigger : 생성,수정,일시정지,재기동
  * 스케줄 등록 수정 이력
  * 스케줄 성공 여부 이력

## 목차
  공식 홈페이지 : http://www.quartz-scheduler.org/

  [0.Quartz란?](#Quartz란?)

  [1.개발환경](#개발환경)

  [2.용어정리](#용어정리)

  [3.Config](#설정)

  [4.프로젝트 구조](#프로젝트구조)

## Quartz란?
Job Scheduling 라이브러리 이며 자바로 개발되어 모든 자바 프로그램에서 사용 가능하고
간단한 interval형식이나 Cron 표현식 스케줄링 지원
* 장점
  * DB 기반의 클러스터 기능 제공
  * 시스템 Fail-over / Random 방식의 로드 분산처리 지원
  * In-memory Job scheduler 제공
  * 여러 기본 플러그인 제공
    * ShutdownHookPlugin – JVM 종료 이벤트 캐치
    * LoggingJobHistoryPlugin – Job 실행 로그 남기기

* 단점
  * Random 방식 클러스터링 기능이라 완벽한 로드 분산 안됨
  * 스케줄링 실행에 대한 히스토리 보관에 대한 개발 필요

* Quartz 흐름
  ![img_1.png](img_1.png)
  출처 : https://www.javarticles.com/2016/03/quartz-scheduler-model.html#prettyPhoto

## 개발환경
* Jdk : OpenJdk 1.8
* DB : Mysql 8.0
* spring boot : 2.7.6
  - spring-boot-starter-data-jpa
  - spring-boot-starter-quartz
  - mysql-connector-j

## 용어정리
* Job
  * Job 인터페이스의 excute 메소드를 trigger 에 의해 실행
  * 개발자는 수행해야 하는 실제 작업을 이 메서드에서 구현
  * Job excute vs QuartzJobBean executeInternal
    * https://stackoverflow.com/questions/66066744/spring-boot-starter-quartz-implements-job-vs-extends-quartzjobbean
* JobDetail
  * Job 을 실행시키기 위한 정보를 담고 있는 객체
  * Job 의 Name,Group JobDataMap 속성 등을 지정
  * Trigger 가 Job 을 수행할 때 JobDetail 기반으로 스케줄링
* Trigger
  * Job 을 실행시킬 스케줄링 조건 (반복 횟수, 시작시간) 등을 담고 있고 Scheduler 는 이 정보를 기반으로 Job 을 수행
  * Trigger 와 Job 연관관계
    * 1 = 1 : 하나의 Trigger 는 하나의 Job 을 반드시 지정
    * N = 1 : 여러 Trigger 가 하나의 Job 을 지정하여 다양한 시간에 스케줄
  * Trigger 종류
    * SimpleTrigger
    * CronTrigger
* JobStroe
  * Job, Trigger에 대한 정보를 저장하는 방식 설정(Memory,DB)
* Listener
  * JobListener : Job 실행 전후로 이벤트를 받을 수 있음
  * TriggerListener : Trigger 실행 전후로 이벤트를 받을 수 있음
## 설정
```java
@Configuration
public class QuartzConfig {

    //...생략
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        schedulerFactoryBean.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        schedulerFactoryBean.setGlobalTriggerListeners(triggersListener);
        schedulerFactoryBean.setGlobalJobListeners(jobsListener);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);


        return schedulerFactoryBean;
    }
}
```
* AutowiringSpringBeanJobFactory
  * Quartz Job 에서 Spring bean 을 참조하기 위해 설정추가

```properties
#Quartz
spring.quartz.scheduler-name=QuartzScheduler
spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO
spring.quartz.properties.org.quartz.threadPool.threadCount=20
spring.quartz.properties.org.quartz.threadPool.threadNamePrefix=QuartzScheduler
spring.quartz.properties.org.quartz.jobStore.tablePrefix=QRTZ_
spring.quartz.properties.org.quartz.jobStore.isClustered=true
spring.quartz.properties.org.quartz.jobStore.class=org.springframework.scheduling.quartz.LocalDataSourceJobStore
#spring.quartz.properties.org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
#spring.quartz.properties.org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreCMT
spring.quartz.properties.org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
spring.quartz.properties.org.quartz.jobStore.useProperties=true
spring.quartz.properties.org.quartz.jobStore.misfireThreshold=60000
```
* jboStore.class
  * JobStoreTX : 트랜잭션을 제어하고 싶은 경우나, 서버 환경 없이 어플리케이션을 운영하려 할 때 사용된다.
  * JobStoreCMT : 어플리케이션 서버 환경 내에서 어플리케이션이 운영되며 컨테이너가 트랜잭션을 관리하도록 하고 싶은 경우 사용된다.
  * LocalDataSourceJobStore : JobStoreTX 사용시 dataSource null 오류 , 스프링에서 대체됨
    * https://github.com/ChamithKodikara/quartz-demo/issues/1
    * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/quartz/LocalDataSourceJobStore.html


* DB Cluster 확인
![img.png](img.png)

## 프로젝트구조
![img_3.png](img_3.png)

## a

