package bteam.example.ecoolshop.config;


import bteam.example.ecoolshop.filter.JwtFilter;
import bteam.example.ecoolshop.util.JwtTokenUtil;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtConfigurer(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void configure(HttpSecurity security) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtTokenUtil);
        security.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}