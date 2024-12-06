package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.UserRepository;
import br.com.migrations_flyway.models.User;
import br.com.migrations_flyway.models.Dtos.UserDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {
    @Mock
    private PagedResourcesAssembler<User> assembler;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Nested
    class returnAllUsers {
        @Test
        @DisplayName("Should return all users with success")
        void getAllUsersWithSuccess() {

        }
    }

    @Nested
    class returnHateoasLinkOnGetUserById {
        @Test
        @DisplayName("Should get user by id")
        void getUserByIdWithSuccess() {
            UUID key = UUID.randomUUID();
            User user = new User(key, "test_name", 23, "933.938.939-3", true);

            doReturn(Optional.of(user)).when(userRepository).findById(key);

            var output = userService.getUserById(key);

            assertNotNull(output);
            assertNotNull(output.getKey(), "the 'key value' should not be null");
            assertNotNull(output.getLinks(), "the links should not be null");

            assertTrue(user.getLinks().hasLink("self"));

            assertEquals(user, output, "entities should be equals");
            assertEquals(user.getKey(), output.getKey());
            assertEquals(user.getName(), output.getName());
            assertEquals(user.getAge(), output.getAge());
            assertEquals(user.getCpf(), output.getCpf());
            assertEquals(user.getEnabled(), output.getEnabled());
        }
    }

    @Nested
    class createUser {
        @Test
        @DisplayName("Should create user by success")
        void createUserWithSuccess() {
            var user = new User();

            var dto = new UserDto(user.getName(), user.getAge(), user.getCpf(), user.getEnabled());

            user.setName(dto.name());
            user.setAge(dto.age());
            user.setCpf(dto.cpf());
            user.setEnabled(dto.enabled());

            when(userRepository.save(user)).thenReturn(user);

            var output = userService.createUser(dto);

            assertNotNull(output);

            assertEquals(user, output);
            assertEquals(user.getName(), output.getName());
            assertEquals(user.getAge(), output.getAge());
            assertEquals(user.getCpf(), output.getCpf());
            assertEquals(user.getEnabled(), output.getEnabled());
        }
    }

    @Nested
    class disableUser {
        @Test
        @DisplayName("Should disable user with success")
        void disableUserWithSuccess() {
            UUID id = UUID.randomUUID();
            User mockUser = new User();
            mockUser.setKey(id);
            mockUser.setEnabled(false);

            doNothing().when(userRepository).disableUser(id);
            when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

            var output = userService.disableUser(id);

            assertNotNull(output, "the output should not be null");

            verify(userRepository).disableUser(id);
            verify(userRepository).findById(id);

        }

        @Test
        @DisplayName("Should throws exception when error occurs in method disable user")
        void ThrowsExceptionWhenErrorOccursInDisableUser() {
            UUID id = UUID.randomUUID();

            doNothing().when(userRepository).disableUser(id);
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> userService.disableUser(id));

            verify(userRepository).disableUser(id);
            verify(userRepository).findById(id);
        }
    }
}