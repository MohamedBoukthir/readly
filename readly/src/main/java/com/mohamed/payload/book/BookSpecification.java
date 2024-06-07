package com.mohamed.payload.book;

import com.mohamed.entities.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    // This method is used to filter books by their owner ID in the database
    public static Specification<Book> withOwnerId(String ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), ownerId);
    }

}
