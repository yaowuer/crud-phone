package com.nf152.phone.web;

import com.nf152.phone.bean.Phone;
import com.nf152.phone.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/edit")
public class PhoneEditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        req.setAttribute("phone", DBUtil.queryOne(Phone.class, "select * from phone where id = ?", id));
        req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String brand = req.getParameter("brand");
        Double price = Double.valueOf(req.getParameter("price"));

        DBUtil.execute("update phone set name=?, brand=?, price=? where id=?",
                name, brand, price, id);
    }
}
