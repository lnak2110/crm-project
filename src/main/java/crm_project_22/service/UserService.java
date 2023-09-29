package crm_project_22.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import crm_project_22.entity.User;
import crm_project_22.repository.UserRepository;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    public boolean create(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        int idRole = Integer.parseInt(req.getParameter("idRole"));

        return userRepository.create(email, password, fullName, phoneNumber, address, idRole);
    }

    public boolean updateById(HttpServletRequest req, HttpServletResponse resp)
            throws JsonSyntaxException, JsonIOException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);

        int id = Integer.parseInt(req.getParameter("id"));
        String email = data.get("email").getAsString();
        String password = data.get("password").getAsString();
        String fullName = data.get("fullName").getAsString();
        String phoneNumber = data.get("phoneNumber").getAsString();
        String address = data.get("address").getAsString();

        if (data.has("idRole")) {
            int idRole = data.get("idRole").getAsInt();

            return userRepository.updateById(id, email, password, fullName, phoneNumber, address,
                    idRole);
        } else {
            return userRepository.updateById(id, email, password, fullName, phoneNumber, address);
        }
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public List<User> getManyOutsideProject(int projectId) {
        return userRepository.getManyOutsideProject(projectId);
    }

    public List<User> getManyInAProject(int projectId) {
        return userRepository.getManyInAProject(projectId);
    }

    public User getOneInAProject(int id, int projectId) {
        return userRepository.getOneInAProject(id, projectId);
    }

    public User getById(int id, boolean includePassword) {
        return userRepository.getById(id, includePassword);
    }

    public boolean deleteById(int id) {
        return userRepository.deleteById(id);
    }

    public boolean deleteFromProject(int id, int projectId) {
        return userRepository.deleteFromProject(id, projectId);
    }
}
