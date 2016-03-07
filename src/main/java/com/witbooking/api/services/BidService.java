package com.witbooking.api.services;

import org.codehaus.jettison.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BidService {
    Optional<String> createBid(int itemID, BigDecimal bidAmount, String sessionKey);

    List<JSONObject> getTopBidListByItemID(int itemID);
}
