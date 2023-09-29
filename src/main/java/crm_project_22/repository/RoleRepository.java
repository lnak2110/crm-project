package crm_project_22.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import crm_project_22.config.MySQLConfig;
import crm_project_22.entity.Role;

public class RoleRepository {
    public int create(String name, String description) {
        int count = 0;
        String query = "INSERT INTO Role(name, description) VALUES(?, ?)";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, description);

            count = statement.executeUpdate();
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

        return count;
    }

    public List<Role> getAll() {
        String query = "SELECT * FROM Role";

        Connection connection = MySQLConfig.getConnection();

        List<Role> roles = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
                role.setDescription(resultSet.getString("description"));

                roles.add(role);
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

        return roles;
    }

    public Role getById(int id) {
        String query = "SELECT * FROM Role WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
                role.setDescription(resultSet.getString("description"));

                return role;
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

    public boolean updateById(String name, String description, int id) {
        String query = "UPDATE Role SET name = ?, description = ? WHERE id = ?";

        Connection connection = MySQLConfig.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, description);
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

    public boolean deleteById(int id) {
        String query = "DELETE FROM Role WHERE id = ?";

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
