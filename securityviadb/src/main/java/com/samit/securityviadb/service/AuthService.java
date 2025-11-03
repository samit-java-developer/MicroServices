package com.samit.securityviadb.service;

import com.samit.securityviadb.dto.LoginDto;
import com.samit.securityviadb.dto.SignupDto;
import com.samit.securityviadb.dto.UserDto;
import com.samit.securityviadb.entity.UserEntity;
import com.samit.securityviadb.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserDto signUp(SignupDto signupDto) {
        Optional<UserEntity> optionalUser =userRepo.findByEmail(signupDto.getEmail());
        if (optionalUser.isPresent()){
            throw new BadCredentialsException("Cannot signup, User already exists with email "+signupDto.getEmail());
        }
        // Map SignupDto to UserEntity and encode the password
        UserEntity mappedUser =modelMapper.map(signupDto,UserEntity.class);
        mappedUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        UserEntity savedUser=userRepo.save(mappedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String logIn(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        UserEntity entity = (UserEntity) authentication.getPrincipal(); //user-entity used from you custom entity class
        return jwtService.createToken(entity);
    }
}
