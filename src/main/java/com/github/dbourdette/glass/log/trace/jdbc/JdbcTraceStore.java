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

package com.github.dbourdette.glass.log.trace.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.github.dbourdette.glass.configuration.Configuration;
import com.github.dbourdette.glass.log.trace.Trace;
import com.github.dbourdette.glass.log.trace.TraceLevel;
import com.github.dbourdette.glass.log.trace.TraceStore;
import com.github.dbourdette.glass.util.Page;
import com.github.dbourdette.glass.util.Query;

/**
 * @author damien bourdette
 */
public class JdbcTraceStore implements TraceStore {
    private static final String TABLE_SUFFIX = "log";

    private NamedParameterJdbcTemplate jdbcTemplate;

    private Configuration configuration;

    public JdbcTraceStore(DataSource dataSource, Configuration configuration) {
        this.configuration = configuration;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void add(Trace trace) {
        String sql = "insert into " + getTableName() +
                " (id, executionId, logLevel, logDate, jobClass, jobGroup, jobName, triggerGroup, triggerName, message, stackTrace, rootCause)" +
                " values (" + configuration.getTablePrefix() + "sequence.nextval, :executionId, :logLevel, :logDate, :jobClass, :jobGroup, :jobName, :triggerGroup, :triggerName, :message, :stackTrace, :rootCause)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("executionId", trace.getExecutionId())
                .addValue("logLevel", trace.getLevel().name())
                .addValue("logDate", trace.getDate())
                .addValue("jobClass", trace.getJobClass())
                .addValue("jobGroup", trace.getJobGroup())
                .addValue("jobName", trace.getJobName())
                .addValue("triggerGroup", trace.getTriggerGroup())
                .addValue("triggerName", trace.getTriggerName())
                .addValue("message", trace.getMessage())
                .addValue("stackTrace", trace.getStackTrace())
                .addValue("rootCause", trace.getRootCause());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Page<Trace> getLogs(Query query) {
        String sql = "from " + getTableName();

        return getLogs(sql, new MapSqlParameterSource(), query);
    }

    @Override
    public Page<Trace> getLogs(Long executionId, Query query) {
        String sql = "from " + configuration.getTablePrefix() + "log where executionId = :executionId";

        SqlParameterSource source = new MapSqlParameterSource().addValue("executionId", executionId);

        return getLogs(sql, source, query);
    }

    @Override
    public synchronized void clear() {
        String sql = "truncate table " + getTableName();

        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    private Page<Trace> getLogs(String sqlBase, SqlParameterSource params, Query query) {
        String sql = query.applySqlLimit("select * " + sqlBase + " order by logDate asc");

        List<Trace> traces = jdbcTemplate.query(sql, params, new RowMapper<Trace>() {
            @Override
            public Trace mapRow(ResultSet rs, int rowNum) throws SQLException {
                return doMapRow(rs, rowNum);
            }
        });

        String countSql = "select count(*) " + sqlBase;

        Page<Trace> page = Page.fromQuery(query);

        page.setItems(traces);
        page.setTotalCount(jdbcTemplate.queryForInt(countSql, params));

        return page;
    }

    private List<Trace> getLogs(String sqlBase, SqlParameterSource params) {
        String sql = "select * " + sqlBase + " order by logDate asc";

        return jdbcTemplate.query(sql, params, new RowMapper<Trace>() {
            @Override
            public Trace mapRow(ResultSet rs, int rowNum) throws SQLException {
                return doMapRow(rs, rowNum);
            }
        });
    }

    private Trace doMapRow(ResultSet rs, int rowNum) throws SQLException {
        Trace trace = new Trace();

        trace.setExecutionId(rs.getLong("executionId"));
        trace.setLevel(TraceLevel.valueOf(rs.getString("logLevel")));
        trace.setDate(rs.getTimestamp("logDate"));
        trace.setJobClass(rs.getString("jobClass"));
        trace.setJobGroup(rs.getString("jobGroup"));
        trace.setJobName(rs.getString("jobName"));
        trace.setTriggerGroup(rs.getString("triggerGroup"));
        trace.setTriggerName(rs.getString("triggerName"));
        trace.setMessage(rs.getString("message"));
        trace.setStackTrace(rs.getString("stackTrace"));
        trace.setRootCause(rs.getString("rootCause"));

        return trace;
    }

    private String getTableName() {
        return configuration.getTablePrefix() + TABLE_SUFFIX;
    }
}
