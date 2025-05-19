package com.shop_sphere.product_service.repository;

import com.shop_sphere.product_service.model.Product;
import com.shop_sphere.product_service.viewmodel.ProductListWithCategoryVm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends CrudRepository<Product, Long> {

    // JPQL - Native SQL
    // (Product) is Entity not Table (in JPQL)
    // In abstract function using native sql
    @Query(value = "SELECT p.name AS productName, p.price AS productPrice FROM product p "
                + "JOIN category c ON p.category_id = c.id "
                + "WHERE c.id = :categoryId", nativeQuery = true)
    List<ProductListWithCategoryVm> findAllByCategoryId(@Param("categoryId") Long categoryId);

    Iterable<Product> findAllByName(String name);
    Optional<Product> findByName(String name);
    boolean existsByName(String name);
}
