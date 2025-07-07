package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long  id;

    @Column(name="name", table="products", nullable=false,  unique = true, length = 50)
    private String name;

    @Column(name="price", table="products")
    private Double price;

    @Column(name="imageUrl", table="products")
    private String imageUrl;

    @Column(name="featured", table="products")
    private Boolean featured;

    @ManyToMany
    @JoinTable(
            name="category_product",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    private List<Category> categories;

    public Product(String name, Double price, String imageUrl, Boolean featured, List<Category> categories) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.categories = categories;
    }
}
