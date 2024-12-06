package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.BookRepository;
import br.com.migrations_flyway.models.Book;
import br.com.migrations_flyway.models.Dtos.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Nested
    class getAllBooks {
        @Test
        @DisplayName("Should return all books with success")
        void getAllBooksWithSuccess() {

        }

        @Test
        @DisplayName("Should throws when error occurs in method get all books")
        void ThrowsExceptionWhenErrorOccursInGetAllBooks() {

        }
    }

    @Nested
    class getBookById {
        @Test
        @DisplayName("Should return book by id with success")
        void getBookByIdWithSuccess() {
            Integer id = 1;
            Book book = new Book(id, "title", "author", 32.2, "20-04-2024");

            when(bookRepository.findById(id)).thenReturn(Optional.of(book));

            var output = bookService.getBookById(id);

            assertNotNull(output);
            assertNotNull(output.getKey());
            assertNotNull(output.getLinks());

            assertTrue(book.getLinks().hasLink("self"));

            assertEquals(book, output);
            assertEquals(book.getKey(), output.getKey());
            assertEquals(book.getTitle(), output.getTitle());
            assertEquals(book.getAuthor(), output.getAuthor());
            assertEquals(book.getLaunch_date(), output.getLaunch_date());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in method get book by id ")
        void ThrowsExceptionWhenErrorOccursInGetBookById() {
            Integer id = 1;
            when(bookRepository.findById(id)).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> bookService.getBookById(id));
        }
    }

    @Nested
    class updatedBook {
        @Test
        @DisplayName("Should updated book with success")
        void updatedBookWithSuccess() {
            Integer id = 3;
            Book book = new Book(id, "title", "author", 32.2, "20-04-2024");

            BookDto dto = new BookDto("title", "author", 32.2, "20-04-2024");

            when(bookRepository.findById(id)).thenReturn(Optional.of(book));
            when(bookRepository.save(any(Book.class))).thenReturn(book);

            var output = bookService.updateBook(id, dto);

            assertNotNull(output);
            assertNotNull(output.getKey());
            assertNotNull(output.getLinks());

            assertTrue(output.getLinks().hasLink("self"));

            assertEquals(book, output);
            assertEquals(book.getKey(), output.getKey());
            assertEquals(book.getTitle(), output.getTitle());
            assertEquals(book.getAuthor(), output.getAuthor());
            assertEquals(book.getLaunch_date(), output.getLaunch_date());
        }

        @Test
        @DisplayName("Should throws exception when error occurs in method updated book")
        void ThrowsExceptionWhenErrorOccursInUpdatedBook() {
            Integer id = 3;
            BookDto dto = new BookDto("title", "author", 32.2, "20-04-2024");

            when(bookRepository.findById(id)).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> bookService.updateBook(id, dto));
        }
    }

    @Nested
    class deleteBook {
        @Test
        @DisplayName("Should delete book with success")
        void deleteBookWithSuccess() {
            Integer id = 3;

            bookService.deleteBook(id);

            verify(bookRepository).deleteById(id);
        }

        @Test
        @DisplayName("Checks if the deleteBookById method has been called at least once")
        void checksIfTheDeleteBookMethodHasBeenCalledAtLeastOnce() {
            Integer id = 3;

            bookService.deleteBook(id);

            verify(bookRepository, times(1)).deleteById(id);
        }
    }
}