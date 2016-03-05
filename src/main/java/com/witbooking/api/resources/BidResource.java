package com.witbooking.api.resources;

import com.witbooking.api.entities.LoginEntity;
import com.witbooking.api.repositories.LoginRepository;
import com.witbooking.api.services.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BidResource {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    private BidService bidService;

    @RequestMapping(value = "{itemID}/bid", method = POST)
    public ResponseEntity<?> createBid(@PathVariable @NotNull int itemID, @RequestParam @NotNull String sessionKey, @RequestBody @NotNull BigDecimal bidAmount) {

        Optional<LoginEntity> login = Optional.ofNullable(loginRepository.findBySessionKey(sessionKey));

        // sessionKey exists and not expired
        if (login.isPresent() && !login.get().getExpireDate().isBefore(LocalDateTime.now())) {
            bidService.createBid(itemID, bidAmount, login.get());
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>("User not logged in for session key <" + sessionKey + ">", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "{itemID}/topBidList", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getTopBidListByItemID(@PathVariable @NotNull int itemID)
    {
        return ResponseEntity.ok(bidService.getTopBidListByItemID(itemID));
    }
}