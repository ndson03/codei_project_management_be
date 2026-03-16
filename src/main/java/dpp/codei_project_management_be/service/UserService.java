package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersForDepartmentPicAssignment(Long departmentId) {
        return userRepository.findAll().stream()
                .filter(user -> !accessControlService.isAdmin(user))
                .toList();
    }
}


