package com.shop_sphere.customer_service.viewmodel.address;

import com.shop_sphere.customer_service.model.UserAddress;

public record UserAddressVm (

    String contactName,
    String address,
    String city,
    String state,
    String country,
    String phoneNumber

) {
    public static UserAddressVm fromModel(String contactName, UserAddress userAddress) {
        return new UserAddressVm(
                contactName,
                userAddress.getAddress(),
                userAddress.getCity(),
                userAddress.getState(),
                userAddress.getCountry(),
                userAddress.getPhoneNumber()
        );
    }
}
