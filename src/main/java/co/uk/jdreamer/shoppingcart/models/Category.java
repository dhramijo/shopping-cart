package co.uk.jdreamer.shoppingcart.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    private int sorting;

}
