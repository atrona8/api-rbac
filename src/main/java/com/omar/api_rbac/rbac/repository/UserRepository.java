 
package com.omar.api_rbac.rbac.repository;

import com.omar.api_rbac.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Charge les rôles et permissions en une requête
    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles r
        LEFT JOIN FETCH r.permissions
        WHERE u.username = :username
    """)
    Optional<User> findByUsernameWithRolesAndPermissions(String username);
}