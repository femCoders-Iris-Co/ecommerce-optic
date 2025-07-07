package com.irisandco.ecommerce_optic.user;

import com.irisandco.ecommerce_optic.cart.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", table="users", nullable=false, unique = true, length = 50)
    private String username;

    @Column(name = "email", table="users", nullable=false, unique = true, length = 50)
    private String email;

    @Column(name = "password", table="users", nullable=false, length = 50)
    private String password;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addCart(Cart cart) {
        cart.setUser(this);
        this.cart = cart;
    }
}
