package com.nf152.phone.web;

import com.nf152.phone.bean.Phone;
import com.nf152.phone.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/listAll")
public class PhoneListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Phone> phones = DBUtil.queryList(Phone.class, "select * from phone");

        req.setAttribute("phones", phones);
        req.getRequestDispatcher("/WEB-INF/views/list.jsp").forward(req, resp);
    }
}
