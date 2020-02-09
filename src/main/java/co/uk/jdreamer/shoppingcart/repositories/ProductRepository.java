package co.uk.jdreamer.shoppingcart.repositories;

import co.uk.jdreamer.shoppingcart.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}