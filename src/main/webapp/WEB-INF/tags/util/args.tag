<%@ tag body-content="empty" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ attribute name="arguments" required="true" rtexprvalue="true" type="java.util.List" description="List of arguments for job" %>
<%@ attribute name="hasHistory" required="false" rtexprvalue="true" type="java.lang.Boolean" description="Does this job supports history ?" %>

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

<h2><span>Jobs arguments</span></h2>
<div class="table">
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Sample Values</th>
            </tr>
        </thead>
        <tbody id="arguments-body-id">
            <%-- If you change this code don't forget to change the javascript part also --%>
            <c:forEach var="argument" items="${jobArguments}">
                <tr>
                    <td style="text-align: center">
                        <c:if test="${argument.required == true}" >
                            <span style="font-weight:bold;" >
                                <c:out value="${argument.name}" />
                                <c:if test="${argument.required == true}" >*</c:if>
                            </span>
                        </c:if>

                        <c:if test="${argument.required == false}" >
                            <c:out value="${argument.name}" />
                        </c:if>
                    </td>
                    <td><c:out value="${argument.description}" /></td>
                    <td><c:if test="${not empty argument.sampleValues}">
                        <c:forEach var="sampleValue" items="${argument.sampleValues}">
                            <c:out value="${sampleValue}" /><br/>
                        </c:forEach>
                    </c:if></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
