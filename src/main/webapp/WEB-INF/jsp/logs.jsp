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
		
		<h1><span>Logs</span></h1>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Message</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${logs}">
                        <tr>
                            <td><fmt:formatDate value="${log.date}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td>${fn:escapeXml(log.message)}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>