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

package com.github.dbourdette.glass.log.execution.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.log.execution.Execution;
import com.github.dbourdette.glass.log.execution.ExecutionResult;
import com.github.dbourdette.glass.log.execution.Executions;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class JdbcExecutions implements Executions {
    private static final String TABLE_SUFFIX = "execution_log";

    private NamedParameterJdbcTemplate jdbcTemplate;

    private Configuration configuration;

    public JdbcExecutions(DataSource dataSource, Configuration configuration) {
        this.configuration = configuration;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Execution jobStarts(JobExecutionContext context) {
        Execution execution = new Execution();

        execution.fillWithContext(context);
        execution.setId(nextId());

        String sql = "insert into " + configuration.getTablePrefix() + "execution_log" +
                " (id, startDate, ended, jobGroup, jobName, triggerGroup, triggerName, jobClass, dataMap, result)" +
                " values (:id, :startDate, :ended, :jobGroup, :jobName, :triggerGroup, :triggerName, :jobClass, :dataMap, :result)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", execution.getId())
                .addValue("startDate", execution.getStartDate())
                .addValue("ended", execution.isEnded())
                .addValue("jobGroup", execution.getJobGroup())
                .addValue("jobName", execution.getJobName())
                .addValue("triggerGroup", execution.getTriggerGroup())
                .addValue("triggerName", execution.getTriggerName())
                .addValue("jobClass", execution.getJobClass())
                .addValue("dataMap", execution.getDataMap())
                .addValue("result", ExecutionResult.SUCCESS.name());

        jdbcTemplate.update(sql, params);

        return execution;
    }

    @Override
    public void jobEnds(Execution execution, JobExecutionContext context) {
        String sql = "update " + getTableName() + " set endDate = :endDate, ended = :ended, result = :result where id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("endDate", new DateTime(context.getFireTime()).plusMillis((int) context.getJobRunTime()).toDate())
                .addValue("ended", true)
                .addValue("result", execution.getResult().name())
                .addValue("id", execution.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Page<Execution> find(Query query) {
        String sql = "from " + getTableName();

        return getLogs(sql, new MapSqlParameterSource(), query);
    }

    @Override
    public Page<Execution> find(String jobGroup, String jobName, Query query) {
        String sql = "from " + configuration.getTablePrefix() + "execution_log where jobGroup = :jobGroup and jobName = :jobName";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("jobGroup", jobGroup)
                .addValue("jobName", jobName);

        return getLogs(sql, source, query);
    }

    @Override
    public synchronized void clear() {
        String sql = "truncate table " + getTableName();

        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    private Page<Execution> getLogs(String sqlBase, SqlParameterSource params, Query query) {
        String sql = query.applySqlLimit("select * " + sqlBase + " order by startDate desc");

        List<Execution> executions = jdbcTemplate.query(sql, params, new RowMapper<Execution>() {
            @Override
            public Execution mapRow(ResultSet rs, int rowNum) throws SQLException {
                Execution execution = new Execution();

                execution.setId(rs.getLong("id"));
                execution.setStartDate(rs.getTimestamp("startDate"));
                execution.setEndDate(rs.getTimestamp("endDate"));
                execution.setEnded(rs.getBoolean("ended"));
                execution.setJobGroup(rs.getString("jobGroup"));
                execution.setJobName(rs.getString("jobName"));
                execution.setTriggerGroup(rs.getString("triggerGroup"));
                execution.setTriggerName(rs.getString("triggerName"));
                execution.setJobClass(rs.getString("jobClass"));
                execution.setDataMap(rs.getString("dataMap"));
                execution.setResult(ExecutionResult.valueOf(rs.getString("result")));

                return execution;
            }
        });

        String countSql = "select count(*) " + sqlBase;

        Page<Execution> page = Page.fromQuery(query);

        page.setItems(executions);
        page.setTotalCount(jdbcTemplate.queryForInt(countSql, params));

        return page;
    }

    private Long nextId() {
        return jdbcTemplate.queryForLong("select " + configuration.getTablePrefix() + "sequence.nextval from dual", new HashMap<String, Object>());
    }

    private String getTableName() {
        return configuration.getTablePrefix() + TABLE_SUFFIX;
    }
}
