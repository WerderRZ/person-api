package com.werdersoft.personapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        LoggingRequestWrapper requestWrapper = new LoggingRequestWrapper(request);
        StringBuilder requestBody = new StringBuilder();

        try (BufferedReader reader = requestWrapper.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // Продолжить обработку запроса
        filterChain.doFilter(requestWrapper, response);
        long duration = System.currentTimeMillis() - startTime;

        // Создание записи лога
        String requestMessage = String.format("request method: %s, request URI: %s, payload: %s",
                requestWrapper.getMethod(), requestWrapper.getRequestURI(), requestBody);
        String responseMessage = String.format("response status: %d, request processing time: %d ms",
                response.getStatus(), duration);

        // Запись лога
        log.info(requestMessage);
        log.info(responseMessage);

    }
}
