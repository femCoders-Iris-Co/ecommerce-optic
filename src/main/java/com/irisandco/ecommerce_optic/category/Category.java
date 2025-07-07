package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", table="categories", nullable=false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public void addProducts(Product product) {
        this.products.add(product);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Category)) { // Replace YourClass with your class name
            return false;
        }
        Category other = (Category) o;
        return  this.id.equals(other.id) && // Replace with your attributes
                this.name == other.name && // Replace with your attributes
                // Add comparisons for all other attributes
                true;
    }
}
