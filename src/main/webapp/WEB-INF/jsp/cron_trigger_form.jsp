<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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

        <h1><span>Edit cron trigger</span></h1>
        <div class="table">
            <form:form action="/jobs/${fn:escapeXml(group)}/${fn:escapeXml(name)}/triggers/${fn:escapeXml(triggerGroup)}/${fn:escapeXml(triggerName)}/edit-cron" commandName="form" method="POST" >
                <table>
                    <tr>
                        <td>job group :</td>
                        <td>${fn:escapeXml(trigger.jobKey.group)}</td>
                    </tr>
                    <tr>
                        <td>job name : </td>
                        <td>${fn:escapeXml(trigger.jobKey.name)}</td>
                    </tr>
                    <tr>
                        <td>group :</td>
                        <td>${fn:escapeXml(trigger.key.group)}</td>
                    </tr>
                    <tr>
                        <td>name :</td>
                        <td>${fn:escapeXml(trigger.key.name)}</td>
                    </tr>
                    <tr>
                        <td>start time (dd/MM/yyyy HH:mm:ss) :</td>
                        <td><form:input path="startTime" /><form:errors path="startTime" /></td>
                    </tr>
                    <tr>
                        <td>end time (dd/MM/yyyy HH:mm:ss) :</td>
                        <td><form:input path="endTime" /><form:errors path="endTime" /></td>
                    </tr>
                    <tr>
                        <td>cron expression :</td>
                        <td><form:input path="cronExpression" /><form:errors path="cronExpression" /></td>
                    </tr>
                    <tr>
                        <td>data map :</td>
                        <td><form:textarea path="dataMap" rows="20" cols="100" /></td>
                    </tr>
                </table>
                <input type="submit" value="update cron trigger"/>
            </form:form>
        </div>

        <util:args arguments="${jobArguments}" />

        <util:cron_doc />
    </body>
</html>
