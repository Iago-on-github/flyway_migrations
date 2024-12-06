package br.com.migrations_flyway.Service;

import br.com.migrations_flyway.Repositories.UsersClientsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersClientsService implements UserDetailsService {
    private final UsersClientsRepository usersClientsRepository;

    public UsersClientsService(UsersClientsRepository usersClientsRepository) {
        this.usersClientsRepository = usersClientsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = usersClientsRepository.findByUsername(username);
        if (user != null) return user;
        else throw new UsernameNotFoundException("Username not found");
    }
}
