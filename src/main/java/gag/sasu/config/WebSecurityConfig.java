package gag.sasu.config;

import gag.sasu.exception.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService jwtUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService,
                             JwtRequestFilter jwtRequestFilter) {
        this.jwtUserDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/posts").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/users/token/{userId}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().permitAll()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}