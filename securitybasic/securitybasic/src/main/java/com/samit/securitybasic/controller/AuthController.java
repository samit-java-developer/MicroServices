package com.samit.securitybasic.controller;


import com.samit.securitybasic.dto.SignupDto;
import com.samit.securitybasic.dto.UserDto;
import com.samit.securitybasic.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto){
        UserDto userDto=authService.signUp(signupDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
