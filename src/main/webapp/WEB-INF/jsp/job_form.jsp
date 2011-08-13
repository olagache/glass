<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib tagdir="/WEB-INF/tags/util" prefix="util"%>

<html>
	<head>
		<util:head />
	</head>
	<body>
		<util:menu />
		
		<h1><span>Edit job</span></h1>

        <div class="table">
            <form:form id="jobActionForm" action="/jobs/${fn:escapeXml(group)}/${fn:escapeXml(name)}/edit" commandName="form" method="POST" >
                <table>
                    <tr>
                        <td>jobClass</td>
                        <td>${fn:escapeXml(jobClass.name)}</td>
                    </tr>
                    <c:if test="${not empty jobDescription}">
                        <tr>
                            <td>description</td>
                            <td>${fn:escapeXml(jobDescription)}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>group</td>
                        <td>${fn:escapeXml(group)}</td>
                    </tr>
                    <tr>
                        <td>name</td>
                        <td>${fn:escapeXml(name)}</td>
                    </tr>
                    <tr>
                        <td valign="top">properties</td>
                        <td><form:textarea path="properties" rows="20" cols="100" /></td>
                    </tr>
                </table>
                <input type="submit" value="update job"/>
            </form:form>
        </div>

        <util:args arguments="${jobArguments}" />
				
	
	</body>
</html>