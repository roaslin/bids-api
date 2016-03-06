package com.witbooking.api.services;

import com.witbooking.api.entities.LoginEntity;
import org.codehaus.jettison.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    void createBid(int itemID, BigDecimal bidAmount, LoginEntity login);

    List<JSONObject> getTopBidListByItemID(int itemID);
}
