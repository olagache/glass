<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib tagdir="/WEB-INF/tags/util" prefix="util"%>

<%--
  ~ Copyright 2011 Damien Bourdette
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<html>
	<head>
		<util:head />
	</head>
	<body>
	
		<util:menu />
		
		<h1><span>Defined triggers</span></h1>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Job Group / name</th>
                        <th>Trigger Group / name</th>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Last fire time</th>
                        <th>Next fire time</th>
                        <th>cron expression</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="jobAndTriggers" items="${jobsAndTriggers}">
                        <c:set var="job" value="${jobAndTriggers.jobDetail}" />
                        <c:forEach var="trigger" items="${jobAndTriggers.triggers}">
                            <tr>
                                <td style="text-align: center;">${fn:escapeXml(trigger.type)}</td>
                                <td><a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}">${fn:escapeXml(job.group)} / ${fn:escapeXml(job.name)}</a></td>
                                <td>${fn:escapeXml(trigger.group)} / ${fn:escapeXml(trigger.name)}</td>
                                <td style="text-align: center;">
                                    <fmt:formatDate value="${trigger.startTime}" pattern="dd/MM/yyyy HH:mm:ss" />
                                    <c:if test="${trigger.running}">&nbsp;<span style="color: red;">running</span></c:if>
                                </td>
                                <td style="text-align: center;"><fmt:formatDate value="${trigger.endTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td style="text-align: center;"><fmt:formatDate value="${trigger.previousFireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td style="text-align: center;"><fmt:formatDate value="${trigger.nextFireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td style="text-align: center;">${trigger.cronExpression}</td>
                                <td style="text-align: center;">
                                    <a onclick="javascript:return(confirm('Are you sure you want to delete ?'));" href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/triggers/${fn:escapeXml(trigger.group)}/${fn:escapeXml(trigger.name)}/delete">
                                        <img alt="delete" src="/static/image/delete.png" />
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="9">
                                    <pre>${fn:escapeXml(trigger.properties)}</pre>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>