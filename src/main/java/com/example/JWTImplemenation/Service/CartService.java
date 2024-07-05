package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.CartDTO;
import com.example.JWTImplemenation.DTO.CartItemDTO;
import com.example.JWTImplemenation.DTO.ProductDTO;
import com.example.JWTImplemenation.Entities.*;
import com.example.JWTImplemenation.Repository.*;
import com.example.JWTImplemenation.Service.IService.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    public CartDTO applyVoucher(Integer userId, String voucherCode) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Voucher voucher = voucherRepository.findByCodeAndStatusTrue(voucherCode).orElseThrow(() -> new IllegalArgumentException("Invalid voucher code"));

        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        if (totalPrice >= voucher.getMinimumPurchase() && voucher.getCurrentUsage() < voucher.getMaxUsage()) {
            double discountedPrice = totalPrice - voucher.getDiscountValue();
            cart.setTotalPrice(discountedPrice);
            cart.setVoucherCode(voucherCode);
            cartRepository.save(cart);

            // Convert Cart entity to CartDTO
            List<CartItemDTO> cartItemDTOs = convertToDTOList(cart.getCartItems());
            return new CartDTO(cartItemDTOs, discountedPrice, voucherCode);
        } else {
            throw new IllegalArgumentException("Voucher conditions not met");
        }
    }

    @Override
    public ResponseEntity<CartDTO> findCartByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            if (cart.isPresent()) {
                List<CartItem> cartItems = cart.get().getCartItems();
                CartDTO cartDTO = new CartDTO();
                cartDTO.setCartItems(convertToDTOList(cartItems));
                cartDTO.setTotalPrice(cart.get().getTotalPrice());
                cartDTO.setVoucherCode(cart.get().getVoucherCode()); // Set the voucher code
                return ResponseEntity.ok(cartDTO);
            } else {
                // Create a new cart if not present
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setCartItems(new ArrayList<>());
                cartRepository.save(newCart);
                CartDTO cartDTO = new CartDTO();
                cartDTO.setCartItems(new ArrayList<>());
                cartDTO.setVoucherCode(null); // No voucher code for new cart
                cartDTO.setTotalPrice(0.0);
                return ResponseEntity.ok(cartDTO);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<CartItemDTO> addToCart(Integer userId, CartItemDTO cartItemRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> watchOptional = productRepository.findById(cartItemRequest.getProduct().getId());

        if (userOptional.isPresent() && watchOptional.isPresent()) {
            User user = userOptional.get();
            Product product = watchOptional.get();

            // Ensure the user has a cart
            Cart cart = user.getCart();
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cart.setCartItems(new ArrayList<>());
                cartRepository.save(cart);
            }

            // Check if the watch is already in the cart
            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst();

            CartItem cartItem;
            if (existingCartItem.isPresent()) {
                // Watch is already in the cart, update the quantity
                cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + 1); // Update quantity
            } else {
                // Add new watch to the cart
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cart.getCartItems().add(cartItem);
            }

            cartItemRepository.save(cartItem);

            // Convert and return DTO
            CartItemDTO responseDTO = convertToDTO(cartItem);
            return ResponseEntity.ok(responseDTO);
        }

        return ResponseEntity.notFound().build();
    }


    @Override
    public ResponseEntity<Void> removeFromCart(Integer userId, Integer cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent() && cartItem.get().getCart().getUser().getId().equals(userId)) {
            cartItemRepository.delete(cartItem.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        Product product = cartItem.getProduct();
        if (product != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setCategory(product.getCategory());
            productDTO.setDescription(product.getDescription());
            productDTO.setStockQuantity(product.getStockQuantity());
            productDTO.setStatus(product.isStatus());
            productDTO.setPrice(product.getPrice());
            productDTO.setCreatedDate(product.getCreatedDate());
            productDTO.setImageUrl(product.getImageUrl().stream().map(image -> image.getImageUrl()).collect(Collectors.toList()));
            cartItemDTO.setProduct(productDTO);
        }

        return cartItemDTO;
    }

    private List<CartItemDTO> convertToDTOList(List<CartItem> cartItems) {
        return cartItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void clearCart(Integer userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        cart.ifPresent(existingCart -> {
            List<CartItem> cartItems = existingCart.getCartItems();
            cartItemRepository.deleteAll(cartItems);
            existingCart.setCartItems(new ArrayList<>());
            cartRepository.save(existingCart);
        });
    }

    @Override
    public List<Integer> findWatchIdsInCart(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
            if (cartOptional.isPresent()) {
                List<CartItem> cartItems = cartOptional.get().getCartItems();
                return cartItems.stream()
                        .map(cartItem -> cartItem.getProduct().getId())
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>(); // Return an empty list if cart or user not found
    }}