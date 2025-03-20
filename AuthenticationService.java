package service;

import lombok.RequiredArgsConstructor;
import model.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import logger.LogStatus;
import logger.Logger;
import logger.SimpleLogger;
import model.dtos.JwtRequest;
import model.dtos.JwtRequest;
import repository.AuthenticationRepository;
import ru.netology.springcourse.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationRepository authRepository;
    private final Logger logger = SimpleLogger.getInstance();
    @Value(value = "${jwt.authorization-header}")
    private String headerName;


    public String login(String userName, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userName,
                    password
            ));
        } catch (BadCredentialsException e) {
            logger.log(LogStatus.ERROR, "Bad credentials: Uncorrected login or password");
            throw new exception.BadCredentialsException(
                    "Bad credentials: Uncorrected login or password"
            );
        }
        User user = userService.findUserByUserName(userName).get();
        String token = jwtTokenUtil.generatedToken(user);
        authRepository.putAuth(token, user.getUsername());
        logger.log(LogStatus.INFO, String.format("Success login from User |'%s'|", user.getUsername()));
        return token;
    }

    public String getUsernameByToken(String jwtToken) {
        return authRepository.getUsername(jwtToken);
    }


}
