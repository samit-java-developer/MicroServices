package com.samit.securityviadb.service;

import com.samit.securityviadb.entity.UserEntity;
import com.samit.securityviadb.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;

    @Override
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(()->new UsernameNotFoundException("User not available :: "+id));
    }
}
