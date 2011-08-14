--
-- A hint submitted by a user: Oracle DB MUST be created as "shared" and the 
-- job_queue_processes parameter  must be greater than 2, otherwise a DB lock 
-- will happen.   However, these settings are pretty much standard after any
-- Oracle install, so most users need not worry about this.
--
-- Many other users (including the primary author of Quartz) have had success
-- runing in dedicated mode, so only consider the above as a hint ;-)
--

delete from glass_fired_triggers;
delete from glass_simple_triggers;
delete from glass_simprop_triggers;
delete from glass_cron_triggers;
delete from glass_blob_triggers;
delete from glass_triggers;
delete from glass_job_details;
delete from glass_calendars;
delete from glass_paused_trigger_grps;
delete from glass_locks;
delete from glass_scheduler_state;

drop table glass_calendars;
drop table glass_fired_triggers;
drop table glass_blob_triggers;
drop table glass_cron_triggers;
drop table glass_simple_triggers;
drop table glass_simprop_triggers;
drop table glass_triggers;
drop table glass_job_details;
drop table glass_paused_trigger_grps;
drop table glass_locks;
drop table glass_scheduler_state;


CREATE TABLE glass_job_details
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL, 
    IS_DURABLE VARCHAR2(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE glass_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL, 
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    NEXT_FIRE_TIME NUMBER(13) NULL,
    PREV_FIRE_TIME NUMBER(13) NULL,
    PRIORITY NUMBER(13) NULL,
    TRIGGER_STATE VARCHAR2(16) NOT NULL,
    TRIGGER_TYPE VARCHAR2(8) NOT NULL,
    START_TIME NUMBER(13) NOT NULL,
    END_TIME NUMBER(13) NULL,
    CALENDAR_NAME VARCHAR2(200) NULL,
    MISFIRE_INSTR NUMBER(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP) 
	REFERENCES glass_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP) 
);
CREATE TABLE glass_simple_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    REPEAT_COUNT NUMBER(7) NOT NULL,
    REPEAT_INTERVAL NUMBER(12) NOT NULL,
    TIMES_TRIGGERED NUMBER(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES glass_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE glass_cron_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID VARCHAR2(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES glass_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE glass_simprop_triggers
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 NUMBER(10) NULL,
    INT_PROP_2 NUMBER(10) NULL,
    LONG_PROP_1 NUMBER(13) NULL,
    LONG_PROP_2 NUMBER(13) NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES glass_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE glass_blob_triggers
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES glass_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE glass_calendars
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR2(200) NOT NULL, 
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);
CREATE TABLE glass_paused_trigger_grps
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);
CREATE TABLE glass_fired_triggers 
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR2(95) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    FIRED_TIME NUMBER(13) NOT NULL,
    PRIORITY NUMBER(13) NOT NULL,
    STATE VARCHAR2(16) NOT NULL,
    JOB_NAME VARCHAR2(200) NULL,
    JOB_GROUP VARCHAR2(200) NULL,
    IS_NONCONCURRENT VARCHAR2(1) NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);
CREATE TABLE glass_scheduler_state 
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);
CREATE TABLE glass_locks
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR2(40) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

create index idx_chrn_j_req_recovery on glass_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_chrn_j_grp on glass_job_details(SCHED_NAME,JOB_GROUP);

create index idx_chrn_t_j on glass_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_chrn_t_jg on glass_triggers(SCHED_NAME,JOB_GROUP);
create index idx_chrn_t_c on glass_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_chrn_t_g on glass_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_chrn_t_state on glass_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_chrn_t_n_state on glass_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_chrn_t_n_g_state on glass_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_chrn_t_next_fire_time on glass_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_chrn_t_nft_st on glass_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_chrn_t_nft_misfire on glass_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_chrn_t_nft_st_misfire on glass_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_chrn_t_nft_st_misfire_grp on glass_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

create index idx_chrn_ft_trig_inst_name on glass_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_chrn_ft_inst_job_req_rcvry on glass_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_chrn_ft_j_g on glass_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_chrn_ft_jg on glass_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_chrn_ft_t_g on glass_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_chrn_ft_tg on glass_fired_triggers(SCHED_NAME,TRIGGER_GROUP);

