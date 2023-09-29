package crm_project_22.service;

import java.util.List;

import crm_project_22.entity.Status;
import crm_project_22.repository.StatusRepository;

public class StatusService {
    private StatusRepository statusRepository = new StatusRepository();

    public List<Status> getAll() {
        return statusRepository.getAll();
    }

    public List<Status> getPercentsInProject(int projectId) {
        return statusRepository.getPercentsInProject(projectId);
    }

    public List<Status> getPercentsOfOneUserInProject(int userId, int projectId) {
        return statusRepository.getPercentsOfOneUserInProject(userId, projectId);
    }

    public List<Status> getPercentsOfOneUser(int userId) {
        return statusRepository.getPercentsOfOneUser(userId);
    }

    public List<Status> getPercentsOfOneLeader(int userId) {
        return statusRepository.getPercentsOfOneLeader(userId);
    }
}
