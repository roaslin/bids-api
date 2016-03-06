package com.witbooking.api.resources;

import com.witbooking.api.documentation.ApiMockMvcBase;
import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.entities.UserEntity;
import com.witbooking.api.repositories.BidRepository;
import com.witbooking.api.repositories.ItemRepository;
import com.witbooking.api.repositories.LoginRepository;
import com.witbooking.api.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BidResourceTestDocumentation extends ApiMockMvcBase {

    public static final String SESSION_KEY = "bbf2a9f4-7e1a-4603-baa6-6e36ba472c6c";
    public static final int ITEM_ID = 2222;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void init() {
        // clean up
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        loginRepository.deleteAll();
        userRepository.deleteAll();

        // create user and login
        UserEntity user = UserEntity.builder()
                                    .id(1)
                                    .build();
        user = userRepository.save(user);

        LoginEntity login = LoginEntity.builder()
                                       .user(user)
                                       .expireDate(LocalDateTime.now().plusMinutes(10))
                                       .sessionKey(SESSION_KEY)
                                       .build();

        loginRepository.save(login);
    }

    @After
    public void end() {
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        loginRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateBid() throws Exception {
        // given
        final String bidAmount = "2.1";
        // when
        mockMvc.perform(post("/{itemID}/bid", ITEM_ID)
                .param("sessionKey", SESSION_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bidAmount))
               // then
               .andExpect(status().isOk())
               // Document
               .andDo(document("bid/post",
                       pathParameters(parameterWithName("itemID").description("Item ID that gets the new bid"))
                       , requestParameters(parameterWithName("sessionKey").description("A valid session Key for a given logged in user"))
               ));
    }
}



