package com.witbooking.api.resources;

import com.witbooking.api.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Login resource endpoint.
 */
@RestController
public class LoginResource {

    @Autowired
    private LoginService loginService;

    /**
     * Resource for users to be logged in.
     * @param userID userID.
     * @return A random UUID string.
     */
    @RequestMapping(value = "{userID}/login", method = GET)
    public ResponseEntity<String> getLoginSessionKey(@PathVariable @NotNull String userID) {
        return ResponseEntity.ok(loginService.getSessionToken(userID));
    }
}
