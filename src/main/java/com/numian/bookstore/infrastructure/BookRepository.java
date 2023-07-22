package com.numian.bookstore.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.numian.bookstore.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    <S extends Book> S save(S book);

    void deleteById(Long id);
}
