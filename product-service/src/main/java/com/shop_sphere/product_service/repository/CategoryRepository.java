package com.shop_sphere.product_service.repository;

import com.shop_sphere.product_service.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository
        extends CrudRepository<Category, Long> {
}
