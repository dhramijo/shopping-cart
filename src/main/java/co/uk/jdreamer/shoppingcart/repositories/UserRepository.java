package co.uk.jdreamer.shoppingcart.repositories;

import co.uk.jdreamer.shoppingcart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
