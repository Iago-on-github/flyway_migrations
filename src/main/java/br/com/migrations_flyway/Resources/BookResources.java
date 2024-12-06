package br.com.migrations_flyway.Resources;

import br.com.migrations_flyway.Service.BookService;
import br.com.migrations_flyway.models.Book;
import br.com.migrations_flyway.models.Dtos.BookDto;
import br.com.migrations_flyway.util.mediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "endpoints for managing books")
public class BookResources {
    private final BookService bookService;
    @Autowired
    public BookResources(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML})
    @Operation(summary = "operation for listing all Books",
    description = "operation for listing all Books on database",
    tags = {"Books"},
    responses = {@ApiResponse(description = "Success", responseCode = "200",
    content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Book.class)))}),
    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<Book>>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                    @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                                    @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, limit, sortDirection, "title");
        return ResponseEntity.ok().body(bookService.getAllBooks(pageable));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML})
    @Operation(summary = "operation for get book by your id",
    description = "operation for get book on database by your id",
    tags = {"Books"},
    responses = {@ApiResponse(description = "Success", responseCode = "200",
    content = @Content(schema = @Schema(implementation = Book.class))),
    @ApiResponse(description = "Bad Request", responseCode = "40", content = @Content),
    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "operation for updated book",
    description = "operation for updated book",
    tags = {"Books"},
    responses = {@ApiResponse(description = "Success", responseCode = "200",
    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody BookDto dto) {
        var book = bookService.updateBook(id, dto);

        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setPrice(dto.price());
        book.setLaunch_date(dto.launch_date());

        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "operation for deleting book",
    description = "operation for deleting book",
    tags = {"Books"},
    responses = {@ApiResponse(description = "Success", responseCode = "200",
    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "201", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
       return ResponseEntity.noContent().build();
    }
}
