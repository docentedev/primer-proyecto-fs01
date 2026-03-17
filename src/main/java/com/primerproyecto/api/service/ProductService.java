package com.primerproyecto.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primerproyecto.api.dto.ProductDTO;
import com.primerproyecto.api.dto.UserDTO;
import com.primerproyecto.api.model.Product;
import com.primerproyecto.api.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductDTO create(ProductDTO p) {
        Product product = converToEntity(p);
        Product pNew = this.productRepository.save(product);
        return convertProductDTO(pNew);
    }

    public List<ProductDTO> list() {
        return productRepository.findAll()
                .stream()
                .map(this::convertProductDTO)
                .collect(Collectors.toList());
    }

    private Product converToEntity(ProductDTO productDTO) {
        Product p = new Product();
        p.setId(productDTO.getId());
        p.setName(productDTO.getName());
        p.setDescription(productDTO.getDescription());
        p.setPrice(productDTO.getPrice());
        return p;
    }

    private ProductDTO convertProductDTO(Product product) {
        ProductDTO p = new ProductDTO();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setPrice(product.getPrice());
        return p;
    }
}
