package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Cart;
import co.uk.jdreamer.shoppingcart.models.Product;
import co.uk.jdreamer.shoppingcart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, Model model, HttpSession session) {

        Product product = productRepository.getOne(id);

        if (session.getAttribute("cart") == null) {
            Map<Integer, Cart> cart = new HashMap<Integer, Cart>();
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);
        } else {
            Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
            if (cart.containsKey(id)) {
                int quantity = cart.get(id).getQuantity();
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++quantity, product.getImage()));
            } else {
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }

        return null;
    }
}
