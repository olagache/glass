drop table glass_execution_log;
drop table glass_log;
drop sequence glass_sequence;

CREATE SEQUENCE glass_sequence;

CREATE TABLE glass_execution_log
  (
    id number(19,0) NOT NULL,
    startDate timestamp NOT NULL,
    endDate timestamp,
    ended varchar(1) NOT NULL,
    jobGroup VARCHAR(200) NOT NULL,
    jobName VARCHAR(200) NOT NULL,
    triggerGroup VARCHAR(200) NOT NULL,
    triggerName VARCHAR(200) NOT NULL,
    jobClass VARCHAR(255) NOT NULL,
    dataMap CLOB,
    result VARCHAR(20) NOT NULL
);

CREATE TABLE glass_log
  (
    id number(19,0) NOT NULL,
    executionId number(19,0) NOT NULL,
    jobClass VARCHAR(255) NOT NULL,
    logLevel VARCHAR(20) NOT NULL,
    logDate timestamp NOT NULL,
    jobGroup VARCHAR(200) NOT NULL,
    jobName VARCHAR(200) NOT NULL,
    triggerGroup VARCHAR(200) NOT NULL,
    triggerName VARCHAR(200) NOT NULL,
    message CLOB,
    stackTrace CLOB,
    rootCause CLOB
);

create index idx_glas_exec_log on glass_execution_log(startDate);
create index idx_glas_exec_log_j on glass_execution_log(jobGroup, jobName);
create index idx_glas_log on glass_log(logDate);
create index idx_glas_log_exec on glass_log(executionId);