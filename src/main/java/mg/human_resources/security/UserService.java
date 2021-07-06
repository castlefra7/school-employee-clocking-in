/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.security;

import java.sql.Connection;
import java.sql.SQLException;
import mg.human_resources.conn.ConnGen;
import mg.human_resources.security.AdminPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author lacha
 */
@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connection conn = null;
        UserDetails userPrinc = null;
        try {
            logger.info(username);
            conn = ConnGen.getConn();
            User user = new User().findByUsername(conn, username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            if ("admin".equals(user.getUser_type().toLowerCase())) {
                userPrinc = new AdminPrincipal(user);
            }
            if ("user".equals(user.getUser_type().toLowerCase())) {
                userPrinc = new UserPrincipal(user);
            }
        } catch (Exception ex) {
            logger.error("user service error", ex.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return userPrinc;
    }

}
