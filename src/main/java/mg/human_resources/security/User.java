/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import mg.human_resources.bl.BaseModel;
import mg.human_resources.gen.FctGen;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author lacha
 */
public class User extends BaseModel {
    private static String COLUMNS = "id;name;password;user_type";
    private String name;
    private String password;
    private String user_type;
    
     @Override
     public BaseModel findById(Object id, Connection conn) throws Exception {
         String req = "select * from users where id = " + id;
         return (BaseModel)FctGen.find(this, req, COLUMNS, conn);
     }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static User findByUsername(Connection conn, String username) throws Exception {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            String sql = "select id, name, password, user_type from users where name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            res = pstmt.executeQuery();
            if (res.next()) {
                user = new User();
                user.setId(res.getInt(1));
                user.setName(res.getString(2));
                user.setPassword(res.getString(3));
                user.setUser_type(res.getString(4));
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (res != null) {
                res.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return user;
    }

    @Override
    public void insert(Connection conn) throws Exception {
        
        String _password = passwordEncoder().encode(getPassword());
        this.setPassword(_password);
        FctGen.insert(this, COLUMNS, "users", conn);
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        String req = "select * from users";
        return (List<BaseModel>)(List<?>)FctGen.findAll(this, req, COLUMNS, conn);
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Connection conn) throws Exception {
        String req = String.format("update users set name = '%s', password = '%s', user_type = '%s' where id = %d",
                getName(), getPassword(), getUser_type(), getId());
        FctGen.update(req, conn);
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
