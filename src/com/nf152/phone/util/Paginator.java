package com.nf152.phone.util;

import java.util.ArrayList;
import java.util.List;


/**
 * 封装分页功能
 *
 * 使用方法：
 * 1. 获取用户参数，并使用这些参数创建 {@link Paginator} 对象
 * 2. 查询数据库，得到记录总数，保存到 {@link Paginator} 对象
 * 3. 查询数据库，得到分页数据，保存到 {@link Paginator} 对象。可以通过 {@link Paginator#getOffset()} 得到数据的开始序号。
 * 4. 保存 {@link Paginator} 到 request 或 session 中，用于页面渲染。
 * 5. 使用保存的 {@link Paginator} 对象渲染页面，可以使用 {@link Paginator#outputPageNav(String, String, boolean, boolean)} 直接输出页码导航
 *
 * <code>
 *
 *     // in the servlet
 *     Paginator pager = new Paginator(当前页, 每页显示数目, 导航栏页数);
 *
 *     pager.setRowCount(dao.count());
 *     pager.setData(dao.findAll(page.getOffset(), page.getSize());
 *
 *     request.addAttribute("pager", pager);
 *
 *
 *     // in the jsp
 *     <style>
 *         #pagination {}
 *     </style>
 *     <table>
 *         <%-- 数据展示 --%>
 *     </table>
 *     <nav>
 *         <%-- 页码导航栏 --%>
 *         ${page.outputPageNav("/url", "pageno", true, true)}
 *     </nav>
 *
 * </code>
 * @param <T>
 */
public class Paginator<T> {

    // 从用户处获取的参数
    private int current = 1;   // 当前页码
    private int pageSize = 10; // 每页数目
    private int linkSize = 5;  // 导航栏页码数目

    // 从数据库查询到的数据
    private int rowCount;      // 表示总共有多少条；
    private List<T> data = new ArrayList<>();  // 获取到的数据

    // 根据上面计算出的数据
    private int pageCount;     // 总共有多少页
    private List<Integer> links = new ArrayList<>();  // 导航条要显示的页码



    /* Constructors */
    public Paginator() {
    }

    public Paginator(int current, int pageSize, int linkSize) {
        this.current = current;
        this.pageSize = pageSize;
        this.linkSize = linkSize;
    }



    /* Accessors */
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getLinkSize() {
        return linkSize;
    }

    public void setLinkSize(int linkSize) {
        this.linkSize = linkSize;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.pageCount = (rowCount + pageSize - 1) / pageSize;
        calcLinks();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getOffset() {
        return (this.current - 1) * this.pageSize + 1;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<Integer> getLinks() {
        return links;
    }

    /**
     * 计算/刷新导航栏要显示的页码
     */
    public void calcLinks() {
        // 根据下面条件计算：
        // 1. 当前页 current
        // 2. 总页数 pageCount
        // 3. 显示数目 linkSize
        int halfLinkSize = this.linkSize/2;
        int startPage = Math.max(this.current - halfLinkSize, 1);
        int endPage = Math.min(this.current + halfLinkSize, this.pageCount);

        if(endPage - startPage < this.linkSize - 1) {
            if(startPage == 1) {
                endPage = Math.min(startPage + this.linkSize - 1, this.pageCount);
            } else {
                startPage = Math.max(endPage - this.linkSize + 1, 1);
            }
        }
        for(int i = startPage; i <= endPage; i++) {
            this.links.add(i);
        }
    }

    /**
     * 输出导航条的 html
     *
     * 简单封装，可根据需求自己定制
     *
     * @param url 请求路径
     * @param pagenoParam 表示请求中页码的字段名称
     * @param isFirstLast 是否显示首页末页
     * @param isPrevNext  是否显示上页下页
     * @return 分页的 html 语句
     */
    public String outputPageNav(String url, String pagenoParam, boolean isFirstLast, boolean isPrevNext) {

        String firstLabel = "首页", lastLabel = "末页", prevLabel = "?", nextLabel = "?";

        String normalLink = "  <li><a href='" + url + "?" + pagenoParam + "=%s'>%s</a></li>\n";
        String currentLink = "  <li><a href='#' class='current'>%s</a></li>\n";
        String readonlyLink = "  <li><a href='#' class='ro'>%s</a></li>\n";

        StringBuilder output = new StringBuilder("<ul id='pagination'>\n");

        // 首页
        if(isFirstLast) {
            if(current == 1) {
                output.append(String.format(readonlyLink, firstLabel));
            } else {
                output.append(String.format(normalLink, 1, firstLabel));
            }
        }

        // 上一页
        if(isPrevNext) {
            if(current < 2) {
                output.append(String.format(readonlyLink, prevLabel));
            } else {
                output.append(String.format(normalLink, current - 1, prevLabel));
            }
        }

        // 第 x 页
        for(Integer i : links) {
            if(current == i) {
                output.append(String.format(currentLink, i));
            } else {
                output.append(String.format(normalLink, i, i));
            }
        }

        // 下一页
        if(isPrevNext) {
            if(current >= pageCount) {
                output.append(String.format(readonlyLink, nextLabel));
            } else {
                output.append(String.format(normalLink, current + 1, nextLabel));
            }
        }

        // 尾页
        if(isFirstLast) {
            if(current == pageCount) {
                output.append(String.format(readonlyLink, lastLabel));
            } else {
                output.append(String.format(normalLink, pageCount, lastLabel));
            }
        }

        return String.valueOf(output.append("</ul>"));
    }

}