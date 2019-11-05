package test.demo.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import test.demo.entity.User;
import test.demo.exception.ErrorMessage;
import test.demo.service.JwtUserDetailsService;
import test.demo.service.UserService;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (isPathPermitted(request)) {
            chain.doFilter(request, response);
        } else {
            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                tryAcquiringAccessToken(request, response, chain, requestTokenHeader);
            } else {
                forbiddenResponse(response, "Token missing");
            }
        }
    }

    private void tryAcquiringAccessToken(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, String requestTokenHeader) throws IOException, ServletException {
        try {
            String jwtToken = requestTokenHeader.substring(7);

            Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
            String email = claims.getSubject();

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserByEmail(email);
                request.setAttribute("userId", user.getUid());

                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            forbiddenResponse(response, "Unable to get token");
        } catch (ExpiredJwtException e) {
            forbiddenResponse(response, "Token expired");
        }
    }

    void forbiddenResponse(HttpServletResponse response, String message) throws IOException {
        log.error(message);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorMessage(message)));
    }

    private boolean isPathPermitted(HttpServletRequest request) {
        return isImagePathPermitted(request) || isPermitted(request);
    }

    private boolean isImagePathPermitted(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("get")
                && request.getServletPath().startsWith("/products/image");
    }

    private boolean isPermitted(HttpServletRequest request) {
        return request.getServletPath().startsWith("/users");
    }
}