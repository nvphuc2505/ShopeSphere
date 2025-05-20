package com.shop_sphere.product_service.repository;

import com.shop_sphere.product_service.model.Product;
import com.shop_sphere.product_service.model.enumeration.ProductCategory;
import com.shop_sphere.product_service.viewmodel.ProductListWithCategoryVm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends CrudRepository<Product, Long> {

    List<ProductListWithCategoryVm> findAllByCategory(ProductCategory category);
    Iterable<Product> findAllByName(String name);
    Optional<Product> findByName(String name);
    boolean existsByName(String name);
}
