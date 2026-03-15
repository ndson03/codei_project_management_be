package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
}

