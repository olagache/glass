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
		
		<h1><span>Run history</span></h1>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>Type</th>
                        <th>Date</th>
                        <th>Job group / name</th>
                        <th>Trigger group / name</th>
                        <th>Job class</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${logs}">
                        <tr>
                            <td>${fn:escapeXml(log.type)}</td>
                            <td><fmt:formatDate value="${log.date}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td><a href="/jobs/${fn:escapeXml(log.jobGroup)}/${fn:escapeXml(log.jobName)}">${fn:escapeXml(log.jobGroup)} / ${fn:escapeXml(log.jobName)}</a></td>
                            <td>${fn:escapeXml(log.triggerGroup)} / ${fn:escapeXml(log.triggerName)}</td>
                            <td>${log.jobClass}</td>
                        </tr>
                        <tr>
                            <td colspan="5">
                                <pre>${fn:escapeXml(log.properties)}</pre>
                                <c:if test="${not empty log.stackTrace}">
                                     <pre>${fn:escapeXml(log.stackTrace)}</pre>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>