package co.uk.jdreamer.shoppingcart.repositories;

import co.uk.jdreamer.shoppingcart.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer> {

    List<Page> findAllByOrderBySortingAsc();

    Page findBySlug(String slug);

    Page findBySlugAndIdNot(String slug, int id);

    //@Query("select p from Page p where p.id <> :id and p.slug = :slug")
    //Page findBySlug(int id, String slug);
}
