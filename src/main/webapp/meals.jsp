<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="1">
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
    </tr>
    <tbody>
        <c:forEach items="${meals}" var="meal">
            <c:if test="${meal.exceed}">
                <c:set var="textColor" value="red"/>
            </c:if>
            <c:if test="${!meal.exceed}">
                <c:set var="textColor" value="green"/>
            </c:if>
            <tr style="color: ${textColor}">

                <td>
                    <c:out value="${meal.dateTime.format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\"))}"/>
                </td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add User</a></p>
</body>
</html>
