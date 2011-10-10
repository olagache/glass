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

package com.github.dbourdette.glass.log.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.log.Log;
import com.github.dbourdette.glass.log.LogLevel;
import com.github.dbourdette.glass.log.LogsStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class JdbcLogsStore implements LogsStore {
    private static final String TABLE_SUFFIX = "log";

    private NamedParameterJdbcTemplate jdbcTemplate;

    private Configuration configuration;

    public JdbcLogsStore(DataSource dataSource, Configuration configuration) {
        this.configuration = configuration;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void add(Log log) {
        String sql = "insert into " + getTableName() +
                " (id, executionId, logLevel, logDate, jobClass, jobGroup, jobName, triggerGroup, triggerName, message, stackTrace, rootCause)" +
                " values (" + configuration.getTablePrefix() + "sequence.nextval, :executionId, :logLevel, :logDate, :jobClass, :jobGroup, :jobName, :triggerGroup, :triggerName, :message, :stackTrace, :rootCause)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("executionId", log.getExecutionId())
                .addValue("logLevel", log.getLevel().name())
                .addValue("logDate", log.getDate())
                .addValue("jobClass", log.getJobClass())
                .addValue("jobGroup", log.getJobGroup())
                .addValue("jobName", log.getJobName())
                .addValue("triggerGroup", log.getTriggerGroup())
                .addValue("triggerName", log.getTriggerName())
                .addValue("message", log.getMessage())
                .addValue("stackTrace", log.getStackTrace())
                .addValue("rootCause", log.getRootCause());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Page<Log> getLogs(Query query) {
        String sql = "from " + getTableName();

        return getLogs(sql, new MapSqlParameterSource(), query);
    }

    @Override
    public Page<Log> getLogs(Long executionId, Query query) {
        String sql = "from " + configuration.getTablePrefix() + "log where executionId = :executionId";

        SqlParameterSource source = new MapSqlParameterSource().addValue("executionId", executionId);

        return getLogs(sql, source, query);
    }

    @Override
    public List<Log> getLogs(Long executionId) {
        String sql = "from " + configuration.getTablePrefix() + "log where executionId = :executionId";

        SqlParameterSource source = new MapSqlParameterSource().addValue("executionId", executionId);

        return getLogs(sql, source);
    }

    @Override
    public synchronized void clear() {
        String sql = "truncate table " + getTableName();

        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    private Page<Log> getLogs(String sqlBase, SqlParameterSource params, Query query) {
        String sql = query.applySqlLimit("select * " + sqlBase + " order by logDate desc");

        List<Log> logs = jdbcTemplate.query(sql, params, new RowMapper<Log>() {
            @Override
            public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRow(rs, rowNum);
            }
        });

        String countSql = "select count(*) " + sqlBase;

        Page<Log> page = Page.fromQuery(query);

        page.setItems(logs);
        page.setTotalCount(jdbcTemplate.queryForInt(countSql, params));

        return page;
    }

    private List<Log> getLogs(String sqlBase, SqlParameterSource params) {
        String sql = "select * " + sqlBase + " order by logDate desc";

        return jdbcTemplate.query(sql, params, new RowMapper<Log>() {
            @Override
            public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRow(rs, rowNum);
            }
        });
    }

    private Log mapRow(ResultSet rs, int rowNum) throws SQLException {
        Log log = new Log();

        log.setExecutionId(rs.getLong("executionId"));
        log.setLevel(LogLevel.valueOf(rs.getString("logLevel")));
        log.setDate(rs.getTimestamp("logDate"));
        log.setJobClass(rs.getString("jobClass"));
        log.setJobGroup(rs.getString("jobGroup"));
        log.setJobName(rs.getString("jobName"));
        log.setTriggerGroup(rs.getString("triggerGroup"));
        log.setTriggerName(rs.getString("triggerName"));
        log.setMessage(rs.getString("message"));
        log.setStackTrace(rs.getString("stackTrace"));
        log.setRootCause(rs.getString("rootCause"));

        return log;
    }

    private String getTableName() {
        return configuration.getTablePrefix() + TABLE_SUFFIX;
    }
}
