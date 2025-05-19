package com.shop_sphere.customer_service.service;

import com.shop_sphere.customer_service.exception.AccessDeniedException;
import com.shop_sphere.customer_service.exception.CustomerNotFoundException;
import com.shop_sphere.customer_service.model.UserAddress;
import com.shop_sphere.customer_service.repository.UserAddressRepository;
import com.shop_sphere.customer_service.viewmodel.address.UserAddressPostVm;
import com.shop_sphere.customer_service.viewmodel.address.UserAddressVm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    // Spring Security uses "anonymousUser" to unauthenticated user
    private static final String UNAUTHENTICATED_USER =  "anonymousUser";
    private final UserAddressRepository userAddressRepository;

    public UserAddressService(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }



    public List<UserAddressVm> getUserAddressList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals(UNAUTHENTICATED_USER))
            throw new AccessDeniedException(username);

        List<UserAddress> userAddresses = userAddressRepository.findAllByUsername(username);

        return userAddresses
                .stream()
                .map(userAddress -> UserAddressVm.fromModel(username, userAddress))
                .toList();
    }

    public UserAddressVm getDefaultUserAddress() {
        // SecurityContextHolder.getContext().getAuthentication().getName();
        // return username of current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(username.equals(UNAUTHENTICATED_USER))
            throw new AccessDeniedException(username);

        UserAddress userAddress = userAddressRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new CustomerNotFoundException("USER ADDRESS", null));

        return UserAddressVm.fromModel(username, userAddress);
    }

    public UserAddressVm create(UserAddressPostVm userAddressPostVm) {
        // SecurityContextHolder.getContext().getAuthentication().getName();
        // return username of current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Spring Security uses "anonymousUser" to unauthenticated user
        if(username.equals(UNAUTHENTICATED_USER))
            throw new AccessDeniedException(username);

        boolean isActive = userAddressRepository.findAllByIsActiveTrue().isEmpty();

        UserAddress userAddress = UserAddress.builder()
                .username(username)
                .address(userAddressPostVm.address())
                .city(userAddressPostVm.city())
                .state(userAddressPostVm.state())
                .country(userAddressPostVm.country())
                .phoneNumber(userAddressPostVm.phoneNumber())
                .isActive(isActive)
                .build();

        return UserAddressVm.fromModel(username, userAddressRepository.save(userAddress));
    }

    public void chooseDefaultAddressById(Long addressId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Spring Security uses "anonymousUser" to unauthenticated user
        if(username.equals(UNAUTHENTICATED_USER))
            throw new AccessDeniedException(username);

        List<UserAddress> userAddresses = userAddressRepository.findAllByUsername(username);
        userAddresses.forEach(
                address ->
                        address.setIsActive(Objects.equals(addressId, address.getId()))
        );

        userAddressRepository.saveAll(userAddresses);
    }
}
