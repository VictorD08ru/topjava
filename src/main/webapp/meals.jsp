<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <th>"Дата/Время"</th>
        <th>"Описание"</th>
        <th>"Калории"</th>
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
                    <c:set var="stringDate" value="${meal.getDateTime().toString().replace(\"T\",\" \")}"/>
                    <c:out value="${stringDate}"/>
                </td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
