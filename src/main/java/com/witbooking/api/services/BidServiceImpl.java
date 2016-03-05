package com.witbooking.api.services;

import com.witbooking.api.entities.BidEntity;
import com.witbooking.api.entities.ItemEntity;
import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.entities.UserEntity;
import com.witbooking.api.repositories.BidRepository;
import com.witbooking.api.repositories.ItemRepository;
import com.witbooking.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void createBid(int itemID, BigDecimal bidAmount, LoginEntity login) {

        // fetch item if exists
        Optional<ItemEntity> item = Optional.ofNullable(itemRepository.findOne(itemID));

        // otherwise we create it
        if (!item.isPresent()) {
            item = Optional.of(itemRepository.save(ItemEntity.builder().id(itemID).build()));
        }

        // create new bid
        BidEntity newBid = BidEntity.builder()
                .amount(bidAmount)
                .item(item.get())
                .user(login.getUser()).build();

        // persist bid
        newBid = bidRepository.save(newBid);

        // update user
        UserEntity user = login.getUser();
        user.getBids().add(newBid);
        userRepository.save(user);

        // update item
        item.get().getBidEntities().add(newBid);
        itemRepository.save(item.get());
    }
}
