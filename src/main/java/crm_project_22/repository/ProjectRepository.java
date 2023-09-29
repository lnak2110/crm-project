package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Project;
import crm_project_22.entity.Status;
import crm_project_22.entity.User;

public class ProjectRepository {
    public boolean create(String name, String description, Timestamp startDate, Timestamp endDate,
            int idUser, int idStatus) {
        String query = "INSERT INTO Project(name, description, startDate, endDate, id_User, id_Status) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setTimestamp(3, startDate);
            statement.setTimestamp(4, endDate);
            statement.setInt(5, idUser);
            statement.setInt(6, idStatus);

            return statement.executeUpdate() > 0;
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

        return false;
    }

    public List<Project> getAll() {
        String query = "SELECT p.*, u.fullName, s.name status_name FROM Project p "
                + "INNER JOIN User u ON p.id_User = u.id "
                + "INNER JOIN Status s ON p.id_Status = s.id";

        Connection connection = MySQLConfig.getConnection();

        List<Project> projects = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setFullName(resultSet.getString("fullName"));
                Status status = new Status();
                status.setName(resultSet.getString("status_name"));

                Project project = new Project(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), user, status);

                projects.add(project);
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

        return projects;
    }

    public List<Project> getAllOfOneLeader(int userId) {
        String query = "SELECT p.*, s.name status_name FROM Project p "
                + "JOIN Status s ON p.id_Status = s.id WHERE p.id_User = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Project> projects = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id_User"));

                Status status = new Status(resultSet.getInt("id_Status"),
                        resultSet.getString("status_name"));

                Project project = new Project(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), user, status);

                projects.add(project);
            }

            return projects;
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

    public Project getById(int id) {
        String query = "SELECT p.*, s.name status_name, s.description status_description "
                + "FROM Project p JOIN User u ON p.id_User = u.id "
                + "JOIN Status s ON p.id_Status = s.id WHERE p.id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getInt("id"));
                project.setName(resultSet.getString("name"));
                project.setDescription(resultSet.getString("description"));
                project.setStartDate(resultSet.getTimestamp("startDate"));
                project.setEndDate(resultSet.getTimestamp("endDate"));

                User user = new User();
                user.setId(resultSet.getInt("id_User"));
                Status status = new Status();
                status.setId(resultSet.getInt("id_Status"));

                project.setUser(user);
                project.setStatus(status);

                return project;
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

    public boolean updateById(int id, String name, String description, Timestamp startDate,
            Timestamp endDate, int idStatus) {
        String query = "UPDATE Project SET name = ?, description = ?, startDate = ?, "
                + "endDate = ?, id_Status = ? WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setTimestamp(3, startDate);
            statement.setTimestamp(4, endDate);
            statement.setInt(5, idStatus);
            statement.setInt(6, id);

            return statement.executeUpdate() > 0;
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

        return false;
    }

    public boolean createManyProjectUser(int id, int[] userIds) {
        String query = "INSERT INTO Project_User (id_Project, id_User) VALUES (?, ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            int totalRowsAffected = 0;

            for (int userId : userIds) {
                statement.setInt(1, id);
                statement.setInt(2, userId);
                totalRowsAffected += statement.executeUpdate();
            }

            if (totalRowsAffected == userIds.length) {
                return true;
            } else {
                return false;
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

        return false;
    }

    public boolean deleteById(int id) {
        String query = "DELETE FROM Project WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
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

        return false;
    }
}
