package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.CategoryService;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import com.irisandco.ecommerce_optic.user.User;
import com.irisandco.ecommerce_optic.user.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Activa Mockito en JUnit 5
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    //Creo un falso repositorio, que no se conecta a la base de datos.
    @Mock
    private ProductRepository productRepository;

    //Creo un servicio de categorías falso. Se usa cuando el ProductService necesita cargar categorías.
    @Mock
    private CategoryService categoryService;

    //Crea un ProductService de verdad, pero le inyecta los dos mocks anteriores.
    @InjectMocks
    private ProductService productService;

    private Product productEntity;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private Product productMapper;

    @BeforeEach
    void setUp() {
        productEntity = new Product(1L,"Sunglasses", 100.0, "image.jpg", true, List.of());
        productResponse = new ProductResponse(1L, "Sunglasses", 100.0, "image.jpg", true, List.of());
        productRequest = new ProductRequest("Sunglasses", 100.0, "image.jpg", true, List.of());
        productMapper =new Product("Sunglasses", 100.0, "image.jpg", true, List.of());
    }

    @Test
    void getAllProducts_whenProductExist_returnListOfProductResponse() {

        when(productRepository.findAll()).thenReturn(List.of(productEntity));

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sunglasses", result.getFirst().name());
        assertEquals(100.0, result.getFirst().price());
        assertEquals("image.jpg", result.getFirst().imageUrl());
        assertEquals(true, result.getFirst().featured());
        assertEquals(List.of(), result.getFirst().categories());

        verify(productRepository, times(1)).findAll();

    }

    @Test
    void grtProductById_whenProductExists_returnProductEntity() {

        Long id = 1L;
        when(productRepository.findById(eq(id))).thenReturn(Optional.of(productEntity));

        Product result = productService.getProductById(id);

        assertNotNull(result);
        assertEquals(Product.class, result.getClass());
        assertEquals("Sunglasses", result.getName());
        assertEquals(100.0, result.getPrice());
        assertEquals("image.jpg", result.getImageUrl());
        assertEquals(true, result.getFeatured());
        assertEquals(List.of(), result.getCategories());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getProductById_whenProductDoesNotExist_throwsEntityNotFoundException() {
        Long id = 2L;
        String messageExpected = "Product with Id " + id + " was not found";

        when(productRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception result = assertThrows(EntityNotFoundException.class, () -> productService.getProductById(id));

        assertEquals(messageExpected,result.getMessage());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getProductResponseById_whenProductExists_returnsProductResponse() {
        Long id = 1L;
        when(productRepository.findById(eq(id))).thenReturn(Optional.of(productEntity));

        ProductResponse result = productService.getProductResponseById(id);

        assertNotNull(result);
        assertEquals(ProductResponse.class, result.getClass());
        assertEquals(1L, result.id());
        assertEquals("Sunglasses", result.name());
        assertEquals(100.0, result.price());
        assertEquals("image.jpg", result.imageUrl());
        assertEquals(true, result.featured());
        assertEquals(List.of(), result.categories());

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void saveProduct_whenCorrectRequest_returnsProductResponse() {
        when(productRepository.existsByNameIgnoreCase(productRequest.name())).thenReturn(false);

        when(productRepository.save(any(Product.class))).thenReturn(productEntity);

        ProductResponse result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals(ProductResponse.class, result.getClass());
        assertEquals("Sunglasses", result.name());
        assertEquals(100.0, result.price());
        assertEquals("image.jpg", result.imageUrl());
        assertEquals(true, result.featured());
        assertEquals(List.of(), result.categories());

        verify(productRepository, times(1)).existsByNameIgnoreCase(productRequest.name());

        verify(productRepository, times(1)).save(any(Product.class));
    }

}
