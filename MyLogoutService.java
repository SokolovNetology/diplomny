package service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import logger.LogStatus;
import logger.Logger;
import logger.SimpleLogger;
import repository.AuthenticationRepository;
import ru.netology.springcourse.JwtTokenUtil;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Service
@RequiredArgsConstructor
public class MyLogoutService implements LogoutHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationRepository authRepository;
    private final Logger logger = SimpleLogger.getInstance();
    @Value(value = "${jwt.authorization-header}")
    private String headerName;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("abrakadabra");
        String authHeader = request.getHeader(headerName);
        String jwtToken = authHeader.substring(7);
        String username = jwtTokenUtil.getUserName(jwtToken);
        authRepository.removeAuth(jwtToken);
        logger.log(LogStatus.INFO, String.format("Success delete token for User |'%s'|", username));
    }
}
