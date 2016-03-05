package com.witbooking.api.repositories;

import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity, Integer> {
    LoginEntity findBySessionKey(String sessionKey);
    LoginEntity findFirstByUserOrderByExpireDate(UserEntity user);
}
