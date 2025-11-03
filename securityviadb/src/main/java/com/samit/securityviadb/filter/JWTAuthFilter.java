package com.samit.securityviadb.filter;

import com.samit.securityviadb.entity.UserEntity;
import com.samit.securityviadb.service.JwtService;
import com.samit.securityviadb.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final UserServiceImpl userServiceImpl;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization"); //Extract the Authorization Header:
        if (requestTokenHeader==null || requestTokenHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token=requestTokenHeader.split("Bearer ")[1];
        Long userId=jwtService.generateUserIdFromToken(token);

        ///Check if userId is Present and the Security Context is Empty
        if (userId!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            //Retrieve the User Entity
            UserEntity user = userServiceImpl.getUserById(userId);
            //Create an Authentication Token
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, null);  //put the user in spring security context holder and in other field we used 'null' for only testing purpose
            //Set Authentication Details
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            //Set Authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
