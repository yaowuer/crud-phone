package com.nf152.phone.web;

import com.nf152.phone.bean.Phone;
import com.nf152.phone.util.DBUtil;
import com.nf152.phone.util.Paginator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/listPerPage")
public class PhoneListPerPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pageNumber = req.getParameter("pageno") == null ? 1 : Integer.parseInt(req.getParameter("pageno"));
        int pageSize = req.getParameter("size") == null ? 2 : Integer.parseInt(req.getParameter("size"));
        int linkSize = 7;

        Paginator<Phone> paginator = new Paginator<>(pageNumber, pageSize, linkSize);

        List<Map<String, Object>> maps = DBUtil.queryForMap("select count(*) c from phone");
        paginator.setRowCount((Integer) maps.get(0).get("c"));

        List<Phone> phones = DBUtil.queryList(Phone.class, "select * from phone limit ? offset ?",
                pageSize,
                paginator.getOffset());
        paginator.setData(phones);

        req.setAttribute("paginator", paginator);
        req.getRequestDispatcher("/WEB-INF/views/listPerPage.jsp").forward(req, resp);
    }
}
