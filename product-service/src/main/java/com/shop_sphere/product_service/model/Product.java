package com.shop_sphere.product_service.model;

import com.shop_sphere.product_service.model.enumeration.ProductStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    private String name;

    private String color;

    private String size;

    private String description;

    private BigDecimal price;

    private Integer quantity;           // Số lượng sản phẩm còn lại

    private Integer sold;               // Số lượng sản phẩm đã bán

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @CreatedDate
    private Instant createdDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

}
