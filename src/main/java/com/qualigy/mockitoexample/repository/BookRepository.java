package com.qualigy.mockitoexample.repository;

import com.qualigy.mockitoexample.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


}
