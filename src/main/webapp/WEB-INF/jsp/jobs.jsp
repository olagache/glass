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
		
		<h1><span>Defined jobs</span></h1>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>group / name</th>
                        <th>class</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="job" items="${jobs}">
                        <tr>
                            <td><a href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}">${fn:escapeXml(job.group)} / ${fn:escapeXml(job.name)}</a></td>
                            <td>${job.jobClass.name}</td>
                            <td style="text-align: center;">
                                <a onclick="javascript:return(confirm('Are you sure you want to delete ?'));" href="/jobs/${fn:escapeXml(job.group)}/${fn:escapeXml(job.name)}/delete">
                                    <img alt="delete" src="/static/image/delete.png" />
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>