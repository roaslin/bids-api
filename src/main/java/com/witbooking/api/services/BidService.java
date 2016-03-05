package com.witbooking.api.services;

import com.witbooking.api.entities.LoginEntity;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    void createBid(int itemID, BigDecimal bidAmount, LoginEntity login);
    List<String> getTopBidListByItemID(int itemID);
}
