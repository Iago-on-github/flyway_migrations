package br.com.migrations_flyway.Resources;

import br.com.migrations_flyway.Service.UserService;
import br.com.migrations_flyway.models.User;
import br.com.migrations_flyway.models.Dtos.UserDto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "endpoints for managing users")
public class UserResources {
    private final UserService userService;

    @Autowired
    public UserResources(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML})
    @Operation(summary = "operation for listing all users",
            description = "operation for listing all users",
            tags = {"Users"},
            responses = {@ApiResponse(description = "Success", responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<User>>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                    @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                                    @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping(value = "/{id}", produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML})
    @Operation(summary = "operation for search user by id",
            description = "operation for search user by id",
            tags = {"Users"},
            responses = {@ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping(value = "/{id}", produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML
    })
    @Operation(summary = "disable user",
            description = "operation for disable user by id",
            tags = {"Users"},
            responses = {@ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<User> disableUser(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok().body(userService.disableUser(id));
    }

    @GetMapping(value = "/findUsersByName/{name}", produces = {
            mediaType.APPLICATION_JSON,
            mediaType.APPLICATION_XML,
            mediaType.APPLICATION_YAML})
    @Operation(summary = "operation for listing users by your name",
            description = "operation for listing users by your name",
            tags = {"Users"},
            responses = {@ApiResponse(description = "Success", responseCode = "200",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PagedModel<EntityModel<User>>> getUsersByName(@PathVariable String name,
                                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                     @RequestParam(value = "description", defaultValue = "asc") String description) {

        var sortDescription = "desc".equalsIgnoreCase(description) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDescription, "name"));
        return ResponseEntity.ok().body(userService.getUsersByName(pageable, name));
    }

    @PostMapping
    @Operation(summary = "operation for create user",
            description = "operation for create user",
            tags = {"Users"},
            responses = {@ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = User.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<User> createUser(@RequestBody UserDto dto, UriComponentsBuilder componentsBuilder) {
        User user = userService.createUser(dto);

        URI uri = componentsBuilder.path("/{id}").buildAndExpand(user.getKey()).toUri();
        return ResponseEntity.created(uri).body(user);
    }
}
