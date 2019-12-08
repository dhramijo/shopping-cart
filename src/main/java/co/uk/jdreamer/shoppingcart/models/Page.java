package co.uk.jdreamer.shoppingcart.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pages")
@Data
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    private String slug;

    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    private int sorting;
}
