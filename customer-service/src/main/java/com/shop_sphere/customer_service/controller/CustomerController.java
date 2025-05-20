package com.shop_sphere.customer_service.controller;

import com.shop_sphere.customer_service.service.CustomerService;
import com.shop_sphere.customer_service.viewmodel.customer.*;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }



    @GetMapping("/backoffice/customers")
    public ResponseEntity<CustomerListVm> getCustomers (
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo
    ) {
        return ResponseEntity.ok(customerService.getCustomers(pageNo));
    }

    @GetMapping("/storefront/customers/profile")
    public ResponseEntity<CustomerVm> getCurrentCustomerProfile() {
        // Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // String username = jwt.getClaimAsString("preferred_username");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Current name: {}", username);
        return ResponseEntity.ok(customerService.getCustomerProfile(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @GetMapping("/backoffice/customers/id/{userId}")
    public ResponseEntity<CustomerVm> getCustomerById(@PathVariable String userId) {
        return ResponseEntity.ok(customerService.getCustomerById(userId));
    }

    @GetMapping("/backoffice/customers/email/{email}")
    public ResponseEntity<CustomerVm> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PostMapping("/backoffice/customers")
    public ResponseEntity<CustomerVm> createCustomer(@Valid @RequestBody CustomerPostVm customerPostVm,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        CustomerVm customerVM = customerService.create(customerPostVm);

        return ResponseEntity
            .created(
                uriComponentsBuilder
                    .replacePath("/backoffice/customers/id/{id}")
                    .buildAndExpand(customerVM.id())
                    .toUri()
            )
            .body(customerVM);
    }

    @PatchMapping("/backoffice/customers/profile/{id}")
    public ResponseEntity<Void> updateProfileCustomerById(@PathVariable String userId,
                                                          @RequestBody CustomerUpdateVm customerUpdateVm) {
        customerService.updateCustomerById(userId, customerUpdateVm);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/storefront/customers/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody CustomerUpdatePasswordVm customerUpdatePasswordVm) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        customerService.changePassword(username, customerUpdatePasswordVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/backoffice/customers/profile/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable String userId) {
        customerService.deleteCustomerById(userId);
        return ResponseEntity.noContent().build();
    }
}
