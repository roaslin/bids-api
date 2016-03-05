package com.witbooking.api.services;

import com.witbooking.api.entities.BidEntity;
import com.witbooking.api.entities.ItemEntity;
import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.repositories.BidRepository;
import com.witbooking.api.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ItemRepository itemRepository;

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
        bidRepository.save(newBid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getTopBidListByItemID(int itemID) {
        ItemEntity item = itemRepository.findOne(itemID);

        List<BidEntity> topBidsByItemID = bidRepository.findByItem(item);

        return topBidsByItemID.stream()
                .map(this::buildBidObject)
                .collect(Collectors.toList());
    }

    private String buildBidObject(BidEntity bidEntity) {
        return bidEntity.getUser().getId() + "\":\"" + bidEntity.getAmount().toString();
    }
}
