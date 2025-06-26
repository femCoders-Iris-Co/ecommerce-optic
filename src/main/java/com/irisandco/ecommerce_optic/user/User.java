package com.irisandco.ecommerce_optic.user;

import jakarta.persistence.*;

@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", table="users", insertable=true, updatable=true, nullable=false, columnDefinition = "VARCHAR(255) NOT NULL UNIQUE")
    private String username;

    @Column(name = "email", table="users", insertable=true, updatable=true, nullable=false, columnDefinition = "VARCHAR(255) NOT NULL UNIQUE")
    private String email;

    @Column(name = "password", table="users", insertable=true, updatable=true, nullable=false, columnDefinition = "VARCHAR(255) NOT NULL")
    private String password;

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

    public Long getId() {
        return id;
    }

   // public void setId(Long id) {
   //     this.id = id;
   // }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
