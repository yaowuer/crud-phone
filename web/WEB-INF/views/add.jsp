<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="addDiv">
    <h3>添加手机</h3>
    <form id="addForm">
        <div>
            <input id="name" name="name" class="name" placeholder="名字" required>
        </div>

        <div>
            <input name="brand" placeholder="公司" required>
        </div>

        <div>
            <input name="price" placeholder="价格" required>
        </div>

        <div>
            <input name="email" placeholder="邮箱" required>
        </div>

        <div>
            <input type="submit" class="submit" value="提交">
            <button class="back">返回</button>
        </div>
    </form>

</div>
