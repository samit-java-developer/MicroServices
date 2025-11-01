package com.samit.securitybasic.service;

import com.samit.securitybasic.dto.SignupDto;
import com.samit.securitybasic.dto.UserDto;
import com.samit.securitybasic.entity.UserEntity;
import com.samit.securitybasic.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


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
}
