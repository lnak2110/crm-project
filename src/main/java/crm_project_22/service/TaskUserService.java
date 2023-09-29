package crm_project_22.service;

import crm_project_22.entity.TaskUser;
import crm_project_22.repository.TaskUserRepository;

public class TaskUserService {
    private TaskUserRepository taskUserRepository = new TaskUserRepository();

    public TaskUser getOne(int taskId) {
        return taskUserRepository.getOne(taskId);
    }
}
