package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final AccessControlService accessControlService;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersForDepartmentPicAssignment(Long departmentId) {
        return userRepository.findAll().stream()
                .filter(user -> !accessControlService.isAdmin(user))
                .filter(user -> isPicCandidate(user.getUsername(), departmentId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByPartId(Long partId) {
        return userRepository.findAll().stream()
                .filter(user -> partId != null && partId.equals(user.getPartId()))
                .toList();
    }

    private boolean isPicCandidate(String username, Long departmentId) {
        if (username == null || username.isBlank()) {
            return false;
        }
        if (departmentId == null) {
            return !departmentRepository.existsByDepartmentPicUsername(username);
        }
        return !departmentRepository.existsByDepartmentPicUsernameAndPartIdNot(username, departmentId);
    }
}


