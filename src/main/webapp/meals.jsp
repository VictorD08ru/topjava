<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<c:set var="formatter" value="${DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")}"/>
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
            <tr style="color: ${meal.exceed ? "red" : "green"}">
                <td>
                    <c:out value="${meal.dateTime.format(formatter)}"/>
                </td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Изменить</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Удалить</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Добавить</a></p>
</body>
</html>
