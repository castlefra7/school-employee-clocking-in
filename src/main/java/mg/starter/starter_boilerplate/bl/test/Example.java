/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate.bl.test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import mg.human_resources.conn.ConnGen;
import mg.human_resources.gen.ColOrder;
import mg.human_resources.gen.ColType;
import mg.human_resources.gen.FctGen;
import mg.starter.starter_boilerplate.rsc.ExampleAttrClass;

/**
 *
 * @author lacha
 */
public final class Example {
    private Integer id;
    private String name;
    private int age;
    private Double salary;
    private Timestamp date_birth;
    
    private static String SQL= "select id, name, date_birth, salary from example";
    
    public Example() {
        
    }
    public Example(String _name, double _salary, Timestamp _date_birth) throws Exception {
        this.setName(_name);
        this.setSalary(_salary);
        this.setDate_birth(_date_birth);
    }
    
    public Example(ExampleAttrClass exampleAttr) throws Exception {
        this.setName(exampleAttr.getName());
        this.setSalary(exampleAttr.getSalary());
        this.setDate_birth(new Timestamp(exampleAttr.getDate_birth().getTime()));
    }
    
    public void insert() throws Exception {
        Connection conn = null;
        
        try {
            conn = ConnGen.getConn();
            this.insert(conn);
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(conn!= null) conn.close();
        }
    }
    
    public static int count(Connection conn) throws SQLException {
        return FctGen.getInt("numbers", "select count(*) as numbers from example", conn);
    }
    
    public static List<Example> findPage(Connection conn, int page, int count) throws Exception {
        String sql = String.format(SQL + " limit %d offset %d", count, page);
        return findAll(conn, sql);
    }
    
    public static List<Example> orderBy(Connection conn, String col, ColOrder colOrder) throws Exception {
        String sql = SQL;
        if(colOrder == ColOrder.ASC) {
            sql = String.format(sql + " order by %s %s", col, "asc");
        } else {
            sql = String.format(sql + " order by %s %s", col, "desc");
        }
        return findAll(conn, sql);
    }
    
    @Override
    public String toString() {
        return this.getId() + " " + this.getName() + " " + this.getSalary() + " " + this.getDate_birth();
    }
    
    public static List<Example> findByCol(Connection conn, String col, String value, ColType colType) throws Exception {
        String sql = SQL;
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
       
        switch(colType) {
            case STR:
                sql =  String.format(sql +" where %s like '%%%s%%'",col, value);
                break;
            case INT:
                sql = String.format(sql + " where %s = %f", col, Double.valueOf(value));
                break;
            case DATE:
                sql = String.format(sql + " where %s = '%s'", col, value);
                break;
            case DOUBLE:
                sql = String.format(sql +" where cast(%s as int) = %d", col, Integer.valueOf(value));
            default:
                sql = sql;
                break;
        }
        return findAll(conn, sql);
    }
    
    public static List<Example> findAll(Connection conn) throws Exception {
        String sql = SQL;
        return findAll(conn, sql);
    }
    
    public static List<Example> findAll(Connection conn, String sql) throws Exception {
        List<Example> result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            pstmt = conn.prepareStatement(sql);
            res = pstmt.executeQuery();
            while(res.next()) {
                int id = res.getInt(1);
                String name = res.getString(2);
                Timestamp date_birth = res.getTimestamp(3);
                double salary = res.getDouble(4);
                Example example = new Example(name, salary, date_birth);
                example.setId(id);
                result.add(example);
            }
            return result;
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(res != null) res.close();
            if(pstmt !=null) pstmt.close();
        }
    }
    
    public void insert(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into example (name, age, salary, date_birth) values (?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getName());
            pstmt.setInt(2, getAge());
            pstmt.setDouble(3, getSalary());
            pstmt.setTimestamp(4, getDate_birth());
            if(pstmt.execute() == false) {
                return;
            }
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(pstmt != null) pstmt.close();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if(name.isEmpty() || name.trim().isEmpty()) throw new Exception("Entrez un nom valide");
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) throws Exception {
        if(salary <= 0) throw new Exception("Entrez un salaire correct");
        this.salary = salary;
    }

    public Timestamp getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Timestamp date_birth) {
        this.date_birth = date_birth;
    }
    
    
}
