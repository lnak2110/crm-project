package crm_project_22.entity;

public class ProjectUser {
    private Project project;
    private User user;

    public ProjectUser() {
    }

    public ProjectUser(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
