/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.glass.history.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.history.ExecutionLog;
import com.github.dbourdette.glass.history.History;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class JdbcHistory implements History {
    private NamedParameterJdbcTemplate jdbcTemplate;

    private Configuration configuration;

    public JdbcHistory(DataSource dataSource, Configuration configuration) {
        this.configuration = configuration;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public ExecutionLog jobStarts(JobExecutionContext context) {
        ExecutionLog log = new ExecutionLog();

        log.fillWithContext(context);
        log.setId(nextId());

        String sql = "insert into " + configuration.getTablePrefix() + "execution_log" +
                " (id, startDate, ended, jobGroup, jobName, triggerGroup, triggerName, jobClass, dataMap, success)" +
                " values (:id, :startDate, :ended, :jobGroup, :jobName, :triggerGroup, :triggerName, :jobClass, :dataMap, :success)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", log.getId())
                .addValue("startDate", log.getStartDate())
                .addValue("ended", log.isEnded())
                .addValue("jobGroup", log.getJobGroup())
                .addValue("jobName", log.getJobName())
                .addValue("triggerGroup", log.getTriggerGroup())
                .addValue("triggerName", log.getTriggerName())
                .addValue("jobClass", log.getJobClass())
                .addValue("dataMap", log.getDataMap())
                .addValue("success", false);

        jdbcTemplate.update(sql, params);

        return log;
    }

    @Override
    public void jobEnds(ExecutionLog log, JobExecutionContext context, JobExecutionException exception) {
        String sql = "update " + configuration.getTablePrefix() + "execution_log" +
                " set endDate = :endDate, ended = :ended, success = :success where id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("endDate", new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate())
                .addValue("ended", true)
                .addValue("success", exception == null)
                .addValue("id", log.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Page<ExecutionLog> getLogs(Query query) {
        String sql = "from " + configuration.getTablePrefix() + "execution_log";

        return getLogs(sql, new MapSqlParameterSource(), query);
    }

    @Override
    public Page<ExecutionLog> getLogs(String jobGroup, String jobName, Query query) {
        String sql = "from " + configuration.getTablePrefix() + "execution_log where jobGroup = :jobGroup and jobName = :jobName";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("jobGroup", jobGroup)
                .addValue("jobName", jobName);

        return getLogs(sql, source, query);
    }

    @Override
    public synchronized void clear() {
        String sql = "truncate " + configuration.getTablePrefix() + "log";

        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    private Page<ExecutionLog> getLogs(String sqlBase, SqlParameterSource params, Query query) {
        String sql = query.applySqlLimit("select * " + sqlBase + " order by startDate desc");

        List<ExecutionLog> logs = jdbcTemplate.query(sql, params, new RowMapper<ExecutionLog>() {
            @Override
            public ExecutionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
                ExecutionLog log = new ExecutionLog();

                log.setId(rs.getLong("id"));
                log.setStartDate(rs.getTimestamp("startDate"));
                log.setEndDate(rs.getTimestamp("endDate"));
                log.setEnded(rs.getBoolean("ended"));
                log.setJobGroup(rs.getString("jobGroup"));
                log.setJobName(rs.getString("jobName"));
                log.setTriggerGroup(rs.getString("triggerGroup"));
                log.setTriggerName(rs.getString("triggerName"));
                log.setJobClass(rs.getString("jobClass"));
                log.setDataMap(rs.getString("dataMap"));
                log.setSuccess(rs.getBoolean("success"));

                return log;
            }
        });

        String countSql = "select count(*) " + sqlBase;

        Page<ExecutionLog> page = Page.fromQuery(query);

        page.setItems(logs);
        page.setTotalCount(jdbcTemplate.queryForInt(countSql, params));

        return page;
    }

    private Long nextId() {
        return jdbcTemplate.queryForLong("select " + configuration.getTablePrefix() + "sequence.nextval from dual", new HashMap<String, Object>());
    }
}
