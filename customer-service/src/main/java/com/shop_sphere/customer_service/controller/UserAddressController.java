package com.shop_sphere.customer_service.controller;

import com.shop_sphere.customer_service.service.UserAddressService;
import com.shop_sphere.customer_service.viewmodel.address.UserAddressPostVm;
import com.shop_sphere.customer_service.viewmodel.address.UserAddressVm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserAddressController {

    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }



    @GetMapping ("/storefront/user-address/list")
    public ResponseEntity<List<UserAddressVm>> getUserAddressList() {
        return ResponseEntity.ok(userAddressService.getUserAddressList());
    }

    @GetMapping("/storefront/user-address/default-address")
    public ResponseEntity<UserAddressVm> getDefaultUserAddress() {
        return ResponseEntity.ok(userAddressService.getDefaultUserAddress());
    }

    @PostMapping("/storefront/user-address")
    public ResponseEntity<UserAddressVm> createAddress(@Valid @RequestBody UserAddressPostVm userAddressPostVm) {
        return ResponseEntity.ok(userAddressService.create(userAddressPostVm));
    }

    @PatchMapping("/storefront/user-address/{id}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable("id") Long addressId) {
        userAddressService.chooseDefaultAddressById(addressId);
        return ResponseEntity.ok().build();
    }
}
