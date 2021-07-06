/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public class FctGen {

    public static boolean exists(String req, Connection conn) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static LocalDate addDay(int numberDay, LocalDate dt) {
        return dt.plusDays(numberDay);
    }

    public static LocalDate minusDay(int numberDay, LocalDate dt) {
        return dt.minusDays(numberDay);
    }

    public static LocalDate convertToLocalDateViaSqlDate(java.util.Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static java.util.Date parseDt(String source) throws ParseException {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-mm-dd hh:mm");

        java.util.Date dt = simple.parse(source);
        return dt;
    }

    public static java.sql.Date parse(String source) throws ParseException {

        SimpleDateFormat simple = new SimpleDateFormat("yyyy-mm-dd");

        java.util.Date dt = simple.parse(source);
        return new java.sql.Date(dt.getTime());
    }

    public static boolean update(String req, Connection conn) throws SQLException {
        logger.info(req);
        boolean result = false;
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(req);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows >= 1) {
                result = true;
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    public static double getDouble(String columnName, String req, Connection conn) throws SQLException {
        double result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble(columnName);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static int getInt(String columnName, String req, Connection conn) throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(columnName);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static String getString(String req, Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException("La connection est nulle");
        }
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(0);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static Date getDate(String req, String colName, Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException("La connection est nulle");
        }
        Date result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            System.out.println(req);
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getTimestamp(colName);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static double getAmount(String req, String colName, Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException("La connection est nulle");
        }
        double result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble(colName);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static Object find(Object ob, String req, String columns, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return find(ob, req, columns.split(";"), conn);
    }

    public static Object find(Object ob, String req, String[] columns, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = null;
        List<Object> all = findAll(ob, req, columns, conn);
        if (all.size() > 0) {
            result = all.get(0);
        }
        return result;
    }

    public static List<Object> findAll(Object ob, String req, String columns, Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
       
        return findAll(ob, req, columns.split(";"), conn);
    }

    public static List<Object> findAll(Object ob, String req, String[] columns, Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if (conn == null) {
            throw new NullPointerException("La connection est nulle");
        }
        List<Object> result = new ArrayList();

        Class cls = ob.getClass();
        Class[] paramsCons = new Class[0];
        Constructor constructor = cls.getConstructor(paramsCons);

        List<Method> methods = getSpecMethods(cls.getMethods(), "set");
        Class superCls = cls.getSuperclass();

        if (!"object".equals(superCls.getSimpleName().toLowerCase())) {
            List<Method> superMethods = getSpecMethods(superCls.getMethods(), "set");
            for (Method supM : superMethods) {

                methods.add(supM);
            }
        }
        List<Method> neededMethods = new ArrayList();

        for (String column : columns) {
            for (Method method : methods) {
                String methodName = method.getName().toLowerCase().substring(3);
                if (methodName.equals(column.toLowerCase())) {
                    neededMethods.add(method);
                    break;
                }
            }
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(req);
            rs = pstmt.executeQuery();
            Object[] params = new Object[0];
            while (rs.next()) {
                Object newInstance = constructor.newInstance(params);
                for (Method method : neededMethods) {
                    setValues(newInstance, method, rs);
                }
                result.add(newInstance);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }

    public static void setValues(Object ob, Method method, ResultSet rs) throws NoSuchMethodException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        if (method.getParameters().length <= 0) {
            throw new NoSuchMethodException("Invalide setter");
        }
        Class returnClass = method.getParameterTypes()[0];
        Object[] args = new Object[1];
        String colName = method.getName().toLowerCase().substring(3);
        if (Number.class.isAssignableFrom(returnClass) || returnClass.getSimpleName().equals("int") || returnClass.getSimpleName().equals("float") || returnClass.getSimpleName().equals("double")) {
            if (returnClass.getSimpleName().equals("int")) {
                args[0] = rs.getInt(colName);
            } else if (returnClass.getSimpleName().equals("double")) {
                args[0] = rs.getDouble(colName);
            } else if (returnClass.getSimpleName().equals("float")) {
                args[0] = rs.getFloat(colName);
            } else if (returnClass.getSimpleName().equals("boolean")) {
                args[0] = rs.getBoolean(colName);
            } else {
                throw new IllegalArgumentException("Type de données non gérer");
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("string")) {
            args[0] = rs.getString(colName);
        } else if (returnClass.getSimpleName().toLowerCase().equals("date")) {
            args[0] = rs.getTimestamp(colName);
        } else if (returnClass.getSimpleName().toLowerCase().equals("timestamp")) {
            Calendar utc = Calendar.getInstance(
                    TimeZone.getTimeZone("UTC"));

            args[0] = rs.getTimestamp(colName, utc);
        }
        method.invoke(ob, args);
    }

    public static List<Method> getSpecMethods(Method[] methods, String prefix) {
        List<Method> result = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith(prefix)) {
                result.add(method);
            }
        }
        return result;
    }

    public static String methodReturnValue(Object ob, Method method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String result = "";
        Class returnClass = method.getReturnType();
        Object[] params = new Object[0];
        Object value;
        value = method.invoke(ob, params);
        if (Number.class.isAssignableFrom(returnClass) || returnClass.getSimpleName().equals("int") || returnClass.getSimpleName().equals("float") || returnClass.getSimpleName().equals("double")) {
            if (value != null) {
                result = String.valueOf(value);
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("string")) {
            if (value != null) {
                result = "'" + (String) value + "'";
            } else {
                result = "'null'";
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("date")) {
            if (value != null) {
                result = "'" + value.toString() + "'";
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("char")) {
            if (value != null) {
                result = "'" + value.toString() + "'";
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("boolean")) {
            if (value != null) {
                result = value.toString();
            }
        } else if (returnClass.getSimpleName().toLowerCase().equals("timestamp")) {
            if (value != null) {
                result = "'" + value.toString() + "'";
            } else {
                result = "null";
            }
        }
        return result;
    }

    public static void insert(Object ob, String columns, String tableName, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        insert(ob, columns.split(";"), tableName, conn);
    }
    static Logger logger = LoggerFactory.getLogger(FctGen.class);

    public static void insert(Object ob, String[] columns, String tableName, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        if (conn == null) {
            throw new NullPointerException("La connection est nulle");
        }
        if (ob == null) {
            throw new NullPointerException("Veuillez spécifier un objet");
        }
        Class cls = ob.getClass();
        List<Method> methods = getSpecMethods(cls.getMethods(), "get");

        String values = "(";
        String columnNames = "(";
        for (String columnName : columns) {
            columnName = columnName.toLowerCase();
            for (Method method : methods) {
                String methodName = method.getName().toLowerCase().substring(3);
                if (columnName.equals(methodName)) {
                    columnNames += columnName + ",";
                    method.getReturnType();
                    values += methodReturnValue(ob, method) + ",";
                    break;
                }
            }
        }
        values = values.substring(0, values.length() - 1) + ")";
        columnNames = columnNames.substring(0, columnNames.length() - 1) + ")";
        String req = String.format("insert into %s %s values %s", tableName, columnNames, values);
        logger.info(req);
        PreparedStatement pstmt = null;
        try {

            pstmt = conn.prepareStatement(req);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
