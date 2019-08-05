<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="root" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>首页</title>
    <script src="${root}/assets/jquery.js"></script>
    <style>
        .doFloat {
            position: fixed;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: white;
            padding: 20px;
        }
    </style>
</head>
<body>

<header>
    <h1>手机商城</h1>
</header>

<div class="container">
    <table>
    </table>

    <button class="addPhone">添加</button>
    <button>反选</button>
    <button>取消所有选择</button>
    <button>选择所有</button>
    <button class="deleteSelection">删除选择</button>
</div>

<script>
    function loadTable() {
        $.ajax({
            method: "GET",
            url: "${root}/listAll"
        }).done(function (res) {
            $("table").html(res);
        })
    }

    function doDelete (e) {
        $.get("${root}/delete?id=" + $(this).closest("tr").attr("data-id"), function (res) {
            e.preventDefault();
            loadTable();
        });
    }

    function doUpdate () {
        $.get("${root}/edit?id=" + $(this).closest("tr").attr("data-id"), function (res) {
            $(res)
                .addClass("doFloat")
                .on('click', '.back', function () {
                    $(this).closest("div").remove();
                })
                .on('click', '.submit', function () {
                    // 将表格的内容提交给后台
                    $.ajax({
                        method: "POST",
                        url: "${pageContext.request.contextPath}/edit",
                        data: $(".editForm").serialize()
                    }).done(function () {
                        $("#editDiv").remove();
                        loadTable();
                        alert("修改成功!");
                    }).fail(function () {
                        alert("修改失败，请检查再试!")
                    });
                })
                .appendTo("body");
        })
    }

    function doAdd (e) {
        $.get("${root}/add", function (res) {
            $(res)
                .addClass("doFloat")
                .on('click', '.back', function () {
                    $(this).closest("div").remove();
                })
                .on('click', '.submit', function () {
                    // 将表格的内容提交给后台
                    $.ajax({
                        method: "POST",
                        url: "${pageContext.request.contextPath}/add",
                        data: $(".addForm").serialize()
                    }).done(function () {
                        $("#addDiv").remove();
                        loadTable();
                        alert("添加成功!");
                    }).fail(function () {
                        alert("添加失败，请检查再试!")
                    });
                })
                .appendTo("body");
        });
    }

    // 页面加载完要做的事情
    $(function () {
        loadTable();

        $("table").on("click", ".del", doDelete)
                   .on('click', ".update", doUpdate);
        $(".addPhone").on('click', doAdd);
    });

</script>

</body>
</html>
