package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.BookRepository;
import br.com.migrations_flyway.Resources.BookResources;
import br.com.migrations_flyway.exceptions.ResourceNotFoundException;
import br.com.migrations_flyway.models.Book;
import br.com.migrations_flyway.models.Dtos.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final PagedResourcesAssembler assembler;
    @Autowired
    public BookService(BookRepository bookRepository, PagedResourcesAssembler assembler) {
        this.bookRepository = bookRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<Book>> getAllBooks(Pageable pageable) {
        Page<Book> books =  bookRepository.findAll(pageable);

        books.forEach(b -> b.add(linkTo(methodOn(BookResources.class).getBookById(b.getKey())).withSelfRel()));

        var link = linkTo(methodOn(BookResources.class)
                .getAllBooks(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(books, link);
    }

    public Book getBookById(Integer id) {
        var getBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No resource for this id"));

        getBook.add(linkTo(methodOn(BookResources.class).getBookById(id)).withSelfRel());

        return getBook;
    }

    public Book updateBook(Integer id, BookDto dto) {
        var book = updatedBookMethod(id, dto);

        return bookRepository.save(book);
    }

    public Book createBook(BookDto dto) {
        Book book = new Book();

        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setPrice(dto.price());
        book.setLaunch_date(dto.launch_date());

        return bookRepository.save(book);
    }

    public void deleteBook (Integer id) {
        bookRepository.deleteById(id);
    }

    private Book updatedBookMethod(Integer id, BookDto dto) {
        Book book = getBookById(id);

        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setPrice(dto.price());
        book.setLaunch_date(dto.launch_date());

        return book;
    }
}
