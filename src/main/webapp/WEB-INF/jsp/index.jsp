<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: wawada
  Date: 10/17/13
  Time: 10:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>

<html>
<head>
    <title>Transaction Reports</title>
</head>
<body>
<base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/">
<c:choose>
    <c:when test="${listIsNull == true}">
        There was a probelm connecting to the database or you do not have permissions to get the list of procedures
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${empty procedures}">
                There are no procedures currently in your database.
            </c:when>
            <c:otherwise>
                <table border="1">
                    <tr>
                        <td colspan="3">List of all available procedures</td>
                    </tr>
                    <tr>
                        <td>Database.Procedure</td>
                        <td>Parameters</td>
                        <td>Url</td>
                    </tr>

                        <%--@elvariable id="procedures" type="java.util.List"--%>
                    <c:forEach items="${procedures}" var="procedure">
                        <tr>
                            <c:set var="linkToProcedure" value="/report/${procedure.database}/${procedure.name}.json"/>
                            <td rowspan="${fn:length(procedure.parameters)}">${procedure.database}.${procedure.name}</td>
                            <td>
                                <table border="1">
                                    <c:choose>
                                        <c:when test="${fn:length(procedure.parameters) > 0}">
                                            <tr>
                                                <td>Parameter Name</td>
                                                <td>Parameter Value</td>
                                            </tr>
                                            <c:forEach var="entry" items="${procedure.parameters}" varStatus="stat">
                                                <c:choose>
                                                    <c:when test="${stat.first}">
                                                        <c:set var="linkToProcedure"
                                                               value="${linkToProcedure}?${entry.key}=<insert_value>"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="linkToProcedure"
                                                               value="${linkToProcedure}&${entry.key}=<insert_value>"/>
                                                    </c:otherwise>
                                                </c:choose>

                                                <tr>
                                                    <td>${entry.key}</td>
                                                    <td>${entry.value}</td>
                                                </tr>

                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td>No Parameters</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </table>
                            </td>
                            <td>
                                <c:out value="${linkToProcedure}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>


</body>
</html>