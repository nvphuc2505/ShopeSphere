package com.shop_sphere.customer_service.repository;

import com.shop_sphere.customer_service.model.UserAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository
        extends CrudRepository<UserAddress, Long> {

    List<UserAddress> findAllByUsername(String username);
    List<UserAddress> findAllByIsActiveTrue();
    Optional<UserAddress> findByUsernameAndIsActiveTrue(String username);

}
