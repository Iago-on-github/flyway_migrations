package br.com.migrations_flyway.Resources;

import br.com.migrations_flyway.Service.AuthenticationService;
import br.com.migrations_flyway.dataVoV1.AccountCredentialsVO;
import br.com.migrations_flyway.models.UsersClients;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthenticationResources {
    private final AuthenticationService authenticationService;
    public AuthenticationResources(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signing")
    @Operation(summary = "Authenticate a User",
            description = "Method for authenticate a clients and return a token")
    public ResponseEntity signing(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing username or password.");

        var token = authenticationService.signin(data);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect client request");

        return ResponseEntity.ok().body(token);
    }

    @PutMapping("/refresh/{username}")
    @Operation(summary = "Refresh a token",
    description = "Refresh a token for authenticate user and returns a token")
    public ResponseEntity refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (checkIfParamsIsNotNull(username, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Missing username or password");

        var token = authenticationService.refreshToken(username, refreshToken);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect client request");

        return ResponseEntity.ok().body(token);
    }

    private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
       return data == null || data.getUsername() == null || data.getUsername().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }

    private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return username == null || username.isBlank() || refreshToken == null || refreshToken.isBlank();
    }
}
