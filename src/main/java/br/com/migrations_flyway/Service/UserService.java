package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.UserRepository;
import br.com.migrations_flyway.Resources.UserResources;
import br.com.migrations_flyway.exceptions.ResourceNotFoundException;
import br.com.migrations_flyway.models.User;
import br.com.migrations_flyway.models.Dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PagedResourcesAssembler<User> assembler;
    @Autowired
    public UserService(UserRepository userRepository, PagedResourcesAssembler<User> assembler) {
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        users.forEach(u -> u.add(linkTo(methodOn(UserResources.class).
                        getUserById(u.getKey())).withSelfRel()));

        var link = linkTo(methodOn(UserResources.class).
                getAllUsers(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(users, link);
    }

    public User getUserById(UUID id) {
        var getUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No resource for this id"));

        getUser.add(linkTo(methodOn(UserResources.class).getUserById(id)).withSelfRel());

        return getUser;
    }

    public User createUser(UserDto dto) {
        var user = new User();

        user.setName(dto.name());
        user.setAge(dto.age());
        user.setCpf(dto.cpf());

        var saveUser = userRepository.save(user);

        saveUser.add(linkTo(methodOn(UserResources.class).getUserById(saveUser.getKey())).withSelfRel());

        return saveUser;
    }

    public PagedModel<EntityModel<User>> getUsersByName(Pageable pageable, String name) {
        Page<User> users = userRepository.findUsersByName(name, pageable);

        users.map(u -> u.add(linkTo(methodOn(UserResources.class).getUserById(u.getKey())).withSelfRel()));

        var link = linkTo(methodOn(UserResources.class)
                .getAllUsers(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(users, link);
    }

    @Transactional
    public User disableUser(UUID id) {
        userRepository.disableUser(id);

        var entity = userRepository.findById(id).orElseThrow(RuntimeException::new);

        entity.add(linkTo(methodOn(UserResources.class).getUserById(id)).withSelfRel());

        return entity;
    }
}
