package com.transaction_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    private String isbn;
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
