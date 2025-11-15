package hkmu.wadd.dao;

import hkmu.wadd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = :role ORDER BY u.fullName")
    List<User> findUsersByRole(@Param("role") String role);

    // Native SQL query example
    @Query(value = "SELECT * FROM users WHERE role = :role", nativeQuery = true)
    List<User> findUsersByRoleNative(@Param("role") String role);

    List<User> findByRole(String role);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u.role FROM User u WHERE u.username = :username") // Use exact field name
    String findRoleByUsername(@Param("username") String username);
}