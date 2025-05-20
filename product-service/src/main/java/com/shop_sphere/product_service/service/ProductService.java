package com.shop_sphere.product_service.service;

import com.shop_sphere.product_service.exception.DuplicatedException;
import com.shop_sphere.product_service.exception.ProductNotFoundException;
import com.shop_sphere.product_service.model.Product;
import com.shop_sphere.product_service.model.enumeration.ProductCategory;
import com.shop_sphere.product_service.model.enumeration.ProductStatus;
import com.shop_sphere.product_service.repository.ProductRepository;
import com.shop_sphere.product_service.viewmodel.ProductListWithCategoryVm;
import com.shop_sphere.product_service.viewmodel.ProductPostVm;
import com.shop_sphere.product_service.viewmodel.ProductUpdateVm;
import com.shop_sphere.product_service.viewmodel.ProductVm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;



    public List<ProductListWithCategoryVm> getProductsByCategory(String category) {
        ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
        return productRepository.findAllByCategory(productCategory);
    }

    public ProductVm createNewProduct(ProductPostVm productPostVm) {

        if (productRepository.existsByName(productPostVm.name()))
            throw new DuplicatedException(productPostVm.name());

        Product product = Product.builder()
                .name(productPostVm.name())
                .category(productPostVm.category())
                .color(productPostVm.color())
                .size(productPostVm.size())
                .description(productPostVm.description())
                .price(productPostVm.price())
                .quantity(productPostVm.quantity())
                .sold(0)
                .status(ProductStatus.IN_STOCK)
                .build();

        return ProductVm.fromProductModel(productRepository.save(product));
    }

    public void updateProductById(Long id, ProductUpdateVm productUpdateVm) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("ID", id.toString()));

        if (productUpdateVm.price() != null && !product.getPrice().equals(productUpdateVm.price()))
            product.setPrice(productUpdateVm.price());

        if (productUpdateVm.quantity() != null) {
            Integer total = product.getQuantity() + productUpdateVm.quantity();
            product.setQuantity(total);
        }

        if (productUpdateVm.sold() != null) {
            Integer sold = product.getSold() + productUpdateVm.sold();
            product.setSold(sold);
        }

        ProductStatus productStatus =
                (product.getQuantity() <= 0) ? ProductStatus.SOLD_OUT : ProductStatus.IN_STOCK;
        product.setStatus(productStatus);

        productRepository.save(product);
        // return ProductVm.fromProductModel(productRepository.save(product));
    }
}
