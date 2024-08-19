package com.myproduct.product_service.services;

import com.myproduct.product_service.dto.AdjustStockRequest;
import com.myproduct.product_service.dto.AdjustStockResponse;
import com.myproduct.product_service.exceptions.InvalidSkuException;
import com.myproduct.product_service.models.Product;
import com.myproduct.product_service.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Iterable<Product> products(){
       return productRepo.findAll();
    }

    public Product create(Product product){
        return productRepo.save(product);
    }

    public Optional<Product> show(String sku, Integer requestedStock){
        List<Product> products = productRepo.findByStockLevelGreaterThanAndSku(requestedStock, sku);
        return products.stream().findFirst();
    }

    public Iterable<Product> InStockProducts(){
        return productRepo.findByStockLevelGreaterThan(0);
    }

//    @Autowired
    @Transactional
    public AdjustStockResponse adjustStock(List<AdjustStockRequest> adjustStockRequests) throws InvalidSkuException {
        AdjustStockResponse asp = new AdjustStockResponse();
        List<Product> products = new ArrayList<>();
        for (AdjustStockRequest adjustStockRequest : adjustStockRequests) {
            try {
                Optional<Product> optionalProduct = show(adjustStockRequest.getSku(), adjustStockRequest.getQuantity());
                if(optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setStockLevel(product.getStockLevel() - adjustStockRequest.getQuantity());
                    productRepo.save(product);
                    products.add(product);
                }else{
                    System.out.println("Error in StockAdjustment for SKU: "+adjustStockRequest.getSku());
                    throw new DataIntegrityViolationException("Throwing exception for demoing Rollback!!!");

//                    throw new InvalidSkuException("Invalid SKU Or SKU is not in Stock: " + adjustStockRequest.getSku());

                }
            }catch (Exception e){
                throw new DataIntegrityViolationException("Invalid SKU Or SKU is not in Stock: " + adjustStockRequest.getSku());
            }
        }
        asp.setProducts(products);
        asp.setErrorCode(200);
        asp.setStatusMessage("Success");
        return asp;
    }

}
