package com.nf152.phone.web;

import com.nf152.phone.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete")
public class PhoneDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 需要若干校验

        String[] ids = req.getParameterValues("id");
        for (String id : ids) {
            DBUtil.execute("delete from phone where id = ?", id);
        }
    }
}
