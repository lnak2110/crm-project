package crm_project_22.entity;

public class TaskUser {
    private Task task;
    private User user;

    public TaskUser() {
    }

    public TaskUser(Task task, User user) {
        this.task = task;
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
