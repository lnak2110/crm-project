package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Project;
import crm_project_22.entity.Task;
import crm_project_22.entity.TaskUser;
import crm_project_22.entity.User;

public class TaskUserRepository {
    public TaskUser getOne(int taskId) {
        String query = "SELECT tu.*, t.id_Project, p.id_User leader_id "
                + "FROM Task_User tu INNER JOIN Task t ON tu.id_Task = t.id "
                + "INNER JOIN Project p ON t.id_Project = p.id WHERE tu.id_Task = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, taskId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User leader = new User();
                leader.setId(resultSet.getInt("leader_id"));

                Project project = new Project();
                project.setId(resultSet.getInt("id_Project"));
                project.setUser(leader);

                Task task = new Task();
                task.setId(resultSet.getInt("id_Task"));
                task.setProject(project);

                User user = new User();
                user.setId(resultSet.getInt("id_User"));

                TaskUser taskUser = new TaskUser(task, user);
                
                return taskUser;
            }
        } catch (SQLException e) {
            System.out.println("Query error: " + e.getLocalizedMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Connection closed error " + e.getLocalizedMessage());
                }
            }
        }

        return null;
    }
}
