package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        // All authenticated users (ADMIN, PIC, USER) can view all projects
        return projectRepository.findAll();
    }
}

