package com.nf152.phone.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    /**
     * usage:
     * <p>
     * DBUtil.execute("insert into xxx (name, age) values (?, ?)", "tom", 12);
     * DBUtil.execute("delete from xxx where id = ?", 22);
     */
    public static int execute(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = getPrepareStatement(conn, false, sql, params);
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("执行 SQL 出错: " + ex.getMessage());
        } finally {
            close(conn, ps, null);
        }
    }

    public static int insertWithLastId(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = getPrepareStatement(conn, true, sql, params);
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return -1;
        } catch (Exception ex) {
            throw new RuntimeException("执行 SQL 出错: " + ex.getMessage());
        } finally {
            close(conn, ps, null);
        }
    }

    /**
     * usage:
     * <p>
     * List<Student> students =
     * DBUtil.queryForList(Student.class, "select * from student where score > ?", 33);
     */
    public static <T> List<T> queryList(Class<T> clazz, String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            List<T> rets = new ArrayList<>();

            conn = getConnection();
            ps = getPrepareStatement(conn, false, sql, params);

            rs = ps.executeQuery();

            while (rs.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String colmnName = metaData.getColumnName(i);  // 字段名
                    String fieldName = getCalmName(colmnName);
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1); // setXxx 方法名
                    Field field = clazz.getDeclaredField(fieldName);
                    Method method = clazz.getDeclaredMethod(methodName, field.getType());
                    method.invoke(instance, field.getType().cast(rs.getObject(i)));
                }
                rets.add(instance);
            }

            return rets.size() > 0 ? rets : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("查询出错: " + ex.getMessage());
        } finally {
            close(conn, ps, rs);
        }
    }

    public static <T> T queryOne(Class<T> clazz, String sql, Object... params) {
        List<T> ts = queryList(clazz, sql, params);
        return ts == null ? null : ts.get(0);
    }

    public static List<Map<String, Object>> queryForMap (String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List<Map<String, Object>> rets = new ArrayList<>();
            conn = getConnection();
            ps = getPrepareStatement(conn, false, sql, params);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);  // 字段名
                    row.put(columnName, rs.getObject(columnName));
                }
                rets.add(row);
            }
            return rets.size() > 0 ? rets : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("查询出错: " + ex.getMessage());
        } finally {
            close(conn, ps, rs);
        }
    }

    private static PreparedStatement getPrepareStatement(Connection conn, boolean isLastId, String sql, Object... params) throws SQLException {
        PreparedStatement ps;
        if (isLastId) {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            ps = conn.prepareStatement(sql);
        }
        System.out.printf("-- %s", sql);
        for (int i = 1; i <= params.length; i++) {
            ps.setObject(i, params[i - 1]);
            System.out.printf(",  %s", params[i - 1]);
        }
        System.out.println();
        return ps;
    }

    private static String getCalmName(String name) {
        if (name != null && !name.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0, len = name.length(); i < len; i++) {
                if (name.charAt(i) == '_') {
                    while (name.charAt(i + 1) == '_') {
                        i++;
                    }
                    stringBuilder.append(("" + name.charAt(++i)).toUpperCase());
                } else {
                    stringBuilder.append(name.charAt(i));
                }
            }
            return stringBuilder.toString();
        }
        return name;
    }

    //////////////////////////

    public static Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:e:/workdir/demo-phone/db/phone.db");
    }

    public static void close(Connection conn, Statement stmt, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception ignored) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception ignored) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void main(String[] args) {
        String sql = "select b.*, d.amount shumu from dingdan_xijie d join book b on d.bookid = b.id where ddid = ?";
        String ddid = "9f50c3b9-ce87-411e-a5e3-f20e22d441ea";
        List<Map<String, Object>> order = DBUtil.queryForMap(sql, ddid);
        System.out.println(order);
    }

}
