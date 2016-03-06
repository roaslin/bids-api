package com.witbooking.api.resources;

import com.witbooking.api.documentation.ApiMockMvcBase;
import com.witbooking.api.entities.BidEntity;
import com.witbooking.api.entities.ItemEntity;
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
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BidResourceTestDocumentation extends ApiMockMvcBase {

    public static final String SESSION_KEY_FIRST_USER = "bbf2a9f4-7e1a-4603-baa6-6e36ba472c6c";
    public static final String SESSION_KEY_SECOND_USER = "189b8a7d-f01f-441d-9b04-8e1d9a3818c5";
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
        cleanUp();
        initUsersLoginsAndItems();
    }

    @After
    public void end() {
        cleanUp();
    }

    @Test
    public void testCreateBid() throws Exception {
        // given
        final String bidAmount = "2.1";
        // when
        mockMvc.perform(post("/{itemID}/bid", ITEM_ID)
                .param("sessionKey", SESSION_KEY_FIRST_USER)
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

    @Test
    public void testGetTopBidListByItemID() throws Exception {
        // given
        final BigDecimal minBidAmount = new BigDecimal(2.1);
        final BigDecimal bidAmount = new BigDecimal(3.1);
        final BigDecimal maxBidAmount = new BigDecimal(5.9);
        
        ItemEntity item = itemRepository.findOne(ITEM_ID);
        UserEntity userOne = userRepository.findOne(1);
        UserEntity userTwo = userRepository.findOne(2);

        placeBid(item, userOne, minBidAmount);
        placeBid(item, userOne, bidAmount);
        placeBid(item, userOne, maxBidAmount);

        placeBid(item, userTwo, minBidAmount);
        placeBid(item, userTwo, bidAmount);

        // when
        mockMvc.perform(get("/{itemID}/topBidList", ITEM_ID))
               // then
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$", hasSize(2)))
               // Document
               .andDo(document("bid/get/topBidList"
                       ,preprocessResponse(prettyPrint())
                       ,pathParameters(parameterWithName("itemID").description("Item ID of which we want to get existing top bids"))
                       , responseFields(
                               fieldWithPath("[]").type(JsonFieldType.ARRAY).description("Array of pairs of user-maxBid for this itemID")
                       )));
    }

    private void initUsersLoginsAndItems() {
        // create users and login
        UserEntity firstUser = UserEntity.builder()
                                         .id(1)
                                         .build();

        UserEntity secondUser = UserEntity.builder()
                                          .id(2)
                                          .build();

        firstUser = userRepository.save(firstUser);
        secondUser = userRepository.save(secondUser);

        // create logins
        LoginEntity firstUserLogin = LoginEntity.builder()
                                                .user(firstUser)
                                                .sessionKey(SESSION_KEY_FIRST_USER)
                                                .expireDate(LocalDateTime.now().plusMinutes(10))
                                                .build();

        LoginEntity secondUserLogin = LoginEntity.builder()
                                                 .user(secondUser)
                                                 .sessionKey(SESSION_KEY_SECOND_USER)
                                                 .expireDate(LocalDateTime.now().plusMinutes(10))
                                                 .build();

        loginRepository.save(firstUserLogin);
        loginRepository.save(secondUserLogin);

        // create item
        ItemEntity item = ItemEntity.builder().id(ITEM_ID).build();
        itemRepository.save(item);
    }

    private void placeBid(ItemEntity item, UserEntity firstUser, BigDecimal amount) {
        BidEntity newBid = BidEntity.builder()
                                    .item(item)
                                    .user(firstUser)
                                    .amount(amount)
                                    .build();
        bidRepository.save(newBid);
    }

    private void cleanUp() {
        bidRepository.deleteAll();
        itemRepository.deleteAll();
        loginRepository.deleteAll();
        userRepository.deleteAll();
    }
}



