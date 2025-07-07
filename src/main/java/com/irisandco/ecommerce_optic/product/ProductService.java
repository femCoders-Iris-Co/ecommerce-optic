package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.*;
import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository PRODUCT_REPOSITORY;
    private final CategoryService CATEGORY_SERVICE;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.PRODUCT_REPOSITORY = productRepository;
        this.CATEGORY_SERVICE = categoryService;
    }

    public List<ProductResponse> getAllProducts() {
        return listToDto(PRODUCT_REPOSITORY.findAll());
    }

    private List<ProductResponse> listToDto(List<Product> products) {
        return products.stream().map(product ->  ProductMapper.toDto(product))
                .toList();
        }

    public Product getProductById(Long id) {
        return PRODUCT_REPOSITORY.findById(id).orElseThrow(() -> new EntityNotFoundException(Product.class.getSimpleName(), "Id", id.toString()));
    }

    public ProductResponse getProductResponseById(Long id) {
        return ProductMapper.toDto(getProductById(id));
    }

    public ProductResponse createProduct (ProductRequest productRequest) {
        if (PRODUCT_REPOSITORY.existsByNameIgnoreCase(productRequest.name())) {throw
            new EntityAlreadyExistsException(Product.class.getSimpleName(), "name", productRequest.name());
        }

        //Obtener categorías por sus nombres
        List<Category> categories = productRequest.categoryNames().stream().map(name -> CATEGORY_SERVICE.getCategoryByName(name)).toList();

        // Convertir el DTO a entidad con las categorías ya cargadas
        Product product = ProductMapper.toEntity(productRequest, categories);

        // Guardar producto
        Product savedProduct = PRODUCT_REPOSITORY.save(product);

        // Devolver el DTO con las categorías incluidas
        return ProductMapper.toDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        //Verify if the id product exist or throw an exception
        Product product = getProductById(id);
        PRODUCT_REPOSITORY.deleteById(id);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
    Product product = getProductById(id);

    String name = productRequest.name().trim();
    if (!product.getName().equalsIgnoreCase(name)) {
            if (PRODUCT_REPOSITORY.existsByNameIgnoreCase(name)) { throw new EntityAlreadyExistsException(Product.class.getSimpleName(), "name", productRequest.name());
    }}
    product.setName(name);
    if (productRequest.price() != null) {
        product.setPrice(productRequest.price());
    }
    if (productRequest.imageUrl() != null) {
        product.setImageUrl(productRequest.imageUrl().trim());
    }
    if (productRequest.featured() != null) {
        product.setFeatured(productRequest.featured());
    }

    List<Category> categories = new ArrayList<>(productRequest.categoryNames().stream().map(categoryName -> CATEGORY_SERVICE.getCategoryByName(categoryName)).toList());

    product.setCategories(categories);

    // Guardar product
    Product savedProduct = PRODUCT_REPOSITORY.save(product);

    return ProductMapper.toDto(savedProduct);
    }

    //Para filtrar
    public List<ProductResponse> filterProducts(String name, String categoryName, Double minPrice, Double maxPrice) {
        if (name != null && !name.isBlank()) {
            return filterByName(name);
        }

        if (categoryName != null && !categoryName.isBlank()) {
            return filterByCategory(categoryName);
        }

        if (minPrice != null) {
            return filterByMinPrice(minPrice);
        }

        if (maxPrice != null) {
            return filterByMaxPrice(maxPrice);
        }

        // Si no hay filtros, devuelve todos
        return getAllProducts();
    }

    public List<ProductResponse> filterByName(String name) {
        return PRODUCT_REPOSITORY.findProductByNameIgnoreCase(name).stream().map(product -> ProductMapper.toDto(product)).toList();
    }

    public List<ProductResponse> filterByCategory(String categoryName) {
        return PRODUCT_REPOSITORY.findByCategories_NameIgnoreCase(categoryName).stream().map(product -> ProductMapper.toDto(product)).toList();
    }

    private List<ProductResponse> filterByMinPrice(Double minPrice) {
        return PRODUCT_REPOSITORY.findByPriceGreaterThanEqual(minPrice).stream().map(product -> ProductMapper.toDto(product)).toList();
    }

    private List<ProductResponse> filterByMaxPrice(Double maxPrice) {
        return PRODUCT_REPOSITORY.findByPriceLessThanEqual(maxPrice).stream().map(product -> ProductMapper.toDto(product)).toList();
    }
    /**
     * Retrieves a list of products based on provided filter parameters.
     *
     * @param name            the product name to filter by (optional)
     * @param categories      list of categories to filter by (optional)
     * @param minimumPrice    minimum price filter (optional)
     * @param maximumPrice    maximum price filter (optional)
     * @param isFeatured      whether the product is featured (optional)
     * @param pageable        pagination information
     * @return                list of products matching the filters
     */
    public Page<Product> getFilteredProducts(String name,
                                             List<String> categories,
                                             BigDecimal minimumPrice,
                                             BigDecimal maximumPrice,
                                             Boolean isFeatured,
                                             Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.filterByParams(
                name,
                categories,
                minimumPrice,
                maximumPrice,
                isFeatured
        );
        return PRODUCT_REPOSITORY.findAll(spec, pageable);
    }
    }




