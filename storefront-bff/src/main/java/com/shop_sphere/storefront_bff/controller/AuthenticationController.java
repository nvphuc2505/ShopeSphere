package com.shop_sphere.storefront_bff.controller;

import com.shop_sphere.storefront_bff.viewmodel.AuthenticationInfoVm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    /*
        -- This class return current authenticated user
     */

    @GetMapping("/authenticated")
    public ResponseEntity<AuthenticationInfoVm> user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null)
            return ResponseEntity.ok(new AuthenticationInfoVm(false, null));

        return ResponseEntity.ok(
            new AuthenticationInfoVm(true, principal.getAttribute("preferred_username"))
        );
    }

}
