package gag.sasu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gag.sasu.entity.User;
import gag.sasu.exception.ErrorMessage;
import gag.sasu.service.JwtUserDetailsService;
import gag.sasu.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        if (isPathNotPermitted(request)) {
            final String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                tryAcquiringAccessToken(request, response, chain, requestTokenHeader);
            } else {
                forbiddenResponse(response, "Token missing");
            }
        } else {
            chain.doFilter(request, response);
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
        } catch (IllegalArgumentException | MalformedJwtException e) {
            forbiddenResponse(response, "Unable to get token");
        } catch (ExpiredJwtException e) {
            forbiddenResponse(response, "Token expired");
        }
    }

    private void forbiddenResponse(HttpServletResponse response, String message) throws IOException {
        log.error(message);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorMessage(message)));
    }

    private boolean isPathNotPermitted(HttpServletRequest request) {
        return isProductPathNotPermitted(request) || isNotPermitted(request);
    }

    private boolean isProductPathNotPermitted(HttpServletRequest request) {
        return (request.getMethod().equalsIgnoreCase("post") && request.getServletPath().startsWith("/posts"))
                || (request.getMethod().equalsIgnoreCase("delete") && request.getServletPath().startsWith("/posts"))
                || (request.getMethod().equalsIgnoreCase("get") && request.getServletPath().startsWith("/users/token"))
                || (request.getMethod().equalsIgnoreCase("post") && request.getServletPath().startsWith("/categories"))
                || (request.getMethod().equalsIgnoreCase("post") && request.getServletPath().startsWith("/comments"))
                || (request.getMethod().equalsIgnoreCase("put") && request.getServletPath().startsWith("/posts/vote"))
                || (request.getMethod().equalsIgnoreCase("get") && request.getServletPath().startsWith("/posts/voted"))
                || (request.getMethod().equalsIgnoreCase("get") && request.getServletPath().startsWith("/posts/user"));
    }

    private boolean isNotPermitted(HttpServletRequest request) {
        return false;
    }
}