<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19.10.2020
  Time: 12:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<head>
    <title>Meals</title>

<style>
    .normal {
        color: green;
    }

    .excess {
        color: red;
    }
</style>
</head>
<body>
<section>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <c:forEach items="${meals}" var="meal">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td> ${meal.id}</td>
            <td>${fn: formatDateTime(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
    </tr>
    </c:forEach>
</table>
    </section>
<br/>
<br/>
<form method="POST" action='meals' >
    Date Time : <input
        type="text" name="dateTime"
        value="<fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${meal.dateTime}" />" /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    <br/>
     <input
        type="submit" value="Submit" />
</form>
</body>
</html>
