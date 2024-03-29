package SAGroup.webShop.controller;

import SAGroup.login.model.UserEntity;
import SAGroup.login.repo.UserRepo;
import SAGroup.webShop.model.CheckoutHistoryDTO;
import SAGroup.webShop.model.ShoppingCart;
import SAGroup.webShop.model.ShoppingCartDTO;
import SAGroup.webShop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserRepo userRepo;
    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserRepo userRepo) {
        this.shoppingCartService = shoppingCartService;
        this.userRepo = userRepo;
    }

    // It's not needed because when the user is created the shopping cart is created automatically.
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ShoppingCart> createShoppingCart(Principal principal) {

        UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ShoppingCart createdShoppingCart = shoppingCartService.createShoppingCart(user);

        if (createdShoppingCart != null) {
            return new ResponseEntity<>(createdShoppingCart, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<ShoppingCart> getShoppingCartForCurrentUser(Principal principal) {

        UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ShoppingCart cart = shoppingCartService.getShoppingCartByUserId(user.getId()).orElse(null);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{articleId}/{quantity}")
    public ResponseEntity<String> addArticleToShoppingCart(Principal principal, @PathVariable Long articleId, @PathVariable int quantity) {

        UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shoppingCartService.addArticleToShoppingCart(user.getId(), articleId, quantity);
        return ResponseEntity.ok("Article added to the shopping cart successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<ShoppingCartDTO>> getAllShoppingCarts() {
        List<ShoppingCartDTO> userShoppingCarts = shoppingCartService.getUserShoppingCarts();
        return new ResponseEntity<>(userShoppingCarts, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteShoppingCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Principal principal) {
        UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("User id: " + user.getId() + " Username: " + user.getUsername() + " is about to checkout");
        shoppingCartService.checkout(user.getId());
        return ResponseEntity.ok("Checkout successful");
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/checkout")
    public ResponseEntity<List <CheckoutHistoryDTO>> checkoutHistory(Principal principal) {
        UserEntity user = userRepo.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Username: " + user.getUsername() + " checkout history: " );
        return ResponseEntity.ok(shoppingCartService.checkoutHistory(user.getUsername()));
    }

}
