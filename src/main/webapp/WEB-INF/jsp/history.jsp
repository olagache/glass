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