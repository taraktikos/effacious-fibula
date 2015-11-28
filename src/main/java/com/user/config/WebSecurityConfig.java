package com.user.config;

import com.user.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/css/**", "/static/css/js/**", "/images/**", "**/favicon.ico", "/webjars/**").permitAll()
                .antMatchers("/switch_user").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .antMatchers("/switch_user_exit").hasRole("PREVIOUS_ADMINISTRATOR")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .antMatchers("/profile/test/**").hasRole("MANAGER")
                .antMatchers("/profile/**").authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("remember-me")
                .permitAll()
                .and()
            .addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new PlaintextPasswordEncoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userDetailsService());
        filter.setUsernameParameter("email");
        filter.setSwitchUserUrl("/switch_user");
        filter.setExitUserUrl("/switch_user_exit");
        filter.setTargetUrl("/");
        return filter;
    }
}
