package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.exception.EntityAlreadyExistsException;
import com.irisandco.ecommerce_optic.exception.EntityNotFoundException;
import com.irisandco.ecommerce_optic.item.Item;
import com.irisandco.ecommerce_optic.item.ItemService;
import com.irisandco.ecommerce_optic.product.Product;
import com.irisandco.ecommerce_optic.product.ProductService;
import com.irisandco.ecommerce_optic.user.User;
import com.irisandco.ecommerce_optic.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    private final CartRepository CART_REPOSITORY;
    private final UserService USER_SERVICE;
    private final ProductService PRODUCT_SERVICE;
    private final ItemService ITEM_SERVICE;

    public CartService(CartRepository cartRepository, UserService userService, ProductService productService, ItemService itemService) {
        CART_REPOSITORY = cartRepository;
        USER_SERVICE = userService;
        PRODUCT_SERVICE = productService;
        ITEM_SERVICE = itemService;
    }

    public Cart getCartByUserId(Long id){
        return CART_REPOSITORY.findCartByUserId(id).orElseThrow(() -> new EntityNotFoundException(Cart.class.getSimpleName(), "userId", id.toString()));
    }

    public CartResponse getCartResponseByUserId(Long id){
        return CartMapper.toDto(getCartByUserId(id));
    }

    public Cart getCartOrCreateByUserId(Long userId){
        return CART_REPOSITORY.findCartByUserId(userId)
                .orElseGet(() -> createCart(userId));
        }

    public Cart createCart(Long userId){
        User user = USER_SERVICE.getUserById(userId);
        if (CART_REPOSITORY.existsByUserId(userId)) {
            throw new EntityAlreadyExistsException(Cart.class.getSimpleName(), "userId", userId.toString());
        }
        Cart cart = new Cart();
        cart.setUser(user);
        return CART_REPOSITORY.save(cart);
    }

    public List<String> addItemToCart(Long userId, Long productId, CartRequest cartRequest){
        Cart cart = getCartOrCreateByUserId(userId);
        Product product = PRODUCT_SERVICE.getProductById(productId);
        int quantity = (cartRequest != null && cartRequest.quantity() != null) ? cartRequest.quantity() : 1;

        cart.getItems().stream()
                .filter(item -> Objects.equals(product.getId(), item.getProduct().getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> ITEM_SERVICE.updateItem(item, quantity),
                        () -> {
                            Item newItem = new Item(quantity, product, cart);
                            cart.addItem(ITEM_SERVICE.createItem(newItem));
                        }
                );

        updateCartPrice(cart);
        CartMapper.toDto(CART_REPOSITORY.save(cart));
        return Arrays.asList(product.getName(), Integer.toString(quantity));
    }

    public String removeItemFromCart(Long userId, Long productId) {
        Cart cart = getCartOrCreateByUserId(userId);
        Product product = PRODUCT_SERVICE.getProductById(productId);
        cart.getItems().stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), product.getId()))
                .findFirst()
                .ifPresentOrElse((itemToRemove) -> {
                        cart.getItems().remove(itemToRemove);
                        ITEM_SERVICE.deleteItemById(itemToRemove.getId());
                        },
                        () -> {
                            throw new EntityNotFoundException(Item.class.getSimpleName(),"product",product.getName(),"this cart");
                        }
                );

            updateCartPrice(cart);
            CartMapper.toDto(CART_REPOSITORY.save(cart));
            return product.getName();
    }

    private void updateCartPrice(Cart cart){
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(totalPrice);
    }

}
