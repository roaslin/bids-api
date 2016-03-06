package com.witbooking.api.resources;

import com.witbooking.api.documentation.ApiMockMvcBase;
import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.entities.UserEntity;
import com.witbooking.api.repositories.LoginRepository;
import com.witbooking.api.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginResourceTestDocumentation extends ApiMockMvcBase {

    public static final String SESSION_KEY_FIRST_USER = "bbf2a9f4-7e1a-4603-baa6-6e36ba472c6c";
    public static final int USER_ID = 1;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        cleanUp();
//        initUserAndLogin();
    }

    @After
    public void end() {
        cleanUp();
    }

    @Test
    public void testGetSessionKey() throws Exception {
        // when
        mockMvc.perform(get("/{userID}/login", USER_ID))
               // then
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isNotEmpty())
               // Document
               .andDo(document("login/get"
                       , pathParameters(parameterWithName("userID").description("Item ID of which we want to get existing top bids"))
               ));
    }

    private void initUserAndLogin() {
        // create users and login
        UserEntity user = UserEntity.builder()
                                         .id(USER_ID)
                                         .build();

        user = userRepository.save(user);

        // create logins
        LoginEntity login = LoginEntity.builder()
                                                .user(user)
                                                .sessionKey(SESSION_KEY_FIRST_USER)
                                                .expireDate(LocalDateTime.now().plusMinutes(10))
                                                .build();

        loginRepository.save(login);
    }

    private void cleanUp() {
        loginRepository.deleteAll();
        userRepository.deleteAll();
    }
}
