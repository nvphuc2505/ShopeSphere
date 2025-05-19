package com.shop_sphere.customer_service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@Table(name = "user_address")
public class UserAddress extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;                // Foreign Key

    private String address;
    private String city;
    private String state;                   // Tỉnh hoặc thành phố của khách hàng
    private String country;
    private String phoneNumber;

    private Boolean isActive;

}
