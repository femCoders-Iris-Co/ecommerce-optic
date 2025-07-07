package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="quantity", table="items", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="cart_id", nullable = false)
    private Cart cart;

    public Item(int quantity, Product product, Cart cart) {
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;;
    }

    public Item(Long id, int quantity, Product product, Cart cart) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.cart = cart;
    }
}
