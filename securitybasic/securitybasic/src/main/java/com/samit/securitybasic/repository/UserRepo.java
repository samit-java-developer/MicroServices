package com.samit.securitybasic.repository;

import com.samit.securitybasic.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    public Optional<UserEntity> findByEmail(String email);
}
