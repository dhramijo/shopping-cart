package co.uk.jdreamer.shoppingcart.controllers.security;

import co.uk.jdreamer.shoppingcart.models.User;
import co.uk.jdreamer.shoppingcart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register(User user) {
        return "register";
    }
}
