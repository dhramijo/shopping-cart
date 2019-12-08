package co.uk.jdreamer.shoppingcart.services;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    PageRepository pageRepository;

    public List<Page> findAllByOrderBySortingAsc() {
        return pageRepository.findAllByOrderBySortingAsc();
    }

    public Page findBySlug(String slug) {
        return pageRepository.findBySlug(slug);
    }

    public Page findBySlugAndIdNot(String slug, int id) {
        return pageRepository.findBySlugAndIdNot(slug, id);
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }

    public Page findPageById(int id) {
        return pageRepository.getOne(id);
    }

    public void deleteById(int id) {
        pageRepository.deleteById(id);
    }
}
