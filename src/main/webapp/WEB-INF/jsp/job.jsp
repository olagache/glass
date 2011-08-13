<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib tagdir="/WEB-INF/tags/util" prefix="util"%>

<html>
	<head>
		<util:head />
	</head>
	<body>
	
		<util:menu />
		
		<h1><span>Job</span></h1>
        <div class="actions">
            <a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/edit">edit</a>
            <a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/fire">fire now</a>
            <a href="/history?job=${job.jobClass.simpleName}">history</a>
            <a onclick="javascript:return(confirm('Are you sure you want to delete ?'));" href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/delete">delete</a>
        </div>
        <div class="table">
            <table>
                <tbody>
                    <tr>
                        <td>Group</td>
                        <td>${fn:escapeXml(job.group)}</td>
                    </tr>
                    <tr>
                        <td>Name</td>
                        <td>${fn:escapeXml(job.name)}</td>
                    </tr>
                    <tr>
                        <td>Class</td>
                        <td>${fn:escapeXml(job.jobClass.name)}</td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td>${fn:escapeXml(jobDescription)}</td>
                    </tr>
                    <tr>
                        <td>Properties</td>
                        <td>
                            <pre>${fn:escapeXml(properties)}</pre>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <util:args arguments="${jobArguments}" />

		<h2><span>Defined triggers</span></h2>
        <div class="actions">
            <a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/triggers/new-cron">add cron</a>
            <a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/triggers/new-simple">add simple</a>
        </div>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Group / name</th>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Last fire time</th>
                        <th>Next fire time</th>
                        <th>cron expression</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="trigger" items="${triggers}">
                        <tr>
                            <td style="text-align: center;">${fn:escapeXml(trigger.type)}</td>
                            <td><a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/triggers/${fn:escapeXml(trigger.group)}/${fn:escapeXml(trigger.name)}/edit">${fn:escapeXml(trigger.group)} / ${fn:escapeXml(trigger.name)}</a></td>
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
                            <td colspan="8">
                                <pre>${fn:escapeXml(trigger.properties)}</pre>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>