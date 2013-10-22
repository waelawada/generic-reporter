<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%--
  Created by IntelliJ IDEA.
  User: wawada
  Date: 10/21/13
  Time: 11:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<table style="width:100%">
<c:if test="${logo != null}">
    <tr><td colspan="200"><img src="${logo}"/></td></tr>
</c:if>
<fmt:bundle basename="application"/>

<c:set value="0" var="counter"/>
<c:forEach items="${resultList}" var="element">
    <c:if test="${fn:length(tableHeaders) gt 0}">
        <tr style="background-color: ${headerColor}">
            <c:forEach var="entry" items="${tableHeaders}">
                <th><c:out value="${entry}"/></th>
            </c:forEach>
        </tr>

    </c:if>
    <c:choose>
        <c:when test="${counter%2 eq 1}">
            <tr style="background-color: ${color1}/>">
        </c:when>
        <c:otherwise>
            <tr style="background-color: ${color2}">
        </c:otherwise>
    </c:choose>

        <c:forEach var="entry" items="${element}">
            <td style="text-align: center">
                <c:out value="${entry.value}"/>
            </td>
        </c:forEach>
    </tr>

    <c:set var="counter" value="${counter+1}"/>
</c:forEach>
</table>
</body>
</html>