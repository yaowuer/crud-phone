<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<tr>
    <th>选择</th>
    <th>编号</th>
    <th>名字</th>
    <th>厂商</th>
    <th>价格</th>
    <th>操作</th>
</tr>
<c:forEach var="phone" items="${phones}" varStatus="s">
    <tr data-id="${phone.id}">
        <td><input type="checkbox" name="id" value="${phone.id}"></td>
        <td>${s.count}</td>
        <td>${phone.id}</td>
        <td>${phone.name}</td>
        <td>${phone.brand}</td>
        <td>${phone.price}</td>
        <td>
            <a href="#" class="del">删除</a>
            <a href="#" class="update">修改</a>
        </td>
    </tr>
</c:forEach>