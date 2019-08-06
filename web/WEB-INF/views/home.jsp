<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="root" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>首页</title>
    <link rel="stylesheet" href="${root}/assets/bootstrap.min.css">

    <script src="${root}/assets/jquery.js"></script>
    <script src="${root}/assets/jquery.validate.min.js"></script>
    <script src="${root}/assets/messages_zh.js"></script>

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
        label.error {
            color: red;
            padding-left: 1em;
        }
    </style>
</head>
<body>

<header>
    <h1>手机商城</h1>
</header>

<div class="container">
    <table class="table table-striped table-hover table-bordered">
    </table>

    <div>
        <button class="addPhone">添加</button>
        <button class="deleteBatch">删除选择</button>
    </div>

    <div style="margin-top: 1em">
        <button class="btn btn-primary">反选</button>
        <button class="btn btn-danger">取消所有选择</button>
        <button class="btn btn-success">选择所有</button>
    </div>
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

    function doDeleteBatch(e) {
        $checked = $("table :checked");
        if ($checked.length < 1) {
            alert("必须要选择至少一个")
        } else {
            if (window.confirm("是否确定删除？")) {
                $.ajax({
                    method: "GET",
                    url: "${root}/delete",
                    data: $checked.serialize() // id=2&id=3&id=5
                }).done(function () {
                    loadTable();
                }).fail(function () {
                    alert("删除失败!");
                });
            }
        }
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
                        data: $("#editForm").serialize()
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
                    // 进行校验
                    // 1. 名字不能为空，而且不能太长
                    // 2. 厂商不能为空，而且名字不能太长
                    // 3. 价格不能为空，而且必须是一个合理的数字

                    $("#addForm").validate({
                        rules: {
                            price: {
                                required: true,
                                number: true,
                                range:[1, 100000]
                            }
                        }
                    });

                    if (!$("#addForm").valid()) return;

                    // 将表格的内容提交给后台
                    $.ajax({
                        method: "POST",
                        url: "${pageContext.request.contextPath}/add",
                        data: $("#addForm").serialize()
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

        $("table").on("click", ".del", doDelete).on('click', ".update", doUpdate);
        $(".addPhone").on('click', doAdd);
        $(".deleteBatch").click(doDeleteBatch);
    });

</script>

</body>
</html>
