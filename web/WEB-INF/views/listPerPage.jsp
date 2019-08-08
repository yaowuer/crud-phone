<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${root}/assets/bootstrap.min.css">

    <style>
        #pagination li {
            float: left;
            height: 20px;
            padding: 5px 10px;
            border: 1px solid gray;
            background: #f5f5f5;
            list-style-type: none;
            border-radius: 3px;
            margin-left: 10px;
        }
        #pagination a {
            text-decoration: none;
        }
    </style>
</head>

<body>
<table>
    <thead>
    <tr>
        <th>选择</th>
        <th>序号</th>
        <th>ID</th>
        <th>名字</th>
        <th>厂商</th>
        <th>价格</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="phone" items="${paginator.data}" varStatus="s">
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
    </tbody>
</table>

<hr>

<p>
    <header>当前 ${paginator.current} 页，总共 ${paginator.pageCount} 页</header>
</p>

<nav style="text-align: center">
    ${paginator.outputPageNav("/phone/listPerPage", "pageno", true, true)}
</nav>

</body>
</html>