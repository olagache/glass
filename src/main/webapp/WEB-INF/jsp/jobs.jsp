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
		
		<h1><span>Defined jobs</span></h1>
        <div class="table">
            <table>
                <thead>
                    <tr>
                        <th>Group / name</th>
                        <th>Class</th>
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
                                    <img alt="delete" src="/glass/velocity/image/delete.png" />
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
	</body>
</html>