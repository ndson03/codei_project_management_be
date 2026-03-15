package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN is allowed to get user list");
        }

        return userRepository.findAll();
    }
}


