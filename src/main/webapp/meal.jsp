<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
    <form method="POST" action='meals' name="frmAddMeal">
        <input type="number" name="mealId" value="${mealForEdit.id}" hidden="hidden">
        Дата/Время : <input
            type="datetime-local" name="dateTime"
            value="<c:out value="${mealForEdit.dateTime}"/>" /> <br />
        Описание : <input
            type="text" name="description"
            value="<c:out value="${mealForEdit.description}"/>" /> <br />
        Калории : <input
            type="number" name="calories"
            value="${mealForEdit.calories}"/> <br />
        <input type="submit" value="OK" />
    </form>
</body>
</html>
