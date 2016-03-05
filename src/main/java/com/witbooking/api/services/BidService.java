package com.witbooking.api.services;

import com.witbooking.api.entities.LoginEntity;

import java.math.BigDecimal;

public interface BidService {
    void createBid(int itemID, BigDecimal bidAmount, LoginEntity login);
}
