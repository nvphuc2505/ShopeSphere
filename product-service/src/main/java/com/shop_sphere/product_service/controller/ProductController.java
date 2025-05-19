package com.shop_sphere.product_service.controller;

import com.shop_sphere.product_service.service.ProductService;
import com.shop_sphere.product_service.viewmodel.ProductPostVm;
import com.shop_sphere.product_service.viewmodel.ProductUpdateVm;
import com.shop_sphere.product_service.viewmodel.ProductVm;
import com.shop_sphere.product_service.viewmodel.ProductListWithCategoryVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping("/storefront/products/category/id/{id}")
    public ResponseEntity<List<ProductListWithCategoryVm>> getProductListByCategoryId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductListByCategoryId(id));
    }

    @PostMapping("/backoffice/products")
    public ResponseEntity<ProductVm> createNewProduct(@Valid @RequestBody ProductPostVm productPostVm) {
        ProductVm productVm = productService.createNewProduct(productPostVm);
        return new ResponseEntity<>(productVm, HttpStatus.CREATED);
    }

    @PatchMapping("/backoffice/products/update/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
                                              @RequestBody ProductUpdateVm productUpdateVm) {
        productService.updateProductById(id, productUpdateVm);
        return ResponseEntity.noContent().build();
    }
}
