package br.com.migrations_flyway.Repositories;

import br.com.migrations_flyway.models.UsersClients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersClientsRepository extends JpaRepository<UsersClients, Integer> {
    @Query("SELECT u FROM UsersClients u WHERE u.userName = :userName")
    UsersClients findByUsername(@Param("userName") String username);
}
