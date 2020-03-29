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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, Model model, HttpSession session, @RequestParam(value = "cartPage", required = false) String cartPage) {

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

        Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");

        int size = 0;
        double total = 0;

        for (Cart value : cart.values()) {
            size += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        model.addAttribute("size", size);
        model.addAttribute("total", total);

        if (cartPage != null) {
            return "redirect:/cart/view";
        }

        return "cart_view";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, Model model, HttpSession session, HttpServletRequest httpServletRequest) {

        Product product = productRepository.getOne(id);
        Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");

        int quantity = cart.get(id).getQuantity();
        if (quantity == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            }
        } else {
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), --quantity, product.getImage()));
        }

        String referLink = httpServletRequest.getHeader("referer");

        return "redirect:" + referLink;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable int id, Model model, HttpSession session, HttpServletRequest httpServletRequest) {

        Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");

        cart.remove(id);
        if (cart.size() == 0) {
            session.removeAttribute("cart");
        }

        String referLink = httpServletRequest.getHeader("referer");

        return "redirect:" + referLink;
    }

    @GetMapping("/clear")
    public String clear(HttpSession session, HttpServletRequest httpServletRequest) {

        session.removeAttribute("cart");

        String referLink = httpServletRequest.getHeader("referer");

        return "redirect:" + referLink;
    }

    @RequestMapping("/view")
    public String view(HttpSession session, Model model) {

        if (session.getAttribute("cart") == null) {
            return "redirect:/";
        }

        Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        model.addAttribute("notCartViewPage", true);

        return "cart";
    }
}
