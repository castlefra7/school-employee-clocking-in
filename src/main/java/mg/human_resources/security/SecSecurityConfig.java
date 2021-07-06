/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author lacha
 */
@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/perform_logout").permitAll()
                .antMatchers("/create-admin").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/error").permitAll()
                
                .antMatchers("/categories/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/employees/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/supplementaries/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/majorees/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/users/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/supplmax/**").hasAuthority(AdminAuthority.ADMIN)
                .antMatchers("/supplmax-form/**").hasAuthority(AdminAuthority.ADMIN)
                
                .antMatchers("/employees-front/**").authenticated()
                .antMatchers("/employees-pointage-front/**").authenticated()
                       
                //.anyRequest().permitAll()
                //.anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/employees-front", true)
                .failureUrl("/login?error=Nom ou mot de passe incorrect")
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID");
        http.cors();
    }

}
