package com.witbooking.api.resources;

import com.witbooking.api.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Bid resource endpoint.
 */
@RestController
public class BidResource {

    @Autowired
    private BidService bidService;

    /**
     * Resource for placing bids
     * @param itemID item to bid for.
     * @param sessionKey a valid login sessionKey.
     * @param bidAmount bid's amount.
     * @return Error string or empty when request was successful.
     */
    @RequestMapping(value = "{itemID}/bid", method = POST)
    public ResponseEntity<?> createBid(@PathVariable @NotNull int itemID, @RequestParam @NotNull String sessionKey, @RequestBody @NotNull BigDecimal bidAmount) {

        Optional<String> notLoggedIn = bidService.createBid(itemID, bidAmount, sessionKey);

        if (notLoggedIn.isPresent()) {
            return new ResponseEntity<>(notLoggedIn.get(), HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Resource for getting top bids for a given itemID
     * @param itemID item to be fetched its bids.
     * @return A JSON array containing pairs of user and max bid.
     */
    @RequestMapping(value = "{itemID}/topBidList", method = RequestMethod.GET)
    public ResponseEntity<String> getTopBidListByItemID(@PathVariable @NotNull int itemID) {
        return ResponseEntity.ok(bidService.getTopBidListByItemID(itemID).toString());
    }
}