package co.uk.jdreamer.shoppingcart.repositories;

import co.uk.jdreamer.shoppingcart.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

}
