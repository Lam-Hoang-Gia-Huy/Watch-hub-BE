package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.CartDTO;
import com.example.JWTImplemenation.DTO.CartItemDTO;
import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.Cart;
import com.example.JWTImplemenation.Entities.CartItem;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.CartItemRepository;
import com.example.JWTImplemenation.Repository.CartRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Repository.UserRepository;
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
    private WatchRespository watchRespository;

    @Override
    public ResponseEntity<CartDTO> findCartByUserId(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            if (cart.isPresent()) {
                List<CartItem> cartItems = cart.get().getCartItems();
                double totalPrice = cartItems.stream().mapToDouble(CartItem::getPrice).sum();
                CartDTO cartDTO = new CartDTO();
                cartDTO.setCartItems(convertToDTOList(cartItems));
                cartDTO.setTotalPrice(totalPrice);
                return ResponseEntity.ok(cartDTO);
            } else {
                // Create a new cart if not present
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setCartItems(new ArrayList<>());
                cartRepository.save(newCart);
                CartDTO cartDTO = new CartDTO();
                cartDTO.setCartItems(new ArrayList<>());
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
        Optional<Watch> watchOptional = watchRespository.findById(cartItemRequest.getWatch().getId());

        if (userOptional.isPresent() && watchOptional.isPresent()) {
            User user = userOptional.get();
            Watch watch = watchOptional.get();

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
                    .filter(item -> item.getWatch().getId().equals(watch.getId()))
                    .findFirst();

            CartItem cartItem;
            if (existingCartItem.isPresent()) {
                // Watch is already in the cart, update the quantity
                cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + 1); // Increment quantity
            } else {
                // Add new watch to the cart
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setWatch(watch);
                cartItem.setQuantity(1); // Set initial quantity to 1
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
        Watch watch = cartItem.getWatch();
        if (watch != null) {
            WatchDTO watchDTO = new WatchDTO();
            watchDTO.setId(watch.getId());
            watchDTO.setName(watch.getName());
            watchDTO.setBrand(watch.getBrand());
            watchDTO.setDescription(watch.getDescription());
            watchDTO.setPaid(watch.isPaid());
            watchDTO.setStatus(watch.isStatus());
            watchDTO.setPrice(watch.getPrice());
            watchDTO.setCreatedDate(watch.getCreatedDate());
            watchDTO.setImageUrl(watch.getImageUrl().stream().map(image -> image.getImageUrl()).collect(Collectors.toList()));
            watchDTO.setAppraisalId(watch.getAppraisal().getId());
            watchDTO.setSellerId(watch.getUser().getId());

            cartItemDTO.setWatch(watchDTO);
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
                        .map(cartItem -> cartItem.getWatch().getId())
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>(); // Return an empty list if cart or user not found
    }}