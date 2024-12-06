package br.com.migrations_flyway.Repositories;

import br.com.migrations_flyway.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
