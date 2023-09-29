package crm_project_22.service;

import crm_project_22.entity.ProjectUser;
import crm_project_22.repository.ProjectUserRepository;

public class ProjectUserService {
    private ProjectUserRepository projectUserRepository = new ProjectUserRepository();

    public ProjectUser getOne(int projectId, int userId) {
        return projectUserRepository.getOne(projectId, userId);
    }
}
