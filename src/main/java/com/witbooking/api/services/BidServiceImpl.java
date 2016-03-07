package com.witbooking.api.services;

import com.witbooking.api.entities.BidEntity;
import com.witbooking.api.entities.ItemEntity;
import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.repositories.BidRepository;
import com.witbooking.api.repositories.ItemRepository;
import com.witbooking.api.repositories.LoginRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Bid's service.
 */
@Service
public class BidServiceImpl implements BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    LoginRepository loginRepository;

    /**
     * Checks if user has a valid sessionKey and places bid. Item is created if it does not exist.
     * @param itemID itemID.
     * @param bidAmount Bid's amount.
     * @param sessionKey A valid user's sessionKey.
     * @return Optional with error String or empty if it was successful placing a bid.
     */
    @Override
    public Optional<String> createBid(int itemID, BigDecimal bidAmount, String sessionKey) {

        // fetch if has login
        Optional<LoginEntity> login = Optional.ofNullable(loginRepository.findBySessionKey(sessionKey));

        // sessionKey exists and not expired
        if (login.isPresent() && !login.get().getExpireDate().isBefore(LocalDateTime.now())) {

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
                                        .user(login.get().getUser()).build();

            // persist bid
            bidRepository.save(newBid);

            // Everything went ok
            return Optional.empty();

        } else {
            return Optional.of("User not logged in for session key <" + sessionKey + ">");
        }
    }

    /**
     * Fetch top bids by itemID.
     * @param itemID itemID.
     * @return A list of JSONObject containing pairs of user and max bid for this item.
     */
    @Override
    @Transactional(readOnly = true)
    public List<JSONObject> getTopBidListByItemID(int itemID) {
        ItemEntity item = itemRepository.findOne(itemID);

        List<BidEntity> topBidsByItemID = bidRepository.findByItem(item);

        return topBidsByItemID.stream()
                              .map(this::buildBidObject)
                              .collect(Collectors.toList());
    }

    /**
     * Builds a json object from entity.
     * @param bidEntity BidEntity.
     * @return JSONObject.
     */
    private JSONObject buildBidObject(BidEntity bidEntity) {
        final JSONObject object = new JSONObject();
        try {
            object.put(Integer.toString(bidEntity.getUser().getId()), bidEntity.getAmount().toString());
        } catch (JSONException e) {
            throw new BidServiceException("Something went wrong creating bid object", e);
        }

        return object;
    }

    /**
     * Custom exception for BidService.
     */
    private static class BidServiceException extends RuntimeException {
        public BidServiceException(String message, Throwable e) {
            super(message, e);
        }
    }
}
