package com.irisandco.ecommerce_optic.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findProductByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    //Para filtrar
    List<Product> findByCategories_NameIgnoreCase(String categoryName);
    List<Product> findByPriceLessThanEqual(Double maxPrice);
    List<Product> findByPriceGreaterThanEqual(Double minPrice);

}
