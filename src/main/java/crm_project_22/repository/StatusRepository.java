package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Status;

public class StatusRepository {
    public List<Status> getAll() {
        String query = "SELECT * FROM Status";

        Connection connection = MySQLConfig.getConnection();

        List<Status> statuses = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Status status = new Status(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"));

                statuses.add(status);
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

        return statuses;
    }

    public List<Status> getPercentsInProject(int projectId) {
        String query = "SELECT s.id statusId, s.name statusName, "
                + "CASE WHEN (SELECT COUNT(*) FROM Task WHERE id_Project = ?) > 0 THEN "
                + "ROUND(COUNT(t.id) * 100 / (SELECT COUNT(*) FROM Task WHERE id_Project = ?), 1) "
                + "ELSE 0 END percentTask FROM Status s "
                + "LEFT JOIN Task t ON t.id_Status = s.id "
                + "AND t.id_Project = ? GROUP BY s.id, s.name";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setInt(2, projectId);
            statement.setInt(3, projectId);

            ResultSet resultSet = statement.executeQuery();

            List<Status> percents = new ArrayList<>();

            while (resultSet.next()) {
                Status status = new Status(resultSet.getInt("statusId"),
                        resultSet.getString("statusName"), resultSet.getDouble("percentTask"));

                percents.add(status);
            }

            return percents;
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

    public List<Status> getPercentsOfOneUserInProject(int userId, int projectId) {
        String query = "SELECT s.id, s.name, COUNT(t.id) taskCount FROM Status s "
                + "LEFT JOIN Task t ON t.id_Status = s.id AND t.id_Project = ? "
                + "AND t.id IN (SELECT id_Task FROM Task_User WHERE id_User = ?) "
                + "GROUP BY s.id, s.name";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query, // to use
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // beforeFirst()

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Status> percents = new ArrayList<>();

            int totalTasks = 0;

            while (resultSet.next()) {
                totalTasks += resultSet.getInt("taskCount");
            }

            // Reset the cursor to the beginning of the result set
            resultSet.beforeFirst();

            // Loop through the result set and calculate the percent for each status
            while (resultSet.next()) {
                int taskCount = resultSet.getInt("taskCount");

                double percentTask = 0.0;

                if (totalTasks > 0) {
                    percentTask = (double) taskCount / totalTasks * 100;
                }

                // Round the percent to one decimal place
                percentTask = Math.round(percentTask * 10.0) / 10.0;

                Status status = new Status(resultSet.getInt("id"), resultSet.getString("name"),
                        percentTask);

                percents.add(status);
            }

            return percents;
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

    public List<Status> getPercentsOfOneUser(int userId) {
        String query = "SELECT s.id, s.name, COUNT(t.id) taskCount FROM Status s "
                + "LEFT JOIN Task t ON s.id = t.id_Status "
                + "AND t.id IN (SELECT id_Task FROM Task_User WHERE id_User = ?) "
                + "GROUP BY s.id, s.name";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query, // to use
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // beforeFirst()

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Status> percents = new ArrayList<>();

            int totalTasks = 0;

            while (resultSet.next()) {
                totalTasks += resultSet.getInt("taskCount");
            }

            // Reset the cursor to the beginning of the result set
            resultSet.beforeFirst();

            // Loop through the result set and calculate the percent for each status
            while (resultSet.next()) {
                int taskCount = resultSet.getInt("taskCount");

                double percentTask = 0.0;

                if (totalTasks > 0) {
                    percentTask = (double) taskCount / totalTasks * 100;
                }

                // Round the percent to one decimal place
                percentTask = Math.round(percentTask * 10.0) / 10.0;

                Status status = new Status(resultSet.getInt("id"), resultSet.getString("name"),
                        percentTask, taskCount);

                percents.add(status);
            }

            return percents;
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

    public List<Status> getPercentsOfOneLeader(int userId) {
        String query = "SELECT s.id, s.name, COUNT(p.id) projectCount FROM Status s "
                + "LEFT JOIN Project p ON s.id = p.id_Status "
                + "AND p.id IN (SELECT id FROM Project WHERE id_User = ?) "
                + "GROUP BY s.id, s.name";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query, // to use
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); // beforeFirst()

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Status> percents = new ArrayList<>();

            int totalProjects = 0;

            while (resultSet.next()) {
                totalProjects += resultSet.getInt("projectCount");
            }

            // Reset the cursor to the beginning of the result set
            resultSet.beforeFirst();

            // Loop through the result set and calculate the percent for each status
            while (resultSet.next()) {
                int projectCount = resultSet.getInt("projectCount");

                double percentProject = 0.0;

                if (totalProjects > 0) {
                    percentProject = (double) projectCount / totalProjects * 100;
                }

                // Round the percent to one decimal place
                percentProject = Math.round(percentProject * 10.0) / 10.0;

                Status status = new Status(resultSet.getInt("id"), resultSet.getString("name"),
                        percentProject, projectCount);

                percents.add(status);
            }

            return percents;
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
