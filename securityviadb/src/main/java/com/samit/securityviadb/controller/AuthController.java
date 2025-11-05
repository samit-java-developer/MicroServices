package com.samit.securityviadb.controller;


import com.samit.securityviadb.dto.LoginDto;
import com.samit.securityviadb.dto.LoginResponseDto;
import com.samit.securityviadb.dto.SignupDto;
import com.samit.securityviadb.dto.UserDto;
import com.samit.securityviadb.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<>("Hello",HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto){
        UserDto userDto=authService.signUp(signupDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
    @PostMapping("/logIn")
    public ResponseEntity<LoginResponseDto> logIn(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){
        LoginResponseDto loginResponseDto= authService.logIn(loginDto);
        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true); // it makes sure that this cookie cannot be accessed by any other it can only be fund with the help of your Http methods
        // no other attacker can be access our website
        // Prevents JavaScript access to the cookie
        response.addCookie(cookie); // Http only cookies can be passed from backend to frontend only
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request){
        String refreshToken= Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equalsIgnoreCase("refreshToken")).findFirst().map(Cookie::getValue).orElseThrow(()->new AuthenticationServiceException("RefreshToken not found"));
        LoginResponseDto loginResponseDto=authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }

}
