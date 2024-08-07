package de.ait_tr.g_33_shop.service;

import de.ait_tr.g_33_shop.domain.dto.ProductDto;
import de.ait_tr.g_33_shop.domain.dto.ProductSupplyDto;
import de.ait_tr.g_33_shop.domain.entity.Product;
import de.ait_tr.g_33_shop.exception_handling.exceptions.*;
import de.ait_tr.g_33_shop.repository.ProductRepository;
import de.ait_tr.g_33_shop.service.interfaces.ProductService;
import de.ait_tr.g_33_shop.service.mapping.ProductMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository repository;
    private ProductMappingService mappingService;

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        Product entity = mappingService.mapDtoToEntity(dto);

        try {
            repository.save(entity);
        } catch (Exception e) {
            throw new FourthTestException(e.getMessage());
        }

        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = getEntityById(id);

        if (product != null && product.isActive()) {
            return mappingService.mapEntityToDto(product);
        }

        throw new ThirdTestException("This is third test exception");
    }

    @Override
    public Product getEntityById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );
    }

    @Override
    public ProductDto update(ProductDto product) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveProductsQuantity() {
        return 0;
    }

    @Override
    public BigDecimal getAllActiveProductsTotalPrice() {
        return null;
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        return null;
    }

    @Override
    @Transactional
    public void attachImage(String imgUrl, String productTitle) {
        Product product = repository.findByTitle(productTitle).orElseThrow(
                () -> new RuntimeException("Product not found")
        );

        product.setImage(imgUrl);
    }

    @Override
    public List<ProductSupplyDto> getAllActiveProductsForSupply() {
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToSupplyDto)
                .toList();
    }
}