package com.myproduct.product_service.repositories;

import com.myproduct.product_service.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends CrudRepository<Product, Integer> {

    @Query("select p from Product p where p.stockLevel > ?1")
    Iterable<Product> findByStockLevelGreaterThan(Integer stockLevel);

    @Query("select p from Product p where p.sku = :sku")
    List<Product> findBySku(@Param("sku") String sku);

    @Query("SELECT p FROM Product p WHERE p.stockLevel >= :stockLevel AND p.sku = :sku")
    List<Product> findByStockLevelGreaterThanAndSku(int stockLevel, String sku);


}
