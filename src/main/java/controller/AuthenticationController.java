package controller;

import lombok.RequiredArgsConstructor;
import model.dtos.JwtRequest;
import model.dtos.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.AuthenticationService;

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
