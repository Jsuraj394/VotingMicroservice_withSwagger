package com.Sura.CandidateService.Config;

import com.Sura.CandidateService.Service.JwtService;
import com.Sura.CandidateService.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username =  null;
        String token = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
             /*this condition checks if the Authorization header is missing or does not start with "Bearer ".
             If either of these conditions is true, it means that there is no JWT token present in the request,
              and the filter should simply continue the filter chain without attempting to authenticate the user. and as per the security filter chain it Allows only login and register .
             No JWT present → just continue the chain */
            filterChain.doFilter(request, response);
            return ;
        }

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim().replaceAll("\\s+", "");;
            username = jwtService.extractUsername(token);
        }
        if (!token.contains(".") || token.split("\\.").length != 3) {
            throw new RuntimeException("Invalid JWT format");   // for this create the spring exception handlers package and use the custom classes
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(UserService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                    );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}