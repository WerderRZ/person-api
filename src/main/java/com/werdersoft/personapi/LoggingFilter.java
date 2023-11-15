package com.werdersoft.personapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        // Продолжить обработку запроса
        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // Создание записи лога
        String requestMessage = String.format("request method: %s, request URI: %s",
                request.getMethod(), request.getRequestURI());
        String responseMessage = String.format("response status: %d, request processing time: %d ms",
                response.getStatus(), duration);

        // Запись лога
        LOGGER.info(requestMessage);
        LOGGER.info(responseMessage);

    }
}
