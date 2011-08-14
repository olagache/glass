<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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
		
		<h1><span>Create job</span></h1>

        <div class="table">
            <form:form id="jobActionForm" action="/jobs/new" commandName="form" method="POST" >
                <table>
                    <tr>
                        <td>group</td>
                        <td><form:input path="group" /><form:errors path="group" /></td>
                    </tr>
                    <tr>
                        <td>name</td>
                        <td><form:input path="name" /><form:errors path="name" /></td>
                    </tr>
                    <tr>
                        <td>class</td>
                        <td>
                            <form:select path="clazz" onchange="javascript:var args=new JobArguments(this[this.selectedIndex].value);args.buildHtml('arguments-body-id')">
                                <form:option  label="------------------------------" value="null"/>
                                <form:options itemLabel="name" itemValue="name" items="${jobClasses}"/>
                            </form:select>
                            <form:errors path="clazz" />
                        </td>
                    </tr>
                    <c:if test="${not empty jobDescription}">
                        <tr>
                            <td>description</td>
                            <td>${fn:escapeXml(jobDescription)}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td valign="top">properties</td>
                        <td><form:textarea path="properties" rows="20" cols="100" /></td>
                    </tr>
                </table>
                <input type="submit" value="create job"/>
            </form:form>
        </div>

        <util:args arguments="${jobArguments}" />
				
	
	</body>
</html>