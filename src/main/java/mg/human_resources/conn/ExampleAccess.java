/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lacha
 */
public class ExampleAccess {
    public String testConnection() throws Exception {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            return testConnection(conn);
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(conn != null) conn.close();
        }
        
    }
    public String testConnection(Connection conn) throws Exception {
        String result = "";
        String req = "select* from test";
        PreparedStatement prep = null;
        ResultSet res = null;
        try {
            prep = conn.prepareStatement(req);
            res = prep.executeQuery();
            while(res.next()) {
                result = res.getString("name");
            }
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(res!=null) res.close();
            if(prep!=null) prep.close();
        }
        return result;
    }
    
    public void createTable() throws Exception {
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = ConnGen.getConn();
            prep = conn.prepareStatement("create table test (id serial primary key, name varchar(255))");
            prep.executeUpdate();
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(prep!=null) prep.close();
            if(conn != null) conn.close();
        }
    }
    
    public void insertinto() throws Exception {
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = ConnGen.getConn();
            prep = conn.prepareStatement("insert into test (name) values ('hih')");
            prep.executeUpdate();
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(prep!=null) prep.close();
            if(conn != null) conn.close();
        }
    }
}
