package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloud_service.model.dtos.request.JwtRequest;
import ru.netology.cloud_service.model.dtos.response.JwtResponse;
import ru.netology.springcourse.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(new JwtResponse(authenticationService.login(
                jwtRequest.getUserName(),
                jwtRequest.getUserName()
        )));
    }

}
