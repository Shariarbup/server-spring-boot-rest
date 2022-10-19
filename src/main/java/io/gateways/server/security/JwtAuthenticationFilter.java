package io.gateways.server.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
//        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            filterChain.doFilter(request, response);
//        }
        String requestToken = request.getHeader("Authorization");
        System.out.println(requestToken);
        String username = null;
        String token = null;
        if(requestToken != null && requestToken.startsWith("Bearer")){
            token = requestToken.substring(7);
            try{
                username = this.jwtTokenHelper.getUsernameFromToken(token);
            }catch(IllegalArgumentException ex){
                System.out.println("Unable to get JWT token"+ ex);
            }catch (ExpiredJwtException ex){
                System.out.println("JWT token has expired");
            }catch(MalformedJwtException ex){
                System.out.println("Invalid JWT");
            }
        }else {
            System.out.println("JWT token does not begin with Bearer");
        }

        //once we get the token then we validate
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           //userdetails maddhome user er details peye gelam ekhon authentication set korte hobe
           if(this.jwtTokenHelper.validateToken(token, userDetails)){
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           }else{
               System.out.println("Invalid JWT token");
           }
        }else{
            System.out.println("username is null or security context is not null");
        }
        filterChain.doFilter(request, response);
    }
}
