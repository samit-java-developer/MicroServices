package com.samit.securityviadb.service;

import com.samit.securityviadb.entity.UserEntity;
import org.springframework.stereotype.Service;

public interface UserService {

    UserEntity getUserById(Long id);
}
