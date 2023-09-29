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
import crm_project_22.entity.Task;
import crm_project_22.entity.User;

public class TaskRepository {
    public boolean create(String name, String description, Timestamp startDate, Timestamp endDate,
            int idUser, int idProject, int idStatus) {
        String insertTaskQuery = "INSERT INTO "
                + "Task (name, description, startDate, endDate, id_Project, id_Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String insertTaskUserQuery = "INSERT INTO Task_User (id_Task, id_User) "
                + "VALUES (LAST_INSERT_ID(), ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            connection.setAutoCommit(false);

            PreparedStatement insertTaskStatement = connection.prepareStatement(insertTaskQuery);
            insertTaskStatement.setString(1, name);
            insertTaskStatement.setString(2, description);
            insertTaskStatement.setTimestamp(3, startDate);
            insertTaskStatement.setTimestamp(4, endDate);
            insertTaskStatement.setInt(5, idProject);
            insertTaskStatement.setInt(6, idStatus);
            int rowsAffected1 = insertTaskStatement.executeUpdate();

            PreparedStatement insertTaskUserStatement = connection
                    .prepareStatement(insertTaskUserQuery);
            insertTaskUserStatement.setInt(1, idUser);
            int rowsAffected2 = insertTaskUserStatement.executeUpdate();

            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection closed error " + e.getLocalizedMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                System.out.println("Connection rollback error " + e1.getLocalizedMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection closed error " + e.getLocalizedMessage());
            }
        }

        return false;
    }

    public List<Task> getAll() {
        String query = "SELECT t.*, p.name project_name, s.name status_name, "
                + "u.id user_id, u.email, u.fullName "
                + "FROM Task t INNER JOIN Project p ON t.id_Project = p.id "
                + "INNER JOIN Status s ON t.id_Status = s.id "
                + "LEFT JOIN Task_User tu ON t.id = tu.id_Task "
                + "LEFT JOIN User u ON tu.id_User = u.id";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            List<Task> tasks = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User(resultSet.getInt("user_id"), resultSet.getString("email"),
                        resultSet.getString("fullName"));

                Project project = new Project();
                project.setId(resultSet.getInt("id_Project"));
                project.setName(resultSet.getString("project_name"));

                Status status = new Status();
                status.setName(resultSet.getString("status_name"));

                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), user, project, status);

                tasks.add(task);
            }

            return tasks;
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

    public List<Task> getAllOfOneUser(int userId) {
        String query = "SELECT t.*, p.id project_id, p.name project_name, "
                + "p.id_User leader_id, u.email leader_email, u.fullName leader_fullName, "
                + "s.id status_id, s.name status_name FROM Task t "
                + "INNER JOIN Project p ON t.id_Project = p.id "
                + "INNER JOIN Status s ON t.id_Status = s.id "
                + "INNER JOIN User u ON p.id_User = u.id "
                + "WHERE t.id IN (SELECT id_Task FROM Task_User WHERE id_User = ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Task> tasks = new ArrayList<>();

            while (resultSet.next()) {
                User leader = new User(resultSet.getInt("leader_id"),
                        resultSet.getString("leader_email"),
                        resultSet.getString("leader_fullName"));

                Project project = new Project();
                project.setId(resultSet.getInt("id_Project"));
                project.setName(resultSet.getString("project_name"));
                project.setUser(leader);

                Status status = new Status();
                status.setId(resultSet.getInt("status_id"));
                status.setName(resultSet.getString("status_name"));

                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), null, project, status);

                tasks.add(task);
            }

            return tasks;
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

    public List<Task> getWithNoUserInProject(int projectId) {
        String query = "SELECT t.*, s.name status_name FROM Task t "
                + "LEFT JOIN Task_User tu ON t.id = tu.id_Task "
                + "INNER JOIN Status s ON t.id_Status = s.id "
                + "WHERE t.id_Project = ? AND tu.id_Task IS NULL";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);

            ResultSet resultSet = statement.executeQuery();

            List<Task> tasks = new ArrayList<>();

            while (resultSet.next()) {
                Status status = new Status(resultSet.getInt("id_Status"),
                        resultSet.getString("status_name"));

                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), null, null, status);

                tasks.add(task);
            }

            return tasks;
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

    public Task getById(int id) {
        String query = "SELECT t.*, p.name project_name, p.id_User leader_id, "
                + "s.name status_name, u.id user_id, u.email, u.fullName FROM Task t "
                + "INNER JOIN Project p ON t.id_Project = p.id "
                + "INNER JOIN Status s ON t.id_Status = s.id "
                + "LEFT JOIN Task_User tu ON t.id = tu.id_Task "
                + "LEFT JOIN User u ON tu.id_User = u.id WHERE t.id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(resultSet.getInt("user_id"), resultSet.getString("email"),
                        resultSet.getString("fullName"));

                User leader = new User();
                leader.setId(resultSet.getInt("leader_id"));

                Project project = new Project();
                project.setId(resultSet.getInt("id_Project"));
                project.setName(resultSet.getString("project_name"));
                project.setUser(leader);

                Status status = new Status(resultSet.getInt("id_Status"),
                        resultSet.getString("status_name"));

                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getTimestamp("startDate"),
                        resultSet.getTimestamp("endDate"), user, project, status);

                return task;
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
            Timestamp endDate, int oldIdUser, int idUser, int idStatus) {
        String updateTaskQuery = "UPDATE Task SET name = ?, description = ?, "
                + "startDate = ?, endDate = ?, id_Status = ? WHERE id = ?";
        String updateTaskUserQuery = "UPDATE Task_User SET id_User = ? "
                + "WHERE id_Task = ? AND id_User != ?";
        String insertTaskUserQuery = "INSERT INTO Task_User (id_Task, id_User) VALUES (?, ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            connection.setAutoCommit(false);

            PreparedStatement updateTaskStatement = connection.prepareStatement(updateTaskQuery);
            updateTaskStatement.setString(1, name);
            updateTaskStatement.setString(2, description);
            updateTaskStatement.setTimestamp(3, startDate);
            updateTaskStatement.setTimestamp(4, endDate);
            updateTaskStatement.setInt(5, idStatus);
            updateTaskStatement.setInt(6, id);
            int rowsAffected1 = updateTaskStatement.executeUpdate();

            PreparedStatement statement;

            int rowsAffected2;
            if (idUser == oldIdUser) {
                rowsAffected2 = 1;
            } else {
                statement = connection.prepareStatement(updateTaskUserQuery);
                statement.setInt(1, idUser);
                statement.setInt(2, id);
                statement.setInt(3, idUser);
                rowsAffected2 = statement.executeUpdate();
            }

            if (rowsAffected2 == 0) {
                statement = connection.prepareStatement(insertTaskUserQuery);
                statement.setInt(1, id);
                statement.setInt(2, idUser);
                rowsAffected2 = statement.executeUpdate();
            }

            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Connection closed error " + e.getLocalizedMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                System.out.println("Connection rollback error " + e1.getLocalizedMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection closed error " + e.getLocalizedMessage());
            }
        }

        return false;
    }

    public boolean updateStatus(int id, int idStatus) {
        String query = "UPDATE Task SET id_Status = ? WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, idStatus);
            statement.setInt(2, id);

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

    public boolean deleteById(int id) {
        String query = "DELETE FROM Task WHERE id = ?";

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
