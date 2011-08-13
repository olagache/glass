<%@ tag body-content="empty" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="menu">
    <span class="title">Chronos</span>
    <ul class="items">
        <li><a href="/">dashboard</a></li>
        <li><a href="/jobs">defined jobs</a></li>
        <li><a href="/jobs/new">add job</a></li>
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