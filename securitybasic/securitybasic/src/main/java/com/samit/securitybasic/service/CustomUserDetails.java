//package com.samit.securitybasic.service;
//
//import com.samit.securitybasic.repository.UserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetails implements UserDetailsService {
//
//    private final UserRepo userRepo;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User name not found with given id :: "+email));
//    }
//}
////