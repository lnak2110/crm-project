package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Role;
import crm_project_22.entity.Status;
import crm_project_22.entity.Task;
import crm_project_22.entity.User;

public class UserRepository {
    public boolean create(String email, String password, String fullName, String phoneNumber,
            String address, int idRole) {
        String query = "INSERT INTO User(email, password, fullName, phoneNumber, address, id_Role)"
                + " VALUES(?, ?, ?, ?, ?, ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, phoneNumber);
            statement.setString(5, address);
            statement.setInt(6, idRole);

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

    public List<User> getAll() {
        String query = "SELECT * FROM User u LEFT JOIN Role r ON u.id_Role = r.id";

        Connection connection = MySQLConfig.getConnection();

        List<User> users = new ArrayList<User>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFullName(resultSet.getString("fullName"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));

                Role role = new Role();
                role.setId(resultSet.getInt("id_Role"));
                role.setName(resultSet.getString("name"));

                user.setRole(role);

                users.add(user);
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

        return users;
    }

    public List<User> getManyOutsideProject(int projectId) {
        String query = "SELECT u.* FROM User u "
                + "LEFT JOIN Project_User pu ON u.id = pu.id_User AND pu.id_Project = ? "
                + "LEFT JOIN Role r ON u.id_Role = r.id "
                + "WHERE pu.id_User IS NULL AND LOWER(r.name) = 'member'";

        Connection connection = MySQLConfig.getConnection();

        List<User> users = new ArrayList<User>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFullName(resultSet.getString("fullName"));

                users.add(user);
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

        return users;
    }

    public List<User> getManyInAProject(int projectId) {
        String query = "WITH users AS (SELECT u.id, u.email, u.fullName FROM User u "
                + "INNER JOIN Project_User pu ON u.id = pu.id_User WHERE pu.id_Project = ?), "
                + "tasks AS (SELECT t.id, t.name, t.description, t.startDate, t.endDate, "
                + "t.id_Status, s.name status_name FROM Task t "
                + "INNER JOIN Status s ON t.id_Status = s.id WHERE t.id_Project = ?) "
                + "SELECT u.id, u.email, u.fullName, t.id task_id, t.name task_name, "
                + "t.description task_description, t.startDate task_startDate, "
                + "t.endDate task_endDate, t.id_Status task_status_id, "
                + "t.status_name task_status_name FROM users u "
                + "LEFT JOIN Task_User tu ON u.id = tu.id_User "
                + "AND tu.id_Task IN (SELECT id FROM tasks) "
                + "LEFT JOIN tasks t ON tu.id_Task = t.id";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setInt(2, projectId);

            ResultSet resultSet = statement.executeQuery();

            Map<Integer, User> users = new HashMap<>();

            while (resultSet.next()) {
                int userID = resultSet.getInt("id");
                String userEmail = resultSet.getString("email");
                String userFullName = resultSet.getString("fullName");

                User user;

                if (users.containsKey(userID)) {
                    user = users.get(userID);
                } else {
                    user = new User(userID, userEmail, userFullName);
                    users.put(userID, user);
                }

                Status taskStatus = new Status(resultSet.getInt("task_status_id"),
                        resultSet.getString("task_status_name"));

                if (!resultSet.wasNull()) {
                    Task task = new Task(resultSet.getInt("task_id"),
                            resultSet.getString("task_name"),
                            resultSet.getString("task_description"),
                            resultSet.getTimestamp("task_startDate"),
                            resultSet.getTimestamp("task_endDate"), null, null, taskStatus);

                    if (user.getTasks() == null) {
                        user.setTasks(new ArrayList<>());
                    }

                    if (task != null) {
                        user.getTasks().add(task);
                    }
                }
            }

            for (User user : users.values()) {
                if (user.getTasks() == null || user.getTasks().isEmpty()) {
                    user.setTasks(Collections.emptyList());
                }
            }

            List<User> userList = new ArrayList<>(users.values());

            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
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

    public User getOneInAProject(int id, int projectId) {
        String query = "SELECT u.id, u.email, u.fullName, t.id task_id, t.name task_name, "
                + "t.description task_description, t.startDate task_startDate, "
                + "t.endDate task_endDate, t.id_Status task_status_id, "
                + "s.name task_status_name FROM User u "
                + "INNER JOIN Task_User tu ON u.id = tu.id_User "
                + "INNER JOIN Task t ON tu.id_Task = t.id AND t.id_Project = ? "
                + "INNER JOIN Status s ON t.id_Status = s.id WHERE u.id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setInt(2, id);

            ResultSet resultSet = statement.executeQuery();

            User user = null;

            while (resultSet.next()) {
                if (user == null) {
                    user = new User(resultSet.getInt("id"), resultSet.getString("email"),
                            resultSet.getString("fullName"));
                }

                Status taskStatus = new Status(resultSet.getInt("task_status_id"),
                        resultSet.getString("task_status_name"));

                Task task = new Task(resultSet.getInt("task_id"), resultSet.getString("task_name"),
                        resultSet.getString("task_description"),
                        resultSet.getTimestamp("task_startDate"),
                        resultSet.getTimestamp("task_endDate"), null, null, taskStatus);

                if (user.getTasks() == null) {
                    user.setTasks(new ArrayList<>());
                }

                if (task != null) {
                    user.getTasks().add(task);
                }
            }

            if (user != null && (user.getTasks() == null || user.getTasks().isEmpty())) {
                user.setTasks(Collections.emptyList());
            }

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
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

    public User getById(int id, boolean includePassword) {
        String query = "SELECT u.*, r.name FROM User u "
                + "LEFT JOIN Role r ON u.id_Role = r.id WHERE u.id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                if (includePassword) {
                    user.setPassword(resultSet.getString("password"));
                }
                user.setFullName(resultSet.getString("fullName"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                user.setAddress(resultSet.getString("address"));

                Role role = new Role(resultSet.getInt("id_Role"), resultSet.getString("name"));
                user.setRole(role);

                return user;
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

    public User getByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM User u JOIN Role r ON u.id_Role = r.id "
                + "WHERE u.email = ? AND u.password = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("email"),
                        resultSet.getString("fullName"));

                Role role = new Role();
                role.setName(resultSet.getString("name"));

                user.setRole(role);

                return user;
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

    public boolean updateById(int id, String email, String password, String fullName,
            String phoneNumber, String address, int idRole) {
        String query = "UPDATE User SET email = ?, password = ?, fullName = ?, "
                + "phoneNumber = ?, address = ?, id_Role = ? WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, phoneNumber);
            statement.setString(5, address);
            statement.setInt(6, idRole);
            statement.setInt(7, id);

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

    public boolean updateById(int id, String email, String password, String fullName,
            String phoneNumber, String address) {
        String query = "UPDATE User SET email = ?, password = ?, fullName = ?, "
                + "phoneNumber = ?, address = ? WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, phoneNumber);
            statement.setString(5, address);
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

    public boolean deleteById(int id) {
        String query = "DELETE FROM User WHERE id = ?";

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

    public boolean deleteFromProject(int id, int projectId) {
        String query = "DELETE pu, tu FROM Project_User pu "
                + "LEFT JOIN Task_User tu ON pu.id_User = tu.id_User "
                + "LEFT JOIN Task t ON tu.id_Task = t.id AND t.id_Project = ? "
                + "WHERE pu.id_Project = ? AND pu.id_User = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, projectId);
            statement.setInt(2, projectId);
            statement.setInt(3, id);

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
