/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.security;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author lacha
 */
public class UserAuthority implements GrantedAuthority {
    public static String USER = "USER";
    public static String ADMIN = "ADMIN";
    
    @Override
    public String getAuthority() {
        return USER;
    }
    
}
