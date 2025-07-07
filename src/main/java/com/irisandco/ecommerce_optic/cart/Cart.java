package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.item.Item;
import com.irisandco.ecommerce_optic.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    @Column(name = "totalPrice", table ="carts")
    private Double totalPrice;

    public void addItem(Item item) {
        item.setCart(this);
        this.items.add(item);
    }
}
