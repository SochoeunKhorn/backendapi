package com.sochoeun.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nameKh;
    private String nameEn;

    /*@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Article> articles;*/
}
