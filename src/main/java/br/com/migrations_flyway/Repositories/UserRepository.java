package br.com.migrations_flyway.Repositories;

import br.com.migrations_flyway.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Query("UPDATE User u SET u.enabled = false WHERE u.id = :id")
    void disableUser(@Param("id") UUID id);
    @Query("SELECT u FROM User u WHERE u.name LIKE LOWER(CONCAT ('%', :name, '%'))")
    Page<User> findUsersByName(@Param("name") String name, Pageable pageable);
}
