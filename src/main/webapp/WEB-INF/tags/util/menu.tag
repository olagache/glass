<%@ tag body-content="empty" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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

<div class="menu">
    <span class="title">Glass</span>
    <ul class="items">
        <li><a href="/">dashboard</a></li>
        <li><a href="/jobs">defined jobs</a></li>
        <li><a href="/jobs/new">add job</a></li>
        <li><a href="/triggers">defined triggers</a></li>
    </ul>
    <ul class="jobs">
        <li><a href="/history">run history</a></li>
        <li><a href="/logs">logs</a></li>
    </ul>
    <span class="standby">
        <c:if test="${not standby}">
            <a onclick="javascript:return(confirm('Are you sure you want to standby scheduler ?'));" href="/standby">standby</a>
        </c:if>
        <c:if test="${standby}">
            <a onclick="javascript:return(confirm('Are you sure you want to start scheduler ?'));" href="/start">start</a>
        </c:if>
    </span>
</div>