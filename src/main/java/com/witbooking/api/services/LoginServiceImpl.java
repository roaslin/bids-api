package com.witbooking.api.services;

import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.entities.UserEntity;
import com.witbooking.api.repositories.LoginRepository;
import com.witbooking.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getSessionToken(String userID) {

        Optional<UserEntity> user = Optional.ofNullable(userRepository.findOne(Integer.valueOf(userID)));
        Optional<LoginEntity> login;

        if (!user.isPresent()) {
            // create user if does not exist
            user = Optional.of(userRepository.save(UserEntity.builder()
                    .id(Integer.valueOf(userID))
                    .build()));
        }

        // fetch login for user
        login = Optional.ofNullable(loginRepository.findByUser(user.get()));

        // if there is no login or is expired
//        if (!login.isPresent() || (LocalDateTime.now().isAfter(login.get().getValidUntil()))) {

            LoginEntity newLogin = LoginEntity.builder()
                    .sessionKey(UUID.randomUUID().toString())
                    .validUntil(LocalDateTime.now().plusMinutes(10))
                    .user(user.get()).build();

            login = Optional.of(loginRepository.save(newLogin));
//        }

//        userRepository.save(user.get());

        return login.get().getSessionKey();
    }
}
