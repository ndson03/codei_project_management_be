package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

