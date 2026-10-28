package com.pk.question_service_v3.controller.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class GatewayOnlyFilter extends OncePerRequestFilter {

    private final byte[] secret;

    public GatewayOnlyFilter(@Value("${gateway.internal-secret}")String secret)
    {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/acutator"))
        {
            filterChain.doFilter(request,response);
            return;
        }

        String header = request.getHeader("X-Gateway-Secret");

        if(header == null || !MessageDigest.isEqual(secret,header.getBytes(StandardCharsets.UTF_8)))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request,response);


    }
}
