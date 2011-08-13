<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib tagdir="/WEB-INF/tags/util" prefix="util"%>
<%@ taglib uri="http://glass.tags/job" prefix="job"%>

<html>
	<head>
		<util:head />
	</head>
	<body>
	
		<util:menu />
		
		<h2><span>Hanged triggers</span></h2>
        <div class="table">
            <c:if test="${fn:length(hangedTriggers) > 0}">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Job group / name</th>
                            <th>Trigger group / name</th>
                            <th>Previous firetime</th>
                            <th>Next firetime</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="trigger" varStatus="loopStatus" items="${hangedTriggers}">
                            <tr>
                                <td>${trigger.jobGroup} / ${trigger.jobName}</td>
                                <td>${trigger.group} / ${trigger.name}</td>
                                <td><fmt:formatDate value="${trigger.previousFireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td><fmt:formatDate value="${trigger.nextFireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td nowrap="nowrap">
                                    <a onclick="javascript:return(confirm('Are you sure you want to restart trigger ?'));"
                                       href="/restartTrigger?group=${fn:escapeXml(trigger.group)}&amp;name=${fn:escapeXml(trigger.name)}">restart</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${fn:length(hangedTriggers) eq 0}">
                none
            </c:if>
        </div>
		
		<h2><span>Running jobs</span></h2>
		<div class="table">
            <c:if test="${fn:length(runningJobs) > 0}">
                <table>
                    <thead>
                        <tr>
                            <th>Job group / name</th>
                            <th>Class</th>
                            <th>FireTime</th>
                            <th>Duration</th> 
                            <th>Stop</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="jobContext" varStatus="loopStatus" items="${runningJobs}">
                            <tr>
                                <td><a href="/jobs/${fn:escapeXml(jobContext.jobDetail.group)}/${fn:escapeXml(jobContext.jobDetail.name)}">${jobContext.jobDetail.group} / ${jobContext.jobDetail.name}</a></td>
                                <td>${jobContext.jobDetail.jobClass}</td>
                                <td style="text-align: center"><fmt:formatDate value="${jobContext.fireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td style="text-align: center">${ (now.time - jobContext.fireTime.time) / 1000 }</td>
                                <td style="text-align: center">
                                    <c:if test="${job:interruptable(jobContext.jobDetail)}">
                                        <a href="/interrupt?group=${fn:escapeXml(jobContext.jobDetail.group)}&amp;name=${fn:escapeXml(jobContext.jobDetail.name)}"><img src="/static/image/stop.png" alt="" ></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${fn:length(runningJobs) eq 0}">
                none
            </c:if>
        </div>

        <h2><span>Paused triggers</span></h2>
		<div class="table">
            <c:if test="${fn:length(pausedTriggers) > 0}">
                <table>
                    <thead>
                        <tr>
                            <th>job group</th>
                            <th>job name</th>
                            <th>group</th>
                            <th>name</th>
                            <th>next firetime</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="trigger" varStatus="loopStatus" items="${pausedTriggers}">
                            <tr>
                                <td>${trigger.jobGroup}</td>
                                <td>${trigger.jobName}</td>
                                <td>${trigger.group}</td>
                                <td>${trigger.name}</td>
                                <td><fmt:formatDate value="${trigger.nextFireTime}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${fn:length(pausedTriggers) eq 0}">
                none
            </c:if>
        </div>
		
	</body>
</html>
