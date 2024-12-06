package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.UsersClientsRepository;
import br.com.migrations_flyway.Security.Jwt.TokenProvider;
import br.com.migrations_flyway.dataVoV1.AccountCredentialsVO;
import br.com.migrations_flyway.dataVoV1.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService {
    private final UsersClientsRepository usersClientsRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationService(UsersClientsRepository usersClientsRepository, TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.usersClientsRepository = usersClientsRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));

            var user = usersClientsRepository.findByUsername(data.getUsername());

            var tokenResponse = new TokenVO();
            if (user != null) tokenResponse = tokenProvider.createAccessToken(data.getUsername(), user.getRoles());
            else throw new UsernameNotFoundException("Username: " + data.getUsername() + " not found.");

            return ResponseEntity.ok(tokenResponse);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password. Try again.");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken) {
        var user = usersClientsRepository.findByUsername(username);

        var tokenResponse = new TokenVO();
        if (tokenResponse != null) tokenResponse = tokenProvider.refreshToken(refreshToken);
        else throw new UsernameNotFoundException("Username " + username + " not found.");

        return ResponseEntity.ok(tokenResponse);
    }
}
