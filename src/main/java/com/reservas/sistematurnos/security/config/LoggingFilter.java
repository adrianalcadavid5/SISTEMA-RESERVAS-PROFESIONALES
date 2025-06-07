package com.reservas.sistematurnos.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String url = request.getRequestURI();
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String clientIp = request.getRemoteAddr();

        filterChain.doFilter(request, response);
        int statusAfter = response.getStatus();

        logger.info("\n****************************" +
                        "\n NUEVA SOLICITUD RECIBIDA" +
                        "\n🔹 Método: {}" +
                        "\n🔹 URL: {}" +
                        "\n🔹 Origin: {}" +
                        "\n🔹 Referer: {}" +
                        "\n🔹 IP: {}" +
                        "\n🔹 Estado: {} ({})" +
                        "\n****************************",
                method, url, origin, referer, clientIp, statusAfter, getHttpStatusMessage(statusAfter));
    }

    private String getHttpStatusMessage(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
