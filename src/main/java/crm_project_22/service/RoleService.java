package crm_project_22.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import crm_project_22.entity.Role;
import crm_project_22.repository.RoleRepository;

public class RoleService {
    private RoleRepository roleRepository = new RoleRepository();

    public boolean checkCreateRole(String name, String description) {
        return roleRepository.create(name, description) > 0;
    }

    public List<Role> getAllRoles() {
        return roleRepository.getAll();
    }

    public Role getById(int id) {
        return roleRepository.getById(id);
    }

    public boolean updateById(HttpServletRequest req, HttpServletResponse resp)
            throws JsonSyntaxException, JsonIOException, IOException {
        Role role = new Gson().fromJson(req.getReader(), Role.class);

        String name = role.getName();
        String description = role.getDescription();
        int id = Integer.parseInt(req.getParameter("id"));

        return roleRepository.updateById(name, description, id);
    }

    public boolean deleteById(int id) {
        return roleRepository.deleteById(id);
    }
}
