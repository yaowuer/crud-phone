<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="editDiv">
    <h3>修改手机</h3>
    <form class="editForm">
        <div>
            <input name="name" placeholder="名字" value="${phone.name}">
        </div>

        <div>
            <input name="brand" placeholder="公司" value="${phone.brand}">
        </div>

        <div>
            <input name="price" placeholder="价格" value="${phone.price}">
        </div>
        <input type="hidden" name="id" value="${phone.id}">
    </form>

    <button class="submit">保存修改</button>
    <button class="back">返回</button>
</div>
