package config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloud_service.logger.LogStatus;
import ru.netology.cloud_service.logger.Logger;
import ru.netology.cloud_service.logger.SimpleLogger;
import ru.netology.cloud_service.utils.JWT.JwtTokenUtil;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    @Value(value = "${jwt.authorization-header}")
    private String headerName;
    private final Logger logger = SimpleLogger.getInstance();
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(headerName);
        String username = null;
        String jwtToken = null;
        //проверяем, что есть заголовок с Авторизацией и он начинается с "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtTokenUtil.getUserName(jwtToken);
            } catch (SignatureException e) {
                logger.log(LogStatus.ERROR, String.format("Signature Exception in Token from User |'%s'|", username));
            } catch (ExpiredJwtException e) {
                logger.log(LogStatus.ERROR, String.format("JWT Token from User |'%s'| has expired", username));
            }
        }

        // Проверяем что Username не пусто и что в конексте ничего не лежит
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtil.getUserRoles(jwtToken).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
            //помещаем токен в контекст
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
