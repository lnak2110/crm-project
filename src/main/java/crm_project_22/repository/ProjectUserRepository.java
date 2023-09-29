package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Project;
import crm_project_22.entity.ProjectUser;
import crm_project_22.entity.User;

public class ProjectUserRepository {
    public ProjectUser getOne(int projectId, int userId) {
        String query = "SELECT pu.*, p.id_User leader_id FROM Project_User pu "
                + "INNER JOIN Project p ON pu.id_Project = p.id "
                + "WHERE pu.id_Project = ? AND pu.id_User = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User leader = new User();
                leader.setId(resultSet.getInt("leader_id"));

                Project project = new Project();
                project.setId(resultSet.getInt("id_Project"));
                project.setUser(leader);

                User user = new User();
                user.setId(resultSet.getInt("id_User"));

                ProjectUser projectUser = new ProjectUser(project, user);

                return projectUser;
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
